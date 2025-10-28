package components;

import java.util.HashMap;
import java.util.Map;

public class Namespace {
    private String name;
    private Map<String, Object> symbols = new HashMap<>();
    private Map<String, Namespace> children = new HashMap<>();
    private Map<String, Function> functions = new HashMap<>();
    private Map<String, ClassDefinition> classes = new HashMap<>();

    public Namespace(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addSymbol(String symbolName, Object symbol) {
        symbols.put(symbolName, symbol);
    }

    public Object getSymbol(String symbolName) {
        return symbols.get(symbolName);
    }

    public Map<String, Object> getSymbols() {
        return symbols;
    }
    public void addChildNamespace(Namespace ns) {
        children.put(ns.getName(), ns);
    }

    public Namespace getChildNamespace(String name) {
        return children.get(name);
    }


    public Namespace getNestedNamespace(String[] names, int index) {
        if (index >= names.length) {
            return this;
        }
        Namespace child = children.get(names[index]);
        if (child == null) return null;
        return child.getNestedNamespace(names, index + 1);
    }
    public void addFunction(String name, Function fn) { functions.put(name, fn); }
    public Function getFunction(String name) { return functions.get(name); }
}