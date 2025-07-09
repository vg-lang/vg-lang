package components.visitors;

import components.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import java.util.*;

public class FunctionVisitor extends BaseVisitor {
    public FunctionVisitor(Interpreter interpreter) {
        super(interpreter);
    }

    public Object visitFunctionDeclaration(vg_langParser.FunctionDeclarationContext ctx) {
        String functionName = ctx.IDENTIFIER().getText();
        List<String> parameters = getParameters(ctx.parameterList());
        Function function = new Function(parameters, ctx.block(), interpreter);

        SymbolTable currentTable = symbolTableStack.getLast();
        currentTable.setFunction(functionName, function);
        currentTable.set(functionName, function);
        return null;
    }

    public Object visitFunctionCall(vg_langParser.FunctionCallContext ctx) {
        String functionName;

        if (ctx.IDENTIFIER() != null) {
            functionName = ctx.IDENTIFIER().getText();
        } else {
            throw new ErrorHandler.VGSyntaxException("Invalid function call syntax", 
                                                   ctx.getStart().getLine(), 
                                                   ctx.getStart().getCharPositionInLine());
        }

        List<vg_langParser.ExpressionContext> argExprs = ctx.argumentList() != null
                ? ctx.argumentList().expression()
                : Collections.emptyList();

        List<Object> argValues = new ArrayList<>();
        for (vg_langParser.ExpressionContext exprCtx : argExprs) {
            argValues.add(interpreter.visit(exprCtx));
        }

        if (interpreter.builtInFunction.containsKey(functionName)) {
            BuiltInFunction builtInFunc = interpreter.builtInFunction.get(functionName);
            return builtInFunc.call(argValues);
        }

        Object funcObj = null;

        for (SymbolTable table : symbolTableStack) {
            if (table.containsFunction(functionName)) {
                funcObj = table.getFunction(functionName);
                break;
            }
            if (table.contains(functionName)) {
                funcObj = table.get(functionName);
                break;
            }
        }

        if (funcObj instanceof FunctionReference) {
            FunctionReference funcRef = (FunctionReference) funcObj;
            return funcRef.call(argValues);
        }

        if (!(funcObj instanceof Function)) {
            int line = ctx.getStart().getLine();
            throw new RuntimeException("Function '" + functionName + "' is not defined at line: " + line);
        }

        Function function = (Function) funcObj;
        List<String> parameters = function.getParameters();

        if (argValues.size() != parameters.size()) {
            throw ErrorHandler.createArgumentCountError(
                functionName, parameters.size(), argValues.size(), ctx.getStart());
        }

        SymbolTable functionSymbolTable = new SymbolTable();
        symbolTableStack.push(functionSymbolTable);

        for (int i = 0; i < parameters.size(); i++) {
            functionSymbolTable.set(parameters.get(i), argValues.get(i));
        }

        Object returnValue = null;
        try {
            interpreter.visit(function.getBlock());
        } catch (ReturnException e) {
            returnValue = e.getValue();
        } finally {
            symbolTableStack.pop();
        }

        return returnValue;
    }

    public Object visitFunctionReference(vg_langParser.FunctionReferenceContext ctx) {
        String functionPath = ctx.qualifiedIdentifier().getText();
        Function function = interpreter.resolveFunctionFromNamespace(functionPath);

        if (function == null) {
            throw new RuntimeException("Function '" + functionPath + "' is not defined.");
        }

        List<Object> capturedArgs = new ArrayList<>();
        if (ctx.argumentList() != null) {
            for (vg_langParser.ExpressionContext exprCtx : ctx.argumentList().expression()) {
                capturedArgs.add(interpreter.visit(exprCtx));
            }
        }

        return new FunctionReference(function, capturedArgs);
    }

    private List<String> getParameters(vg_langParser.ParameterListContext paramCtx) {
        List<String> params = new ArrayList<>();
        if (paramCtx != null) {
            for (TerminalNode id : paramCtx.IDENTIFIER()) {
                params.add(id.getText());
            }
        }
        return params;
    }
} 