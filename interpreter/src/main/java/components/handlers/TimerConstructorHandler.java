package components.handlers;

import components.ErrorHandler;
import components.FunctionReference;
import components.HandlerContext;
import components.Interpreter;
import components.LanguageObjectWrapper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles: VgSystemCall("javax.swing.Timer", "<init>", delayMs, callback)
 *
 * Implements HandlerContext so SystemCallHandler can inject line/column
 * for meaningful error messages — no constructor args needed.
 */
public class TimerConstructorHandler extends BaseHandler implements HandlerContext {

    private int currentLine;
    private int currentColumn;

    @Override
    public void setContext(int line, int column) {
        this.currentLine   = line;
        this.currentColumn = column;
    }

    @Override
    public boolean matches(String className, String methodName, List<Object> args) {
        return is(className, methodName,
                "javax.swing.Timer", "<init>", args, 2);
    }

    @Override
    public Object handle(String className, String methodName, List<Object> args) throws Exception {
        Object delayObj = args.get(0);
        if (!(delayObj instanceof Number)) {
            throw new ErrorHandler.VGException(
                "First argument to Timer constructor must be a number", currentLine, currentColumn);
        }
        int delay = ((Number) delayObj).intValue();

        FunctionReference funcRef = extractFunction(args.get(1));

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                funcRef.getFunction().call(new ArrayList<>(funcRef.getCapturedArgs()));
            }
        };

        javax.swing.Timer timer = new javax.swing.Timer(delay, listener);
        Interpreter.registerTimer(timer);
        return new LanguageObjectWrapper(timer);
    }
}
