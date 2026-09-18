package components.handlers;

import components.FunctionReference;
import components.LanguageObjectWrapper;
import components.SpecialCallHandler;

import java.util.List;

/**
 * Shared utilities for all special call handlers.
 * Every handler extends this instead of implementing SpecialCallHandler directly.
 */
public abstract class BaseHandler implements SpecialCallHandler {

    /**
     * Unwraps a LanguageObjectWrapper to get the underlying Java object.
     */
    protected Object unwrap(Object obj) {
        if (obj instanceof LanguageObjectWrapper) {
            return ((LanguageObjectWrapper) obj).getObject();
        }
        return obj;
    }

    /**
     * Extracts a FunctionReference from an argument, throwing clearly if it isn't one.
     */
    protected FunctionReference extractFunction(Object arg) {
        if (arg instanceof FunctionReference) {
            return (FunctionReference) arg;
        }
        throw new RuntimeException(
            "Expected a function reference, got: " + (arg == null ? "null" : arg.getClass().getSimpleName())
        );
    }

    /**
     * Convenience matcher — checks class name, method name, and argument count all at once.
     */
    protected boolean is(String className, String methodName,
                         String expectedClass, String expectedMethod,
                         List<?> args, int expectedArgCount) {
        return className.equals(expectedClass)
            && methodName.equals(expectedMethod)
            && args.size() == expectedArgCount;
    }
}
