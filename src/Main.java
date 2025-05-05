import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import components.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class Main {

    public static void main(String[] args) {
        String versionnumber = "1.4.0";
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

                        if (!Files.exists(packageFolder)) {
                            Files.createDirectories(packageFolder);
                            System.out.println("Created packages directory: " + packageFolder);
                        }

                        String sourceCode = new String(Files.readAllBytes(Paths.get(filePath)));
                        Interpreter interpreter = new Interpreter(packageFolder.toString());

                        loadAvailableLibraries(interpreter, packageFolder.toString(), false);

                        String libraryFolder = System.getenv("VG_LIBRARIES_PATH");
                        if (libraryFolder != null && !libraryFolder.isEmpty()) {
                            loadAvailableLibraries(interpreter, libraryFolder, false);
                        }

                        Path projectsFolder = projectRoot.resolve("projects");
                        if (Files.exists(projectsFolder) && Files.isDirectory(projectsFolder)) {
                            Path projectPackagesFolder = projectsFolder.resolve("packages");
                            if (Files.exists(projectPackagesFolder)) {
                                loadAvailableLibraries(interpreter, projectPackagesFolder.toString(), false);
                            }
                        }

                        try {
                            interpreter.interpret(sourceCode);
                        } catch (ErrorHandler.VGException e) {
                            int line = e.getLine();
                            int column = e.getColumn();

                            if (line <= 0) {
                                System.err.println("VG Error: " + e.getMessage());
                            } else {
                                ErrorHandler.reportRuntimeError(line, column, e.getMessage());
                            }
                        } catch (Exception e) {
                            System.err.println("Runtime error: " + e.getMessage());
                        }
                    } catch (IOException e) {
                        ErrorHandler.reportError("File Error", "Error reading file: " + e.getMessage());
                    }
                }
            } else {
                startREPL();
            }
        } catch (Exception e) {
            System.err.println("VG Language internal error: " + e.getMessage());
            System.err.println("Please report this issue to the VG Language team.");
        }
    }

    private static void startREPL() {
        System.out.println("VG Language Interactive REPL (Read-Eval-Print Loop)");
        System.out.println("Type 'exit' or 'quit' to exit the REPL");
        System.out.println("Type 'help' for available commands");

        Scanner scanner = new Scanner(System.in);

        Path currentPath = Paths.get("").toAbsolutePath();
        Path packageFolder = currentPath.resolve("packages");

        Path projectPackagesFolder = null;
        Path projectsFolder = Paths.get("").resolve("projects");
        if (Files.exists(projectsFolder) && Files.isDirectory(projectsFolder)) {
            projectPackagesFolder = projectsFolder.resolve("packages");
            if (Files.exists(projectPackagesFolder)) {
                System.out.println("Found project packages folder: " + projectPackagesFolder);
            }
        }

        try {
            if (!Files.exists(packageFolder)) {
                Files.createDirectories(packageFolder);
                System.out.println("Created packages directory: " + packageFolder);
            }
        } catch (IOException e) {
            System.err.println("Error creating packages directory: " + e.getMessage());
            return;
        }

        Interpreter interpreter = new Interpreter(packageFolder.toString());


        List<String> loadedLibraries = loadAvailableLibraries(interpreter, packageFolder.toString(), true);

        if (projectPackagesFolder != null && Files.exists(projectPackagesFolder)) {
            loadedLibraries.addAll(loadAvailableLibraries(interpreter, projectPackagesFolder.toString(), true));
        }

        String libraryFolder = System.getenv("VG_LIBRARIES_PATH");
        if (libraryFolder != null && !libraryFolder.isEmpty()) {
            loadedLibraries.addAll(loadAvailableLibraries(interpreter, libraryFolder, true));
        }

        while (true) {
            System.out.print("vg> ");

            StringBuilder inputBuilder = new StringBuilder();
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                System.out.println("Exiting VG REPL...");
                break;
            } else if (input.equalsIgnoreCase("help")) {
                showREPLHelp();
                continue;
            } else if (input.equalsIgnoreCase("clear")) {
                try {
                    final String os = System.getProperty("os.name");
                    if (os.contains("Windows")) {
                        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    } else {
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                    }
                } catch (Exception e) {
                    System.err.println("Error clearing console: " + e.getMessage());
                }
                continue;
            } else if (input.equalsIgnoreCase("libs")) {
                if (loadedLibraries.isEmpty()) {
                    System.out.println("No libraries are currently loaded.");
                } else {
                    System.out.println("Loaded libraries: " + String.join(", ", loadedLibraries));
                }
                continue;
            }

            inputBuilder.append(input);

            boolean isCompleteCode = isCompleteCode(inputBuilder.toString());

            while (!isCompleteCode) {
                System.out.print("... ");
                String additionalInput = scanner.nextLine();

                if (additionalInput.trim().equals(".")) {
                    break;
                }

                inputBuilder.append("\n").append(additionalInput);
                isCompleteCode = isCompleteCode(inputBuilder.toString());
            }

            String finalInput = inputBuilder.toString();

            if (!finalInput.isEmpty()) {
                try {
                    Object result = interpreter.interpret(finalInput);

                    if (result != null) {
                        System.out.println("=> " + result);
                    }
                } catch (ErrorHandler.VGException e) {
                    int line = e.getLine();
                    int column = e.getColumn();

                    if (line <= 0) {
                        System.err.println("VG Error: " + e.getMessage());
                    } else {
                        ErrorHandler.reportRuntimeError(line, column, e.getMessage());
                    }
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }
    }

    private static boolean isCompleteCode(String code) {

        int openBraces = 0;
        int closeBraces = 0;
        boolean inString = false;
        boolean inSingleLineComment = false;
        boolean inMultiLineComment = false;
        char[] codeChars = code.toCharArray();

        for (int i = 0; i < codeChars.length; i++) {
            char c = codeChars[i];

            if (inSingleLineComment) {
                if (c == '\n') {
                    inSingleLineComment = false;
                }
                continue;
            }

            if (inMultiLineComment) {
                if (c == '#' && i > 0 && codeChars[i - 1] == '/') {
                    inMultiLineComment = false;
                }
                continue;
            }

            if (inString) {
                if (c == '"' && (i == 0 || codeChars[i - 1] != '\\')) {
                    inString = false;
                }
                continue;
            }

            if (c == '#' && i < codeChars.length - 1 && codeChars[i + 1] == '#') {
                inSingleLineComment = true;
                i++;
                continue;
            }

            if (c == '/' && i < codeChars.length - 1 && codeChars[i + 1] == '#') {
                inMultiLineComment = true;
                i++;
                continue;
            }

            if (c == '"') {
                inString = true;
                continue;
            }

            if (c == '{') {
                openBraces++;
            } else if (c == '}') {
                closeBraces++;
            }
        }

        if (inString || inMultiLineComment) {
            return false;
        }

        if (openBraces != closeBraces) {
            return false;
        }

        String trimmed = code.trim();
        if (trimmed.isEmpty()) {
            return true;
        }

        char lastChar = trimmed.charAt(trimmed.length() - 1);
        boolean endsWithSemicolon = lastChar == ';';
        boolean endsWithBrace = lastChar == '}';

        return openBraces > 0 || endsWithSemicolon || endsWithBrace;
    }

    private static void showREPLHelp() {
        System.out.println("Available REPL commands:");
        System.out.println("  help   - Show this help message");
        System.out.println("  exit   - Exit the REPL");
        System.out.println("  quit   - Exit the REPL");
        System.out.println("  clear  - Clear the console");
        System.out.println("  libs   - Show currently loaded libraries");
        System.out.println("  .      - End multi-line input (when in multi-line mode)");

    }

    private static void showHelp() {
        System.out.println("VG Language Interpreter");
        System.out.println("Usage:");
        System.out.println("  vg <file>                  Run a VG program");
        System.out.println("  vg                         Start the REPL mode");
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

    private static String findMatchingOpenBrace(String[] lines, int closeBraceLine) {
        int braceCount = 1;
        for (int i = closeBraceLine - 1; i >= 0; i--) {
            String line = lines[i];
            for (int j = line.length() - 1; j >= 0; j--) {
                if (line.charAt(j) == '}') braceCount++;
                if (line.charAt(j) == '{') {
                    braceCount--;
                    if (braceCount == 0) {
                        return line;
                    }
                }
            }
        }
        return "";
    }


    private static List<String> loadAvailableLibraries(Interpreter interpreter, String folderPath, boolean verbose) {
        List<String> loadedLibraries = new ArrayList<>();

        if (folderPath == null || folderPath.isEmpty()) {
            return loadedLibraries;
        }

        Path path = Paths.get(folderPath);
        if (!Files.exists(path) || !Files.isDirectory(path)) {

            return loadedLibraries;
        }

        try {
            Files.list(path).forEach(file -> {
                if (Files.isDirectory(file)) {
                    try {
                        Files.list(file).forEach(subfile -> {
                            String subFileName = subfile.getFileName().toString();
                            if (subFileName.toLowerCase().endsWith(".vglib") && Files.isRegularFile(subfile)) {
                                String libName = subFileName.substring(0, subFileName.lastIndexOf('.'));
                                if (tryImportLibrary(interpreter, libName, verbose)) {
                                    loadedLibraries.add(libName);
                                }
                            }
                        });
                    } catch (IOException e) {
                    }
                } else if (file.getFileName().toString().toLowerCase().endsWith(".vglib") && Files.isRegularFile(file)) {
                    String libName = file.getFileName().toString();
                    libName = libName.substring(0, libName.lastIndexOf('.'));
                    if (tryImportLibrary(interpreter, libName, verbose)) {
                        loadedLibraries.add(libName);
                    }
                }
            });


        } catch (IOException e){}

        return loadedLibraries;
    }


    private static boolean tryImportLibrary(Interpreter interpreter, String libName, boolean verbose) {
        try {
            String importStatement = "import " + libName + ".*;";
            interpreter.interpret(importStatement);


            return true;
        } catch (Exception e) {

            return false;
        }
    }
}