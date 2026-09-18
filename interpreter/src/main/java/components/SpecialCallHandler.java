package components;

import java.util.List;

public interface SpecialCallHandler {
    boolean matches(String className, String methodName, List<Object> args);
    Object handle(String className, String methodName, List<Object> args) throws Exception;
}
