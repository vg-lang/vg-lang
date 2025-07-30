package components;

/**
 * Wrapper class that tracks a symbol's value along with its origin namespace
 * and whether it has conflicts with other imported symbols.
 */
public class SymbolEntry {
    private Object value;
    private String originNamespace;
    private boolean isAmbiguous;
    
    public SymbolEntry(Object value, String originNamespace) {
        this.value = value;
        this.originNamespace = originNamespace;
        this.isAmbiguous = false;
    }
    
    public Object getValue() {
        return value;
    }
    
    public String getOriginNamespace() {
        return originNamespace;
    }
    
    public boolean isAmbiguous() {
        return isAmbiguous;
    }
    
    public void setAmbiguous(boolean ambiguous) {
        this.isAmbiguous = ambiguous;
    }
    
    @Override
    public String toString() {
        return "SymbolEntry{value=" + value + ", origin=" + originNamespace + ", ambiguous=" + isAmbiguous + "}";
    }
} 