package components;

import components.visitors.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import org.antlr.v4.runtime.Token;
import java.util.stream.Stream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class Interpreter extends vg_langBaseVisitor<Object> {
    private Deque<SymbolTable> symbolTableStack = new ArrayDeque<>();
    private Map<String, Set<String>> allowedMethods = new HashMap<>();
    private Set<String> allowedClasses = new HashSet<>();
    public Map<String, BuiltInFunction> builtInFunction = new HashMap<>();

    SymbolTable globalSymbolTable;
    ModuleRegistry moduleRegistry;
    private String libraryFolder = System.getenv("VG_LIBRARIES_PATH");
    private int currentLine = 0;
    private int currentColumn = 0;

    // Debug support
    private boolean debugMode = false;
    private Set<Integer> breakpoints = new HashSet<>();
    private Scanner debugScanner = null;
    private boolean debugPaused = false;
    private boolean debugStepping = false;
    private boolean debugStepInto = false;
    private boolean debugStepOver = false;
    private boolean debugStepOut = false;
    private boolean skipNextStatement = false;
    private int stepOverDepth = 0;
    private int stepOutTargetDepth = 0;
    private int currentCallDepth = 0;
    
    // Background command processing
    private volatile boolean isRunning = true;
    private Thread commandListenerThread = null;
    
    // Timer management for debugging
    private List<javax.swing.Timer> pausedTimers = new ArrayList<>();
    private static List<javax.swing.Timer> globalTimers = new ArrayList<>();
    
    /**
     * Register a timer globally for debugging purposes
     */
    public static void registerTimer(javax.swing.Timer timer) {
        synchronized (globalTimers) {
            globalTimers.add(timer);
        }
    }
    
    /**
     * Unregister a timer when it's stopped
     */
    public static void unregisterTimer(javax.swing.Timer timer) {
        synchronized (globalTimers) {
            globalTimers.remove(timer);
        }
    }

    // Visitor instances
    private ProgramVisitor programVisitor;
    private ExpressionVisitor expressionVisitor;
    private StatementVisitor statementVisitor;
    private DeclarationVisitor declarationVisitor;
    private FunctionVisitor functionVisitor;
    private VariableVisitor variableVisitor;
    private LiteralVisitor literalVisitor;
    private ImportVisitor importVisitor;
    private ArithmeticVisitor arithmeticVisitor;
    private BasicExpressionVisitor basicExpressionVisitor;
    private SimpleStatementVisitor simpleStatementVisitor;
    private ClassVisitor classVisitor;

    public Interpreter(String projectPackageFolder) {
        globalSymbolTable = new SymbolTable();
        symbolTableStack.push(globalSymbolTable);
        
        globalSymbolTable.setConstant("true", true);
        globalSymbolTable.setConstant("false", false);
        
        registerBuiltInFunction();

        String configPath = System.getenv("VG_APP_CONFIG");
        loadLangConfigFile(configPath + "/allowed_configurations.vgenv");
        moduleRegistry = new ModuleRegistry();

        // Initialize visitors first
        programVisitor = new ProgramVisitor(this);
        expressionVisitor = new ExpressionVisitor(this);
        statementVisitor = new StatementVisitor(this);
        declarationVisitor = new DeclarationVisitor(this);
        functionVisitor = new FunctionVisitor(this);
        variableVisitor = new VariableVisitor(this);
        literalVisitor = new LiteralVisitor(this);
        importVisitor = new ImportVisitor(this);
        arithmeticVisitor = new ArithmeticVisitor(this);
        basicExpressionVisitor = new BasicExpressionVisitor(this);
        simpleStatementVisitor = new SimpleStatementVisitor(this);
        classVisitor = new ClassVisitor(this);

        // Load libraries after visitors are initialized
        loadLibrariesFromFolder(libraryFolder);
        loadLibrariesFromFolder(projectPackageFolder);
    }

    public SymbolTable getGlobalSymbolTable() {
        return globalSymbolTable;
    }

    public ModuleRegistry getModuleRegistry() {
        return moduleRegistry;
    }

    public Deque<SymbolTable> getSymbolTableStack() {
        return symbolTableStack;
    }

    public ClassVisitor getClassVisitor() {
        return classVisitor;
    }

    private void setupErrorHandling(vg_langParser parser) {
        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                  int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new ErrorHandler.VGSyntaxException(msg, line, charPositionInLine);
            }
        });
    }

    public void loadLibrariesFromFolder(String folderPath) {
        try {
            Path path = Paths.get(folderPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("Created packages directory: " + folderPath);
            }

            try (Stream<Path> paths = Files.walk(path)) {
                paths.filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".vglib"))
                        .forEach(p -> loadLibraryFile(p.toString()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading libraries from folder: " + folderPath, e);
        }
    }

    private void updatePosition(Token token) {
        if (token != null) {
            currentLine = token.getLine();
            currentColumn = token.getCharPositionInLine();
            
            // Check for breakpoint when line changes
            checkBreakpoint(currentLine);
        }
    }

    public void loadLibraryFile(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);

            CharStream input = CharStreams.fromString(content);
            vg_langLexer lexer = new vg_langLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            vg_langParser parser = new vg_langParser(tokens);
            
            setupErrorHandling(parser);

            vg_langParser.ProgramContext programCtx = parser.program();

            for (vg_langParser.StatementContext stmtCtx : programCtx.statement()) {
                if (stmtCtx.libraryDeclaration() != null) {
                    importVisitor.visitLibraryDeclaration(stmtCtx.libraryDeclaration());
                }
            }
        } catch (IOException e) {
            if (e instanceof java.nio.file.NoSuchFileException) {
                throw new ErrorHandler.VGFileException(
                    "Library file not found: " + filePath + ". Make sure the Software is installed correctly or that the file exists.",
                    currentLine, currentColumn
                );
            } else {
                throw new ErrorHandler.VGFileException(
                    "Error reading library file: " + filePath + " - " + e.getMessage(),
                    currentLine, currentColumn
                );
            }
        }
    }

    private void registerBuiltInFunction() {
        BuiltInFunction VgSystemCall = new BuiltInFunction() {
            @Override
            public Object call(List<Object> args) {
                SystemCallHandler handler = new SystemCallHandler(allowedMethods, allowedClasses, currentLine, currentColumn);
                return handler.handleSystemCall(args);
            }
        };
        BuiltInFunctionWrapper wrappedVgSystemCall = new BuiltInFunctionWrapper(VgSystemCall, this);
        builtInFunction.put("VgSystemCall", VgSystemCall);
        globalSymbolTable.setFunction("VgSystemCall", wrappedVgSystemCall);
        globalSymbolTable.set("VgSystemCall", wrappedVgSystemCall);
    }

    private void loadLangConfigFile(String filepath) {
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(filepath)), StandardCharsets.UTF_8);

            if (fileContent.startsWith("ENCRYPTED:")) {
                String encryptedPart = fileContent.substring("ENCRYPTED:".length());
                fileContent = CryptoUtil.decrypt(encryptedPart);
                System.out.println("Decrypted the configuration file.");
            }

            List<String> lines = Arrays.asList(fileContent.split("\\r?\\n"));
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split(":");
                if (parts.length != 2) {
                    throw new RuntimeException("Invalid config line (missing colon): " + line);
                }
                String className = parts[0].trim();
                allowedClasses.add(className);

                String methodsPart = parts[1].trim();
                String[] methods = methodsPart.split(",");
                Set<String> methodsSet = new HashSet<>();
                for (String method : methods) {
                    method = method.trim();
                    if (!method.isEmpty()) {
                        methodsSet.add(method);
                    }
                }
                allowedMethods.put(className, methodsSet);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading allowed configuration file: " + e.getMessage(), e);
        }
    }

    // Getters for current line and column
    public int getCurrentLine() {
        return currentLine;
    }

    public int getCurrentColumn() {
        return currentColumn;
    }

    // Setters for current line and column
    public void setCurrentLine(int line) {
        this.currentLine = line;
    }

    public void setCurrentColumn(int column) {
        this.currentColumn = column;
    }

    // Delegate visitor methods to specialized visitors
    @Override
    public Object visitProgram(vg_langParser.ProgramContext ctx) {
        return programVisitor.visitProgram(ctx);
    }

    @Override
    public Object visitFunctionDeclaration(vg_langParser.FunctionDeclarationContext ctx) {
        // Update position for debugging and check if we should skip
        if (ctx.getStart() != null) {
            updatePosition(ctx.getStart());
            
            // After updatePosition (which may have triggered a pause and step-over command),
            // check if we should skip this statement
            if (skipNextStatement) {
                System.out.println("Debug: Skipping function declaration due to step-over");
                skipNextStatement = false; // Reset the flag
                return null; // Skip the function declaration entirely
            }
        }
        
        return functionVisitor.visitFunctionDeclaration(ctx);
    }

    @Override
    public Object visitFunctionCall(vg_langParser.FunctionCallContext ctx) {
        return functionVisitor.visitFunctionCall(ctx);
    }

    @Override
    public Object visitFunctionReference(vg_langParser.FunctionReferenceContext ctx) {
        return functionVisitor.visitFunctionReference(ctx);
    }

    @Override
    public Object visitReturnStatement(vg_langParser.ReturnStatementContext ctx) {
        return simpleStatementVisitor.visitReturnStatement(ctx);
    }

    @Override
    public Object visitVariableDeclaration(vg_langParser.VariableDeclarationContext ctx) {
        // Update position for debugging
        if (ctx.getStart() != null) {
            updatePosition(ctx.getStart());
        }
        return declarationVisitor.visitVariableDeclaration(ctx);
    }

    @Override
    public Object visitVariableDeclarationNoSemi(vg_langParser.VariableDeclarationNoSemiContext ctx) {
        return declarationVisitor.visitVariableDeclarationNoSemi(ctx);
    }

    @Override
    public Object visitConstDeclaration(vg_langParser.ConstDeclarationContext ctx) {
        return declarationVisitor.visitConstDeclaration(ctx);
    }

    @Override
    public Object visitStructDeclaration(vg_langParser.StructDeclarationContext ctx) {
        return declarationVisitor.visitStructDeclaration(ctx);
    }

    @Override
    public Object visitEnumDeclaration(vg_langParser.EnumDeclarationContext ctx) {
        return declarationVisitor.visitEnumDeclaration(ctx);
    }

    @Override
    public Object visitClassDeclaration(vg_langParser.ClassDeclarationContext ctx) {
        return classVisitor.visitClassDeclaration(ctx);
    }

    @Override
    public Object visitNewExpression(vg_langParser.NewExpressionContext ctx) {
        return classVisitor.visitNewExpression(ctx);
    }

    @Override
    public Object visitAssignment(vg_langParser.AssignmentContext ctx) {
        // Update position for debugging
        if (ctx.getStart() != null) {
            updatePosition(ctx.getStart());
        }
        return variableVisitor.visitAssignment(ctx);
    }

    @Override
    public Object visitAssignmentNoSemi(vg_langParser.AssignmentNoSemiContext ctx) {
        return variableVisitor.visitAssignmentNoSemi(ctx);
    }

    @Override
    public Object visitLeftHandSide(vg_langParser.LeftHandSideContext ctx) {
        return variableVisitor.visitLeftHandSide(ctx);
    }

    @Override
    public Object visitPrintStatement(vg_langParser.PrintStatementContext ctx) {
        // Update position for debugging
        if (ctx.getStart() != null) {
            updatePosition(ctx.getStart());
        }
        return simpleStatementVisitor.visitPrintStatement(ctx);
    }

    @Override
    public Object visitIfStatement(vg_langParser.IfStatementContext ctx) {
        // Update position for debugging
        if (ctx.getStart() != null) {
            updatePosition(ctx.getStart());
        }
        return statementVisitor.visitIfStatement(ctx);
    }

    @Override
    public Object visitForStatement(vg_langParser.ForStatementContext ctx) {
        return statementVisitor.visitForStatement(ctx);
    }

    @Override
    public Object visitForEachStatement(vg_langParser.ForEachStatementContext ctx) {
        return statementVisitor.visitForEachStatement(ctx);
    }

    @Override
    public Object visitWhileStatement(vg_langParser.WhileStatementContext ctx) {
        return statementVisitor.visitWhileStatement(ctx);
    }

    @Override
    public Object visitDoWhileStatement(vg_langParser.DoWhileStatementContext ctx) {
        return statementVisitor.visitDoWhileStatement(ctx);
    }

    @Override
    public Object visitSwitchStatement(vg_langParser.SwitchStatementContext ctx) {
        return statementVisitor.visitSwitchStatement(ctx);
    }

    @Override
    public Object visitBreakStatement(vg_langParser.BreakStatementContext ctx) {
        return statementVisitor.visitBreakStatement(ctx);
    }

    @Override
    public Object visitContinueStatement(vg_langParser.ContinueStatementContext ctx) {
        return statementVisitor.visitContinueStatement(ctx);
    }

    @Override
    public Object visitTryStatement(vg_langParser.TryStatementContext ctx) {
        return statementVisitor.visitTryStatement(ctx);
    }

    @Override
    public Object visitThrowStatement(vg_langParser.ThrowStatementContext ctx) {
        return simpleStatementVisitor.visitThrowStatement(ctx);
    }

    @Override
    public Object visitLibraryDeclaration(vg_langParser.LibraryDeclarationContext ctx) {
        return importVisitor.visitLibraryDeclaration(ctx);
    }

    @Override
    public Object visitNamespaceDeclaration(vg_langParser.NamespaceDeclarationContext ctx) {
        return importVisitor.visitNamespaceDeclaration(ctx);
    }

    @Override
    public Object visitImportStatement(vg_langParser.ImportStatementContext ctx) {
        return importVisitor.visitImportStatement(ctx);
    }

    @Override
    public Object visitLogicalOrExpression(vg_langParser.LogicalOrExpressionContext ctx) {
        return expressionVisitor.visitLogicalOrExpression(ctx);
    }

    @Override
    public Object visitEqualityExpression(vg_langParser.EqualityExpressionContext ctx) {
        return expressionVisitor.visitEqualityExpression(ctx);
    }

    @Override
    public Object visitRelationalExpression(vg_langParser.RelationalExpressionContext ctx) {
        return expressionVisitor.visitRelationalExpression(ctx);
    }

    @Override
    public Object visitAdditiveExpression(vg_langParser.AdditiveExpressionContext ctx) {
        return arithmeticVisitor.visitAdditiveExpression(ctx);
    }

    @Override
    public Object visitMultiplicativeExpression(vg_langParser.MultiplicativeExpressionContext ctx) {
        return arithmeticVisitor.visitMultiplicativeExpression(ctx);
    }

    @Override
    public Object visitUnaryExpression(vg_langParser.UnaryExpressionContext ctx) {
        return basicExpressionVisitor.visitUnaryExpression(ctx);
    }

    @Override
    public Object visitPrimary(vg_langParser.PrimaryContext ctx) {
        return basicExpressionVisitor.visitPrimary(ctx);
    }

    @Override
    public Object visitLiteral(vg_langParser.LiteralContext ctx) {
        return literalVisitor.visitLiteral(ctx);
    }

    @Override
    public Object visitArrayLiteral(vg_langParser.ArrayLiteralContext ctx) {
        return literalVisitor.visitArrayLiteral(ctx);
    }

    @Override
    public Object visitPostfixExpression(vg_langParser.PostfixExpressionContext ctx) {
        return basicExpressionVisitor.visitPostfixExpression(ctx);
    }

    private boolean toBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue() != 0;
        }
        if (value instanceof String) {
            return !((String) value).isEmpty();
        }
        return value != null;
    }

    private String getVGTypeName(Object value) {
        if (value == null) return "null";
        if (value instanceof Integer) return "int";
        if (value instanceof Double) return "double";
        if (value instanceof Boolean) return "boolean";
        if (value instanceof String) return "string";
        if (value instanceof List) return "array";
        if (value instanceof Map) return "struct";
        if (value instanceof Function) return "function";
        if (value instanceof Namespace) return "namespace";
        if (value instanceof Library) return "library";
        if (value instanceof components.Enum) return "enum";
        if (value instanceof ClassDefinition) return "class";
        if (value instanceof ClassInstance) return ((ClassInstance) value).getClassName();
        if (value instanceof MethodReference) return "method";
        if (value instanceof StaticMethodReference) return "static_method";
        
        String className = value.getClass().getName();
        if (className.startsWith("java.")) {
            return className.substring(className.lastIndexOf('.') + 1).toLowerCase();
        }
        return value.getClass().getSimpleName().toLowerCase();
    }

    public Function resolveFunctionFromNamespace(String functionPath) {
        String[] parts = functionPath.split("\\.");
        if (parts.length == 1) {
            return globalSymbolTable.getFunction(parts[0]);
        }

        Library lib = moduleRegistry.getLibrary(parts[0]);
        if (lib == null) {
            throw new RuntimeException("Library not found: " + parts[0]);
        }

        Namespace namespace = lib.getNamespace(parts[1]);
        if (namespace == null) {
            throw new RuntimeException("Namespace not found: " + parts[1]);
        }

        if (parts.length == 3) {
            Function function = (Function) namespace.getSymbol(parts[2]);
            if (function == null) {
                throw new RuntimeException("Function not found: " + parts[2]);
            }
            return function;
        }

        throw new RuntimeException("Invalid function path: " + functionPath);
    }

    public Object interpret(String code) {
        try {
            CharStream input = CharStreams.fromString(code);
            vg_langLexer lexer = new vg_langLexer(input);
            
            lexer.removeErrorListeners();
            lexer.addErrorListener(new VGErrorListener());
            
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            vg_langParser parser = new vg_langParser(tokens);
            
            parser.removeErrorListeners();
            parser.addErrorListener(new VGErrorListener());
            
            vg_langParser.ProgramContext programCtx = parser.program();
            return visit(programCtx);
        } catch (ErrorHandler.VGException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorHandler.VGException("Error interpreting code: " + e.getMessage(), currentLine, currentColumn);
        }
    }

    // Debug support methods
    public void enableDebugMode() {
        this.debugMode = true;
        this.debugScanner = new Scanner(System.in);
        startCommandListener();
    }

    public void addBreakpoint(int lineNumber) {
        this.breakpoints.add(lineNumber);
    }

    public void removeBreakpoint(int lineNumber) {
        this.breakpoints.remove(lineNumber);
    }

    public void clearBreakpoints() {
        this.breakpoints.clear();
    }

    public void checkBreakpointAt(int line) {
        checkBreakpoint(line);
    }

    public void incrementCallDepth() {
        currentCallDepth++;
    }

    public void decrementCallDepth() {
        currentCallDepth--;
    }

    private void checkBreakpoint(int line) {
        if (!debugMode) return;
        
        currentLine = line;
        
        boolean shouldPause = false;
        
        // Check if we should pause based on different conditions
        if (breakpoints.contains(line)) {
            shouldPause = true;
        } else if (debugStepInto) {
            shouldPause = true;
        } else if (debugStepOver && currentCallDepth <= stepOverDepth) {
            shouldPause = true;
        } else if (debugStepOut && currentCallDepth <= stepOutTargetDepth) {
            shouldPause = true;
        }
        
        if (shouldPause) {
            debugPaused = true;
            pauseAllTimers(); // Pause timers when debugging starts
            System.out.println("Debug: Paused at line " + line + " (depth: " + currentCallDepth + ")");
            
            // Automatically output variables and functions for IDE
            System.out.println("DEBUG_VARIABLES_START");
            printCurrentVariablesForIDE();
            System.out.println("DEBUG_VARIABLES_END");
            
            System.out.println("DEBUG_FUNCTIONS_START");
            printCurrentFunctionsForIDE();
            System.out.println("DEBUG_FUNCTIONS_END");
            
            while (debugPaused) {
                System.out.print("Debug> ");
                System.out.flush(); // Ensure prompt is shown immediately
                
                String command = debugScanner.nextLine().trim().toLowerCase();
                
                switch (command) {
                    case "continue":
                    case "c":
                        debugPaused = false;
                        debugStepping = false;
                        debugStepInto = false;
                        debugStepOver = false;
                        debugStepOut = false;
                        resumeAllTimers(); // Resume timers when continuing
                        break;
                    case "step":
                    case "s":
                    case "step_into":
                        debugPaused = false;
                        debugStepping = true;
                        debugStepInto = true;
                        debugStepOver = false;
                        debugStepOut = false;
                        resumeAllTimers(); // Resume timers when stepping
                        break;
                    case "step_over":
                    case "so":
                        debugPaused = false;
                        debugStepping = true;
                        debugStepInto = false;
                        debugStepOver = true;
                        debugStepOut = false;
                        stepOverDepth = currentCallDepth;
                        // Set flag to skip the current statement we're paused at
                        skipNextStatement = true;
                        resumeAllTimers(); // Resume timers when stepping over
                        break;
                    case "step_out":
                    case "sout":
                        debugPaused = false;
                        debugStepping = true;
                        debugStepInto = false;
                        debugStepOver = false;
                        debugStepOut = true;
                        stepOutTargetDepth = currentCallDepth - 1;
                        resumeAllTimers(); // Resume timers when stepping out
                        break;
                    case "variables":
                    case "vars":
                    case "v":
                        printCurrentVariables();
                        printCurrentFunctions();
                        break;
                    case "help":
                    case "h":
                        printDebugHelp();
                        break;
                    case "quit":
                    case "q":
                        System.exit(0);
                        break;
                    case "addbreak":
                    case "ab":
                        // Add breakpoint command format: addbreak 123
                        if (command.contains(" ")) {
                            try {
                                String[] parts = command.split("\\s+");
                                if (parts.length >= 2) {
                                    int lineNum = Integer.parseInt(parts[1]);
                                    breakpoints.add(lineNum);
                                    System.out.println("Debug: Added breakpoint at line " + lineNum);
                                } else {
                                    System.out.println("Usage: addbreak <line_number>");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid line number. Usage: addbreak <line_number>");
                            }
                        } else {
                            System.out.println("Usage: addbreak <line_number>");
                        }
                        break;
                    case "removebreak":
                    case "rb":
                        // Remove breakpoint command format: removebreak 123
                        if (command.contains(" ")) {
                            try {
                                String[] parts = command.split("\\s+");
                                if (parts.length >= 2) {
                                    int lineNum = Integer.parseInt(parts[1]);
                                    if (breakpoints.remove(lineNum)) {
                                        System.out.println("Debug: Removed breakpoint at line " + lineNum);
                                    } else {
                                        System.out.println("No breakpoint found at line " + lineNum);
                                    }
                                } else {
                                    System.out.println("Usage: removebreak <line_number>");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid line number. Usage: removebreak <line_number>");
                            }
                        } else {
                            System.out.println("Usage: removebreak <line_number>");
                        }
                        break;
                    case "listbreaks":
                    case "lb":
                        System.out.println("Current breakpoints:");
                        if (breakpoints.isEmpty()) {
                            System.out.println("  No breakpoints set");
                        } else {
                            for (int breakpointLine : breakpoints.stream().sorted().collect(java.util.stream.Collectors.toList())) {
                                System.out.println("  Line " + breakpointLine);
                            }
                        }
                        break;
                    default:
                        // Check if it's a dynamic breakpoint command from IDE
                        if (command.startsWith("addbreak ")) {
                            try {
                                int lineNum = Integer.parseInt(command.substring(9));
                                breakpoints.add(lineNum);
                                System.out.println("Debug: Added breakpoint at line " + lineNum);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid line number in addbreak command");
                            }
                        } else if (command.startsWith("removebreak ")) {
                            try {
                                int lineNum = Integer.parseInt(command.substring(12));
                                if (breakpoints.remove(lineNum)) {
                                    System.out.println("Debug: Removed breakpoint at line " + lineNum);
                                } else {
                                    System.out.println("No breakpoint found at line " + lineNum);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid line number in removebreak command");
                            }
                        } else {
                            System.out.println("Unknown command: " + command + ". Type 'help' for available commands.");
                        }
                }
            }
        }
    }

    private void printCurrentVariables() {
        System.out.println("Current variables:");
        SymbolTable currentScope = symbolTableStack.peek();
        if (currentScope != null && !currentScope.getVariables().isEmpty()) {
            currentScope.getVariables().forEach((name, value) -> {
                System.out.println("  " + name + " = " + value);
            });
        } else {
            System.out.println("  No variables defined");
        }
    }

    private void printCurrentFunctions() {
        System.out.println("Defined functions:");
        // Check all symbol tables for functions (starting from global)
        boolean foundFunctions = false;
        for (SymbolTable scope : symbolTableStack) {
            if (!scope.getFunctions().isEmpty()) {
                scope.getFunctions().forEach((name, function) -> {
                    System.out.println("  " + name + "()");
                });
                foundFunctions = true;
            }
        }
        if (!foundFunctions) {
            System.out.println("  No user-defined functions");
        }
    }

    private void printCurrentVariablesForIDE() {
        SymbolTable currentScope = symbolTableStack.peek();
        if (currentScope != null && !currentScope.getVariables().isEmpty()) {
            currentScope.getVariables().forEach((name, value) -> {
                // Only show variables that are NOT built-in functions or constants
                if (!builtInFunction.containsKey(name) && 
                    !name.equals("true") && !name.equals("false")) {
                    System.out.println(name + "=" + value);
                }
            });
        }
    }

    private void printCurrentFunctionsForIDE() {
        // Check all symbol tables for functions, but exclude built-in functions
        for (SymbolTable scope : symbolTableStack) {
            if (!scope.getFunctions().isEmpty()) {
                scope.getFunctions().forEach((name, function) -> {
                    // Only show functions that are NOT built-in functions
                    if (!builtInFunction.containsKey(name)) {
                        System.out.println(name);
                    }
                });
            }
        }
    }

    private void printDebugHelp() {
        System.out.println("Debug commands:");
        System.out.println("  continue (c)       - Continue execution");
        System.out.println("  step (s)           - Step into next line");
        System.out.println("  step_over (so)     - Step over function calls");
        System.out.println("  step_out (sout)    - Step out of current function");
        System.out.println("  variables (v)      - Show current variables and functions");
        System.out.println("  addbreak <line>    - Add breakpoint at line number");
        System.out.println("  removebreak <line> - Remove breakpoint at line number");
        System.out.println("  listbreaks (lb)    - List all current breakpoints");
        System.out.println("  help (h)           - Show this help");
        System.out.println("  quit (q)           - Exit program");
    }
    
    /**
     * Pause all running timers during debugging
     */
    private void pauseAllTimers() {
        try {
            synchronized (globalTimers) {
                // Pause all running timers from the global registry
                for (javax.swing.Timer timer : globalTimers) {
                    if (timer != null && timer.isRunning()) {
                        timer.stop();
                        pausedTimers.add(timer);
                    }
                }
                if (pausedTimers.size() > 0) {
                    System.out.println("Debug: Paused " + pausedTimers.size() + " timers");
                }
            }
        } catch (Exception e) {
            // If timer pausing fails, continue debugging anyway
            System.err.println("Warning: Could not pause timers: " + e.getMessage());
        }
    }
    
    /**
     * Resume all paused timers when debugging continues
     */
    private void resumeAllTimers() {
        try {
            for (javax.swing.Timer timer : pausedTimers) {
                if (timer != null) {
                    timer.start();
                }
            }
            if (pausedTimers.size() > 0) {
                System.out.println("Debug: Resumed " + pausedTimers.size() + " timers");
            }
            pausedTimers.clear();
        } catch (Exception e) {
            // If timer resuming fails, continue anyway
            System.err.println("Warning: Could not resume timers: " + e.getMessage());
        }
    }
    
    /**
     * Start background thread to listen for debug commands while program is running
     */
    private void startCommandListener() {
        if (commandListenerThread != null && commandListenerThread.isAlive()) {
            return; // Already running
        }
        
        commandListenerThread = new Thread(() -> {
            while (isRunning && debugMode) {
                try {
                    if (debugScanner.hasNextLine()) {
                        String command = debugScanner.nextLine().trim();
                        processRuntimeCommand(command);
                    }
                    Thread.sleep(10); // Small delay to prevent busy waiting
                } catch (Exception e) {
                    // Continue listening even if there's an error
                }
            }
        });
        commandListenerThread.setDaemon(true); // Don't prevent JVM shutdown
        commandListenerThread.start();
    }
    
    /**
     * Process commands received while program is running (not paused)
     */
    private void processRuntimeCommand(String command) {
        String[] parts = command.split("\\s+");
        String cmd = parts[0].toLowerCase();
        
        try {
            switch (cmd) {
                case "addbreak":
                case "add_breakpoint":
                    if (parts.length > 1) {
                        int line = Integer.parseInt(parts[1]);
                        breakpoints.add(line);
                        System.out.println("Debug: Added breakpoint at line " + line);
                    }
                    break;
                case "removebreak":
                case "remove_breakpoint":
                    if (parts.length > 1) {
                        int line = Integer.parseInt(parts[1]);
                        breakpoints.remove(line);
                        System.out.println("Debug: Removed breakpoint at line " + line);
                    }
                    break;
                case "list_breakpoints":
                case "breakpoints":
                    System.out.println("Active breakpoints: " + breakpoints);
                    break;
                case "quit":
                case "q":
                    isRunning = false;
                    cleanup();
                    System.exit(0);
                    break;
                default:
                    // Ignore unknown commands during runtime
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error processing command: " + e.getMessage());
        }
    }
    
    /**
     * Cleanup resources when debugging ends
     */
    private void cleanup() {
        isRunning = false;
        if (commandListenerThread != null) {
            commandListenerThread.interrupt();
        }
    }
}