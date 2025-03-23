import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import components.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class Main {

    public static void main(String[] args) {
        String versionnumber ="1.3.0";
        try {


            if (args.length > 0) {
                if (args[0].equals("--help")) {

                    showHelp();
                }
                else if (args[0].equals("--version")) {

                    displayversion(versionnumber);
                }
                else {

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
                        String source = new String(Files.readAllBytes(Paths.get(filePath)));
                        Interpreter interpreter = new Interpreter();

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

                        ErrorHandler.reportError("Unexpected Error",
                                "An unexpected error occurred: " + e.getMessage());
                    }

                }
            } else {
                ErrorHandler.reportError("Command Error", "No file specified. Use --help for more information.");            }
        } catch (Exception e) {
            System.err.println("VG Language internal error: " + e.getMessage());
            System.err.println("Please report this issue to the VG Language team.");
        }

    }

    private static void showHelp() {
        System.out.println("Usage: VG  <file.vg> [options]");
        System.out.println("Options:");
        System.out.println("  --help          Show this help menu");
        System.out.println("  --version          Show version information");
        System.out.println("  <file.vg>  The vg source file to interpret");

    }
    private static void displayversion(String versionnumber) {
        System.out.println("VG Version : "+versionnumber);
    }
}