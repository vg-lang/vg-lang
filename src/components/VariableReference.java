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
        else if(isConstant()) {
            throw new RuntimeException("Cannot assign to a constant variable: " + name);
        }
    }
    public Object getValue() {
        Object value = symbolTable.get(name);
        return value;

    }
    public String getName() {
        return name;
    }
    public boolean isConstant() {

        return symbolTable.isConstant(name);
    }
}