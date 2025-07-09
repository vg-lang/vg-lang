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
    public Object visitAssignment(vg_langParser.AssignmentContext ctx) {
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
        return simpleStatementVisitor.visitPrintStatement(ctx);
    }

    @Override
    public Object visitIfStatement(vg_langParser.IfStatementContext ctx) {
        return statementVisitor.visitIfStatement(ctx);
    }

    @Override
    public Object visitForStatement(vg_langParser.ForStatementContext ctx) {
        return statementVisitor.visitForStatement(ctx);
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
}