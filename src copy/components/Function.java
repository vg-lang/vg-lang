package components;

import java.util.List;
public class Function {
    private List<String> parameters;
    private vg_langParser.BlockContext block;
    private Interpreter interpreter;


    public Function(List<String> parameters, vg_langParser.BlockContext block, Interpreter interpreter) {
        this.parameters = parameters;
        this.block = block;
        this.interpreter = interpreter;
    }

    public List<String> getParameters() {return parameters;}

    public vg_langParser.BlockContext getBlock() {return block;}

    public Object call(List<Object> args) {
        if (args.size() != parameters.size()) {
            int line = block != null && block.start != null ? block.start.getLine() : 0;
            int column = block != null && block.start != null ? block.start.getCharPositionInLine() : 0;
            
            throw new ErrorHandler.VGArgumentException(
                "Function expects " + parameters.size() + " arguments but got " + args.size(),
                line, column
            );
        }
        
        SymbolTable functionScope = new SymbolTable();
        
        for (int i = 0; i < parameters.size(); i++) {
            functionScope.set(parameters.get(i), args.get(i));
        }
        
        interpreter.getSymbolTableStack().push(functionScope);
        
        Object result = null;
        try {
            interpreter.visit(block);
        } catch (ReturnException e) {
            result = e.getValue();
        } finally {
            interpreter.getSymbolTableStack().pop();
        }
        
        return result;
    }
}
