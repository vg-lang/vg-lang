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

    public void processImport(String importPath) {
        String[] parts = importPath.split("\\.");
        if (parts.length < 2) {
            throw new RuntimeException("Invalid import path: " + importPath);
        }

        String libraryName = parts[0];
        Library library = interpreter.getModuleRegistry().getLibrary(libraryName);
        if (library == null) {
            throw new RuntimeException("Library not found: " + libraryName);
        }

        String namespaceName = parts[1];
        Namespace namespace = library.getNamespace(namespaceName);
        if (namespace == null) {
            throw new RuntimeException("Namespace not found: " + namespaceName);
        }

        // If importing a specific namespace (e.g., import Guilibrary.window)
        // add the namespace itself as a symbol
        interpreter.getSymbolTableStack().peek().set(namespaceName, namespace);

        // If importing all symbols from a namespace (e.g., import Guilibrary.window.*)
        if (parts.length > 2 && parts[2].equals("*")) {
            // Import all symbols from the namespace into the current symbol table
            for (Map.Entry<String, Object> entry : namespace.getSymbols().entrySet()) {
                interpreter.getSymbolTableStack().peek().set(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public Object visitImportStatement(vg_langParser.ImportStatementContext ctx) {
        String importPath = ctx.importPath().getText();
        processImport(importPath);
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