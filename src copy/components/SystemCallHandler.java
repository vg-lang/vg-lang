package components;

import java.awt.event.*;
import java.lang.reflect.*;
import java.util.*;

public class SystemCallHandler {
    private final Map<String, Set<String>> allowedMethods;
    private final Set<String> allowedClasses;
    private final int currentLine;
    private final int currentColumn;

    public SystemCallHandler(Map<String, Set<String>> allowedMethods, Set<String> allowedClasses, int currentLine, int currentColumn) {
        this.allowedMethods = allowedMethods;
        this.allowedClasses = allowedClasses;
        this.currentLine = currentLine;
        this.currentColumn = currentColumn;
    }

    public Object handleSystemCall(List<Object> args) {
        if (args.size() < 2) {
            throw new RuntimeException("CallJava requires at least 2 arguments: className and methodName");
        }
        String className = args.get(0).toString();
        String memberName = args.get(1).toString();
        List<Object> methodArgs = args.size() > 2 ? args.subList(2, args.size()) : Collections.emptyList();
        try {
            Class<?> clazz = Class.forName(className);
            if (className.equals("components.MyGUI$MyButton") && memberName.equals("setOnClick") && methodArgs.size() == 2) {
                Object instanceObj = methodArgs.get(0);
                if (instanceObj instanceof LanguageObjectWrapper) {
                    instanceObj = ((LanguageObjectWrapper) instanceObj).getObject();
                }

                Object secondArg = methodArgs.get(1);
                if (!(secondArg instanceof FunctionReference)) {
                    throw new RuntimeException("setOnClick requires a FunctionReference argument");
                }
                FunctionReference funcRef = (FunctionReference) secondArg;

                ActionListener listener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        List<Object> finalArgs = new ArrayList<>(funcRef.getCapturedArgs());
                        funcRef.getFunction().call(finalArgs);
                    }
                };

                Method setOnClickMethod = clazz.getMethod("setOnClick", ActionListener.class);
                setOnClickMethod.invoke(instanceObj, listener);

                return null;
            }
            if (className.equals("components.MyGUI") && memberName.equals("setOnKeyPress") && methodArgs.size() == 2) {
                Object instanceObj = methodArgs.get(0);
                if (instanceObj instanceof LanguageObjectWrapper) {
                    instanceObj = ((LanguageObjectWrapper) instanceObj).getObject();
                }

                Object secondArg = methodArgs.get(1);
                if (!(secondArg instanceof FunctionReference)) {
                    throw new RuntimeException("setOnKeyPress requires a FunctionReference argument.");
                }
                FunctionReference funcRef = (FunctionReference) secondArg;

                KeyListener listener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        List<Object> argValues = new ArrayList<>(funcRef.getCapturedArgs());
                        argValues.add(e.getKeyCode());
                        funcRef.getFunction().call(argValues);
                    }
                };

                Method method = clazz.getMethod("setOnKeyPress", KeyListener.class);
                method.invoke(instanceObj, listener);

                return null;
            }
            if (className.equals("components.MyGUI") && memberName.equals("setOnKeyPress") && methodArgs.size() == 2) {
                Object instanceObj = methodArgs.get(0);
                final Object keyPressArg1 = methodArgs.get(1);
                final FunctionReference keyPressCallbackFunction = extractFunctionFromArg(keyPressArg1);

                KeyListener listener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        List<Object> argValues = new ArrayList<>();
                        argValues.add(e.getKeyCode());
                        keyPressCallbackFunction.getFunction().call(argValues);
                    }
                };

                Method method = clazz.getMethod(memberName, KeyListener.class);
                Object instance = ((LanguageObjectWrapper) instanceObj).getObject();
                method.invoke(instance, listener);
                return null;
            }
            if (className.equals("src.MyGUI") && memberName.equals("setOnMousePress") && methodArgs.size() == 2) {
                Object instanceObj = methodArgs.get(0);
                FunctionReference callbackFunction = extractFunctionFromArg(methodArgs.get(1));

                MyGUI gui = (MyGUI) ((LanguageObjectWrapper) instanceObj).getObject();
                gui.setOnMousePress(callbackFunction);
                return null;
            }
            if (className.equals("src.MyGUI") && memberName.equals("setOnMouseDrag") && methodArgs.size() == 2) {
                Object instanceObj = methodArgs.get(0);
                FunctionReference callbackFunction = extractFunctionFromArg(methodArgs.get(1));

                Method method = clazz.getMethod("setOnMouseDrag", FunctionReference.class);
                Object instance = ((LanguageObjectWrapper) instanceObj).getObject();
                method.invoke(instance, callbackFunction);
                return null;
            }

            if (className.equals("components.MyGUI") && memberName.equals("setOnKeyReleased") && methodArgs.size() == 2) {
                Object instanceObj = methodArgs.get(0);
                final Object keyReleaseArg1 = methodArgs.get(1);
                final FunctionReference keyReleaseCallbackFunction = extractFunctionFromArg(keyReleaseArg1);

                KeyListener listener = new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        List<Object> argValues = new ArrayList<>();
                        argValues.add(e.getKeyCode());
                        keyReleaseCallbackFunction.getFunction().call(argValues);
                    }
                };

                Method method = clazz.getMethod(memberName, KeyListener.class);
                Object instance = ((LanguageObjectWrapper) instanceObj).getObject();
                method.invoke(instance, listener);
                return null;
            }

            if (className.equals("javax.swing.Timer") && memberName.equals("<init>") && methodArgs.size() == 2) {
                Object delayObj = methodArgs.get(0);
                int delay;
                if (delayObj instanceof Number) {
                    delay = ((Number) delayObj).intValue();
                } else {
                    throw new ErrorHandler.VGException("First argument to Timer constructor must be a number",currentLine,currentColumn);
                }

                Object arg1 = methodArgs.get(1);
                FunctionReference callbackFunction = extractFunctionFromArg(arg1);

                ActionListener listener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        List<Object> argValues = new ArrayList<>();
                        callbackFunction.getFunction().call(argValues);
                    }
                };

                Constructor<?> constructor = clazz.getConstructor(int.class, ActionListener.class);
                Object instance = constructor.newInstance(delay, listener);

                return new LanguageObjectWrapper(instance);
            }
            if (!isMethodAllowed(clazz, memberName)) {
                throw new RuntimeException("Access to method '" + memberName + "' in class '" + className + "' is not allowed.");
            }
            Object instance = null;
            if (!memberName.equals("<init>")) {
                if (methodArgs.size() > 0 && methodArgs.get(0) instanceof LanguageObjectWrapper) {
                    instance = ((LanguageObjectWrapper) methodArgs.get(0)).getLanguageObject();
                    methodArgs = methodArgs.subList(1, methodArgs.size());
                }
            }

            AccessibleObject accessibleObject = null;
            if (memberName.equals("<init>")) {
                Constructor<?> constructor = findConstructor(clazz, methodArgs);
                if (constructor == null) {
                    throw new RuntimeException("Constructor not found in class '" + className + "' with " + methodArgs.size() + " arguments.");
                }
                accessibleObject = constructor;
            } else {
                Method method = findMethod(clazz, memberName, methodArgs);
                if (method == null) {
                    throw new RuntimeException("Method '" + memberName + "' not found in class '" + className + "'");
                }
                accessibleObject = method;
            }

            Object[] javaArgs = convertArguments(methodArgs, accessibleObject);

            Object result;
            if (accessibleObject instanceof Constructor<?>) {
                result = ((Constructor<?>) accessibleObject).newInstance(javaArgs);
            } else {
                result = ((Method) accessibleObject).invoke(instance, javaArgs);
            }

            if (result != null && !isPrimitiveOrWrapper(result.getClass())) {
                return new LanguageObjectWrapper(result);
            } else if (result instanceof List) {
                return convertJavaListToLanguageArray((List<?>) result);
            } else {
                return result;
            }
        } catch (InvocationTargetException ite) {
            Throwable cause = ite.getCause();
            if (cause != null) {
                String details = cause.toString();
                throw new RuntimeException("Error invoking system method: " + details, ite);
            } else {
                throw new RuntimeException("Error invoking system method: <no cause>", ite);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isMethodAllowed(Class<?> clazz, String methodName) {
        String className = clazz.getName();
        if (!allowedClasses.contains(className)) {
            return false;
        }
        Set<String> methods = allowedMethods.get(className);
        return methods != null && (methods.contains(methodName) || methods.contains("*"));
    }

    private Constructor<?> findConstructor(Class<?> clazz, List<Object> args) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (matchParameterTypes(constructor.getParameterTypes(), args)) {
                return constructor;
            }
        }
        return null;
    }

    private boolean matchParameterTypes(Class<?>[] paramTypes, List<Object> args) {
        if (paramTypes.length != args.size()) {
            return false;
        }
        for (int i = 0; i < paramTypes.length; i++) {
            if (!isAssignable(paramTypes[i], args.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isAssignable(Class<?> paramType, Object arg) {
        if (arg == null) {
            return !paramType.isPrimitive();
        }
        if (arg instanceof LanguageObjectWrapper) {
            arg = ((LanguageObjectWrapper) arg).getObject();
        }
        Class<?> argClass = arg.getClass();
        if (paramType.isPrimitive()) {
            Class<?> wrapperClass = getWrappedClass(paramType);
            return wrapperClass.isAssignableFrom(argClass);
        }
        return paramType.isAssignableFrom(argClass);
    }

    private Class<?> getWrappedClass(Class<?> primitiveType) {
        if (primitiveType == boolean.class) return Boolean.class;
        if (primitiveType == byte.class) return Byte.class;
        if (primitiveType == char.class) return Character.class;
        if (primitiveType == double.class) return Double.class;
        if (primitiveType == float.class) return Float.class;
        if (primitiveType == int.class) return Integer.class;
        if (primitiveType == long.class) return Long.class;
        if (primitiveType == short.class) return Short.class;
        if (primitiveType == void.class) return Void.class;
        return primitiveType;
    }

    private Method findMethod(Class<?> clazz, String methodName, List<Object> args) {
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName) && matchParameterTypes(method.getParameterTypes(), args)) {
                return method;
            }
        }
        return null;
    }

    private Object[] convertArguments(List<Object> args, AccessibleObject accessibleObject) {
        Class<?>[] paramTypes;
        if (accessibleObject instanceof Method) {
            paramTypes = ((Method) accessibleObject).getParameterTypes();
        } else {
            paramTypes = ((Constructor<?>) accessibleObject).getParameterTypes();
        }

        Object[] convertedArgs = new Object[args.size()];
        for (int i = 0; i < args.size(); i++) {
            Object arg = args.get(i);
            if (arg instanceof LanguageObjectWrapper) {
                convertedArgs[i] = ((LanguageObjectWrapper) arg).getObject();
            } else {
                convertedArgs[i] = arg;
            }
        }
        return convertedArgs;
    }

    private List<Object> convertJavaListToLanguageArray(List<?> javaList) {
        List<Object> result = new ArrayList<>();
        for (Object item : javaList) {
            if (item != null && !isPrimitiveOrWrapper(item.getClass())) {
                result.add(new LanguageObjectWrapper(item));
            } else {
                result.add(item);
            }
        }
        return result;
    }

    private boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == Boolean.class ||
                clazz == Character.class ||
                clazz == Byte.class ||
                clazz == Short.class ||
                clazz == Integer.class ||
                clazz == Long.class ||
                clazz == Float.class ||
                clazz == Double.class ||
                clazz == Void.class ||
                clazz == String.class;
    }

    private FunctionReference extractFunctionFromArg(Object arg) {
        if (arg instanceof FunctionReference) {
            return (FunctionReference) arg;
        }
        throw new RuntimeException("Expected a function reference");
    }
} 