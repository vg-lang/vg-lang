package components.visitors;

import components.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.tree.ParseTree;

public class ImportVisitor extends BaseVisitor {
    public ImportVisitor(Interpreter interpreter) {
        super(interpreter);
    }

    public void processImport(String importPath, String alias) {
        String[] parts = importPath.split("\\.");
        if (parts.length < 2) {
            throw new RuntimeException("Invalid import path: " + importPath);
        }

        String libraryName = parts[0];
        Library library = interpreter.getModuleRegistry().getLibrary(libraryName);
        if (library == null) {
            throw new RuntimeException("Library not found: " + libraryName);
        }

        // Handle different import patterns
        if (parts.length == 3 && parts[2].equals("*")) {
            // Import all symbols from a specific namespace (e.g., import IO.File.*)
            String namespaceName = parts[1];
            Namespace namespace = library.getNamespace(namespaceName);
            if (namespace == null) {
                throw new RuntimeException("Namespace not found: " + namespaceName);
            }
            
            String fullNamespaceName = libraryName + "." + namespaceName;
            SymbolTable currentScope = interpreter.getSymbolTableStack().peek();
            
            // Import symbols with conflict detection
            for (Map.Entry<String, Object> entry : namespace.getSymbols().entrySet()) {
                String symbolName = entry.getKey();
                Object symbolValue = entry.getValue();
                
                // Use the new setWithOrigin method for conflict detection
                currentScope.setWithOrigin(symbolName, symbolValue, fullNamespaceName);
            }
            
            // Also make the namespace available for qualified access
            currentScope.set(namespaceName, namespace);
            
        } else if (parts.length == 3 && !parts[2].equals("*")) {
            // Import a specific function from a namespace (e.g., import MathLib.power.pow)
            String namespaceName = parts[1];
            String functionName = parts[2];
            Namespace namespace = library.getNamespace(namespaceName);
            if (namespace == null) {
                throw new RuntimeException("Namespace not found: " + namespaceName);
            }
            
            Object function = namespace.getSymbol(functionName);
            if (function == null) {
                throw new RuntimeException("Function not found: " + functionName + " in namespace " + namespaceName);
            }
            
            String symbolName = alias != null ? alias : functionName;
            String fullFunctionName = libraryName + "." + namespaceName + "." + functionName;
            interpreter.getSymbolTableStack().peek().setWithOrigin(symbolName, function, fullFunctionName);
            
        } else if (parts.length == 2) {
            // Import a specific namespace (e.g., import IO.File or import IO.File as fileOps)
            String namespaceName = parts[1];
            Namespace namespace = library.getNamespace(namespaceName);
            if (namespace == null) {
                throw new RuntimeException("Namespace not found: " + namespaceName);
            }
            String symbolName = alias != null ? alias : namespaceName;
            interpreter.getSymbolTableStack().peek().set(symbolName, namespace);
        } else {
            throw new RuntimeException("Invalid import path: " + importPath);
        }
    }

    @Override
    public Object visitImportStatement(vg_langParser.ImportStatementContext ctx) {
        String importPath = ctx.importPath().getText();
        String alias = null;
        
        // Check if there's an alias specified
        if (ctx.IDENTIFIER() != null) {
            alias = ctx.IDENTIFIER().getText();
        }
        
        processImport(importPath, alias);
        return null;
    }

    @Override
    public Object visitLibraryDeclaration(vg_langParser.LibraryDeclarationContext ctx) {
        String libraryName = ctx.IDENTIFIER().getText();
        Library library = new Library(libraryName);
        for (vg_langParser.NamespaceDeclarationContext nsCtx : ctx.namespaceDeclaration()) {
            String nsName = nsCtx.IDENTIFIER().getText();
            Namespace namespace = new Namespace(nsName);
            processNamespaceBody(nsCtx, namespace);
            library.addNamespace(namespace);
        }
        interpreter.getModuleRegistry().addLibrary(library);
        return library;
    }

    @Override
    public Object visitNamespaceDeclaration(vg_langParser.NamespaceDeclarationContext ctx) {
        String nsName = ctx.IDENTIFIER().getText();
        Namespace namespace = new Namespace(nsName);
        processNamespaceBody(ctx, namespace);
        return namespace;
    }

    private void processNamespaceBody(vg_langParser.NamespaceDeclarationContext nsCtx, Namespace namespace) {
        for (vg_langParser.FunctionDeclarationContext funcCtx : nsCtx.functionDeclaration()) {
            String functionName = funcCtx.IDENTIFIER().getText();
            Function function = new Function(
                    getParameters(funcCtx.parameterList()),
                    funcCtx.block(),
                    interpreter
            );
            namespace.addSymbol(functionName, function);
        }

        for (vg_langParser.VariableDeclarationContext varCtx : nsCtx.variableDeclaration()) {
            String varName = varCtx.IDENTIFIER().getText();
            Object value = interpreter.visit(varCtx.expression());
            namespace.addSymbol(varName, value);
        }

        for (vg_langParser.ConstDeclarationContext constCtx : nsCtx.constDeclaration()) {
            String constName = constCtx.IDENTIFIER().getText();
            Object value = interpreter.visit(constCtx.expression());
            namespace.addSymbol(constName, value);
        }

        for (vg_langParser.NamespaceDeclarationContext childNsCtx : nsCtx.namespaceDeclaration()) {
            String childName = childNsCtx.IDENTIFIER().getText();
            Namespace childNs = new Namespace(childName);
            processNamespaceBody(childNsCtx, childNs);
            namespace.addChildNamespace(childNs);
        }
    }

    private List<String> getParameters(vg_langParser.ParameterListContext paramCtx) {
        List<String> params = new ArrayList<>();
        if (paramCtx != null) {
            for (var id : paramCtx.IDENTIFIER()) {
                params.add(id.getText());
            }
        }
        return params;
    }

    @Override
    public Object visit(ParseTree tree) {
        return super.visit(tree);
    }
} 