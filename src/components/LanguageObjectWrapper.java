package components;

public class LanguageObjectWrapper {
    private Object languageObject;
    public LanguageObjectWrapper(Object languageObject) {
        this.languageObject = languageObject;
    }
    public Object getLanguageObject() {
        return languageObject;
    }
    public void setLanguageObject(Object languageObject) {
        this.languageObject = languageObject;
    }
}
