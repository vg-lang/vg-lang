package components;

import java.util.List;

public class MethodReference {
    private ClassInstance instance;
    private String methodName;
    
    public MethodReference(ClassInstance instance, String methodName) {
        this.instance = instance;
        this.methodName = methodName;
    }
    
    public ClassInstance getInstance() {
        return instance;
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public Object call(List<Object> args, Interpreter interpreter) {
        return interpreter.getClassVisitor().callMethod(instance, methodName, args);
    }
    
    @Override
    public String toString() {
        return "MethodReference[" + instance.getClassName() + "." + methodName + "]";
    }
} 