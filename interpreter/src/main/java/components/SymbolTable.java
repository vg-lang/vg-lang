package components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SymbolTable {
    private Map<String, SymbolEntry> variables = new HashMap<>();
    private Set<String> constants = new HashSet<>();
    private Map<String, Function> functions = new HashMap<>();

    public void set(String name, Object value) {
        variables.put(name, new SymbolEntry(value, null));
    }
    
    public void setWithOrigin(String name, Object value, String originNamespace) {
        SymbolEntry existing = variables.get(name);
        if (existing != null && existing.getOriginNamespace() != null && 
            !existing.getOriginNamespace().equals(originNamespace)) {
            // Mark both symbols as ambiguous
            existing.setAmbiguous(true);
            SymbolEntry newEntry = new SymbolEntry(value, originNamespace);
            newEntry.setAmbiguous(true);
            variables.put(name, newEntry);
        } else {
            variables.put(name, new SymbolEntry(value, originNamespace));
        }
    }

    public Object get(String name) {
        SymbolEntry entry = variables.get(name);
        return entry != null ? entry.getValue() : null;
    }
    
    public SymbolEntry getEntry(String name) {
        return variables.get(name);
    }
    
    public boolean isAmbiguous(String name) {
        SymbolEntry entry = variables.get(name);
        return entry != null && entry.isAmbiguous();
    }

    public boolean contains(String name) {
        return variables.containsKey(name);
    }
    
    public boolean isConstant(String name) {
        return constants.contains(name);
    }
    
    public void setConstant(String name, Object value) {
        variables.put(name, new SymbolEntry(value, null));
        constants.add(name);
    }

    public Map<String, Object> getVariables() {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, SymbolEntry> entry : variables.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getValue());
        }
        return result;
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
