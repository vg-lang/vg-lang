package components;

import java.util.HashMap;
import java.util.Map;

public class Struct {
    private String name;
    private Map<String, Object> fields = new HashMap<>();

    public Struct(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setField(String fieldName, Object value) {
        fields.put(fieldName, value);
    }

    public Object getField(String fieldName) {
        if (!fields.containsKey(fieldName)) {
            throw new RuntimeException("Field '" + fieldName + "' does not exist in struct '" + name + "'");
        }
        return fields.get(fieldName);
    }

    public boolean hasField(String fieldName) {
        return fields.containsKey(fieldName);
    }

    public Map<String, Object> getFields() {
        return new HashMap<>(fields);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" {");
        boolean first = true;
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(entry.getKey()).append(": ");
            Object value = entry.getValue();
            sb.append(value == null ? "null" : value.toString());
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
} 