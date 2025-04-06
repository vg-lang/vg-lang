import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

import components.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class Main {

    public static void main(String[] args) {
        String versionnumber = "1.3.0";
        try {
            if (args.length > 0) {
                if (args[0].equals("--help")) {
                    showHelp();
                } else if (args[0].equals("--version")) {
                    System.out.println("VG Version : " + versionnumber);
                } else if (args[0].equals("--docgen")) {

                    if (args.length < 3) {
                        System.out.println("Error: Missing arguments for documentation generation.");
                        System.out.println("Usage: vg --docgen <input-path> <output-directory>");
                        System.out.println("  <input-path> can be a single file or a directory");
                        System.out.println("  <output-directory> is where the documentation will be generated");
                        return;
                    }
                    
                    String inputPath = args[1];
                    String outputDir = args[2];
                    
                    try {
                        generateDocumentation(inputPath, outputDir);
                        System.out.println("Documentation generated successfully in: " + outputDir);
                    } catch (Exception e) {
                        System.err.println("Error generating documentation: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    String filePath = args[0];
                    File file = new File(filePath);
                    if (!file.exists()) {
                        ErrorHandler.reportError("File Error", "Could not find file: " + filePath);
                        return;
                    }

                    if (!file.canRead()) {
                        ErrorHandler.reportError("File Error", "Cannot read file: " + filePath + ". Check permissions.");
                        return;
                    }

                    if (!filePath.toLowerCase().endsWith(".vg")) {
                        ErrorHandler.reportError("File Error", "File must have .vg extension: " + filePath);
                        return;
                    }
                    ErrorHandler.setCurrentFile(filePath);
                    try {
                        Path scriptPath = Paths.get(filePath).toAbsolutePath();
                        Path projectRoot = scriptPath.getParent();
                        Path packageFolder = projectRoot.resolve("packages");
                        String source = new String(Files.readAllBytes(Paths.get(filePath)));
                        Interpreter interpreter = new Interpreter(packageFolder.toString());

                        vg_langLexer lexer = new vg_langLexer(CharStreams.fromString(source));
                        lexer.removeErrorListeners();
                        lexer.addErrorListener(new VGErrorListener());

                        CommonTokenStream tokens = new CommonTokenStream(lexer);
                        vg_langParser parser = new vg_langParser(tokens);
                        parser.removeErrorListeners();
                        parser.addErrorListener(new VGErrorListener());
                        interpreter.visit(parser.program());
                    } catch (ParseCancellationException e) {
                        ErrorHandler.reportError("Syntax Error", e.getMessage());
                    } catch (ErrorHandler.VGTypeException e) {
                        ErrorHandler.reportTypeError(e.getLine(), e.getColumn(), e.getMessage());
                    } catch (ErrorHandler.VGNameException e) {
                        ErrorHandler.reportRuntimeError(e.getLine(), e.getColumn(), e.getMessage());
                    } catch (ErrorHandler.VGImportException e) {
                        ErrorHandler.reportError("Import Error", e.getMessage());
                    } catch (ErrorHandler.VGException e) {
                        ErrorHandler.reportRuntimeError(e.getLine(), e.getColumn(), e.getMessage());
                    } catch (Exception e) {
                        if (e.getMessage() != null && e.getMessage().contains("Index -1 out of bounds")) {
                            String source = new String(Files.readAllBytes(Paths.get(filePath)));
                            String[] lines = source.split("\n");
                            
                            // Find problematic lines
                            int structLine = -1;
                            int enumLine = -1;
                            
                            for (int i = 0; i < lines.length; i++) {
                                String line = lines[i].trim();
                                if (line.endsWith("};")) {
                                    if (line.startsWith("}") || findMatchingOpenBrace(lines, i).contains("struct")) {
                                        structLine = i + 1; // +1 because line numbers are 1-based
                                        break;
                                    } else if (findMatchingOpenBrace(lines, i).contains("enum")) {
                                        enumLine = i + 1;
                                        break;
                                    }
                                }
                            }
                            
                            if (structLine != -1) {
                                ErrorHandler.reportError("Syntax Error", 
                                    "Invalid struct declaration at line " + structLine + ". Remove the semicolon after the closing brace '}' of the struct.");
                            } 
                            else if (enumLine != -1) {
                                ErrorHandler.reportError("Syntax Error", 
                                    "Invalid enum declaration at line " + enumLine + ". Remove the semicolon after the closing brace '}' of the enum.");
                            }
                            else {
                                ErrorHandler.reportError("Syntax Error", 
                                    "Invalid struct or enum declaration. Make sure each field ends with a semicolon and there is no semicolon after the closing brace.");
                            }
                        } else {
                            ErrorHandler.reportError("Unexpected Error", e.getMessage());
                        }
                    }
                }
            } else {
                ErrorHandler.reportError("Command Error", "No file specified. Use --help for more information.");
            }
        } catch (Exception e) {
            System.err.println("VG Language internal error: " + e.getMessage());
            System.err.println("Please report this issue to the VG Language team.");
        }
    }

    private static void showHelp() {
        System.out.println("VG Language Interpreter");
        System.out.println("Usage:");
        System.out.println("  vg <file>                  Run a VG program");
        System.out.println("  vg --help                  Show this help message");
        System.out.println("  vg --version               Show the version number");
        System.out.println("  vg --docgen <in> <out>     Generate documentation");
        System.out.println("");
        System.out.println("Documentation Generation:");
        System.out.println("  vg --docgen <input-path> <output-directory>");
        System.out.println("    <input-path> can be a single file or a directory");
        System.out.println("    <output-directory> is where the documentation will be generated");
        System.out.println("");
        System.out.println("Examples:");
        System.out.println("  vg program.vg              Run program.vg");
        System.out.println("  vg --docgen program.vg ./docs");
        System.out.println("  vg --docgen libraries/Guilibrary.vglib ./docs");
        System.out.println("  vg --docgen . ./docs       Generate docs for entire project");
    }

    private static void generateDocumentation(String inputPath, String outputDir) throws IOException {
        DocGenerator docGen = new DocGenerator(outputDir);
        
        File inputFile = new File(inputPath);
        if (inputFile.isDirectory()) {

            docGen.generateProjectDocs(inputPath);
        } else if (inputFile.isFile()) {

            docGen.generateFileDoc(inputPath);
        } else {
            throw new IOException("Input path does not exist: " + inputPath);
        }
    }

    // Helper method to find the matching opening brace and return the line content
    private static String findMatchingOpenBrace(String[] lines, int closeBraceLine) {
        int braceCount = 1;
        for (int i = closeBraceLine - 1; i >= 0; i--) {
            String line = lines[i];
            for (int j = line.length() - 1; j >= 0; j--) {
                if (line.charAt(j) == '}') braceCount++;
                if (line.charAt(j) == '{') {
                    braceCount--;
                    if (braceCount == 0) {
                        return line; // Return the line with the opening brace
                    }
                }
            }
        }
        return "";
    }
}