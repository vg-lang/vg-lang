package components;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class ErrorHandler {
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BOLD = "\u001B[1m";

    private static boolean useColorOutput = true;
    private static String currentFilePath = "";


    public static void setUseColorOutput(boolean useColor) {
        useColorOutput = useColor;
    }


    public static void setCurrentFile(String filePath) {
        currentFilePath = filePath;
    }


    public static void reportSyntaxError(Token token, String message) {
        String location = token != null ?
                String.format("line %d:%d", token.getLine(), token.getCharPositionInLine() + 1) :
                "unknown location";

        printError("Syntax Error", location, message);
    }

    public static void reportRuntimeError(int line, int column, String message) {
        String location = String.format("line %d:%d", line, column);
        printError("Runtime Error", location, message);
    }


    public static void reportError(String errorType, String message) {
        printError(errorType, null, message);
    }


    public static void reportTypeError(int line, int column, String message) {
        String location = String.format("line %d:%d", line, column);
        printError("Type Error", location, message);
    }


    private static void printError(String errorType, String location, String message) {
        StringBuilder sb = new StringBuilder();

        if (useColorOutput) {
            sb.append(ANSI_BOLD);
            sb.append(ANSI_RED);
        }

        sb.append("VG Error: ").append(errorType);

        if (useColorOutput) {
            sb.append(ANSI_RESET);
        }

        if (location != null) {
            sb.append(" at ").append(location);
        }

        if (!currentFilePath.isEmpty()) {
            sb.append(" in file '").append(currentFilePath).append("'");
        }

        sb.append("\n");

        if (useColorOutput) {
            sb.append(ANSI_YELLOW);
        }

        sb.append("  → ").append(message);

        if (useColorOutput) {
            sb.append(ANSI_RESET);
        }

        System.err.println(sb.toString());
    }


    public static class VGException extends RuntimeException {
        private final int line;
        private final int column;

        public VGException(String message, int line, int column) {
            super(message);
            this.line = line;
            this.column = column;
        }

        public int getLine() { return line; }
        public int getColumn() { return column; }
    }

    public static class VGTypeException extends VGException {
        public VGTypeException(String message, int line, int column) {
            super(message, line, column);
        }
    }

    public static class VGNameException extends VGException {
        public VGNameException(String message, int line, int column) {
            super(message, line, column);
        }
    }

    public static class VGImportException extends VGException {
        public VGImportException(String message, int line, int column) {
            super(message, line, column);
        }
    }

    public static RuntimeException createSyntaxError(ParserRuleContext ctx, String message) {
        int line = ctx.start.getLine();
        int column = ctx.start.getCharPositionInLine();
        return new RuntimeException("Syntax error at line " + line + ", column " + column + ": " + message);
    }
    
    public static RuntimeException createSyntaxError(Token token, String message) {
        int line = token.getLine();
        int column = token.getCharPositionInLine();
        return new RuntimeException("Syntax error at line " + line + ", column " + column + ": " + message);
    }
    
    public static RuntimeException createRuntimeError(String message) {
        return new RuntimeException("Runtime error: " + message);
    }
    
    public static RuntimeException handleException(Exception e, ParserRuleContext ctx) {
        String msg = e.getMessage();
        if (msg != null) {
            if (msg.contains("Index -1 out of bounds")) {
                return createSyntaxError(ctx, "Invalid syntax. Check for missing semicolons or extra semicolons.");
            }

        }
        return createSyntaxError(ctx, "Unexpected error: " + e.getMessage());
    }
}