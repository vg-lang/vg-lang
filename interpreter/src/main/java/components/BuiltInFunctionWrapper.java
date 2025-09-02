package components;


import java.util.Collections;
import java.util.List;


public class BuiltInFunctionWrapper extends Function {
    private final BuiltInFunction bif;


    public BuiltInFunctionWrapper(BuiltInFunction bif, Interpreter interpreter) {

        super(Collections.emptyList(), null, interpreter);
        this.bif = bif;
    }

    @Override
    public Object call(List<Object> args) {
        return bif.call(args);
    }
}