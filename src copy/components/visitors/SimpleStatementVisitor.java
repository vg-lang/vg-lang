package components.visitors;

import components.Interpreter;
import components.ReturnException;
import components.vg_langBaseVisitor;
import components.vg_langParser;

public class SimpleStatementVisitor extends vg_langBaseVisitor<Object> {
    private final Interpreter interpreter;

    public SimpleStatementVisitor(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public Object visitReturnStatement(vg_langParser.ReturnStatementContext ctx) {
        Object returnValue = null;
        if (ctx.expression() != null) {
            returnValue = interpreter.visit(ctx.expression());
        }
        throw new ReturnException(returnValue);
    }

    @Override
    public Object visitPrintStatement(vg_langParser.PrintStatementContext ctx) {
        StringBuilder output = new StringBuilder();
        for (vg_langParser.ExpressionContext exprCtx : ctx.expression()) {
            Object value = interpreter.visit(exprCtx);
            output.append(value).append(" ");
        }
        System.out.println(output.toString().trim());
        return null;
    }

    @Override
    public Object visitThrowStatement(vg_langParser.ThrowStatementContext ctx) {
        Object value = interpreter.visit(ctx.expression());
        throw new RuntimeException((String) value);
    }
} 