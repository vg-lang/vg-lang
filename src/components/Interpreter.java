package components;

import java.util.*;

public class Interpreter extends vg_langBaseVisitor {
    public Deque<SymbolTable> symbolTableStack = new ArrayDeque<>();
    public Interpreter() {
        symbolTableStack.push(new SymbolTable());
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
    public Object visitVariableDeclaration(vg_langParser.VariableDeclarationContext ctx) {
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
        currentTable.setConstant(constName, value); // a method that sets a variable and marks it as const
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
        }

         else {
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
        }  else if (ctx.expression() != null) {
            return visit(ctx.expression());
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
        }
        else if (ctx.arrayLiteral() != null) {
            return visit(ctx.arrayLiteral());
        }
        return null;
    }
    @Override
    public Object visitPostfixExpression(vg_langParser.PostfixExpressionContext ctx) {
        Object value = visit(ctx.primary());
        for (vg_langParser.ExpressionContext indexExpr : ctx.expression()) {
            Object indexObj = visit(indexExpr);
            if (!(indexObj instanceof Number)) {
                throw new RuntimeException("Array index must be a number.");
            }
            int index = ((Number) indexObj).intValue();
            if (!(value instanceof List)) {
                throw new RuntimeException("Cannot index into non-array value.");
            }
            List<?> array = (List<?>) value;
            if (index < 0 || index >= array.size()) {
                throw new RuntimeException("Array index out of bounds.");
            }
            value = array.get(index);
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

}