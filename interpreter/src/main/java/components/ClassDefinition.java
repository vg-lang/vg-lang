package components;

import java.util.*;

public class ClassDefinition {
    private String name;
    private String superClassName;
    private ClassDefinition superClass;
    private Map<String, Object> fieldDefaults = new HashMap<>();
    private Map<String, FieldInfo> fieldInfo = new HashMap<>();
    private List<VGConstructor> constructors = new ArrayList<>();
    private Map<String, VGMethod> methods = new HashMap<>();
    private Map<String, VGMethod> staticMethods = new HashMap<>();
    
    public ClassDefinition(String name) {
        this.name = name;
    }
    
    public ClassDefinition(String name, String superClassName) {
        this.name = name;
        this.superClassName = superClassName;
    }
    
    public String getName() {
        return name;
    }
    
    public String getSuperClassName() {
        return superClassName;
    }
    
    public void setSuperClass(ClassDefinition superClass) {
        this.superClass = superClass;
    }
    
    public ClassDefinition getSuperClass() {
        return superClass;
    }
    
    public void addField(String fieldName, Object defaultValue, boolean isPrivate, boolean isStatic, boolean isConst) {
        fieldDefaults.put(fieldName, defaultValue);
        fieldInfo.put(fieldName, new FieldInfo(isPrivate, isStatic, isConst));
    }
    
    public void addConstructor(VGConstructor constructor) {
        constructors.add(constructor);
    }
    
    public void addMethod(String methodName, VGMethod method) {
        if (method.isStatic()) {
            staticMethods.put(methodName, method);
        } else {
            methods.put(methodName, method);
        }
    }
    
    public List<VGConstructor> getConstructors() {
        return constructors;
    }
    
    public VGConstructor findConstructor(int paramCount) {
        for (VGConstructor constructor : constructors) {
            if (constructor.getParameterCount() == paramCount) {
                return constructor;
            }
        }
        return null;
    }
    
    public VGMethod getMethod(String methodName) {
        VGMethod method = methods.get(methodName);
        if (method == null && superClass != null) {
            method = superClass.getMethod(methodName);
        }
        return method;
    }
    
    public VGMethod getStaticMethod(String methodName) {
        VGMethod method = staticMethods.get(methodName);
        if (method == null && superClass != null) {
            method = superClass.getStaticMethod(methodName);
        }
        return method;
    }
    
    public boolean hasMethod(String methodName) {
        return methods.containsKey(methodName) || (superClass != null && superClass.hasMethod(methodName));
    }
    
    public boolean hasStaticMethod(String methodName) {
        return staticMethods.containsKey(methodName) || (superClass != null && superClass.hasStaticMethod(methodName));
    }
    
    public Map<String, Object> getFieldDefaults() {
        Map<String, Object> allDefaults = new HashMap<>();
        if (superClass != null) {
            allDefaults.putAll(superClass.getFieldDefaults());
        }
        allDefaults.putAll(fieldDefaults);
        return allDefaults;
    }
    
    public Map<String, FieldInfo> getFieldInfo() {
        Map<String, FieldInfo> allInfo = new HashMap<>();
        if (superClass != null) {
            allInfo.putAll(superClass.getFieldInfo());
        }
        allInfo.putAll(fieldInfo);
        return allInfo;
    }
    
    public boolean hasField(String fieldName) {
        return fieldDefaults.containsKey(fieldName) || (superClass != null && superClass.hasField(fieldName));
    }
    
    public FieldInfo getFieldInfo(String fieldName) {
        FieldInfo info = fieldInfo.get(fieldName);
        if (info == null && superClass != null) {
            info = superClass.getFieldInfo(fieldName);
        }
        return info;
    }
    
    public ClassInstance createInstance() {
        return new ClassInstance(this);
    }
    
    public static class FieldInfo {
        private final boolean isPrivate;
        private final boolean isStatic;
        private final boolean isConst;
        
        public FieldInfo(boolean isPrivate, boolean isStatic, boolean isConst) {
            this.isPrivate = isPrivate;
            this.isStatic = isStatic;
            this.isConst = isConst;
        }
        
        public boolean isPrivate() {
            return isPrivate;
        }
        
        public boolean isStatic() {
            return isStatic;
        }
        
        public boolean isConst() {
            return isConst;
        }
    }
} 