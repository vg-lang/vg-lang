package components;

import java.util.Map;

public class StructDefinition {
    private String name;
    private Map<String, Object> fieldDefaults;

    public StructDefinition(String name, Map<String, Object> fieldDefaults) {
        this.name = name;
        this.fieldDefaults = fieldDefaults;
    }

    public Struct createInstance() {
        Struct instance = new Struct(name);
        for (Map.Entry<String, Object> entry : fieldDefaults.entrySet()) {
            instance.setField(entry.getKey(), entry.getValue());
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public boolean hasField(String fieldName) {
        return fieldDefaults.containsKey(fieldName);
    }
}