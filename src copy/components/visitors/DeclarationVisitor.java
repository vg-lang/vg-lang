package components.visitors;

import components.*;
import java.util.*;

public class DeclarationVisitor extends BaseVisitor {
    public DeclarationVisitor(Interpreter interpreter) {
        super(interpreter);
    }

    public Object visitVariableDeclaration(vg_langParser.VariableDeclarationContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        Object value = interpreter.visit(ctx.expression());
        currentSymbolTable().set(varName, value);
        return null;
    }

    public Object visitVariableDeclarationNoSemi(vg_langParser.VariableDeclarationNoSemiContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        Object value = interpreter.visit(ctx.expression());
        currentSymbolTable().set(varName, value);
        return null;
    }

    public Object visitConstDeclaration(vg_langParser.ConstDeclarationContext ctx) {
        String constName = ctx.IDENTIFIER().getText();
        Object value = interpreter.visit(ctx.expression());
        currentSymbolTable().setConstant(constName, value);
        return null;
    }

    public Object visitStructDeclaration(vg_langParser.StructDeclarationContext ctx) {
        try {
            String structName = ctx.IDENTIFIER().getText();

            Map<String, Object> fieldDefaults = new HashMap<>();
            for (vg_langParser.StructFieldContext fieldCtx : ctx.structField()) {
                String fieldName = fieldCtx.IDENTIFIER().getText();
                fieldDefaults.put(fieldName, null);
            }

            StructDefinition structDef = new StructDefinition(structName, fieldDefaults);
            currentSymbolTable().set(structName, structDef);

            return null;
        } catch (Exception e) {
            throw ErrorHandler.handleException(e, ctx);
        }
    }

    public Object visitEnumDeclaration(vg_langParser.EnumDeclarationContext ctx) {
        try {
            String enumName = ctx.IDENTIFIER().getText();
            components.Enum enumObj = new components.Enum(enumName);

            int autoValue = 0;
            for (vg_langParser.EnumValueContext valueCtx : ctx.enumValue()) {
                String valueName = valueCtx.IDENTIFIER().getText();
                Object value;

                if (valueCtx.expression() != null) {
                    value = interpreter.visit(valueCtx.expression());
                    if (value instanceof Number) {
                        autoValue = ((Number) value).intValue() + 1;
                    }
                } else {
                    value = autoValue++;
                }

                enumObj.addValue(valueName, value);
            }

            currentSymbolTable().set(enumName, enumObj);
            return null;
        } catch (Exception e) {
            throw ErrorHandler.handleException(e, ctx);
        }
    }
} 