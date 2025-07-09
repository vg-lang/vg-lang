package components.visitors;

import components.*;

public class StatementVisitor extends BaseVisitor {
    public StatementVisitor(Interpreter interpreter) {
        super(interpreter);
    }

    public Object visitIfStatement(vg_langParser.IfStatementContext ctx) {
        if (toBoolean(interpreter.visit(ctx.expression()))) {
            interpreter.visit(ctx.ifBlock);
        } else {
            boolean executed = false;

            for (vg_langParser.ElseIfStatementContext elifCtx : ctx.elseIfStatement()) {
                if (toBoolean(interpreter.visit(elifCtx.expression()))) {
                    interpreter.visit(elifCtx.block());
                    executed = true;
                    break;
                }
            }

            if (!executed && ctx.elseStatement() != null) {
                interpreter.visit(ctx.elseStatement().block());
            }
        }
        return null;
    }

    public Object visitForStatement(vg_langParser.ForStatementContext ctx) {
        symbolTableStack.push(new SymbolTable());

        if (ctx.forInit() != null) {
            interpreter.visit(ctx.forInit());
        }

        while (true) {
            if (ctx.forCondition() != null) {
                Object conditionValue = interpreter.visit(ctx.forCondition());
                if (!toBoolean(conditionValue)) {
                    break;
                }
            }

            interpreter.visit(ctx.block());
            if (ctx.forUpdate() != null) {
                interpreter.visit(ctx.forUpdate());
            }
        }

        symbolTableStack.pop();
        return null;
    }

    public Object visitWhileStatement(vg_langParser.WhileStatementContext ctx) {
        while (toBoolean(interpreter.visit(ctx.expression()))) {
            interpreter.visit(ctx.block());
        }
        return null;
    }

    public Object visitDoWhileStatement(vg_langParser.DoWhileStatementContext ctx) {
        do {
            interpreter.visit(ctx.block());
        } while (toBoolean(interpreter.visit(ctx.expression())));
        return null;
    }

    public Object visitTryStatement(vg_langParser.TryStatementContext ctx) {
        try {
            interpreter.visit(ctx.block());
        } catch (RuntimeException e) {
            boolean handled = false;
            for (vg_langParser.CatchStatementContext catchCtx : ctx.catchStatement()) {
                String exceptionVar = catchCtx.IDENTIFIER().getText();

                SymbolTable catchSymbolTable = new SymbolTable();
                symbolTableStack.push(catchSymbolTable);

                String exceptionMessage = e.getMessage() != null ? e.getMessage() : "An error occurred";
                catchSymbolTable.set(exceptionVar, exceptionMessage);
                
                try {
                    interpreter.visit(catchCtx.block());
                    handled = true;
                    break;
                } finally {
                    symbolTableStack.pop();
                }
            }
            if (!handled) {
                throw e;
            }
        } finally {
            if (ctx.finallyStatement() != null) {
                interpreter.visit(ctx.finallyStatement().block());
            }
        }
        return null;
    }
} 