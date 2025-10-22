package components.visitors;

import components.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

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
            
        } else if (parts.length == 2) {
            // Import a specific namespace (e.g., import IO.File or import IO.File as fileOps)
            String namespaceName = parts[1];
            Namespace namespace = library.getNamespace(namespaceName);
            if (namespace == null) {
                throw new RuntimeException("Namespace not found: " + namespaceName);
            }
            String symbolName = alias != null ? alias : namespaceName;
            interpreter.getSymbolTableStack().peek().set(symbolName, namespace);
        } else if (parts.length == 3 && !parts[2].equals("*")) {
            // Import a specific function from a namespace (e.g., import MathLib.constants.pi)
            String namespaceName = parts[1];
            String functionName = parts[2];
            
            Namespace namespace = library.getNamespace(namespaceName);
            if (namespace == null) {
                throw new RuntimeException("Namespace not found: " + namespaceName);
            }
            
            Object symbol = namespace.getSymbol(functionName);
            if (symbol == null) {
                throw new RuntimeException("Symbol not found: " + functionName + " in namespace " + namespaceName);
            }
            
            String symbolName = alias != null ? alias : functionName;
            interpreter.getSymbolTableStack().peek().set(symbolName, symbol);
        } else {
            throw new RuntimeException("Invalid import path: " + importPath);
        }
    }

    public void processFileImport(String filePath, String alias) {
        try {
            // Resolve the file path relative to the importing file's directory
            Path resolvedPath = resolveImportPath(filePath);
            
            // Load the file content
            String content = new String(Files.readAllBytes(resolvedPath), StandardCharsets.UTF_8);
            
            // Save the current file path and set the new one for nested imports
            String previousFile = ErrorHandler.getCurrentFile();
            ErrorHandler.setCurrentFile(resolvedPath.toString());
            
            try {
                // Parse the file
                CharStream input = CharStreams.fromString(content);
                vg_langLexer lexer = new vg_langLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                vg_langParser parser = new vg_langParser(tokens);
                
                // Parse the program
                vg_langParser.ProgramContext programCtx = parser.program();
                
                // Execute the imported file like a normal program (similar to ProgramVisitor)
                // First pass: Process all function and class declarations
                for (vg_langParser.StatementContext stmtCtx : programCtx.statement()) {
                    if (stmtCtx.functionDeclaration() != null) {
                        interpreter.visit(stmtCtx.functionDeclaration());
                    } else if (stmtCtx.classDeclaration() != null) {
                        interpreter.visit(stmtCtx.classDeclaration());
                    }
                }



                // Second pass: Process all other statements (excluding functions and classes)
                for (vg_langParser.StatementContext stmtCtx : programCtx.statement()) {
                    if (stmtCtx.functionDeclaration() == null && stmtCtx.classDeclaration() == null) {
                        interpreter.visit(stmtCtx);
                    }
                }
                
                // If an alias is specified, we need to handle it for classes
                if (alias != null) {
                    SymbolTable currentScope = interpreter.getSymbolTableStack().peek();
                    
                    // Find classes that were defined in the imported file and create alias mappings
                    for (vg_langParser.StatementContext stmtCtx : programCtx.statement()) {
                        if (stmtCtx.classDeclaration() != null) {
                            String className = stmtCtx.classDeclaration().IDENTIFIER(0).getText();
                            Object classDef = currentScope.get(className);
                            if (classDef != null) {
                                currentScope.set(alias, classDef);
                                // Optionally remove the original class name to avoid conflicts
                                // currentScope.remove(className);
                            }
                        }
                    }
                }
            } finally {
                // Restore the previous file path
                ErrorHandler.setCurrentFile(previousFile);
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filePath + " - " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error parsing file: " + filePath + " - " + e.getMessage());
        }
    }
    
    private Path resolveImportPath(String filePath) {
        // Get the current file being processed
        String currentFile = ErrorHandler.getCurrentFile();
        
        // If we have a current file, resolve relative to its directory
        if (currentFile != null && !currentFile.isEmpty()) {
            Path currentFilePath = Paths.get(currentFile);
            Path currentDir = currentFilePath.getParent();
            
            if (currentDir != null) {
                Path relativePath = currentDir.resolve(filePath);
                if (Files.exists(relativePath)) {
                    return relativePath;
                }
            }
        }
        
        // Fall back to current working directory
        Path absolutePath = Paths.get(filePath);
        if (Files.exists(absolutePath)) {
            return absolutePath;
        }
        
        // If neither exists, return the original path to get a proper error message
        return Paths.get(filePath);
    }

    @Override
    public Object visitImportStatement(vg_langParser.ImportStatementContext ctx) {
        String importPath = ctx.importPath().getText();
        String alias = null;
        
        // Check if there's an alias specified
        if (ctx.IDENTIFIER() != null) {
            alias = ctx.IDENTIFIER().getText();
        }
        
        // Check if it's a file import (string literal) or library import (dot notation)
        if (importPath.startsWith("\"") && importPath.endsWith("\"")) {
            // File import: import "path/to/file.vg"
            String filePath = importPath.substring(1, importPath.length() - 1); // Remove quotes
            processFileImport(filePath, alias);
        } else {
            // Library import: import Library.Namespace
            processImport(importPath, alias);
        }
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

        // Process class declarations in the namespace
        for (vg_langParser.ClassDeclarationContext classCtx : nsCtx.classDeclaration()) {
            String className = classCtx.IDENTIFIER(0).getText(); // First identifier is the class name
            ClassDefinition classDef = (ClassDefinition) interpreter.visit(classCtx);
            namespace.addSymbol(className, classDef);
        }
    }

    private List<String> getParameters(vg_langParser.ParameterListContext paramCtx) {
        List<String> params = new ArrayList<>();
        if (paramCtx != null) {
            for (TerminalNode id : paramCtx.IDENTIFIER()) {
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