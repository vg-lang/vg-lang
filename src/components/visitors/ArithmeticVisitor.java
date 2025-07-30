package components.visitors;

import components.ErrorHandler;
import components.Interpreter;
import components.vg_langBaseVisitor;
import components.vg_langParser;

import java.util.List;

public class ArithmeticVisitor extends vg_langBaseVisitor<Object> {
    private final Interpreter interpreter;

    public ArithmeticVisitor(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public Object visitAdditiveExpression(vg_langParser.AdditiveExpressionContext ctx) {
        Object result = interpreter.visit(ctx.multiplicativeExpression(0));

        for (int i = 1; i < ctx.multiplicativeExpression().size(); i++) {
            String operator = ctx.getChild(2 * i - 1).getText();
            Object right = interpreter.visit(ctx.multiplicativeExpression(i));
            result = evaluateArithmetic(result, right, operator);
        }

        return result;
    }

    @Override
    public Object visitMultiplicativeExpression(vg_langParser.MultiplicativeExpressionContext ctx) {
        Object result = interpreter.visit(ctx.unaryExpression(0));

        for (int i = 1; i < ctx.unaryExpression().size(); i++) {
            String operator = ctx.getChild(2 * i - 1).getText();
            Object right = interpreter.visit(ctx.unaryExpression(i));
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
                    return leftNum.doubleValue() + rightNum.doubleValue();
                } else {
                    return leftNum.intValue() + rightNum.intValue();
                }
            case "-":
                if (isDouble) {
                    return leftNum.doubleValue() - rightNum.doubleValue();
                } else {
                    return leftNum.intValue() - rightNum.intValue();
                }
            case "*":
                if (isDouble) {
                    return leftNum.doubleValue() * rightNum.doubleValue();
                } else {
                    return leftNum.intValue() * rightNum.intValue();
                }
            case "/":
                if (rightNum.doubleValue() == 0) {
                    throw new RuntimeException("Division by zero");
                }
                if (isDouble) {
                    return leftNum.doubleValue() / rightNum.doubleValue();
                } else {
                    return leftNum.intValue() / rightNum.intValue();
                }
            case "%":
                if (isDouble) {
                    return leftNum.doubleValue() % rightNum.doubleValue();
                } else {
                    return leftNum.intValue() % rightNum.intValue();
                }
            default:
                throw new RuntimeException("Unknown operator '" + operator + "'.");
        }
    }
} 