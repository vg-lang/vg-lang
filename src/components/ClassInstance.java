package components;

import java.util.HashMap;
import java.util.Map;

public class ClassInstance {
    private ClassDefinition classDefinition;
    private Map<String, Object> fields = new HashMap<>();
    private boolean isInitialized = false;
    
    public ClassInstance(ClassDefinition classDefinition) {
        this.classDefinition = classDefinition;
        initializeFields();
    }
    
    private void initializeFields() {
        Map<String, Object> defaults = classDefinition.getFieldDefaults();
        for (Map.Entry<String, Object> entry : defaults.entrySet()) {
            fields.put(entry.getKey(), entry.getValue());
        }
    }
    
    public ClassDefinition getClassDefinition() {
        return classDefinition;
    }
    
    public String getClassName() {
        return classDefinition.getName();
    }
    
    public void setField(String fieldName, Object value) {
        ClassDefinition.FieldInfo fieldInfo = classDefinition.getFieldInfo(fieldName);
        if (fieldInfo == null) {
            throw new RuntimeException("Field '" + fieldName + "' does not exist in class '" + getClassName() + "'");
        }
        
        if (fieldInfo.isConst() && fields.containsKey(fieldName)) {
            throw new RuntimeException("Cannot modify const field '" + fieldName + "' in class '" + getClassName() + "'");
        }
        
        fields.put(fieldName, value);
    }
    
    public Object getField(String fieldName) {
        if (!fields.containsKey(fieldName)) {
            throw new RuntimeException("Field '" + fieldName + "' does not exist in class '" + getClassName() + "'");
        }
        return fields.get(fieldName);
    }
    
    public boolean hasField(String fieldName) {
        return fields.containsKey(fieldName);
    }
    
    public boolean isFieldPrivate(String fieldName) {
        ClassDefinition.FieldInfo fieldInfo = classDefinition.getFieldInfo(fieldName);
        return fieldInfo != null && fieldInfo.isPrivate();
    }
    
    public boolean isFieldStatic(String fieldName) {
        ClassDefinition.FieldInfo fieldInfo = classDefinition.getFieldInfo(fieldName);
        return fieldInfo != null && fieldInfo.isStatic();
    }
    
    public boolean isFieldConst(String fieldName) {
        ClassDefinition.FieldInfo fieldInfo = classDefinition.getFieldInfo(fieldName);
        return fieldInfo != null && fieldInfo.isConst();
    }
    
    public VGMethod getMethod(String methodName) {
        return classDefinition.getMethod(methodName);
    }
    
    public boolean hasMethod(String methodName) {
        return classDefinition.hasMethod(methodName);
    }
    
    public Map<String, Object> getFields() {
        return new HashMap<>(fields);
    }
    
    public void setInitialized(boolean initialized) {
        this.isInitialized = initialized;
    }
    
    public boolean isInitialized() {
        return isInitialized;
    }
    
    public boolean isInstanceOf(String className) {
        ClassDefinition current = classDefinition;
        while (current != null) {
            if (current.getName().equals(className)) {
                return true;
            }
            current = current.getSuperClass();
        }
        return false;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClassName()).append(" {");
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