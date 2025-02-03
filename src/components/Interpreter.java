package components;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class Interpreter  extends vg_langBaseVisitor<Object>{
    public Deque<SymbolTable> symbolTableStack = new ArrayDeque<>();

    public Interpreter(){
        symbolTableStack.push(new SymbolTable());
    }
    private SymbolTable currentSymbolTable() {
        return symbolTableStack.peek();
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
