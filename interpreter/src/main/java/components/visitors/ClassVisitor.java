package components.visitors;

import components.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class ClassVisitor extends BaseVisitor {
    
    public ClassVisitor(Interpreter interpreter) {
        super(interpreter);
    }
    
    public Object visitClassDeclaration(vg_langParser.ClassDeclarationContext ctx) {
        String className = ctx.IDENTIFIER(0).getText();
        String superClassName = null;
        
        if (ctx.IDENTIFIER().size() > 1) {
            superClassName = ctx.IDENTIFIER(1).getText();
        }
        
        // Create class definition
        ClassDefinition classDef = new ClassDefinition(className, superClassName);
        
        // If there's a superclass, resolve it
        if (superClassName != null) {
            Object superClassObj = currentSymbolTable().get(superClassName);
            if (superClassObj instanceof ClassDefinition) {
                classDef.setSuperClass((ClassDefinition) superClassObj);
            } else {
                throw new RuntimeException("Superclass '" + superClassName + "' not found or not a class");
            }
        }
        
        // Process class members
        for (vg_langParser.ClassMemberContext memberCtx : ctx.classMember()) {
            if (memberCtx.fieldDeclaration() != null) {
                visitFieldDeclaration(memberCtx.fieldDeclaration(), classDef);
            } else if (memberCtx.methodDeclaration() != null) {
                visitMethodDeclaration(memberCtx.methodDeclaration(), classDef);
            } else if (memberCtx.constructorDeclaration() != null) {
                visitConstructorDeclaration(memberCtx.constructorDeclaration(), classDef);
            }
        }
        
        // Register the class in the symbol table
        currentSymbolTable().set(className, classDef);
        
        return classDef;
    }
    
    private void visitFieldDeclaration(vg_langParser.FieldDeclarationContext ctx, ClassDefinition classDef) {
        String fieldName = ctx.IDENTIFIER().getText();
        boolean isPrivate = ctx.accessModifier() != null && ctx.accessModifier().getText().equals("private");
        
        // Check for method modifiers (now a list)
        boolean isStatic = false;
        boolean isConst = false;
        if (ctx.methodModifier() != null) {
            for (vg_langParser.MethodModifierContext modCtx : ctx.methodModifier()) {
                String modifierText = modCtx.getText();
                if ("static".equals(modifierText)) {
                    isStatic = true;
                } else if ("const".equals(modifierText)) {
                    isConst = true;
                }
            }
        }
        
        Object defaultValue = null;
        if (ctx.expression() != null) {
            defaultValue = interpreter.visit(ctx.expression());
        }
        
        classDef.addField(fieldName, defaultValue, isPrivate, isStatic, isConst);
    }
    
    private void visitMethodDeclaration(vg_langParser.MethodDeclarationContext ctx, ClassDefinition classDef) {
        String methodName = ctx.IDENTIFIER().getText();
        boolean isPrivate = ctx.accessModifier() != null && ctx.accessModifier().getText().equals("private");
        
        // Check for method modifiers (now a list)
        boolean isStatic = false;
        boolean isConst = false;
        if (ctx.methodModifier() != null) {
            for (vg_langParser.MethodModifierContext modCtx : ctx.methodModifier()) {
                String modifierText = modCtx.getText();
                if ("static".equals(modifierText)) {
                    isStatic = true;
                } else if ("const".equals(modifierText)) {
                    isConst = true;
                }
            }
        }
        
        List<String> parameters = new ArrayList<>();
        if (ctx.parameterList() != null) {
            for (TerminalNode paramNode : ctx.parameterList().IDENTIFIER()) {
                parameters.add(paramNode.getText());
            }
        }
        
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        
        VGMethod method = new VGMethod(methodName, parameters, ctx.block(), isPrivate, isStatic, isConst, line, column);
        classDef.addMethod(methodName, method);
    }
    
    private void visitConstructorDeclaration(vg_langParser.ConstructorDeclarationContext ctx, ClassDefinition classDef) {
        boolean isPrivate = ctx.accessModifier() != null && ctx.accessModifier().getText().equals("private");
        
        List<String> parameters = new ArrayList<>();
        if (ctx.parameterList() != null) {
            for (TerminalNode paramNode : ctx.parameterList().IDENTIFIER()) {
                parameters.add(paramNode.getText());
            }
        }
        
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        
        VGConstructor constructor = new VGConstructor(parameters, ctx.block(), isPrivate, line, column);
        classDef.addConstructor(constructor);
    }
    
    public Object visitNewExpression(vg_langParser.NewExpressionContext ctx) {
        String className = ctx.IDENTIFIER().getText();
        
        // Look up the class definition through the entire symbol table stack
        Object classObj = null;
        for (SymbolTable table : interpreter.getSymbolTableStack()) {
            classObj = table.get(className);
            if (classObj instanceof ClassDefinition) {
                break;
            }
        }
        
        if (!(classObj instanceof ClassDefinition)) {
            throw new RuntimeException("Class '" + className + "' not found");
        }
        
        ClassDefinition classDef = (ClassDefinition) classObj;
        
        // Create instance
        ClassInstance instance = classDef.createInstance();
        
        // Get constructor arguments
        List<Object> args = new ArrayList<>();
        if (ctx.argumentList() != null) {
            for (vg_langParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
                args.add(interpreter.visit(argCtx));
            }
        }
        
        // Find and call constructor
        VGConstructor constructor = classDef.findConstructor(args.size());
        if (constructor == null) {
            throw new RuntimeException("No constructor found for class '" + className + "' with " + args.size() + " parameters");
        }
        
        // Check access control
        if (constructor.isPrivate()) {
            throw new RuntimeException("Constructor is private and cannot be accessed");
        }
        
        // Call constructor
        callConstructor(instance, constructor, args);
        
        return instance;
    }
    
    private void callConstructor(ClassInstance instance, VGConstructor constructor, List<Object> args) {
        // Create new scope for constructor
        SymbolTable constructorScope = new SymbolTable();
        interpreter.getSymbolTableStack().push(constructorScope);
        
        try {
            // Set 'this' reference
            constructorScope.set("this", instance);
            
            // Set parameters
            List<String> parameters = constructor.getParameters();
            for (int i = 0; i < parameters.size(); i++) {
                constructorScope.set(parameters.get(i), args.get(i));
            }
            
            // Execute constructor body
            interpreter.visit(constructor.getCodeBlock());
            
            // Mark instance as initialized
            instance.setInitialized(true);
            
        } catch (ReturnException e) {
            // Constructors shouldn't return values, but handle it gracefully
        } finally {
            interpreter.getSymbolTableStack().pop();
        }
    }
    
    public Object callMethod(ClassInstance instance, String methodName, List<Object> args) {
        VGMethod method = instance.getMethod(methodName);
        if (method == null) {
            throw new RuntimeException("Method '" + methodName + "' not found in class '" + instance.getClassName() + "'");
        }
        
        // Check access control
        if (method.isPrivate()) {
            throw new RuntimeException("Method '" + methodName + "' is private and cannot be accessed");
        }
        
        // Check parameter count
        if (method.getParameterCount() != args.size()) {
            throw new RuntimeException("Method '" + methodName + "' expects " + method.getParameterCount() + " parameters, got " + args.size());
        }
        
        return executeMethod(instance, method, args);
    }
    
    public Object callStaticMethod(ClassDefinition classDef, String methodName, List<Object> args) {
        VGMethod method = classDef.getStaticMethod(methodName);
        if (method == null) {
            throw new RuntimeException("Static method '" + methodName + "' not found in class '" + classDef.getName() + "'");
        }
        
        // Check access control
        if (method.isPrivate()) {
            throw new RuntimeException("Static method '" + methodName + "' is private and cannot be accessed");
        }
        
        // Check parameter count
        if (method.getParameterCount() != args.size()) {
            throw new RuntimeException("Static method '" + methodName + "' expects " + method.getParameterCount() + " parameters, got " + args.size());
        }
        
        return executeStaticMethod(classDef, method, args);
    }
    
    private Object executeMethod(ClassInstance instance, VGMethod method, List<Object> args) {
        SymbolTable methodScope = new SymbolTable();
        interpreter.getSymbolTableStack().push(methodScope);
        
        try {
            // Set 'this' reference
            methodScope.set("this", instance);
            
            // Set parameters
            List<String> parameters = method.getParameters();
            for (int i = 0; i < parameters.size(); i++) {
                methodScope.set(parameters.get(i), args.get(i));
            }
            
            // Execute method body
            interpreter.visit(method.getCodeBlock());
            
            return null; // No return value
            
        } catch (ReturnException e) {
            return e.getValue();
        } finally {
            interpreter.getSymbolTableStack().pop();
        }
    }
    
    private Object executeStaticMethod(ClassDefinition classDef, VGMethod method, List<Object> args) {
        SymbolTable methodScope = new SymbolTable();
        interpreter.getSymbolTableStack().push(methodScope);
        
        try {
            // Set parameters (no 'this' for static methods)
            List<String> parameters = method.getParameters();
            for (int i = 0; i < parameters.size(); i++) {
                methodScope.set(parameters.get(i), args.get(i));
            }
            
            // Execute method body
            interpreter.visit(method.getCodeBlock());
            
            return null; // No return value
            
        } catch (ReturnException e) {
            return e.getValue();
        } finally {
            interpreter.getSymbolTableStack().pop();
        }
    }
} 