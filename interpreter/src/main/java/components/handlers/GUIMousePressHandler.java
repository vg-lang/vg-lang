package components.handlers;

import components.FunctionReference;
import components.MyGUI;
import components.LanguageObjectWrapper;

import java.util.List;

/**
 * Handles: VgSystemCall("components.MyGUI", "setOnMousePress", instance, callback)
 */
public class GUIMousePressHandler extends BaseHandler {

    @Override
    public boolean matches(String className, String methodName, List<Object> args) {
        return is(className, methodName,
                "components.MyGUI", "setOnMousePress", args, 2);
    }

    @Override
    public Object handle(String className, String methodName, List<Object> args) throws Exception {
        Object instance           = unwrap(args.get(0));
        FunctionReference funcRef = extractFunction(args.get(1));

        ((MyGUI) instance).setOnMousePress(funcRef);
        return null;
    }
}
