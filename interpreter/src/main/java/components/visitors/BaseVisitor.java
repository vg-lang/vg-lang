package components.visitors;

import components.*;
import org.antlr.v4.runtime.Token;
import java.util.*;

public abstract class BaseVisitor extends vg_langBaseVisitor<Object> {
    protected Deque<SymbolTable> symbolTableStack;
    protected Map<String, BuiltInFunction> builtInFunction;
    protected SymbolTable globalSymbolTable;
    protected ModuleRegistry moduleRegistry;
    protected int currentLine;
    protected int currentColumn;
    protected Interpreter interpreter;

    public BaseVisitor(Interpreter interpreter) {
        this.interpreter = interpreter;
        this.symbolTableStack = interpreter.getSymbolTableStack();
        this.builtInFunction = interpreter.builtInFunction;
        this.globalSymbolTable = interpreter.getGlobalSymbolTable();
        this.moduleRegistry = interpreter.getModuleRegistry();
    }

    protected SymbolTable currentSymbolTable() {
        return symbolTableStack.peek();
    }

    protected boolean toBoolean(Object value) {
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

    protected void updatePosition(Token token) {
        if (token != null) {
            currentLine = token.getLine();
            currentColumn = token.getCharPositionInLine();
        }
    }

    protected String getVGTypeName(Object value) {
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
} 