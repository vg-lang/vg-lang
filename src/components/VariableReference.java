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
        else {
            // Array element assignment
            Object array = symbolTable.get(name);
            for (int i = 0; i < indices.size() - 1; i++) {
                int idx = indices.get(i);
                if (!(array instanceof List)) {
                    throw new RuntimeException("Cannot index into non-array value.");
                }
                List<?> list = (List<?>) array;
                if (idx < 0 || idx >= list.size()) {
                    throw new RuntimeException("Array index out of bounds.");
                }
                array = list.get(idx);
            }
            int lastIndex = indices.get(indices.size() - 1);
            if (!(array instanceof List)) {
                throw new RuntimeException("Cannot index into non-array value.");
            }
            List<Object> list = (List<Object>) array;
            if (lastIndex < 0 || lastIndex >= list.size()) {
                throw new RuntimeException("Array index out of bounds.");
            }
            list.set(lastIndex, value);
        }
    }
    public Object getValue() {
        Object value = symbolTable.get(name);
        for (int idx : indices) {
            if (!(value instanceof List)) {
                throw new RuntimeException("Cannot index into non-array value.");
            }
            List<?> list = (List<?>) value;
            if (idx < 0 || idx >= list.size()) {
                throw new RuntimeException("Array index out of bounds.");
            }
            value = list.get(idx);
        }
        return value;

    }
    public String getName() {
        return name;
    }
    public boolean isConstant() {

        return symbolTable.isConstant(name);
    }
}