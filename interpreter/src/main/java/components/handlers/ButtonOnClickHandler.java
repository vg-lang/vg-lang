package components.handlers;

import components.FunctionReference;
import components.LanguageObjectWrapper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles: VgSystemCall("components.MyGUI$MyButton", "setOnClick", instance, callback)
 */
public class ButtonOnClickHandler extends BaseHandler {

    @Override
    public boolean matches(String className, String methodName, List<Object> args) {
        return is(className, methodName,
                "components.MyGUI$MyButton", "setOnClick", args, 2);
    }

    @Override
    public Object handle(String className, String methodName, List<Object> args) throws Exception {
        Object instance      = unwrap(args.get(0));
        FunctionReference funcRef = extractFunction(args.get(1));

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Object> callArgs = new ArrayList<>(funcRef.getCapturedArgs());
                funcRef.getFunction().call(callArgs);
            }
        };

        Method method = Class.forName(className).getMethod("setOnClick", ActionListener.class);
        method.invoke(instance, listener);
        return null;
    }
}
