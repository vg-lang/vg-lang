package components.visitors;

import components.*;
import java.util.*;
import org.antlr.v4.runtime.Token;

public class LiteralVisitor extends BaseVisitor {
    public LiteralVisitor(Interpreter interpreter) {
        super(interpreter);
    }

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
            return interpreter.visit(ctx.arrayLiteral());
        }
        return null;
    }

    public Object visitArrayLiteral(vg_langParser.ArrayLiteralContext ctx) {
        List<Object> elements = new ArrayList<>();
        for (vg_langParser.ExpressionContext exprCtx : ctx.expression()) {
            Object value = interpreter.visit(exprCtx);
            elements.add(value);
        }
        return elements;
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
} 