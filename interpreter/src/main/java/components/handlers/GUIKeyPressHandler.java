package components.handlers;

import components.FunctionReference;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles: VgSystemCall("components.MyGUI", "setOnKeyPress", instance, callback)
 */
public class GUIKeyPressHandler extends BaseHandler {

    @Override
    public boolean matches(String className, String methodName, List<Object> args) {
        return is(className, methodName,
                "components.MyGUI", "setOnKeyPress", args, 2);
    }

    @Override
    public Object handle(String className, String methodName, List<Object> args) throws Exception {
        Object instance           = unwrap(args.get(0));
        FunctionReference funcRef = extractFunction(args.get(1));

        KeyListener listener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                List<Object> callArgs = new ArrayList<>(funcRef.getCapturedArgs());
                callArgs.add(e.getKeyCode());
                funcRef.getFunction().call(callArgs);
            }
        };

        Method method = Class.forName(className).getMethod("setOnKeyPress", KeyListener.class);
        method.invoke(instance, listener);
        return null;
    }
}
