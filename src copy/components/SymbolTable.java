package components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SymbolTable {
    private Map<String, Object> variables = new HashMap<>();
    private Set<String> constants = new HashSet<>();
    private Map<String, Function> functions = new HashMap<>();

    public void set(String name, Object value) {
        variables.put(name, value);
    }

    public Object get(String name) {
        return variables.get(name);
    }

    public boolean contains(String name) {
        return variables.containsKey(name);
    }
    public boolean isConstant(String name) {
        return constants.contains(name);
    }
    public void setConstant(String name, Object value) {
        variables.put(name, value);
        constants.add(name);
    }

    public Map<String, Object> getVariables() {
        return variables;
    }
    public void setFunction(String name, Function function) {
        functions.put(name, function);
    }


    public Function getFunction(String name) {
        return functions.get(name);
    }


    public boolean containsFunction(String name) {
        return functions.containsKey(name);
    }


    public Map<String, Function> getFunctions() {
        return functions;
    }

}
