package components;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import java.awt.EventQueue;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.util.HashSet;
import java.util.Set;

public class ErrorHandler implements Thread.UncaughtExceptionHandler {
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BOLD = "\u001B[1m";

    private static boolean useColorOutput = true;
    private static String currentFilePath = "";
    private static Set<String> reportedErrors = new HashSet<>();
    private static final long ERROR_RESET_INTERVAL = 5000; // 5 seconds

    static {
        // Install the global exception handler
        Thread.setDefaultUncaughtExceptionHandler(new ErrorHandler());
        
        // Disable standard Java stack traces
        System.setProperty("java.awt.exceptionHandler", "components.ErrorHandler");
        
        // Install our custom exception handler for AWT events
        installAWTExceptionHandler();
        
        // Start a timer to reset error tracking
        Thread resetThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(ERROR_RESET_INTERVAL);
                    resetErrorTracking();
                } catch (InterruptedException e) {
                    // Ignore
                }
            }
        });
        resetThread.setDaemon(true);  // Mark as daemon thread so it doesn't prevent JVM exit
        resetThread.start();
    }
    
    /**
     * Reset the error tracking to allow errors to be shown again
     */
    private static synchronized void resetErrorTracking() {
        reportedErrors.clear();
    }
    
    /**
     * Check if an error has already been reported to prevent duplicates
     */
    private static synchronized boolean isErrorAlreadyReported(String errorMsg, String location) {
        String key = errorMsg + "|" + location;
        if (reportedErrors.contains(key)) {
            return true;
        }
        reportedErrors.add(key);
        return false;
    }
    
    /**
     * Handles uncaught exceptions from all threads
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof VGException) {
            VGException vge = (VGException) e;
            int line = vge.getLine();
            int column = vge.getColumn();
            String message = vge.getMessage();
            String location = (line > 0) ? String.format("at line %d:%d", line, column) : "";
            
            // Check if we've already reported this error
            if (isErrorAlreadyReported(message, location)) {
                return;
            }
            
            if (message.contains("Error in function call: Argument error: Function expects")) {
                int expectedArgs = extractNumberAfter(message, "expects ");
                int receivedArgs = extractNumberAfter(message, "but got ");
                
                formatAndPrintError("Function call error", location, buildFunctionArgumentErrorMessage(expectedArgs, receivedArgs));
            } else {
                if (line > 0) {
                    // For errors with a known location
                    reportRuntimeError(line, column, message);
                } else {
                    // For errors without specific location
                    formatAndPrintError("Runtime Error", null, message);
                }
            }
        } else if (e.getMessage() != null && e.getMessage().contains("Error in function call: Argument error: Function expects")) {
            String message = e.getMessage();
            int expectedArgs = extractNumberAfter(message, "expects ");
            int receivedArgs = extractNumberAfter(message, "but got ");
            
            // Check if we've already reported this error
            if (isErrorAlreadyReported(message, "")) {
                return;
            }
            
            formatAndPrintError("Function call error", null, buildFunctionArgumentErrorMessage(expectedArgs, receivedArgs));
        } else {
            // Check if we've already reported this error
            if (isErrorAlreadyReported(e.getMessage(), "")) {
                return;
            }
            
            formatAndPrintError("Runtime Error", null, e.getMessage());
        }
    }

    /**
     * Installs a custom exception handler for AWT event dispatch thread
     */
    private static void installAWTExceptionHandler() {
        EventQueue.invokeLater(() -> {
            EventQueue newQueue = new EventQueue() {
                @Override
                protected void dispatchEvent(AWTEvent event) {
                    try {
                        super.dispatchEvent(event);
                    } catch (Throwable t) {
                        handleAWTException(t);
                    }
                }
            };
            
            Toolkit.getDefaultToolkit().getSystemEventQueue().push(newQueue);
        });
    }
    
    /**
     * Handles exceptions thrown in the AWT event dispatch thread
     */
    private static void handleAWTException(Throwable t) {
        if (t instanceof VGException) {
            VGException vge = (VGException) t;
            String message = vge.getMessage();
            int line = vge.getLine();
            int column = vge.getColumn();
            String location = (line > 0) ? String.format("at line %d:%d", line, column) : "";
            
            // Check if we've already reported this error
            if (isErrorAlreadyReported(message, location)) {
                return;
            }
            
            if (message.contains("Error in function call: Argument error: Function expects")) {
                int expectedArgs = extractNumberAfter(message, "expects ");
                int receivedArgs = extractNumberAfter(message, "but got ");
                
                formatAndPrintError("Function call error", location, buildFunctionArgumentErrorMessage(expectedArgs, receivedArgs));
            } else {
                if (line > 0) {
                    // For errors with a known location
                    reportRuntimeError(line, column, message);
                } else {
                    // For errors without specific location
                    formatAndPrintError("Runtime Error", null, message);
                }
            }
            // Don't exit the program - just return to allow window to be displayed
            return;
        }
        
        String message = t.getMessage();
        if (message == null) {
            message = t.getClass().getName();
        }
        
        // Check for function argument errors
        if (message.contains("Error in function call: Argument error: Function expects")) {
            // Extract the expected argument count and received argument count
            int expectedArgs = extractNumberAfter(message, "expects ");
            int receivedArgs = extractNumberAfter(message, "but got ");
            
            // Extract line number information if available
            int lineStart = message.indexOf("line ") + 5;
            String locationInfo = "";
            if (lineStart > 5) {
                int lineEnd = message.indexOf(":", lineStart);
                if (lineEnd > lineStart) {
                    String lineStr = message.substring(lineStart, lineEnd);
                    try {
                        int line = Integer.parseInt(lineStr);
                        locationInfo = String.format("at line %d", line);
                    } catch (NumberFormatException e) {
                        // Ignore if we can't parse line number
                    }
                }
            }
            
            // Check if we've already reported this error
            if (isErrorAlreadyReported(message, locationInfo)) {
                return;
            }
            
            formatAndPrintError("Function call error", locationInfo, buildFunctionArgumentErrorMessage(expectedArgs, receivedArgs));
            // Don't exit the program
            return;
        }
        
        if (t.toString().contains("Method") && t.toString().contains("not found")) {
            message = "Method not allowed: " + extractMethodName(t.toString());
        }
        
        // Check if we've already reported this error
        if (isErrorAlreadyReported(message, "")) {
            return;
        }
        
        formatAndPrintError("Runtime Error", null, formatUserFriendlyMessage(message));
        // Don't exit the program
    }
    
    /**
     * Builds a consistent error message for function argument errors
     */
    private static String buildFunctionArgumentErrorMessage(int expectedArgs, int receivedArgs) {
        StringBuilder sb = new StringBuilder();
        sb.append("Expected ").append(expectedArgs).append(" arguments, but you provided ").append(receivedArgs);
        
        if (expectedArgs > receivedArgs) {
            sb.append("\n    You are missing ").append(expectedArgs - receivedArgs).append(" argument(s)");
        } else {
            sb.append("\n    You provided ").append(receivedArgs - expectedArgs).append(" too many argument(s)");
        }
        
        return sb.toString();
    }
    
    /**
     * Formats and prints an error message with consistent coloring
     */
    private static void formatAndPrintError(String errorType, String location, String message) {
        printError(errorType, location, message);
    }
    
    /**
     * Extracts a number from a string after the given prefix
     */
    private static int extractNumberAfter(String message, String prefix) {
        try {
            int startIndex = message.indexOf(prefix) + prefix.length();
            if (startIndex > prefix.length()) {
                StringBuilder number = new StringBuilder();
                for (int i = startIndex; i < message.length(); i++) {
                    char c = message.charAt(i);
                    if (Character.isDigit(c)) {
                        number.append(c);
                    } else {
                        break;
                    }
                }
                return Integer.parseInt(number.toString());
            }
        } catch (Exception e) {
            // Just fall back to 0 if we can't parse it
        }
        return 0;
    }
    
    /**
     * Attempts to find the function name from the stack trace
     */
    private static String findFunctionNameFromStackTrace(Throwable t) {
        StackTraceElement[] stackTrace = t.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getMethodName().equals("call") && 
                element.getClassName().contains("Function")) {
                // Try to find the function name in previous stack frames
                for (int i = 0; i < stackTrace.length; i++) {
                    String method = stackTrace[i].getMethodName();
                    if (method.startsWith("visit") && 
                        stackTrace[i].getClassName().contains("Interpreter")) {
                        // This might be the function call context
                        return null; // We would need deeper analysis to get the actual name
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Makes error messages more user-friendly
     */
    private static String formatUserFriendlyMessage(String message) {
        if (message.contains("Error in function call")) {
            return "Function call error: Incorrect number of arguments provided";
        }
        // Add more error message translations as needed
        return message;
    }
    
    /**
     * Extracts the method name from an error message
     */
    private static String extractMethodName(String errorMessage) {
        int startIndex = errorMessage.indexOf("'") + 1;
        int endIndex = errorMessage.indexOf("'", startIndex);
        if (startIndex > 0 && endIndex > startIndex) {
            return errorMessage.substring(startIndex, endIndex);
        }
        return "unknown method";
    }

    public static void setUseColorOutput(boolean useColor) {
        useColorOutput = useColor;
    }


    public static void setCurrentFile(String filePath) {
        currentFilePath = filePath;
    }


    public static void reportSyntaxError(Token token, String message) {
        String location = token != null ?
                String.format("line %d:%d", token.getLine(), token.getCharPositionInLine() + 1) :
                "unknown location";

        printError("Syntax Error", location, message);
    }

    public static void reportRuntimeError(int line, int column, String message) {
        String location = String.format("line %d:%d", line, column);
        printError("Runtime Error", location, message);
    }


    public static void reportError(String errorType, String message) {
        printError(errorType, null, message);
    }


    public static void reportTypeError(int line, int column, String message) {
        String location = String.format("line %d:%d", line, column);
        printError("Type Error", location, message);
    }


    private static void printError(String errorType, String location, String message) {
        StringBuilder sb = new StringBuilder();

        if (useColorOutput) {
            sb.append(ANSI_BOLD);
            sb.append(ANSI_RED);
        }

        sb.append("VG Error: ").append(errorType);

        if (useColorOutput) {
            sb.append(ANSI_RESET);
        }

        if (location != null) {
            sb.append(" at ").append(location);
        }

        if (!currentFilePath.isEmpty()) {
            sb.append(" in file '").append(currentFilePath).append("'");
        }

        sb.append("\n");

        if (useColorOutput) {
            sb.append(ANSI_YELLOW);
        }

        sb.append("  → ").append(message);

        if (useColorOutput) {
            sb.append(ANSI_RESET);
        }

        System.err.println(sb.toString());
    }


    public static class VGException extends RuntimeException {
        private final int line;
        private final int column;

        public VGException(String message, int line, int column) {
            super(message);
            this.line = line;
            this.column = column;
        }
        
        public VGException(String message, Throwable cause) {
            super(message, cause);
            this.line = -1;
            this.column = -1;
        }

        public int getLine() { return line; }
        public int getColumn() { return column; }
        
        @Override
        public String toString() {
            return getMessage();
        }
        
        @Override
        public void printStackTrace(java.io.PrintStream s) {
            // Only print the error message without the stack trace
            s.println(getMessage());
            if (line > 0 && column > 0) {
                s.println("At line " + line + ", column " + column);
            }
        }
        
        @Override
        public void printStackTrace(java.io.PrintWriter w) {
            // Only print the error message without the stack trace
            w.println(getMessage());
            if (line > 0 && column > 0) {
                w.println("At line " + line + ", column " + column);
            }
        }
    }

    public static class VGTypeException extends VGException {
        public VGTypeException(String message, int line, int column) {
            super(message, line, column);
        }
    }

    public static class VGNameException extends VGException {
        public VGNameException(String message, int line, int column) {
            super(message, line, column);
        }
    }

    public static class VGImportException extends VGException {
        public VGImportException(String message, int line, int column) {
            super(message, line, column);
        }
    }

    public static class VGSyntaxException extends VGException {
        public VGSyntaxException(String message, int line, int column) {
            super("Syntax error: " + message, line, column);
        }
    }
    
    public static class VGFileException extends VGException {
        public VGFileException(String message, int line, int column) {
            super("File error: " + message, line, column);
        }
    }
    
    public static class VGArgumentException extends VGException {
        public VGArgumentException(String message, int line, int column) {
            super("Argument error: " + message, line, column);
        }
    }

    public static RuntimeException createSyntaxError(ParserRuleContext ctx, String message) {
        int line = ctx.start.getLine();
        int column = ctx.start.getCharPositionInLine();
        return new RuntimeException("Syntax error at line " + line + ", column " + column + ": " + message);
    }
    
    public static RuntimeException createSyntaxError(Token token, String message) {
        int line = token.getLine();
        int column = token.getCharPositionInLine();
        return new RuntimeException("Syntax error at line " + line + ", column " + column + ": " + message);
    }
    
    public static RuntimeException createRuntimeError(String message) {
        return new RuntimeException("Runtime error: " + message);
    }
    
    public static RuntimeException handleException(Exception e, ParserRuleContext ctx) {
        String msg = e.getMessage();
        if (msg != null) {
            if (msg.contains("Index -1 out of bounds")) {
                return createSyntaxError(ctx, "Invalid syntax. Check for missing semicolons or extra semicolons.");
            }

        }
        return createSyntaxError(ctx, "Unexpected error: " + e.getMessage());
    }
    public static InterruptedException interrupted(Exception e, ParserRuleContext ctx) {
        return new InterruptedException("Interrupted: " + e.getMessage());
    }

    public static VGSyntaxException createSyntaxError(String message, Token token) {
        return new VGSyntaxException(message, token.getLine(), token.getCharPositionInLine());
    }
    
    public static VGSyntaxException createMissingQuoteError(Token token) {
        return new VGSyntaxException("String literal is missing closing quote", 
                                    token.getLine(), token.getCharPositionInLine());
    }
    
    public static VGArgumentException createArgumentCountError(String functionName, int expected, int actual, Token token) {
        return new VGArgumentException(
            "Function '" + functionName + "' expects " + expected + " arguments but got " + actual,
            token.getLine(), token.getCharPositionInLine());
    }
    
    public static VGSyntaxException createUnexpectedSemicolonError(Token token) {
        return new VGSyntaxException("Unexpected semicolon", token.getLine(), token.getCharPositionInLine());
    }
    
    public static VGImportException createImportError(String importPath, String reason, Token token) {
        return new VGImportException(
            "Invalid import '" + importPath + "': " + reason,
            token.getLine(), token.getCharPositionInLine());
    }
}