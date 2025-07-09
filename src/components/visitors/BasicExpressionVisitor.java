package components.visitors;

import components.*;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BasicExpressionVisitor extends vg_langBaseVisitor<Object> {
    private final Interpreter interpreter;

    public BasicExpressionVisitor(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public Object visitUnaryExpression(vg_langParser.UnaryExpressionContext ctx) {
        if (ctx.unaryExpression() != null) {
            Object value = interpreter.visit(ctx.unaryExpression());
            String operator = ctx.getChild(0).getText();
            switch (operator) {
                case "+":
                    if (!(value instanceof Number)) {
                        throw new RuntimeException("Unary '+' operator requires a numeric operand.");
                    }
                    return value;
                case "-":
                    if (!(value instanceof Number)) {
                        throw new RuntimeException("Unary '-' operator requires a numeric operand.");
                    }
                    if (value instanceof Integer) {
                        return -((Integer) value);
                    } else if (value instanceof Double) {
                        return -((Double) value);
                    }
                    break;
                case "!":
                    return !toBoolean(value);
                default:
                    throw new RuntimeException("Unknown unary operator '" + operator + "'.");
            }
        } else {
            return interpreter.visit(ctx.postfixExpression());
        }
        return null;
    }

    @Override
    public Object visitPrimary(vg_langParser.PrimaryContext ctx) {
        if (ctx.literal() != null) {
            return interpreter.visit(ctx.literal());
        } else if (ctx.IDENTIFIER() != null) {
            String varName = ctx.IDENTIFIER().getText();
            
            if (varName.equals("true")) {
                return true;
            } else if (varName.equals("false")) {
                return false;
            }
            
            Object value = null;
            for (SymbolTable table : interpreter.getSymbolTableStack()) {
                if (table.contains(varName)) {
                    value = table.get(varName);
                    break;
                }
            }
            
            if (value == null) {
                throw new RuntimeException("Variable '" + varName + "' is not defined.");
            }
            return value;
        } else if (ctx.expression() != null) {
            return interpreter.visit(ctx.expression());
        } else if (ctx.functionCall() != null) {
            return interpreter.visit(ctx.functionCall());
        }
        return null;
    }

    @Override
    public Object visitPostfixExpression(vg_langParser.PostfixExpressionContext ctx) {
        Object value = interpreter.visit(ctx.primary());

        for (vg_langParser.PostfixOpContext opCtx : ctx.postfixOp()) {
            String opText = opCtx.getChild(0).getText();
            if (".".equals(opText)) {
                String memberName = opCtx.IDENTIFIER().getText();
                
                if (value instanceof Integer || value instanceof Double || 
                    value instanceof Boolean || value instanceof String) {
                    
                    String typeName = getVGTypeName(value);
                    
                    updatePosition(opCtx.start);
                    throw new ErrorHandler.VGTypeException(
                        "Dot operator not supported on primitive type: " + typeName,
                        getCurrentLine(), getCurrentColumn()
                    );
                }
                
                if (value instanceof Namespace) {
                    Namespace ns = (Namespace) value;
                    Object member = ns.getSymbol(memberName);
                    if (member == null) {
                        updatePosition(opCtx.start);
                        throw new ErrorHandler.VGNameException(
                                "Member '" + memberName + "' not found in namespace.",
                                getCurrentLine(), getCurrentColumn()
                        );
                    }
                    value = member;
                } else if (value instanceof StructDefinition) {
                    if (memberName.equals("createInstance")) {
                        value = ((StructDefinition) value).createInstance();
                    } else {
                        StructDefinition structDef = (StructDefinition) value;
                        Struct struct = structDef.createInstance();
                        if (!struct.hasField(memberName)) {
                            throw new RuntimeException("Field '" + memberName + "' not found in struct '" + struct.getName() + "'");
                        }
                        value = struct.getField(memberName);
                    }
                } else if (value instanceof Struct) {
                    Struct struct = (Struct) value;
                    if (!struct.hasField(memberName)) {
                        throw new RuntimeException("Field '" + memberName + "' not found in struct '" + struct.getName() + "'");
                    }
                    value = struct.getField(memberName);
                } else if (value instanceof components.Enum) {
                    components.Enum enumObj = (components.Enum) value;
                    if (!enumObj.hasValue(memberName)) {
                        throw new RuntimeException("Value '" + memberName + "' not found in enum '" + enumObj.getName() + "'");
                    }
                    value = enumObj.getValue(memberName);
                } else {
                    updatePosition(opCtx.start);
                    throw new ErrorHandler.VGTypeException(
                            "Dot operator not supported on type: " + getVGTypeName(value),
                            getCurrentLine(), getCurrentColumn()
                    );
                }
            } else if ("(".equals(opText)) {
                List<Object> argValues = new ArrayList<>();
                vg_langParser.ArgumentListContext argsCtx = opCtx.argumentList();
                if (argsCtx != null) {
                    for (vg_langParser.ExpressionContext exprCtx : argsCtx.expression()) {
                        argValues.add(interpreter.visit(exprCtx));
                    }
                }

                if (value instanceof Function) {
                    try {
                        value = ((Function) value).call(argValues);
                    } catch (IndexOutOfBoundsException e) {
                        updatePosition(opCtx.start);
                        throw new ErrorHandler.VGTypeException(
                                "Incorrect number of arguments for function call. Check the function signature.",
                                getCurrentLine(), getCurrentColumn()
                        );
                    } catch (Exception e) {
                        updatePosition(opCtx.start);
                        throw new ErrorHandler.VGException(
                                "Error in function call: " + e.getMessage(),
                                getCurrentLine(), getCurrentColumn()
                        );
                    }
                } else {
                    updatePosition(opCtx.start);
                    throw new ErrorHandler.VGTypeException(
                            "Cannot call a non-function value: " + getVGTypeName(value),
                            getCurrentLine(), getCurrentColumn()
                    );
                }
            } else if ("[".equals(opText)) {
                Object indexObj = interpreter.visit(opCtx.expression());
                if (!(indexObj instanceof Number)) {
                    updatePosition(opCtx.start);
                    throw new ErrorHandler.VGTypeException(
                            "Array index must be a number, got: " + getVGTypeName(indexObj),
                            getCurrentLine(), getCurrentColumn()
                    );
                }
                int index = ((Number) indexObj).intValue();
                if (!(value instanceof List)) {
                    updatePosition(opCtx.start);
                    throw new ErrorHandler.VGTypeException(
                            "Cannot use [] operator on non-array value: " + getVGTypeName(value),
                            getCurrentLine(), getCurrentColumn()
                    );
                }
                List<?> list = (List<?>) value;
                if (index < 0 || index >= list.size()) {
                    updatePosition(opCtx.start);
                    throw new ErrorHandler.VGException(
                            "Array index out of bounds: index " + index + " exceeds array length " + list.size(),
                            getCurrentLine(), getCurrentColumn()
                    );
                }
                value = list.get(index);
            }
        }
        return value;
    }

    private boolean toBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue() != 0;
        }
        if (value instanceof String) {
            return !((String) value).isEmpty();
        }
        return value != null;
    }

    private String getVGTypeName(Object value) {
        if (value == null) return "null";
        if (value instanceof Integer) return "int";
        if (value instanceof Double) return "double";
        if (value instanceof Boolean) return "boolean";
        if (value instanceof String) return "string";
        if (value instanceof List) return "array";
        if (value instanceof Map) return "struct";
        if (value instanceof Function) return "function";
        if (value instanceof Namespace) return "namespace";
        if (value instanceof Library) return "library";
        if (value instanceof components.Enum) return "enum";
        
        String className = value.getClass().getName();
        if (className.startsWith("java.")) {
            return className.substring(className.lastIndexOf('.') + 1).toLowerCase();
        }
        return value.getClass().getSimpleName().toLowerCase();
    }

    private void updatePosition(Token token) {
        if (token != null) {
            interpreter.setCurrentLine(token.getLine());
            interpreter.setCurrentColumn(token.getCharPositionInLine());
        }
    }

    private int getCurrentLine() {
        return interpreter.getCurrentLine();
    }

    private int getCurrentColumn() {
        return interpreter.getCurrentColumn();
    }
} 