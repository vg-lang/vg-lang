package components;

import java.util.List;

public class VariableReference {
     private SymbolTable table;
    private String name;
    private List<Integer> indices;
    private String fieldName;

    private Struct struct;
    private ClassInstance classInstance;
    
    public VariableReference(SymbolTable table, String name, List<Integer> indices) {

        this.table = table;

        this.name = name;

        this.indices = indices;
    }
    public VariableReference(Struct struct, String fieldName) {

        this.struct = struct;

        this.fieldName = fieldName;

    }
    
    public VariableReference(ClassInstance classInstance, String fieldName) {
        this.classInstance = classInstance;
        this.fieldName = fieldName;
    }



    public void setValue(Object value) {
        if (struct != null) {

            struct.setField(fieldName, value);

            return;

        }
        
        if (classInstance != null) {
            classInstance.setField(fieldName, value);
            return;
        }

        Object currentValue = table.get(name);

        if (!indices.isEmpty() && currentValue instanceof List) {

            List<Object> list = (List<Object>) currentValue;
            for (int i = 0; i < indices.size() - 1; i++) {

                int index = indices.get(i);

                if (index < 0 || index >= list.size() || !(list.get(index) instanceof List)) {

                    throw new RuntimeException("Invalid array access");

                }

                list = (List<Object>) list.get(index);

            }





            int finalIndex = indices.get(indices.size() - 1);

            if (finalIndex < 0 || finalIndex >= list.size()) {

                throw new RuntimeException("Array index out of bounds");

            }

            list.set(finalIndex, value);

        } else {



            table.set(name, value);

        }

    }


    public Object getValue() {
        if (struct != null) {

            return struct.getField(fieldName);
        }
        
        if (classInstance != null) {
            return classInstance.getField(fieldName);
        }
        Object value = table.get(name);

        if (!indices.isEmpty() && value instanceof List) {

            List<Object> list = (List<Object>) value;
            for (int index : indices) {

                if (index < 0 || index >= list.size()) {

                    throw new RuntimeException("Array index out of bounds");

                }

                value = list.get(index);

                if (value instanceof List && index < indices.size() - 1) {

                    list = (List<Object>) value;

                }

            }

        }
        return value;
    }
    public String getName() {
        return name;
    }
    public boolean isConstant() {
        if (struct != null) {

            return false;

        }
        return table != null && table.isConstant(name);

    }
}