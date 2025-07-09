package components;

import java.util.HashMap;
import java.util.Map;

public class Enum {
    private String name;
    private Map<String, Object> values = new HashMap<>();

    public Enum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addValue(String valueName, Object value) {
        values.put(valueName, value);
    }

    public Object getValue(String valueName) {
        if (!values.containsKey(valueName)) {
            throw new RuntimeException("Value '" + valueName + "' does not exist in enum '" + name + "'");
        }
        return values.get(valueName);
    }

    public boolean hasValue(String valueName) {
        return values.containsKey(valueName);
    }

    public Map<String, Object> getValues() {
        return new HashMap<>(values);
    }

    @Override
    public String toString() {
        return name;
    }
} 