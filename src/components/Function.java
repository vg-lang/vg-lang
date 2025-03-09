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

    public Object call(List<Object> argValues){
        SymbolTable functionSymbolTable = new SymbolTable();

        interpreter.symbolTableStack.push(functionSymbolTable);

        for (int i = 0; i < parameters.size(); i++) {
            functionSymbolTable.set(parameters.get(i), argValues.get(i));
        }
        Object returnValue = null;
        try{
            interpreter.visit(block);
        } catch (ReturnException e) {
            returnValue = e.getValue();
        }
         finally {
            interpreter.symbolTableStack.pop();
        }
        return returnValue;
    }
}
