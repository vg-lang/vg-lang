package components.visitors;

import components.*;
import java.util.*;

public class ExpressionVisitor extends BaseVisitor {
    public ExpressionVisitor(Interpreter interpreter) {
        super(interpreter);
    }

    public Object visitLogicalOrExpression(vg_langParser.LogicalOrExpressionContext ctx) {
        Object left = interpreter.visit(ctx.logicalAndExpression(0));
        for (int i = 1; i < ctx.logicalAndExpression().size(); i++) {
            if (toBoolean(left)) {
                return true;
            }
            Object right = interpreter.visit(ctx.logicalAndExpression(i));
            left = toBoolean(right);
        }
        return left;
    }

    public Object visitEqualityExpression(vg_langParser.EqualityExpressionContext ctx) {
        Object left = interpreter.visit(ctx.relationalExpression(0));
        for (int i = 1; i < ctx.relationalExpression().size(); i++) {
            String op = ctx.getChild(2 * i - 1).getText();
            Object right = interpreter.visit(ctx.relationalExpression(i));
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

    public Object visitRelationalExpression(vg_langParser.RelationalExpressionContext ctx) {
        Object left = interpreter.visit(ctx.additiveExpression(0));
        for (int i = 1; i < ctx.additiveExpression().size(); i++) {
            String op = ctx.getChild(2 * i - 1).getText();
            Object right = interpreter.visit(ctx.additiveExpression(i));
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
                return isDouble ? leftNum.doubleValue() + rightNum.doubleValue() : leftNum.intValue() + rightNum.intValue();
            case "-":
                return isDouble ? leftNum.doubleValue() - rightNum.doubleValue() : leftNum.intValue() - rightNum.intValue();
            case "*":
                return isDouble ? leftNum.doubleValue() * rightNum.doubleValue() : leftNum.intValue() * rightNum.intValue();
            case "/":
                if (rightNum.doubleValue() == 0) {
                    throw new RuntimeException("Division by zero");
                }
                return isDouble ? leftNum.doubleValue() / rightNum.doubleValue() : leftNum.intValue() / rightNum.intValue();
            case "%":
                return isDouble ? leftNum.doubleValue() % rightNum.doubleValue() : leftNum.intValue() % rightNum.intValue();
            default:
                throw new RuntimeException("Unknown operator '" + operator + "'.");
        }
    }
} 