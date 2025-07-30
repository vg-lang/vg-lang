package components;

import java.util.List;

public class StaticMethodReference {
    private ClassDefinition classDefinition;
    private String methodName;
    
    public StaticMethodReference(ClassDefinition classDefinition, String methodName) {
        this.classDefinition = classDefinition;
        this.methodName = methodName;
    }
    
    public ClassDefinition getClassDefinition() {
        return classDefinition;
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public Object call(List<Object> args, Interpreter interpreter) {
        return interpreter.getClassVisitor().callStaticMethod(classDefinition, methodName, args);
    }
    
    @Override
    public String toString() {
        return "StaticMethodReference[" + classDefinition.getName() + "." + methodName + "]";
    }
} 