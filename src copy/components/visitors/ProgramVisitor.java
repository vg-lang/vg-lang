package components.visitors;

import components.*;
import components.vg_langParser.StatementContext;

public class ProgramVisitor extends BaseVisitor {
    public ProgramVisitor(Interpreter interpreter) {
        super(interpreter);
    }

    public Object visitProgram(vg_langParser.ProgramContext ctx) {
        // First pass: Process all function declarations
        for (StatementContext stmtCtx : ctx.statement()) {
            if (stmtCtx.functionDeclaration() != null) {
                interpreter.visit(stmtCtx.functionDeclaration());
            }
        }

        // Second pass: Process all other statements
        for (StatementContext stmtCtx : ctx.statement()) {
            if (stmtCtx.functionDeclaration() == null) {
                interpreter.visit(stmtCtx);
            }
        }
        return null;
    }
} 