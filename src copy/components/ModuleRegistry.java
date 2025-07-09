package components;

import java.util.HashMap;
import java.util.Map;

public class ModuleRegistry {
    private Map<String, Library> libraries = new HashMap<>();

    public void addLibrary(Library lib) {
        libraries.put(lib.getName(), lib);
    }

    public Library getLibrary(String name) {
        return libraries.get(name);
    }

    public boolean containsLibrary(String name) {
        return libraries.containsKey(name);
    }
}
