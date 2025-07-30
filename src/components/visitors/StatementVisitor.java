package components.visitors;

import components.*;
import java.util.Objects;

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

        try {
            while (true) {
                if (ctx.forCondition() != null) {
                    Object conditionValue = interpreter.visit(ctx.forCondition());
                    if (!toBoolean(conditionValue)) {
                        break;
                    }
                }

                try {
                    interpreter.visit(ctx.block());
                } catch (ContinueException e) {
                    // Continue to next iteration
                }
                
                if (ctx.forUpdate() != null) {
                    interpreter.visit(ctx.forUpdate());
                }
            }
        } catch (BreakException e) {
            // Break out of loop
        }

        symbolTableStack.pop();
        return null;
    }

    public Object visitWhileStatement(vg_langParser.WhileStatementContext ctx) {
        try {
            while (toBoolean(interpreter.visit(ctx.expression()))) {
                try {
                    interpreter.visit(ctx.block());
                } catch (ContinueException e) {
                    // Continue to next iteration
                }
            }
        } catch (BreakException e) {
            // Break out of loop
        }
        return null;
    }

    public Object visitDoWhileStatement(vg_langParser.DoWhileStatementContext ctx) {
        try {
            do {
                try {
                    interpreter.visit(ctx.block());
                } catch (ContinueException e) {
                    // Continue to next iteration
                }
            } while (toBoolean(interpreter.visit(ctx.expression())));
        } catch (BreakException e) {
            // Break out of loop
        }
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

    public Object visitSwitchStatement(vg_langParser.SwitchStatementContext ctx) {
        Object switchValue = interpreter.visit(ctx.expression());
        boolean matched = false;
        boolean fallthrough = false;
        
        try {
            // Check case statements
            for (vg_langParser.SwitchCaseContext caseCtx : ctx.switchCase()) {
                Object caseValue = interpreter.visit(caseCtx.expression());
                
                if (fallthrough || Objects.equals(switchValue, caseValue)) {
                    matched = true;
                    fallthrough = true;
                    
                    // Execute statements in this case
                    for (vg_langParser.StatementContext stmtCtx : caseCtx.statement()) {
                        interpreter.visit(stmtCtx);
                    }
                }
            }
            
            // Check default case if no match or if we're in fallthrough mode
            if ((!matched || fallthrough) && ctx.defaultCase() != null) {
                for (vg_langParser.StatementContext stmtCtx : ctx.defaultCase().statement()) {
                    interpreter.visit(stmtCtx);
                }
            }
        } catch (BreakException e) {
            // Break out of switch
        }
        
        return null;
    }

    public Object visitBreakStatement(vg_langParser.BreakStatementContext ctx) {
        throw new BreakException();
    }

    public Object visitContinueStatement(vg_langParser.ContinueStatementContext ctx) {
        throw new ContinueException();
    }

    public Object visitForEachStatement(vg_langParser.ForEachStatementContext ctx) {
        String itemVariable = ctx.IDENTIFIER().getText();
        Object collection = interpreter.visit(ctx.expression());
        
        // Create a new symbol table for the loop scope
        symbolTableStack.push(new SymbolTable());
        
        try {
            if (collection instanceof java.util.List) {
                java.util.List<?> list = (java.util.List<?>) collection;
                for (Object item : list) {
                    // Set the loop variable
                    symbolTableStack.peek().set(itemVariable, item);
                    
                    try {
                        interpreter.visit(ctx.block());
                    } catch (ContinueException e) {
                        // Continue to next iteration
                        continue;
                    }
                }
            } else if (collection instanceof Object[]) {
                Object[] array = (Object[]) collection;
                for (Object item : array) {
                    // Set the loop variable
                    symbolTableStack.peek().set(itemVariable, item);
                    
                    try {
                        interpreter.visit(ctx.block());
                    } catch (ContinueException e) {
                        // Continue to next iteration
                        continue;
                    }
                }
            } else if (collection instanceof String) {
                String str = (String) collection;
                for (int i = 0; i < str.length(); i++) {
                    // Set the loop variable to each character
                    symbolTableStack.peek().set(itemVariable, String.valueOf(str.charAt(i)));
                    
                    try {
                        interpreter.visit(ctx.block());
                    } catch (ContinueException e) {
                        // Continue to next iteration
                        continue;
                    }
                }
            } else {
                throw new RuntimeException("For-each loop requires an array, list, or string, got: " + 
                    (collection == null ? "null" : collection.getClass().getSimpleName()));
            }
        } catch (BreakException e) {
            // Break out of loop
        } finally {
            symbolTableStack.pop();
        }
        
        return null;
    }
} 