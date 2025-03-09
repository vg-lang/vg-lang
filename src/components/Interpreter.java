package components;

import com.sun.jdi.InvocationException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;

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
import java.util.stream.Stream;

public class Interpreter extends vg_langBaseVisitor {
    public Deque<SymbolTable> symbolTableStack = new ArrayDeque<>();
    private Map<String, Set<String>> allowedMethods = new HashMap<>();
    private Set<String> allowedClasses = new HashSet<>();
    public Map<String,BuiltInFunction> builtInFunction = new HashMap<>();
    SymbolTable globalSymbolTable;
    ModuleRegistry moduleRegistry;
    private String libraryFolder = "libraries";
    public Interpreter() {
        globalSymbolTable = new SymbolTable();
        symbolTableStack.push(globalSymbolTable);
        registerBuiltInFunction();

        // String configPath = System.getenv("MY_APP_CONFIG");
        String configPath = "C:/Users/hodif/Desktop/usn2024/vg lang/Configuration";
        loadLangConfigFile(configPath + "/allowed_configurations.vgenv");
        moduleRegistry = new ModuleRegistry();
        loadLibrariesFromFolder(libraryFolder);


    }
    public void loadLibrariesFromFolder(String folderPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".vglib"))
                    .forEach(path -> loadLibraryFile(path.toString()));
        } catch (IOException e) {
            throw new RuntimeException("Error loading libraries from folder: " + folderPath, e);
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


            vg_langParser.ProgramContext programCtx = parser.program();


            for (vg_langParser.StatementContext stmtCtx : programCtx.statement()) {
                if (stmtCtx.libraryDeclaration() != null) {

                    processLibraryDeclaration(stmtCtx.libraryDeclaration());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading library file: " + filePath);
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
            throw new RuntimeException("Invalid import path: " + importPath);
        }


        String libName = parts[0];
        Library lib = moduleRegistry.getLibrary(libName);
        if (lib == null) {

            String libraryFilePath = libraryFolder + "/" + libName + ".vglib";
            loadLibraryFile(libraryFilePath);
            lib = moduleRegistry.getLibrary(libName);
            if (lib == null) {
                throw new RuntimeException("Library not found after attempting load: " + libName);
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
            System.out.println("DEBUG: Imported all symbols from nested namespace '" + String.join(".", nsPath) + "'.");
            return;
        }


        if (parts.length == 2) {
            String nsName = parts[1];
            Namespace ns = lib.getNamespace(nsName);
            if (ns == null) {
                throw new RuntimeException("Namespace not found: " + nsName);
            }
            globalSymbolTable.set(nsName, ns);
            System.out.println("DEBUG: Imported namespace '" + nsName + "' into global scope.");
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
            System.out.println("DEBUG: Imported symbol or namespace '" + symbolName + "' from nested path '" + String.join(".", nsPath) + "' into global scope.");
            return;
        }

        throw new RuntimeException("Import path format not recognized: " + importPath);
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

    private void registerBuiltInFunction(){
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
            if(allowedClasses.contains(className)){
                Set<String> classMethods = allowedMethods.get(className);
                if(classMethods != null && classMethods.contains(methodName)) {
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
            // Handle Object[][] conversion
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

    private Object VgSystemCall(List<Object> args) {
        if (args.size() < 2) {
            throw new RuntimeException("CallJava requires at least 2 arguments: className and methodName");
        }
        String className = args.get(0).toString();
        String memberName = args.get(1).toString();
        List<Object> methodArgs = args.size() > 2 ? args.subList(2, args.size()) : Collections.emptyList();
        try {

            Class<?> clazz = Class.forName(className);

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
        }
        catch (InvocationTargetException ite) {

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
            System.out.println("Loaded allowed classes and methods from " + filepath);
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
            System.out.println(functionName);
        } else {
            throw new RuntimeException("Invalid function call.");
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

        if (!(funcObj instanceof Function)) {
            int line = ctx.getStart().getLine();
            throw new RuntimeException("Function '" + functionName + "' is not defined at line: " + line);
        }

        Function function = (Function) funcObj;
        List<String> parameters = function.getParameters();


        if (argValues.size() != parameters.size()) {
            int line = ctx.getStart().getLine();
            throw new RuntimeException("At line: " + line + " Function '" + functionName + "' expects " + parameters.size() + " arguments but got " + argValues.size());
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
        // Use the same logic as the regular assignment.
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
        if (ctx.IDENTIFIER() != null) {
            String varName = ctx.IDENTIFIER().getText();

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

            return new VariableReference(targetTable, varName, indices);
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
            return getVariable(varName);
        } else if (ctx.expression() != null) {
            return visit(ctx.expression());
        } else if (ctx.functionCall() != null) {
            return visit(ctx.functionCall());
        }
        return null;
    }

    private String unescapeString(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '\\') {
                if (i + 1 >= str.length()) {
                    throw new RuntimeException("Invalid escape sequence at end of string");
                }
                char nextChar = str.charAt(++i);
                switch (nextChar) {
                    case 'b':
                        sb.append('\b');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    case 'n':
                        sb.append('\n');
                        break;
                    case 'f':
                        sb.append('\f');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case '"':
                        sb.append('\"');
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    case 'u':
                        if (i + 4 >= str.length()) {
                            throw new RuntimeException("Invalid Unicode escape sequence");
                        }
                        String hex = str.substring(i + 1, i + 5);
                        i += 4;
                        try {
                            sb.append((char) Integer.parseInt(hex, 16));
                        } catch (NumberFormatException e) {
                            throw new RuntimeException("Invalid Unicode escape sequence: \\u" + hex);
                        }
                        break;
                    default:
                        throw new RuntimeException("Unknown escape sequence: \\" + nextChar);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    @Override
    public Object visitLiteral(vg_langParser.LiteralContext ctx) {
        if (ctx.INT() != null) {
            return Integer.parseInt(ctx.INT().getText());
        } else if (ctx.DOUBLE() != null) {
            return Double.parseDouble(ctx.DOUBLE().getText());
        } else if (ctx.STRING_LITERAL() != null) {
            String rawString = ctx.STRING_LITERAL().getText();
            String unescapedString = unescapeString(rawString.substring(1, rawString.length() - 1));
            return unescapedString;

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
                if (value instanceof Namespace) {
                    Namespace ns = (Namespace) value;
                    Object member = ns.getSymbol(memberName);
                    if (member == null) {
                        throw new RuntimeException("Member '" + memberName + "' not found in namespace.");
                    }
                    value = member;
                } else {
                    throw new RuntimeException("Dot operator not supported on type: " + value.getClass().getName());
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
                    value = ((Function) value).call(argValues);
                } else {
                    throw new RuntimeException("Attempted to call a non-function: " + value);
                }
            } else if ("[".equals(opText)) {

                Object indexObj = visit(opCtx.expression());
                if (!(indexObj instanceof Number)) {
                    throw new RuntimeException("Array index must be a number.");
                }
                int index = ((Number) indexObj).intValue();
                if (!(value instanceof List)) {
                    throw new RuntimeException("Cannot index into non-array value.");
                }
                List<?> list = (List<?>) value;
                if (index < 0 || index >= list.size()) {
                    throw new RuntimeException("Array index out of bounds.");
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
        } while (toBoolean(visit(ctx.expression()))); // Re-check condition at the end
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
        String importPath = ctx.importPath().getText();
        this.processImport(importPath);
        return null;
    }
}

