package components;

import java.util.*;

public class Interpreter  extends vg_langBaseVisitor<Object>{
    public Deque<SymbolTable> symbolTableStack = new ArrayDeque<>();

    public Interpreter(){
        symbolTableStack.push(new SymbolTable());
    }
    private SymbolTable currentSymbolTable() {
        return symbolTableStack.peek();
    }

    // general functionality
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
            List<Integer> indices = new ArrayList<>();
            if (targetTable == null) {
                throw new RuntimeException("Variable '" + varName + "' is not defined.");
            }
            return new VariableReference(targetTable, varName, indices);
        }
        return  null;
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

        }
        else if (ctx.expression() != null) {
            return visit(ctx.expression());
        }
        return null;
    }


    // variables
    @Override
    public Object visitVariableDeclaration(vg_langParser.VariableDeclarationContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        Object value = visit(ctx.expression());

        currentSymbolTable().set(varName, value);
        return null;
    }
    private Object getVariable(String name) {

        if (name.contains(".")) {
            return symbolTableStack.getLast().get(name);
        }

        for (SymbolTable table : symbolTableStack) {
            if (table.contains(name)) {
                return table.get(name);
            }
        }
        throw new RuntimeException("Variable '" + name + "' is not defined.");
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

    // print rule
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
}
