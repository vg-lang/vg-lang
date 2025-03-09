package components;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Library {
    private String name;
    private Map<String, Namespace> namespaces = new HashMap<>();

    public Library(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addNamespace(Namespace ns) {
        namespaces.put(ns.getName(), ns);
    }

    public Namespace getNamespace(String name) {
        return namespaces.get(name);
    }

    public Collection<Namespace> getNamespaces() {
        return namespaces.values();
    }
}