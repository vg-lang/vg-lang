package components;

import java.lang.reflect.*;
import java.util.*;

public class SystemCallHandler {
    private final Map<String, Set<String>> allowedMethods;
    private final Set<String> allowedClasses;
    private final int currentLine;
    private final int currentColumn;

    private final List<SpecialCallHandler> handlers;

    public SystemCallHandler(Map<String, Set<String>> allowedMethods,
                             Set<String> allowedClasses,
                             int currentLine, int currentColumn) {
        this.allowedMethods = allowedMethods;
        this.allowedClasses = allowedClasses;
        this.currentLine    = currentLine;
        this.currentColumn  = currentColumn;
        this.handlers       = discoverHandlers();
    }

    // ---------------------------------------------------------------------------
    // Auto-discovery via ServiceLoader — zero extra dependencies, built into JDK.
    // To register a new handler, add its fully-qualified class name to:
    //   resources/META-INF/services/components.SpecialCallHandler
    // ---------------------------------------------------------------------------
    private List<SpecialCallHandler> discoverHandlers() {
        List<SpecialCallHandler> found = new ArrayList<>();

        ServiceLoader<SpecialCallHandler> loader =
            ServiceLoader.load(SpecialCallHandler.class);

        for (SpecialCallHandler handler : loader) {
            // Handlers that need line/column context implement HandlerContext
            if (handler instanceof HandlerContext) {
                ((HandlerContext) handler).setContext(currentLine, currentColumn);
            }
            found.add(handler);
        }
        return found;
    }

    // ---------------------------------------------------------------------------
    // Entry point — try special handlers first, fall through to reflection.
    // ---------------------------------------------------------------------------
    public Object handleSystemCall(List<Object> args) {
        if (args.size() < 2) {
            throw new RuntimeException("VgSystemCall requires at least 2 arguments: className and methodName");
        }

        String className  = args.get(0).toString();
        String methodName = args.get(1).toString();
        List<Object> methodArgs = args.size() > 2
            ? args.subList(2, args.size())
            : Collections.emptyList();

        for (SpecialCallHandler handler : handlers) {
            if (handler.matches(className, methodName, methodArgs)) {
                try {
                    return handler.handle(className, methodName, methodArgs);
                } catch (Exception e) {
                    throw new RuntimeException(
                        "Handler failed for " + className + "." + methodName + ": " + e.getMessage(), e);
                }
            }
        }

        return handleGenericReflection(className, methodName, methodArgs);
    }

    // ---------------------------------------------------------------------------
    // Generic reflection fallback — unchanged from original.
    // ---------------------------------------------------------------------------
    private Object handleGenericReflection(String className, String methodName, List<Object> methodArgs) {
        try {
            Class<?> clazz = Class.forName(className);

            if (!isMethodAllowed(clazz, methodName)) {
                throw new RuntimeException(
                    "Access to method '" + methodName + "' in class '" + className + "' is not allowed.");
            }

            Object instance = null;
            if (!methodName.equals("<init>")) {
                if (!methodArgs.isEmpty() && methodArgs.get(0) instanceof LanguageObjectWrapper) {
                    instance   = ((LanguageObjectWrapper) methodArgs.get(0)).getLanguageObject();
                    methodArgs = methodArgs.subList(1, methodArgs.size());
                }
            }

            AccessibleObject accessibleObject;
            if (methodName.equals("<init>")) {
                Constructor<?> constructor = findConstructor(clazz, methodArgs);
                if (constructor == null) {
                    throw new RuntimeException(
                        "Constructor not found in class '" + className + "' with " + methodArgs.size() + " arguments.");
                }
                accessibleObject = constructor;
            } else {
                Method method = findMethod(clazz, methodName, methodArgs);
                if (method == null) {
                    throw new RuntimeException(
                        "Method '" + methodName + "' not found in class '" + className + "'");
                }
                accessibleObject = method;
            }

            Object[] javaArgs = convertArguments(methodArgs, accessibleObject);

            Object result;
            if (accessibleObject instanceof Constructor<?>) {
                result = ((Constructor<?>) accessibleObject).newInstance(javaArgs);
            } else {
                result = ((Method) accessibleObject).invoke(instance, javaArgs);

                // Track Timer stop for the debugger
                if (instance instanceof javax.swing.Timer && methodName.equals("stop")) {
                    Interpreter.unregisterTimer((javax.swing.Timer) instance);
                }
            }

            if (result == null)                          return null;
            if (result instanceof List)                  return convertJavaListToLanguageArray((List<?>) result);
            if (isPrimitiveOrWrapper(result.getClass())) return result;
            return new LanguageObjectWrapper(result);

        } catch (InvocationTargetException ite) {
            Throwable cause = ite.getCause();
            throw new RuntimeException(
                "Error invoking system method: " + (cause != null ? cause.toString() : "<no cause>"), ite);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    // ---------------------------------------------------------------------------
    // Helpers (unchanged from original)
    // ---------------------------------------------------------------------------

    private boolean isMethodAllowed(Class<?> clazz, String methodName) {
        String className = clazz.getName();
        if (!allowedClasses.contains(className)) return false;
        Set<String> methods = allowedMethods.get(className);
        return methods != null && (methods.contains(methodName) || methods.contains("*"));
    }

    private Constructor<?> findConstructor(Class<?> clazz, List<Object> args) {
        for (Constructor<?> c : clazz.getConstructors()) {
            if (matchParameterTypes(c.getParameterTypes(), args)) return c;
        }
        return null;
    }

    private Method findMethod(Class<?> clazz, String methodName, List<Object> args) {
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(methodName) && matchParameterTypes(m.getParameterTypes(), args)) return m;
        }
        return null;
    }

    private boolean matchParameterTypes(Class<?>[] paramTypes, List<Object> args) {
        if (paramTypes.length != args.size()) return false;
        for (int i = 0; i < paramTypes.length; i++) {
            if (!isAssignable(paramTypes[i], args.get(i))) return false;
        }
        return true;
    }

    private boolean isAssignable(Class<?> paramType, Object arg) {
        if (arg == null) return !paramType.isPrimitive();
        if (arg instanceof LanguageObjectWrapper) arg = ((LanguageObjectWrapper) arg).getObject();
        Class<?> argClass = arg.getClass();
        // VG numbers are Double or Long at runtime — allow them to match any numeric type
        if (arg instanceof Number) {
            if (paramType == int.class    || paramType == Integer.class) return true;
            if (paramType == double.class || paramType == Double.class)  return true;
            if (paramType == long.class   || paramType == Long.class)    return true;
            if (paramType == float.class  || paramType == Float.class)   return true;
            if (paramType == short.class  || paramType == Short.class)   return true;
            if (paramType == byte.class   || paramType == Byte.class)    return true;
        }
        if (paramType.isPrimitive()) return getWrappedClass(paramType).isAssignableFrom(argClass);
        return paramType.isAssignableFrom(argClass);
    }

    private Class<?> getWrappedClass(Class<?> p) {
        if (p == boolean.class) return Boolean.class;
        if (p == byte.class)    return Byte.class;
        if (p == char.class)    return Character.class;
        if (p == double.class)  return Double.class;
        if (p == float.class)   return Float.class;
        if (p == int.class)     return Integer.class;
        if (p == long.class)    return Long.class;
        if (p == short.class)   return Short.class;
        if (p == void.class)    return Void.class;
        return p;
    }

    private Object[] convertArguments(List<Object> args, AccessibleObject accessibleObject) {
        Class<?>[] paramTypes = (accessibleObject instanceof Constructor<?>)
            ? ((Constructor<?>) accessibleObject).getParameterTypes()
            : ((Method) accessibleObject).getParameterTypes();

        Object[] converted = new Object[args.size()];
        for (int i = 0; i < args.size(); i++) {
            Object arg = args.get(i);
            if (arg instanceof LanguageObjectWrapper) arg = ((LanguageObjectWrapper) arg).getObject();

            // Narrow VG numeric types (Double/Long) to whatever primitive the method actually expects
            if (arg instanceof Number && i < paramTypes.length) {
                Number n        = (Number) arg;
                Class<?> target = paramTypes[i];
                if      (target == int.class    || target == Integer.class) arg = n.intValue();
                else if (target == double.class || target == Double.class)  arg = n.doubleValue();
                else if (target == long.class   || target == Long.class)    arg = n.longValue();
                else if (target == float.class  || target == Float.class)   arg = n.floatValue();
                else if (target == short.class  || target == Short.class)   arg = n.shortValue();
                else if (target == byte.class   || target == Byte.class)    arg = n.byteValue();
            }

            converted[i] = arg;
        }
        return converted;
    }

    private List<Object> convertJavaListToLanguageArray(List<?> javaList) {
        List<Object> result = new ArrayList<>();
        for (Object item : javaList) {
            result.add((item != null && !isPrimitiveOrWrapper(item.getClass()))
                ? new LanguageObjectWrapper(item)
                : item);
        }
        return result;
    }

    private boolean isPrimitiveOrWrapper(Class<?> c) {
        return c.isPrimitive()
            || c == Boolean.class || c == Character.class || c == Byte.class
            || c == Short.class   || c == Integer.class   || c == Long.class
            || c == Float.class   || c == Double.class    || c == Void.class
            || c == String.class;
    }
}