package components;

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

public class Interpreter extends vg_langBaseVisitor {
    public Deque<SymbolTable> symbolTableStack = new ArrayDeque<>();
    private Map<String, Set<String>> allowedMethods = new HashMap<>();
    private Set<String> allowedClasses = new HashSet<>();
    public Map<String, BuiltInFunction> builtInFunction = new HashMap<>();

    SymbolTable globalSymbolTable;
    ModuleRegistry moduleRegistry;
    private String libraryFolder = System.getenv("VG_LIBRARIES_PATH");
    private int currentLine = 0;
    private int currentColumn = 0;

    public Interpreter(String projectPackageFolder) {
        globalSymbolTable = new SymbolTable();
        symbolTableStack.push(globalSymbolTable);
        
        globalSymbolTable.setConstant("true", true);
        globalSymbolTable.setConstant("false", false);
        
        registerBuiltInFunction();

        String configPath = System.getenv("VG_APP_CONFIG");
        loadLangConfigFile(configPath + "/allowed_configurations.vgenv");
        moduleRegistry = new ModuleRegistry();
        loadLibrariesFromFolder(libraryFolder);
        loadLibrariesFromFolder(projectPackageFolder);
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

    public void processLibraryDeclaration(vg_langParser.LibraryDeclarationContext ctx) {
        String libraryName = ctx.IDENTIFIER().getText();
        Library library = new Library(libraryName);
        for (vg_langParser.NamespaceDeclarationContext nsCtx : ctx.namespaceDeclaration()) {
            String nsName = nsCtx.IDENTIFIER().getText();
            Namespace namespace = new Namespace(nsName);
            processNamespaceBody(nsCtx, namespace);
            library.addNamespace(namespace);
        }
        moduleRegistry.addLibrary(library);
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
                    processLibraryDeclaration(stmtCtx.libraryDeclaration());
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

    private void processNamespaceBody(vg_langParser.NamespaceDeclarationContext nsCtx, Namespace namespace) {
        for (vg_langParser.FunctionDeclarationContext funcCtx : nsCtx.functionDeclaration()) {
            String functionName = funcCtx.IDENTIFIER().getText();
            Function function = new Function(
                    getParameters(funcCtx.parameterList()),
                    funcCtx.block(),
                    this
            );
            namespace.addSymbol(functionName, function);
        }

        for (vg_langParser.VariableDeclarationContext varCtx : nsCtx.variableDeclaration()) {
            String varName = varCtx.IDENTIFIER().getText();
            Object value = visit(varCtx.expression());
            namespace.addSymbol(varName, value);
        }
        for (vg_langParser.ConstDeclarationContext constCtx : nsCtx.constDeclaration()) {
            String constName = constCtx.IDENTIFIER().getText();
            Object value = visit(constCtx.expression());
            namespace.addSymbol(constName, value);
        }
        for (vg_langParser.NamespaceDeclarationContext childNsCtx : nsCtx.namespaceDeclaration()) {
            String childName = childNsCtx.IDENTIFIER().getText();
            Namespace childNs = new Namespace(childName);
            processNamespaceBody(childNsCtx, childNs);
            namespace.addChildNamespace(childNs);
        }
    }

    public void processImport(String importPath) {
        try {
            importPath = importPath.trim();
            if (importPath.endsWith(";")) {
                importPath = importPath.substring(0, importPath.length() - 1);
            }

            String[] rawParts = importPath.split("\\.");
            List<String> partsList = new ArrayList<>();
            for (String part : rawParts) {
                if (!part.isEmpty()) {
                    partsList.add(part);
                }
            }
            String[] parts = partsList.toArray(new String[0]);

            if (parts.length < 2) {
                throw new ErrorHandler.VGImportException(
                    "Invalid import path: " + importPath + ". Format should be 'library.namespace[.symbol]'",
                    currentLine > 0 ? currentLine : 1,
                    currentColumn > 0 ? currentColumn : 0
                );
            }

            String libName = parts[0];
            Library lib = moduleRegistry.getLibrary(libName);
            if (lib == null) {
                try {
                    String libraryFilePath = libraryFolder + "/" + libName + ".vglib";
                    loadLibraryFile(libraryFilePath);
                    lib = moduleRegistry.getLibrary(libName);
                    if (lib == null) {
                        throw new ErrorHandler.VGImportException(
                            "Library '" + libName + "' not found after attempting to load it",
                            currentLine, currentColumn
                        );
                    }

                } catch (ErrorHandler.VGFileException e) {
                    throw new ErrorHandler.VGImportException(
                        "Cannot import library '" + libName + "': " + e.getMessage(),
                        currentLine, currentColumn
                    );
                } catch (RuntimeException e) {
                    if (e.getCause() instanceof IOException) {
                        throw new ErrorHandler.VGFileException(
                            "Cannot import library '" + libName + "': Library file not found or cannot be read. " +
                            "Check if the library is installed in " + libraryFolder,
                            currentLine, currentColumn
                        );
                    } else {
                        throw e;
                    }
                }
            }

            if (parts[parts.length - 1].equals("*")) {
                String[] nsPath = Arrays.copyOfRange(parts, 1, parts.length - 1);
                Namespace ns;
                if (nsPath.length == 1) {
                    ns = lib.getNamespace(nsPath[0]);
                } else {
                    ns = lib.getNamespace(nsPath[0]);
                    if (ns == null) {
                        throw new RuntimeException("Namespace not found: " + nsPath[0]);
                    }
                    ns = ns.getNestedNamespace(nsPath, 1);
                }
                if (ns == null) {
                    throw new RuntimeException("Nested namespace not found for path: " + String.join(".", nsPath));
                }

                for (Map.Entry<String, Object> entry : ns.getSymbols().entrySet()) {
                    if (entry.getValue() instanceof Function) {
                        globalSymbolTable.setFunction(entry.getKey(), (Function) entry.getValue());
                        globalSymbolTable.set(entry.getKey(), entry.getValue());
                    } else {
                        globalSymbolTable.set(entry.getKey(), entry.getValue());
                    }
                }

                return;
            }

            if (parts.length == 2) {
                String nsName = parts[1];
                Namespace ns = lib.getNamespace(nsName);
                if (ns == null) {
                    throw new RuntimeException("Namespace not found: " + nsName);
                }
                globalSymbolTable.set(nsName, ns);
                return;
            }

            if (parts.length >= 3) {
                String symbolName = parts[parts.length - 1];

                String[] nsPath = Arrays.copyOfRange(parts, 1, parts.length - 1);
                Namespace ns;
                if (nsPath.length == 1) {
                    ns = lib.getNamespace(nsPath[0]);
                } else {
                    ns = lib.getNamespace(nsPath[0]);
                    if (ns == null) {
                        throw new RuntimeException("Namespace not found: " + nsPath[0]);
                    }
                    ns = ns.getNestedNamespace(nsPath, 1);
                }
                if (ns == null) {
                    throw new RuntimeException("Nested namespace not found for path: " + String.join(".", nsPath));
                }
                Object symbol = ns.getSymbol(symbolName);
                if (symbol == null) {
                    symbol = ns.getChildNamespace(symbolName);
                    if (symbol == null) {
                        throw new RuntimeException("Symbol or nested namespace not found: " + symbolName);
                    }
                }
                if (symbol instanceof Function) {
                    globalSymbolTable.setFunction(symbolName, (Function) symbol);
                    globalSymbolTable.set(symbolName, symbol);
                } else {
                    globalSymbolTable.set(symbolName, symbol);
                }
            }

        } catch (ErrorHandler.VGException e) {
            if (e.getLine() > 0) {
                throw e;
            }
            throw new ErrorHandler.VGException(e.getMessage(), 
                currentLine > 0 ? currentLine : 1,
                currentColumn > 0 ? currentColumn : 0);
        } catch (Exception e) {
            throw new ErrorHandler.VGImportException(
                "Error importing '" + importPath + "': " + e.getMessage(),
                currentLine > 0 ? currentLine : 1,
                currentColumn > 0 ? currentColumn : 0
            );
        }
    }

    private void importNamespace(Namespace ns) {
        for (Map.Entry<String, Object> entry : ns.getSymbols().entrySet()) {
            globalSymbolTable.set(entry.getKey(), entry.getValue());
        }
    }

    private java.util.List<String> getParameters(vg_langParser.ParameterListContext paramCtx) {
        java.util.List<String> params = new java.util.ArrayList<>();
        if (paramCtx != null) {
            for (var id : paramCtx.IDENTIFIER()) {
                params.add(id.getText());
            }
        }
        return params;
    }

    private SymbolTable currentSymbolTable() {
        return symbolTableStack.peek();
    }

    private Object getVariable(String name) {
        for (SymbolTable table : symbolTableStack) {
            if (table.contains(name)) {
                return table.get(name);
            }
        }
        throw new RuntimeException("Variable '" + name + "' is not defined.");
    }

    private void registerBuiltInFunction() {
        BuiltInFunction VgSystemCall = new BuiltInFunction() {
            @Override
            public Object call(List<Object> args) {
                return VgSystemCall(args);
            }
        };
        BuiltInFunctionWrapper wrappedVgSystemCall = new BuiltInFunctionWrapper(VgSystemCall, this);
        builtInFunction.put("VgSystemCall", VgSystemCall);
        globalSymbolTable.setFunction("VgSystemCall", wrappedVgSystemCall);
        globalSymbolTable.set("VgSystemCall", wrappedVgSystemCall);
    }

    private boolean isMethodAllowed(Class<?> vgclass, String methodName) {
        while (vgclass != null) {
            String className = vgclass.getName();
            if (allowedClasses.contains(className)) {
                Set<String> classMethods = allowedMethods.get(className);
                if (classMethods != null && classMethods.contains(methodName)) {
                    return true;
                }
            }
            vgclass = vgclass.getSuperclass();
        }
        return false;
    }

    private Constructor<?> findConstructor(Class<?> vgclass, List<Object> args) {
        for (Constructor<?> constructor : vgclass.getConstructors()) {
            if (constructor.getParameterCount() == args.size()) {
                if (matchParameterTypes(constructor.getParameterTypes(), args)) {
                    return constructor;
                }
            }
        }
        return null;
    }

    private boolean matchParameterTypes(Class<?>[] paramTypes, List<Object> args) {
        for (int i = 0; i < paramTypes.length; i++) {
            Object arg = args.get(i);
            Class<?> paramType = paramTypes[i];
            if (!isAssignable(paramType, arg)) {
                return false;
            }
        }
        return true;
    }

    private boolean isAssignable(Class<?> paramType, Object arg) {
        if (arg instanceof LanguageObjectWrapper) {
            arg = ((LanguageObjectWrapper) arg).getLanguageObject();
        }

        if (paramType.isPrimitive()) {
            paramType = getWrappedclasses(paramType);
        }

        if (arg == null) {
            return !paramType.isPrimitive();
        }

        return paramType.isAssignableFrom(arg.getClass());
    }

    private Class<?> getWrappedclasses(Class<?> primitiveType) {
        if (primitiveType == int.class) {
            return Integer.class;
        } else if (primitiveType == double.class) {
            return Double.class;
        } else if (primitiveType == boolean.class) {
            return Boolean.class;
        } else if (primitiveType == char.class) {
            return Character.class;
        } else if (primitiveType == long.class) {
            return Long.class;
        } else if (primitiveType == short.class) {
            return Short.class;
        } else if (primitiveType == byte.class) {
            return Byte.class;
        } else if (primitiveType == float.class) {
            return Float.class;
        } else {
            return primitiveType;
        }
    }

    private Method findMethod(Class<?> vgclass, String methodName, List<Object> args) {
        while (vgclass != null) {
            for (Method method : vgclass.getDeclaredMethods()) {
                if (method.getName().equals(methodName) && method.getParameterCount() == args.size()) {
                    if (matchParameterTypes(method.getParameterTypes(), args)) {
                        return method;
                    }
                }
            }
            vgclass = vgclass.getSuperclass();
        }
        return null;
    }

    private Object[] convertArguments(List<Object> args, AccessibleObject accessibleObject) {
        Class<?>[] paramTypes;
        if (accessibleObject instanceof Method) {
            paramTypes = ((Method) accessibleObject).getParameterTypes();
        } else if (accessibleObject instanceof Constructor<?>) {
            paramTypes = ((Constructor<?>) accessibleObject).getParameterTypes();
        } else {
            throw new IllegalArgumentException("AccessibleObject must be a Method or Constructor");
        }

        Object[] javaArgs = new Object[args.size()];
        for (int i = 0; i < args.size(); i++) {
            Object arg = args.get(i);
            Class<?> paramType = paramTypes[i];
            if (paramType == boolean.class || paramType == Boolean.class) {
                if (arg instanceof Boolean) {
                    javaArgs[i] = arg;
                } else if (arg instanceof Number) {
                    int num = ((Number) arg).intValue();
                    if (num == 1) {
                        javaArgs[i] = true;
                    } else if (num == 0) {
                        javaArgs[i] = false;
                    } else {
                        throw new RuntimeException("Invalid number for boolean parameter: " + num);
                    }
                } else if (arg instanceof String) {
                    String str = (String) arg;
                    if (str.equals("1")) {
                        javaArgs[i] = true;
                    } else if (str.equals("0")) {
                        javaArgs[i] = false;
                    } else {
                        throw new RuntimeException("Invalid string for boolean parameter: " + str);
                    }
                } else {
                    throw new RuntimeException("Invalid type for boolean parameter: " + arg.getClass().getName());
                }
                continue;
            }
            if (arg instanceof LanguageObjectWrapper) {
                arg = ((LanguageObjectWrapper) arg).getLanguageObject();
            }

            if (paramType.isPrimitive()) {
                paramType = getWrappedclasses(paramType);
            }
            if (paramType.isArray() && paramType.getComponentType() == Object.class && arg instanceof List) {
                List<?> outerList = (List<?>) arg;
                Object[][] javaArray = new Object[outerList.size()][];
                for (int j = 0; j < outerList.size(); j++) {
                    Object inner = outerList.get(j);
                    if (inner instanceof List) {
                        List<?> innerList = (List<?>) inner;
                        Object[] innerArray = new Object[innerList.size()];
                        for (int k = 0; k < innerList.size(); k++) {
                            innerArray[k] = innerList.get(k);
                        }
                        javaArray[j] = innerArray;
                    } else {
                        throw new RuntimeException("Each series must be a list containing a name and values.");
                    }
                }
                javaArgs[i] = javaArray;
                continue;
            }
            if (arg == null) {
                javaArgs[i] = null;
            } else if (paramType.isAssignableFrom(arg.getClass())) {
                javaArgs[i] = arg;
            } else if (arg instanceof Number) {
                Number num = (Number) arg;
                if (paramType == Integer.class) {
                    javaArgs[i] = num.intValue();
                } else if (paramType == Double.class) {
                    javaArgs[i] = num.doubleValue();
                } else if (paramType == Float.class) {
                    javaArgs[i] = num.floatValue();
                } else if (paramType == Long.class) {
                    javaArgs[i] = num.longValue();
                } else if (paramType == Short.class) {
                    javaArgs[i] = num.shortValue();
                } else if (paramType == Byte.class) {
                    javaArgs[i] = num.byteValue();
                } else {
                    throw new RuntimeException("Cannot convert number to " + paramType.getName());
                }
            } else if (paramType == String.class) {
                javaArgs[i] = arg.toString();
            } else {
                throw new RuntimeException("Cannot convert argument of type " + arg.getClass().getName() + " to " + paramType.getName());
            }
        }
        return javaArgs;
    }

    private List<Object> convertJavaListToLanguageArray(List<?> javaList) {
        List<Object> languageArray = new ArrayList<>();
        for (Object item : javaList) {
            if (item instanceof List) {
                languageArray.add(convertJavaListToLanguageArray((List<?>) item));
            } else {
                languageArray.add(item);
            }
        }
        return languageArray;
    }

    private boolean isPrimitiveOrWrapper(Class<?> vgclass) {
        return vgclass.isPrimitive() ||
                vgclass == Boolean.class ||
                vgclass == Byte.class ||
                vgclass == Character.class ||
                vgclass == Double.class ||
                vgclass == Float.class ||
                vgclass == Integer.class ||
                vgclass == Long.class ||
                vgclass == Short.class ||
                vgclass == String.class ||
                vgclass == Void.class;
    }
    private FunctionReference extractFunctionFromArg(Object arg) {
        if (arg instanceof FunctionReference) {
            return (FunctionReference) arg;
        } else if (arg instanceof Function) {
            return new FunctionReference((Function) arg, new ArrayList<>());
        } else if (arg instanceof LanguageObjectWrapper) {
            Object obj = ((LanguageObjectWrapper) arg).getObject();
            if (obj instanceof FunctionReference) {
                return (FunctionReference) obj;
            } else if (obj instanceof Function) {
                return new FunctionReference((Function) obj, new ArrayList<>());
            }
        }
        throw new ErrorHandler.VGException("Argument must be a function reference",currentLine,currentColumn);
    }
    private Object VgSystemCall(List<Object> args) {
        if (args.size() < 2) {
            throw new RuntimeException("CallJava requires at least 2 arguments: className and methodName");
        }
        String className = args.get(0).toString();
        String memberName = args.get(1).toString();
        List<Object> methodArgs = args.size() > 2 ? args.subList(2, args.size()) : Collections.emptyList();
        try {
            Class<?> clazz = Class.forName(className);
            if (className.equals("components.MyGUI$MyButton") && memberName.equals("setOnClick") && methodArgs.size() == 2) {
                Object instanceObj = methodArgs.get(0);
                if (instanceObj instanceof LanguageObjectWrapper) {
                    instanceObj = ((LanguageObjectWrapper) instanceObj).getObject();
                }

                Object secondArg = methodArgs.get(1);
                if (!(secondArg instanceof FunctionReference)) {
                    throw new RuntimeException("setOnClick requires a FunctionReference argument");
                }
                FunctionReference funcRef = (FunctionReference) secondArg;

                ActionListener listener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        List<Object> finalArgs = new ArrayList<>(funcRef.getCapturedArgs());
                        funcRef.getFunction().call(finalArgs);
                    }
                };

                Method setOnClickMethod = clazz.getMethod("setOnClick", ActionListener.class);
                setOnClickMethod.invoke(instanceObj, listener);

                return null;
            }
            if (className.equals("components.MyGUI") && memberName.equals("setOnKeyPress") && methodArgs.size() == 2) {
                Object instanceObj = methodArgs.get(0);
                if (instanceObj instanceof LanguageObjectWrapper) {
                    instanceObj = ((LanguageObjectWrapper) instanceObj).getObject();
                }

                Object secondArg = methodArgs.get(1);
                if (!(secondArg instanceof FunctionReference)) {
                    throw new RuntimeException("setOnKeyPress requires a FunctionReference argument.");
                }
                FunctionReference funcRef = (FunctionReference) secondArg;

                KeyListener listener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        List<Object> argValues = new ArrayList<>(funcRef.getCapturedArgs());
                        argValues.add(e.getKeyCode());
                        funcRef.getFunction().call(argValues);
                    }
                };

                Method method = clazz.getMethod("setOnKeyPress", KeyListener.class);
                method.invoke(instanceObj, listener);

                return null;
            }
            if (className.equals("components.MyGUI") && memberName.equals("setOnKeyPress") && methodArgs.size() == 2) {
                Object instanceObj = methodArgs.get(0);
                final Object keyPressArg1 = methodArgs.get(1);
                final FunctionReference keyPressCallbackFunction = extractFunctionFromArg(keyPressArg1);

                KeyListener listener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        List<Object> argValues = new ArrayList<>();
                        argValues.add(e.getKeyCode());
                        keyPressCallbackFunction.getFunction().call(argValues);
                    }
                };

                Method method = clazz.getMethod(memberName, KeyListener.class);
                Object instance = ((LanguageObjectWrapper) instanceObj).getObject();
                method.invoke(instance, listener);
                return null;
            }
            if (className.equals("src.MyGUI") && memberName.equals("setOnMousePress") && methodArgs.size() == 2) {
                Object instanceObj = methodArgs.get(0);
                FunctionReference callbackFunction = extractFunctionFromArg(methodArgs.get(1));

                MyGUI gui = (MyGUI) ((LanguageObjectWrapper) instanceObj).getObject();
                gui.setOnMousePress(callbackFunction);
                return null;
            }
            if (className.equals("src.MyGUI") && memberName.equals("setOnMouseDrag") && methodArgs.size() == 2) {
                Object instanceObj = methodArgs.get(0);
                FunctionReference callbackFunction = extractFunctionFromArg(methodArgs.get(1));

                Method method = clazz.getMethod("setOnMouseDrag", FunctionReference.class);
                Object instance = ((LanguageObjectWrapper) instanceObj).getObject();
                method.invoke(instance, callbackFunction);
                return null;
            }

            if (className.equals("components.MyGUI") && memberName.equals("setOnKeyReleased") && methodArgs.size() == 2) {
                Object instanceObj = methodArgs.get(0);
                final Object keyReleaseArg1 = methodArgs.get(1);
                final FunctionReference keyReleaseCallbackFunction = extractFunctionFromArg(keyReleaseArg1);

                KeyListener listener = new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        List<Object> argValues = new ArrayList<>();
                        argValues.add(e.getKeyCode());
                        keyReleaseCallbackFunction.getFunction().call(argValues);
                    }
                };

                Method method = clazz.getMethod(memberName, KeyListener.class);
                Object instance = ((LanguageObjectWrapper) instanceObj).getObject();
                method.invoke(instance, listener);
                return null;
            }

            if (className.equals("javax.swing.Timer") && memberName.equals("<init>") && methodArgs.size() == 2) {
                Object delayObj = methodArgs.get(0);
                int delay;
                if (delayObj instanceof Number) {
                    delay = ((Number) delayObj).intValue();
                } else {
                    throw new ErrorHandler.VGException("First argument to Timer constructor must be a number",currentLine,currentColumn);
                }

                Object arg1 = methodArgs.get(1);
                FunctionReference callbackFunction = extractFunctionFromArg(arg1);

                ActionListener listener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        List<Object> argValues = new ArrayList<>();
                        callbackFunction.getFunction().call(argValues);
                    }
                };

                Constructor<?> constructor = clazz.getConstructor(int.class, ActionListener.class);
                Object instance = constructor.newInstance(delay, listener);

                return new LanguageObjectWrapper(instance);
            }
            if (!isMethodAllowed(clazz, memberName)) {
                throw new RuntimeException("Access to method '" + memberName + "' in class '" + className + "' is not allowed.");
            }
            Object instance = null;
            if (!memberName.equals("<init>")) {
                if (methodArgs.size() > 0 && methodArgs.get(0) instanceof LanguageObjectWrapper) {
                    instance = ((LanguageObjectWrapper) methodArgs.get(0)).getLanguageObject();
                    methodArgs = methodArgs.subList(1, methodArgs.size());
                }
            }

            AccessibleObject accessibleObject = null;
            if (memberName.equals("<init>")) {
                Constructor<?> constructor = findConstructor(clazz, methodArgs);
                if (constructor == null) {
                    throw new RuntimeException("Constructor not found in class '" + className + "' with " + methodArgs.size() + " arguments.");
                }
                accessibleObject = constructor;
            } else {
                Method method = findMethod(clazz, memberName, methodArgs);
                if (method == null) {
                    throw new RuntimeException("Method '" + memberName + "' not found in class '" + className + "'");
                }
                accessibleObject = method;
            }

            Object[] javaArgs = convertArguments(methodArgs, accessibleObject);

            Object result;
            if (accessibleObject instanceof Constructor<?>) {
                result = ((Constructor<?>) accessibleObject).newInstance(javaArgs);
            } else {
                result = ((Method) accessibleObject).invoke(instance, javaArgs);
            }

            if (result != null && !isPrimitiveOrWrapper(result.getClass())) {
                return new LanguageObjectWrapper(result);
            } else if (result instanceof List) {
                return convertJavaListToLanguageArray((List<?>) result);
            } else {
                return result;
            }
        } catch (InvocationTargetException ite) {
            Throwable cause = ite.getCause();
            if (cause != null) {
                String details = cause.toString();
                throw new RuntimeException("Error invoking system method: " + details, ite);
            } else {
                throw new RuntimeException("Error invoking system method: <no cause>", ite);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
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

    private void setVariable(String name, Object value) {
        for (SymbolTable table : symbolTableStack) {
            if (table.contains(name)) {
                table.set(name, value);
                return;
            }
        }

        symbolTableStack.peek().set(name, value);
    }

    @Override
    public Object visitProgram(vg_langParser.ProgramContext ctx) {
        for (vg_langParser.StatementContext stmtCtx : ctx.statement()) {
            if (stmtCtx.functionDeclaration() != null) {
                visit(stmtCtx.functionDeclaration());
            }
        }

        for (vg_langParser.StatementContext stmtCtx : ctx.statement()) {
            if (stmtCtx.functionDeclaration() == null) {
                visit(stmtCtx);
            }
        }
        return null;
    }

    @Override
    public Object visitFunctionDeclaration(vg_langParser.FunctionDeclarationContext ctx) {
        String functionName = ctx.IDENTIFIER().getText();
        List<String> parameters = new ArrayList<>();
        if (ctx.parameterList() != null) {
            for (TerminalNode paramNode : ctx.parameterList().IDENTIFIER()) {
                parameters.add(paramNode.getText());
            }
        }
        Function function = new Function(parameters, ctx.block(), this);

        SymbolTable currentTable = symbolTableStack.getLast();
        currentTable.setFunction(functionName, function);
        currentTable.set(functionName, function);
        return null;
    }

    @Override
    public Object visitFunctionCall(vg_langParser.FunctionCallContext ctx) {
        String functionName;

        if (ctx.IDENTIFIER() != null) {
            functionName = ctx.IDENTIFIER().getText();
        } else {
            throw new ErrorHandler.VGSyntaxException("Invalid function call syntax", 
                                                   ctx.getStart().getLine(), 
                                                   ctx.getStart().getCharPositionInLine());
        }

        List<vg_langParser.ExpressionContext> argExprs = ctx.argumentList() != null
                ? ctx.argumentList().expression()
                : Collections.emptyList();

        List<Object> argValues = new ArrayList<>();
        for (vg_langParser.ExpressionContext exprCtx : argExprs) {
            argValues.add(visit(exprCtx));
        }

        if (builtInFunction.containsKey(functionName)) {
            BuiltInFunction builtInFunc = builtInFunction.get(functionName);
            return builtInFunc.call(argValues);
        }

        Object funcObj = null;

        for (SymbolTable table : symbolTableStack) {
            if (table.containsFunction(functionName)) {
                funcObj = table.getFunction(functionName);
                break;
            }
            if (table.contains(functionName)) {
                funcObj = table.get(functionName);
                break;
            }
        }
        if (funcObj instanceof FunctionReference) {
            FunctionReference funcRef = (FunctionReference) funcObj;
            return funcRef.call(argValues);
        }
        if (!(funcObj instanceof Function)) {
            int line = ctx.getStart().getLine();
            throw new RuntimeException("Function '" + functionName + "' is not defined at line: " + line);
        }

        Function function = (Function) funcObj;
        List<String> parameters = function.getParameters();

        if (argValues.size() != parameters.size()) {
            throw ErrorHandler.createArgumentCountError(
                functionName, parameters.size(), argValues.size(), ctx.getStart());
        }

        SymbolTable functionSymbolTable = new SymbolTable();
        symbolTableStack.push(functionSymbolTable);

        for (int i = 0; i < parameters.size(); i++) {
            functionSymbolTable.set(parameters.get(i), argValues.get(i));
        }

        Object returnValue = null;
        try {
            visit(function.getBlock());
        } catch (ReturnException e) {
            returnValue = e.getValue();
        } finally {
            symbolTableStack.pop();
        }

        return returnValue;
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

    @Override
    public Object visitReturnStatement(vg_langParser.ReturnStatementContext ctx) {
        Object returnValue = null;
        if (ctx.expression() != null) {
            returnValue = visit(ctx.expression());
        }
        throw new ReturnException(returnValue);
    }

    @Override
    public Object visitVariableDeclaration(vg_langParser.VariableDeclarationContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        Object value = visit(ctx.expression());

        currentSymbolTable().set(varName, value);
        return null;
    }

    @Override
    public Object visitVariableDeclarationNoSemi(vg_langParser.VariableDeclarationNoSemiContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        Object value = visit(ctx.expression());
        currentSymbolTable().set(varName, value);
        return null;
    }

    @Override
    public Object visitConstDeclaration(vg_langParser.ConstDeclarationContext ctx) {
        String constName = ctx.IDENTIFIER().getText();
        Object value = visit(ctx.expression());
        SymbolTable currentTable = currentSymbolTable();
        currentTable.setConstant(constName, value);
        return null;
    }

    @Override
    public Object visitAssignment(vg_langParser.AssignmentContext ctx) {
        VariableReference varRef = (VariableReference) visit(ctx.leftHandSide());
        if (varRef.isConstant()) {
            throw new RuntimeException("Cannot reassign to a constant variable '" + varRef.getName() + "'.");
        }
        Object value = visit(ctx.expression());

        varRef.setValue(value);
        return null;
    }

    @Override
    public Object visitAssignmentNoSemi(vg_langParser.AssignmentNoSemiContext ctx) {
        VariableReference varRef = (VariableReference) visit(ctx.leftHandSide());
        if (varRef.isConstant()) {
            throw new RuntimeException("Cannot reassign to a constant variable '" + varRef.getName() + "'.");
        }
        Object value = visit(ctx.expression());
        varRef.setValue(value);
        return value;
    }

    @Override
    public Object visitLeftHandSide(vg_langParser.LeftHandSideContext ctx) {
        if (ctx.IDENTIFIER().size() == 1) {
            String varName = ctx.IDENTIFIER(0).getText();

            List<Integer> indices = new ArrayList<>();
            if (ctx.expression() != null && !ctx.expression().isEmpty()) {
                for (vg_langParser.ExpressionContext exprCtx : ctx.expression()) {
                    Object indexObj = visit(exprCtx);
                    if (!(indexObj instanceof Number)) {
                        throw new RuntimeException("Array index must be a number.");
                    }
                    indices.add(((Number) indexObj).intValue());
                }
            }

            SymbolTable targetTable = null;
            for (SymbolTable table : symbolTableStack) {
                if (table.contains(varName)) {
                    targetTable = table;
                    break;
                }
            }

            if (targetTable == null) {
                throw new RuntimeException("Variable '" + varName + "' is not defined.");
            }

            return new VariableReference(targetTable, varName, indices);
        } else if (ctx.IDENTIFIER().size() == 2) {
            String objName = ctx.IDENTIFIER(0).getText();
            String fieldName = ctx.IDENTIFIER(1).getText();

            Object obj = getVariable(objName);
            
            if (obj instanceof StructDefinition) {
                // Convert StructDefinition to Struct instance for assignment
                StructDefinition structDef = (StructDefinition) obj;
                Struct struct = structDef.createInstance();
                
                // Store the new struct instance back in the variable
                for (SymbolTable table : symbolTableStack) {
                    if (table.contains(objName)) {
                        table.set(objName, struct);
                        break;
                    }
                }
                
                return new VariableReference(struct, fieldName);
            } else if (obj instanceof Struct) {
                Struct struct = (Struct) obj;
                return new VariableReference(struct, fieldName);
            } else {
                throw new RuntimeException("Cannot access field '" + fieldName + "' on non-struct object '" + objName + "'");
            }
        } else {
            throw new RuntimeException("Invalid assignment target.");
        }
    }

    @Override
    public Object visitPrintStatement(vg_langParser.PrintStatementContext ctx) {
        StringBuilder output = new StringBuilder();
        for (vg_langParser.ExpressionContext exprCtx : ctx.expression()) {
            Object value = visit(exprCtx);
            output.append(value).append(" ");
        }
        System.out.println(output.toString().trim());
        return null;
    }

    @Override
    public Object visitIfStatement(vg_langParser.IfStatementContext ctx) {
        if (toBoolean(visit(ctx.expression()))) {
            visit(ctx.ifBlock);
        } else {
            boolean executed = false;

            for (vg_langParser.ElseIfStatementContext elifCtx : ctx.elseIfStatement()) {
                if (toBoolean(visit(elifCtx.expression()))) {
                    visit(elifCtx.block());
                    executed = true;
                    break;
                }
            }

            if (!executed && ctx.elseStatement() != null) {
                visit(ctx.elseStatement().block());
            }
        }
        return null;
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

    @Override
    public Object visitLogicalOrExpression(vg_langParser.LogicalOrExpressionContext ctx) {
        Object left = visit(ctx.logicalAndExpression(0));
        for (int i = 1; i < ctx.logicalAndExpression().size(); i++) {
            if (toBoolean(left)) {
                return true;
            }
            Object right = visit(ctx.logicalAndExpression(i));
            left = toBoolean(right);
        }
        return left;
    }

    @Override
    public Object visitEqualityExpression(vg_langParser.EqualityExpressionContext ctx) {
        Object left = visit(ctx.relationalExpression(0));
        for (int i = 1; i < ctx.relationalExpression().size(); i++) {
            String op = ctx.getChild(2 * i - 1).getText();
            Object right = visit(ctx.relationalExpression(i));
            switch (op) {
                case "==":
                    left = Objects.equals(left, right);
                    break;
                case "!=":
                    left = !Objects.equals(left, right);
                    break;
                default:
                    throw new RuntimeException("Unknown operator: " + op);
            }
        }
        return left;
    }

    @Override
    public Object visitRelationalExpression(vg_langParser.RelationalExpressionContext ctx) {
        Object left = visit(ctx.additiveExpression(0));
        for (int i = 1; i < ctx.additiveExpression().size(); i++) {
            String op = ctx.getChild(2 * i - 1).getText();
            Object right = visit(ctx.additiveExpression(i));
            if (!(left instanceof Number) || !(right instanceof Number)) {
                throw new RuntimeException("Operands must be numbers.");
            }
            double leftNum = ((Number) left).doubleValue();
            double rightNum = ((Number) right).doubleValue();
            switch (op) {
                case "<":
                    left = leftNum < rightNum;
                    break;
                case "<=":
                    left = leftNum <= rightNum;
                    break;
                case ">":
                    left = leftNum > rightNum;
                    break;
                case ">=":
                    left = leftNum >= rightNum;
                    break;
                default:
                    throw new RuntimeException("Unknown operator: " + op);
            }
        }
        return left;
    }

    @Override
    public Object visitAdditiveExpression(vg_langParser.AdditiveExpressionContext ctx) {
        Object result = visit(ctx.multiplicativeExpression(0));

        for (int i = 1; i < ctx.multiplicativeExpression().size(); i++) {
            String operator = ctx.getChild(2 * i - 1).getText();
            Object right = visit(ctx.multiplicativeExpression(i));

            result = evaluateArithmetic(result, right, operator);
        }

        return result;
    }

    @Override
    public Object visitMultiplicativeExpression(vg_langParser.MultiplicativeExpressionContext ctx) {
        Object result = visit(ctx.unaryExpression(0));

        for (int i = 1; i < ctx.unaryExpression().size(); i++) {
            String operator = ctx.getChild(2 * i - 1).getText();
            Object right = visit(ctx.unaryExpression(i));

            result = evaluateArithmetic(result, right, operator);
        }

        return result;
    }

    private Object evaluateArithmetic(Object left, Object right, String operator) {
        if (left instanceof List || right instanceof List) {
            throw new RuntimeException("Cannot perform arithmetic operations on arrays.");
        }
        if (left instanceof String || right instanceof String) {
            if (operator.equals("+")) {
                return String.valueOf(left) + String.valueOf(right);
            } else {
                throw new RuntimeException("Invalid operator '" + operator + "' for string operands.");
            }
        }

        if (!(left instanceof Number) || !(right instanceof Number)) {
            throw new RuntimeException("Invalid operands for operator '" + operator + "'.");
        }

        Number leftNum = (Number) left;
        Number rightNum = (Number) right;
        boolean isDouble = leftNum instanceof Double || rightNum instanceof Double;

        switch (operator) {
            case "+":
                if (isDouble) {
                    double result = leftNum.doubleValue() + rightNum.doubleValue();
                    return result;
                } else {
                    int result = leftNum.intValue() + rightNum.intValue();
                    return result;
                }
            case "-":
                if (isDouble) {
                    double result = leftNum.doubleValue() - rightNum.doubleValue();
                    return result;
                } else {
                    int result = leftNum.intValue() - rightNum.intValue();
                    return result;
                }
            case "*":
                if (isDouble) {
                    double result = leftNum.doubleValue() * rightNum.doubleValue();
                    return result;
                } else {
                    int result = leftNum.intValue() * rightNum.intValue();
                    return result;
                }
            case "/":
                if (rightNum.doubleValue() == 0) {
                    throw new RuntimeException("Division by zero");
                }
                if (isDouble) {
                    double result = leftNum.doubleValue() / rightNum.doubleValue();
                    return result;
                } else {
                    int result = leftNum.intValue() / rightNum.intValue();
                    return result;
                }
            case "%":
                if (isDouble) {
                    double result = leftNum.doubleValue() % rightNum.doubleValue();
                    return result;
                } else {
                    int result = leftNum.intValue() % rightNum.intValue();
                    return result;
                }
            default:
                throw new RuntimeException("Unknown operator '" + operator + "'.");
        }
    }

    @Override
    public Object visitUnaryExpression(vg_langParser.UnaryExpressionContext ctx) {
        if (ctx.unaryExpression() != null) {
            Object value = visit(ctx.unaryExpression());
            String operator = ctx.getChild(0).getText();
            switch (operator) {
                case "+":
                    if (!(value instanceof Number)) {
                        throw new RuntimeException("Unary '+' operator requires a numeric operand.");
                    }
                    return value;
                case "-":
                    if (!(value instanceof Number)) {
                        throw new RuntimeException("Unary '-' operator requires a numeric operand.");
                    }
                    if (value instanceof Integer) {
                        return -((Integer) value);
                    } else if (value instanceof Double) {
                        return -((Double) value);
                    }
                    break;
                case "!":
                    return !toBoolean(value);
                default:
                    throw new RuntimeException("Unknown unary operator '" + operator + "'.");
            }
        } else {
            return visit(ctx.postfixExpression());
        }
        return null;
    }

    @Override
    public Object visitPrimary(vg_langParser.PrimaryContext ctx) {
        if (ctx.literal() != null) {
            return visit(ctx.literal());
        } else if (ctx.IDENTIFIER() != null) {
            String varName = ctx.IDENTIFIER().getText();
            
            if (varName.equals("true")) {
                return true;
            } else if (varName.equals("false")) {
                return false;
            }
            
            Object value = null;
            for (SymbolTable table : symbolTableStack) {
                if (table.contains(varName)) {
                    value = table.get(varName);
                    break;
                }
            }
            
            if (value == null) {
                throw new RuntimeException("Variable '" + varName + "' is not defined.");
            }
            return value;
        } else if (ctx.expression() != null) {
            return visit(ctx.expression());
        } else if (ctx.functionCall() != null) {
            return visit(ctx.functionCall());
        }
        return null;
    }

    private String unescapeString(String str) {
        StringBuilder result = new StringBuilder();
        boolean inEscape = false;
        
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            
            if (inEscape) {
                switch (c) {
                    case 'n': result.append('\n'); break;
                    case 'r': result.append('\r'); break;
                    case 't': result.append('\t'); break;
                    case '\\': result.append('\\'); break;
                    case '"': result.append('"'); break;
                    case '\'': result.append('\''); break;
                    default:
                        throw new ErrorHandler.VGSyntaxException(
                            "Invalid escape sequence in string: \\" + c, 
                            currentLine, currentColumn);
                }
                inEscape = false;
            } else if (c == '\\') {
                inEscape = true;
            } else {
                result.append(c);
            }
        }
        
        if (inEscape) {
            throw new ErrorHandler.VGSyntaxException(
                "String ends with incomplete escape sequence", 
                currentLine, currentColumn);
        }
        
        return result.toString();
    }

    @Override
    public Object visitLiteral(vg_langParser.LiteralContext ctx) {
        if (ctx.INT() != null) {
            return Integer.parseInt(ctx.INT().getText());
        } else if (ctx.DOUBLE() != null) {
            return Double.parseDouble(ctx.DOUBLE().getText());
        } else if (ctx.STRING_LITERAL() != null) {
            String rawString = ctx.STRING_LITERAL().getText();
            try {
                String unescapedString = unescapeString(rawString.substring(1, rawString.length() - 1));
                return unescapedString;
            } catch (StringIndexOutOfBoundsException e) {
                throw ErrorHandler.createMissingQuoteError(ctx.STRING_LITERAL().getSymbol());
            } catch (RuntimeException e) {
                throw new ErrorHandler.VGSyntaxException(
                    "Invalid string literal: " + e.getMessage(),
                    ctx.STRING_LITERAL().getSymbol().getLine(),
                    ctx.STRING_LITERAL().getSymbol().getCharPositionInLine()
                );
            }
        } else if (ctx.TRUE() != null) {
            return true;
        } else if (ctx.FALSE() != null) {
            return false;
        } else if (ctx.arrayLiteral() != null) {
            return visit(ctx.arrayLiteral());
        }
        return null;
    }

    @Override
    public Object visitPostfixExpression(vg_langParser.PostfixExpressionContext ctx) {
        Object value = visit(ctx.primary());

        for (vg_langParser.PostfixOpContext opCtx : ctx.postfixOp()) {
            String opText = opCtx.getChild(0).getText();
            if (".".equals(opText)) {
                String memberName = opCtx.IDENTIFIER().getText();
                
                if (value instanceof Integer || value instanceof Double || 
                    value instanceof Boolean || value instanceof String) {
                    
                    String typeName = getVGTypeName(value);
                    
                    updatePosition(opCtx.start);
                    throw new ErrorHandler.VGTypeException(
                        "Dot operator not supported on primitive type: " + typeName,
                        currentLine, currentColumn
                    );
                }
                
                if (value instanceof Namespace) {
                    Namespace ns = (Namespace) value;
                    Object member = ns.getSymbol(memberName);
                    if (member == null) {
                        updatePosition(opCtx.start);
                        throw new ErrorHandler.VGNameException(
                                "Member '" + memberName + "' not found in namespace.",
                                currentLine, currentColumn
                        );
                    }
                    value = member;
                } else if (value instanceof StructDefinition) {
                    // Automatically create a Struct instance if accessing fields on a StructDefinition
                    if (memberName.equals("createInstance")) {
                        // Support for explicit createInstance() call
                        value = ((StructDefinition) value).createInstance();
                    } else {
                        // Auto-create instance and access the field 
                        StructDefinition structDef = (StructDefinition) value;
                        Struct struct = structDef.createInstance();
                        if (!struct.hasField(memberName)) {
                            throw new RuntimeException("Field '" + memberName + "' not found in struct '" + struct.getName() + "'");
                        }
                        value = struct.getField(memberName);
                    }
                } else if (value instanceof Struct) {
                    Struct struct = (Struct) value;
                    if (!struct.hasField(memberName)) {
                        throw new RuntimeException("Field '" + memberName + "' not found in struct '" + struct.getName() + "'");
                    }
                    value = struct.getField(memberName);
                } else if (value instanceof Enum) {
                    Enum enumObj = (Enum) value;
                    if (!enumObj.hasValue(memberName)) {
                        throw new RuntimeException("Value '" + memberName + "' not found in enum '" + enumObj.getName() + "'");
                    }
                    value = enumObj.getValue(memberName);
                } else {
                    updatePosition(opCtx.start);
                    throw new ErrorHandler.VGTypeException(
                            "Dot operator not supported on type: " + getVGTypeName(value),
                            currentLine, currentColumn
                    );
                }
            } else if ("(".equals(opText)) {
                List<Object> argValues = new ArrayList<>();
                vg_langParser.ArgumentListContext argsCtx = opCtx.argumentList();
                if (argsCtx != null) {
                    for (vg_langParser.ExpressionContext exprCtx : argsCtx.expression()) {
                        argValues.add(visit(exprCtx));
                    }
                }

                if (value instanceof Function) {
                    try {
                        value = ((Function) value).call(argValues);
                    } catch (IndexOutOfBoundsException e) {
                        updatePosition(opCtx.start);
                        throw new ErrorHandler.VGTypeException(
                                "Incorrect number of arguments for function call. Check the function signature.",
                                currentLine, currentColumn
                        );
                    } catch (Exception e) {
                        updatePosition(opCtx.start);
                        throw new ErrorHandler.VGException(
                                "Error in function call: " + e.getMessage(),
                                currentLine, currentColumn
                        );
                    }

                } else {
                    updatePosition(opCtx.start);
                    throw new ErrorHandler.VGTypeException(
                            "Cannot call a non-function value: " + getVGTypeName(value),
                            currentLine, currentColumn
                    );
                }
            } else if ("[".equals(opText)) {
                Object indexObj = visit(opCtx.expression());
                if (!(indexObj instanceof Number)) {
                    updatePosition(opCtx.start);
                    throw new ErrorHandler.VGTypeException(
                            "Array index must be a number, got: " + getVGTypeName(indexObj),
                            currentLine, currentColumn
                    );
                }
                int index = ((Number) indexObj).intValue();
                if (!(value instanceof List)) {
                    updatePosition(opCtx.start);
                    throw new ErrorHandler.VGTypeException(
                            "Cannot use [] operator on non-array value: " + getVGTypeName(value),
                            currentLine, currentColumn
                    );
                }
                List<?> list = (List<?>) value;
                if (index < 0 || index >= list.size()) {
                    updatePosition(opCtx.start);
                    throw new ErrorHandler.VGException(
                            "Array index out of bounds: index " + index + " exceeds array length " + list.size(),
                            currentLine, currentColumn
                    );
                }
                value = list.get(index);
            }
        }
        return value;
    }

    @Override
    public Object visitArrayLiteral(vg_langParser.ArrayLiteralContext ctx) {
        List<Object> elements = new ArrayList<>();
        for (vg_langParser.ExpressionContext exprCtx : ctx.expression()) {
            Object value = visit(exprCtx);
            elements.add(value);
        }
        return elements;
    }

    @Override
    public Object visitForStatement(vg_langParser.ForStatementContext ctx) {
        symbolTableStack.push(new SymbolTable());

        if (ctx.forInit() != null) {
            visit(ctx.forInit());
        }

        while (true) {
            if (ctx.forCondition() != null) {
                Object conditionValue = visit(ctx.forCondition());
                if (!toBoolean(conditionValue)) {
                    break;
                }
            }

            visit(ctx.block());
            if (ctx.forUpdate() != null) {
                visit(ctx.forUpdate());
            }
        }

        symbolTableStack.pop();
        return null;
    }

    @Override
    public Object visitWhileStatement(vg_langParser.WhileStatementContext ctx) {
        while (toBoolean(visit(ctx.expression()))) {
            visit(ctx.block());
        }
        return null;
    }

    @Override
    public Object visitDoWhileStatement(vg_langParser.DoWhileStatementContext ctx) {
        do {
            visit(ctx.block());
        } while (toBoolean(visit(ctx.expression())));
        return null;
    }

    @Override
    public Object visitThrowStatement(vg_langParser.ThrowStatementContext ctx) {
        Object value = visit(ctx.expression());
        throw new RuntimeException((String) value);
    }

    @Override
    public Object visitTryStatement(vg_langParser.TryStatementContext ctx) {
        try {
            visit(ctx.block());
        } catch (RuntimeException e) {
            boolean handled = false;
            for (vg_langParser.CatchStatementContext catchCtx : ctx.catchStatement()) {
                String exceptionVar = catchCtx.IDENTIFIER().getText();

                SymbolTable catchSymbolTable = new SymbolTable();
                symbolTableStack.push(catchSymbolTable);

                String exceptionMessage = e.getMessage() != null ? e.getMessage() : "An error occurred";

                String exceptionOutput = exceptionMessage;
                catchSymbolTable.set(exceptionVar, exceptionOutput);
                try {
                    visit(catchCtx.block());
                    handled = true;
                    break;
                } finally {
                    symbolTableStack.pop();
                }
            }
            if (!handled) {
                throw e;
            }
        } finally {
            if (ctx.finallyStatement() != null) {
                visit(ctx.finallyStatement().block());
            }
        }
        return null;
    }

    @Override
    public Object visitLibraryDeclaration(vg_langParser.LibraryDeclarationContext ctx) {
        String libName = ctx.IDENTIFIER().getText();
        Library library = new Library(libName);

        for (vg_langParser.NamespaceDeclarationContext nsCtx : ctx.namespaceDeclaration()) {
            Namespace ns = (Namespace) visit(nsCtx);
            library.addNamespace(ns);
        }

        this.moduleRegistry.addLibrary(library);
        return library;
    }

    @Override
    public Object visitNamespaceDeclaration(vg_langParser.NamespaceDeclarationContext ctx) {
        String nsName = ctx.IDENTIFIER().getText();
        Namespace namespace = new Namespace(nsName);

        for (vg_langParser.FunctionDeclarationContext funcCtx : ctx.functionDeclaration()) {
            String functionName = funcCtx.IDENTIFIER().getText();
            Function function = new Function(
                    getParameters(funcCtx.parameterList()),
                    funcCtx.block(),
                    this
            );
            namespace.addSymbol(functionName, function);
        }

        for (vg_langParser.VariableDeclarationContext varCtx : ctx.variableDeclaration()) {
            String varName = varCtx.IDENTIFIER().getText();
            Object value = visit(varCtx.expression());
            namespace.addSymbol(varName, value);
        }

        for (vg_langParser.ConstDeclarationContext constCtx : ctx.constDeclaration()) {
            String constName = constCtx.IDENTIFIER().getText();
            Object value = visit(constCtx.expression());
            namespace.addSymbol(constName, value);
        }
        return namespace;
    }

    @Override
    public Object visitImportStatement(vg_langParser.ImportStatementContext ctx) {
        updatePosition(ctx.start);
        
        String importPath = ctx.importPath().getText();
        this.processImport(importPath);
        return null;
    }

    @Override
    public Object visitFunctionReference(vg_langParser.FunctionReferenceContext ctx) {
        String functionPath = ctx.qualifiedIdentifier().getText();

        Function function = resolveFunctionFromNamespace(functionPath);

        if (function == null) {
            throw new RuntimeException("Function '" + functionPath + "' is not defined.");
        }

        List<Object> capturedArgs = new ArrayList<>();
        if (ctx.argumentList() != null) {
            for (vg_langParser.ExpressionContext exprCtx : ctx.argumentList().expression()) {
                capturedArgs.add(visit(exprCtx));
            }
        }

        return new FunctionReference(function, capturedArgs);
    }

    @Override
    public Object visitStructDeclaration(vg_langParser.StructDeclarationContext ctx) {
        try {
            String structName = ctx.IDENTIFIER().getText();

            Map<String, Object> fieldDefaults = new HashMap<>();
            for (vg_langParser.StructFieldContext fieldCtx : ctx.structField()) {
                String fieldName = fieldCtx.IDENTIFIER().getText();
                fieldDefaults.put(fieldName, null);
            }

            StructDefinition structDef = new StructDefinition(structName, fieldDefaults);
            currentSymbolTable().set(structName, structDef);

            return null;
        } catch (Exception e) {
            throw ErrorHandler.handleException(e, ctx);
        }
    }

    @Override
    public Object visitEnumDeclaration(vg_langParser.EnumDeclarationContext ctx) {
        try {
            String enumName = ctx.IDENTIFIER().getText();
            Enum enumObj = new Enum(enumName);

            int autoValue = 0;
            for (vg_langParser.EnumValueContext valueCtx : ctx.enumValue()) {
                String valueName = valueCtx.IDENTIFIER().getText();
                Object value;

                if (valueCtx.expression() != null) {
                    value = visit(valueCtx.expression());
                    if (value instanceof Number) {
                        autoValue = ((Number) value).intValue() + 1;
                    }
                } else {
                    value = autoValue++;
                }

                enumObj.addValue(valueName, value);
            }

            currentSymbolTable().set(enumName, enumObj);

            return null;
        } catch (Exception e) {
            throw ErrorHandler.handleException(e, ctx);
        }
    }

    private void handleSyntaxError(String message, org.antlr.v4.runtime.ParserRuleContext ctx) {
        int line = ctx.start.getLine();
        int column = ctx.start.getCharPositionInLine();
        throw new ErrorHandler.VGSyntaxException(message, line, column);
    }

    private void checkForUnexpectedSemicolon(Token token, String context) {
        if (token.getText().equals(";")) {
            throw ErrorHandler.createUnexpectedSemicolonError(token);
        }
    }

    private void validateFunctionArguments(String functionName, List<String> parameters, 
                                         List<Object> arguments, Token token) {
        if (arguments.size() != parameters.size()) {
            throw ErrorHandler.createArgumentCountError(
                functionName, parameters.size(), arguments.size(), token);
        }
    }

    private void validateArrayIndex(Object indexObj, List<?> array, Token token) {
        if (!(indexObj instanceof Number)) {
            throw new ErrorHandler.VGTypeException(
                "Array index must be a number, got: " + getVGTypeName(indexObj),
                token.getLine(), token.getCharPositionInLine()
            );
        }
        
        int index = ((Number) indexObj).intValue();
        if (index < 0 || index >= array.size()) {
            throw new ErrorHandler.VGException(
                "Array index out of bounds: index " + index + " exceeds array length " + array.size(),
                token.getLine(), token.getCharPositionInLine()
            );
        }
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
        if (value instanceof Enum) return "enum";
        
        String className = value.getClass().getName();
        if (className.startsWith("java.")) {
            return className.substring(className.lastIndexOf('.') + 1).toLowerCase();
        }
        return value.getClass().getSimpleName().toLowerCase();
    }
}