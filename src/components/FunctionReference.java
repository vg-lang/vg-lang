package components;

import java.util.ArrayList;
import java.util.List;

public class FunctionReference {
    private final Function function;
    private final List<Object> capturedArgs;

    public FunctionReference(Function function, List<Object> capturedArgs) {
        this.function = function;
        this.capturedArgs = capturedArgs;
    }

    public Object call(List<Object> args) {
        List<Object> finalArgs = new ArrayList<>(capturedArgs);
        finalArgs.addAll(args);
        return function.call(finalArgs);
    }
    public Function getFunction() {
        return function;
    }

    public List<Object> getCapturedArgs() {
        return capturedArgs;
    }
}
