package components;

import javax.management.*;
import java.lang.management.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * ProfilerManager handles profiling data collection and communication with the IDE.
 * Provides performance metrics like CPU usage, memory usage, thread count, etc.
 */
public class ProfilerManager {
    private static ProfilerManager instance;
    private boolean profilingEnabled = false;
    private long startTime;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private final MemoryMXBean memoryBean;
    private final ThreadMXBean threadBean;
    private final RuntimeMXBean runtimeBean;
    private final OperatingSystemMXBean osBean;
    private final CompilationMXBean compilationBean;
    private final List<GarbageCollectorMXBean> gcBeans;
    
    // Profiling data storage
    private final List<ProfileData> profileHistory = new ArrayList<>();
    private final int MAX_HISTORY_SIZE = 1000;
    
    // Performance counters
    private long executionStartTime;
    private long lastGcTime = 0;
    private long lastGcCollections = 0;
    
    private ProfilerManager() {
        this.memoryBean = ManagementFactory.getMemoryMXBean();
        this.threadBean = ManagementFactory.getThreadMXBean();
        this.runtimeBean = ManagementFactory.getRuntimeMXBean();
        this.osBean = ManagementFactory.getOperatingSystemMXBean();
        this.compilationBean = ManagementFactory.getCompilationMXBean();
        this.gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        this.executorService = Executors.newFixedThreadPool(2);
    }
    
    public static synchronized ProfilerManager getInstance() {
        if (instance == null) {
            instance = new ProfilerManager();
        }
        return instance;
    }
    
    /**
     * Enable profiling and start the profiling server for IDE communication
     */
    public void enableProfiling(int port) {
        if (profilingEnabled) {
            return;
        }
        
        profilingEnabled = true;
        executionStartTime = System.currentTimeMillis();
        startTime = System.currentTimeMillis();
        
        try {
            // Start profiling data server
            startProfilingServer(port);
            System.out.println("PROFILER: Profiling enabled on port " + port);
            
            // Start data collection thread
            startDataCollection();
            
        } catch (IOException e) {
            System.err.println("PROFILER: Failed to start profiling server: " + e.getMessage());
            profilingEnabled = false;
        }
    }
    
    /**
     * Disable profiling and stop the server
     */
    public void disableProfiling() {
        profilingEnabled = false;
        
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // Ignore
            }
        }
        
        if (executorService != null) {
            executorService.shutdown();
        }
        
        System.out.println("PROFILER: Profiling disabled");
    }
    
    /**
     * Start the server that communicates with the IDE
     */
    private void startProfilingServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        
        executorService.submit(() -> {
            while (profilingEnabled && !serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    handleClientConnection(clientSocket);
                } catch (IOException e) {
                    if (profilingEnabled) {
                        System.err.println("PROFILER: Error accepting client connection: " + e.getMessage());
                    }
                }
            }
        });
    }
    
    /**
     * Handle IDE client connection and send profiling data
     */
    private void handleClientConnection(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if ("GET_PROFILE_DATA".equals(inputLine)) {
                    ProfileData currentData = collectCurrentProfileData();
                    out.println(currentData.toJson());
                } else if ("GET_PROFILE_HISTORY".equals(inputLine)) {
                    out.println(getProfileHistoryJson());
                } else if ("RESET_PROFILE".equals(inputLine)) {
                    resetProfiling();
                    out.println("PROFILE_RESET");
                }
            }
            
        } catch (IOException e) {
            System.err.println("PROFILER: Error handling client connection: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }
    
    /**
     * Start continuous data collection
     */
    private void startDataCollection() {
        executorService.submit(() -> {
            while (profilingEnabled) {
                try {
                    ProfileData data = collectCurrentProfileData();
                    addToHistory(data);
                    Thread.sleep(1000); // Collect data every second
                } catch (InterruptedException e) {
                    break;
                } catch (Exception e) {
                    System.err.println("PROFILER: Error collecting profile data: " + e.getMessage());
                }
            }
        });
    }
    
    /**
     * Collect current profiling data
     */
    private ProfileData collectCurrentProfileData() {
        ProfileData data = new ProfileData();
        data.timestamp = System.currentTimeMillis();
        data.elapsedTime = data.timestamp - executionStartTime;
        
        // Memory information
        MemoryUsage heapMemory = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemory = memoryBean.getNonHeapMemoryUsage();
        
        data.heapUsed = heapMemory.getUsed();
        data.heapMax = heapMemory.getMax();
        data.heapCommitted = heapMemory.getCommitted();
        data.nonHeapUsed = nonHeapMemory.getUsed();
        data.nonHeapMax = nonHeapMemory.getMax();
        
        // Thread information
        data.threadCount = threadBean.getThreadCount();
        data.peakThreadCount = threadBean.getPeakThreadCount();
        data.daemonThreadCount = threadBean.getDaemonThreadCount();
        
        // GC information
        long totalGcTime = 0;
        long totalGcCollections = 0;
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            totalGcTime += gcBean.getCollectionTime();
            totalGcCollections += gcBean.getCollectionCount();
        }
        data.gcTime = totalGcTime;
        data.gcCollections = totalGcCollections;
        data.gcTimeDelta = totalGcTime - lastGcTime;
        data.gcCollectionsDelta = totalGcCollections - lastGcCollections;
        lastGcTime = totalGcTime;
        lastGcCollections = totalGcCollections;
        
        // CPU information with multiple fallback methods
        data.cpuUsage = getCpuUsage();
        data.systemCpuUsage = getSystemCpuUsage();
        
        // Compilation information
        if (compilationBean != null) {
            data.compilationTime = compilationBean.getTotalCompilationTime();
        }
        
        return data;
    }
    
    /**
     * Add data to history, maintaining size limit
     */
    private void addToHistory(ProfileData data) {
        synchronized (profileHistory) {
            profileHistory.add(data);
            if (profileHistory.size() > MAX_HISTORY_SIZE) {
                profileHistory.remove(0);
            }
        }
    }
    
    /**
     * Reset profiling data
     */
    public void resetProfiling() {
        synchronized (profileHistory) {
            profileHistory.clear();
        }
        executionStartTime = System.currentTimeMillis();
        lastGcTime = 0;
        lastGcCollections = 0;
    }
    
    /**
     * Get profile history as JSON
     */
    private String getProfileHistoryJson() {
        synchronized (profileHistory) {
            StringBuilder json = new StringBuilder();
            json.append("{\"history\":[");
            for (int i = 0; i < profileHistory.size(); i++) {
                if (i > 0) json.append(",");
                json.append(profileHistory.get(i).toJson());
            }
            json.append("]}");
            return json.toString();
        }
    }
    
    /**
     * Method to be called when starting execution of VG code
     */
    public void startExecution() {
        if (profilingEnabled) {
            executionStartTime = System.currentTimeMillis();
        }
    }
    
    /**
     * Method to be called when finishing execution of VG code
     */
    public void endExecution() {
        if (profilingEnabled) {
            System.out.println("PROFILER: Execution completed in " + (System.currentTimeMillis() - executionStartTime) + "ms");
        }
    }
    
    /**
     * Get process CPU usage with multiple fallback methods
     */
    private double getCpuUsage() {
        // Method 1: Try reflection for platform-specific CPU methods
        try {
            java.lang.reflect.Method method = osBean.getClass().getMethod("getProcessCpuLoad");
            Object result = method.invoke(osBean);
            if (result instanceof Double) {
                double cpuLoad = (Double) result;
                if (cpuLoad >= 0) {
                    return cpuLoad * 100.0;
                }
            }
        } catch (Exception e) {
            // Method not available, try alternatives
        }
        
        // Method 2: Use ThreadMXBean CPU time (less accurate but more reliable)
        try {
            if (threadBean.isCurrentThreadCpuTimeSupported()) {
                long cpuTime = threadBean.getCurrentThreadCpuTime();
                if (cpuTime > 0) {
                    // This gives us nanoseconds of CPU time for current thread
                    // Convert to a rough percentage estimate
                    long wallTime = System.currentTimeMillis() - startTime;
                    if (wallTime > 0) {
                        double cpuPercent = (cpuTime / 1_000_000.0) / wallTime * 100.0;
                        return Math.min(cpuPercent, 100.0); // Cap at 100%
                    }
                }
            }
        } catch (Exception e) {
            // This method also failed
        }
        
        // Method 3: Estimate based on active vs total threads
        try {
            int activeThreads = threadBean.getThreadCount();
            int availableProcessors = Runtime.getRuntime().availableProcessors();
            
            // Rough estimate: assume some CPU usage if we have active threads
            if (activeThreads > 0) {
                // Basic heuristic: more threads = more CPU usage
                double estimatedUsage = Math.min((activeThreads * 5.0), 50.0);
                return estimatedUsage;
            }
        } catch (Exception e) {
            // Even this failed
        }
        
        // Fallback: Return a small positive value to indicate the process is running
        return 1.0;
    }
    
    /**
     * Get system CPU usage with fallback methods
     */
    private double getSystemCpuUsage() {
        try {
            java.lang.reflect.Method method = osBean.getClass().getMethod("getSystemCpuLoad");
            Object result = method.invoke(osBean);
            if (result instanceof Double) {
                double cpuLoad = (Double) result;
                if (cpuLoad >= 0) {
                    return cpuLoad * 100.0;
                }
            }
        } catch (Exception e) {
            // Method not available
        }
        
        // Fallback: Try to estimate based on system load average
        try {
            double loadAverage = osBean.getSystemLoadAverage();
            if (loadAverage >= 0) {
                int processors = osBean.getAvailableProcessors();
                // Convert load average to rough CPU percentage
                double cpuPercent = (loadAverage / processors) * 100.0;
                return Math.min(cpuPercent, 100.0);
            }
        } catch (Exception e) {
            // This also failed
        }
        
        // Final fallback: return unknown
        return -1.0;
    }
    
    /**
     * Data class for profiling information
     */
    public static class ProfileData {
        public long timestamp;
        public long elapsedTime;
        public long heapUsed;
        public long heapMax;
        public long heapCommitted;
        public long nonHeapUsed;
        public long nonHeapMax;
        public int threadCount;
        public int peakThreadCount;
        public int daemonThreadCount;
        public long gcTime;
        public long gcCollections;
        public long gcTimeDelta;
        public long gcCollectionsDelta;
        public double cpuUsage;
        public double systemCpuUsage;
        public long compilationTime;
        
        public String toJson() {
            return String.format(java.util.Locale.US,
                "{\"timestamp\":%d,\"elapsedTime\":%d,\"heapUsed\":%d,\"heapMax\":%d," +
                "\"heapCommitted\":%d,\"nonHeapUsed\":%d,\"nonHeapMax\":%d,\"threadCount\":%d," +
                "\"peakThreadCount\":%d,\"daemonThreadCount\":%d,\"gcTime\":%d,\"gcCollections\":%d," +
                "\"gcTimeDelta\":%d,\"gcCollectionsDelta\":%d,\"cpuUsage\":%.2f,\"systemCpuUsage\":%.2f," +
                "\"compilationTime\":%d}",
                timestamp, elapsedTime, heapUsed, heapMax, heapCommitted, nonHeapUsed, nonHeapMax,
                threadCount, peakThreadCount, daemonThreadCount, gcTime, gcCollections,
                gcTimeDelta, gcCollectionsDelta, cpuUsage, systemCpuUsage, compilationTime
            );
        }
    }
}
