package components.visitors;

import components.*;
import java.util.*;

public class VariableVisitor extends BaseVisitor {
    public VariableVisitor(Interpreter interpreter) {
        super(interpreter);
    }

    public Object visitAssignment(vg_langParser.AssignmentContext ctx) {
        VariableReference varRef = (VariableReference) interpreter.visit(ctx.leftHandSide());
        if (varRef.isConstant()) {
            throw new RuntimeException("Cannot reassign to a constant variable '" + varRef.getName() + "'.");
        }
        Object value = interpreter.visit(ctx.expression());
        varRef.setValue(value);
        return null;
    }

    public Object visitAssignmentNoSemi(vg_langParser.AssignmentNoSemiContext ctx) {
        VariableReference varRef = (VariableReference) interpreter.visit(ctx.leftHandSide());
        if (varRef.isConstant()) {
            throw new RuntimeException("Cannot reassign to a constant variable '" + varRef.getName() + "'.");
        }
        Object value = interpreter.visit(ctx.expression());
        varRef.setValue(value);
        return value;
    }

    public Object visitLeftHandSide(vg_langParser.LeftHandSideContext ctx) {
        // Check for 'this.fieldName' pattern
        if (ctx.getText().startsWith("this.")) {
            String fieldName = ctx.IDENTIFIER(0).getText();
            
            // Get the current 'this' object from the symbol table
            Object thisObj = null;
            for (SymbolTable table : symbolTableStack) {
                if (table.contains("this")) {
                    thisObj = table.get("this");
                    break;
                }
            }
            
            if (thisObj == null) {
                throw new RuntimeException("'this' is not available in this context.");
            }
            
            if (thisObj instanceof ClassInstance) {
                ClassInstance instance = (ClassInstance) thisObj;
                return new VariableReference(instance, fieldName);
            } else {
                throw new RuntimeException("Cannot access field '" + fieldName + "' on 'this' - not a class instance.");
            }
        }
        else if (ctx.IDENTIFIER().size() == 1) {
            String varName = ctx.IDENTIFIER(0).getText();

            List<Integer> indices = new ArrayList<>();
            if (ctx.expression() != null && !ctx.expression().isEmpty()) {
                for (vg_langParser.ExpressionContext exprCtx : ctx.expression()) {
                    Object indexObj = interpreter.visit(exprCtx);
                    if (!(indexObj instanceof Number)) {
                        throw new RuntimeException("Array index must be a number.");
                    }
                    indices.add(((Number) indexObj).intValue());
                }
            }

            SymbolTable targetTable = null;
            for (SymbolTable table : symbolTableStack) {
                if (table.contains(varName)) {
                    targetTable = table;
                    
                    // Check if the symbol is ambiguous
                    if (table.isAmbiguous(varName)) {
                        throw new RuntimeException("Ambiguous symbol '" + varName + "'. " +
                            "This symbol exists in multiple imported namespaces. " +
                            "Use 'namespace." + varName + "' to specify which one you want.");
                    }
                    
                    break;
                }
            }

            if (targetTable == null) {
                throw new RuntimeException("Variable '" + varName + "' is not defined.");
            }

            return new VariableReference(targetTable, varName, indices);
        } else if (ctx.IDENTIFIER().size() == 2) {
            String objName = ctx.IDENTIFIER(0).getText();
            String fieldName = ctx.IDENTIFIER(1).getText();

            Object obj = getVariable(objName);
            
            if (obj instanceof StructDefinition) {
                StructDefinition structDef = (StructDefinition) obj;
                Struct struct = structDef.createInstance();
                
                for (SymbolTable table : symbolTableStack) {
                    if (table.contains(objName)) {
                        table.set(objName, struct);
                        break;
                    }
                }
                
                return new VariableReference(struct, fieldName);
            } else if (obj instanceof Struct) {
                Struct struct = (Struct) obj;
                return new VariableReference(struct, fieldName);
            } else if (obj instanceof ClassInstance) {
                ClassInstance instance = (ClassInstance) obj;
                return new VariableReference(instance, fieldName);
            } else {
                throw new RuntimeException("Cannot access field '" + fieldName + "' on non-struct/class object '" + objName + "'");
            }
        } else {
            throw new RuntimeException("Invalid assignment target.");
        }
    }

    private Object getVariable(String name) {
        SymbolTable foundTable = null;
        for (SymbolTable table : symbolTableStack) {
            if (table.contains(name)) {
                foundTable = table;
                
                // Check if the symbol is ambiguous
                if (table.isAmbiguous(name)) {
                    throw new RuntimeException("Ambiguous symbol '" + name + "'. " +
                        "This symbol exists in multiple imported namespaces. " +
                        "Use 'namespace." + name + "' to specify which one you want.");
                }
                
                return table.get(name);
            }
        }
        throw new RuntimeException("Variable '" + name + "' is not defined.");
    }
} 