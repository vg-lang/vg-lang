package components;

/**
 * Optional interface for handlers that need line/column info for error messages.
 * Implement this alongside SpecialCallHandler if your handler throws VGException.
 * SystemCallHandler will call setContext() automatically during discovery.
 */
public interface HandlerContext {
    void setContext(int line, int column);
}
