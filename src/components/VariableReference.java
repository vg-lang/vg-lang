package components;

import java.util.List;

public class VariableReference {
    private SymbolTable symbolTable;
    private String name;
    private List<Integer> indices;

    public VariableReference(SymbolTable symbolTable, String name, List<Integer> indices) {
        this.symbolTable = symbolTable;
        this.name = name;
        this.indices = indices;
    }

    public void setValue(Object value) {
        if (indices.isEmpty()) {
            symbolTable.set(name, value);
        }
    }
    public Object getValue() {
        Object value = symbolTable.get(name);
        return value;

    }
    public String getName() {
        return name;
    }
}