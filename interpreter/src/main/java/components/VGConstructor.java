package components;

import java.util.List;

public class VGConstructor {
    private List<String> parameters;
    private vg_langParser.BlockContext codeBlock;
    private boolean isPrivate;
    private int line;
    private int column;
    
    public VGConstructor(List<String> parameters, vg_langParser.BlockContext codeBlock, boolean isPrivate, int line, int column) {
        this.parameters = parameters;
        this.codeBlock = codeBlock;
        this.isPrivate = isPrivate;
        this.line = line;
        this.column = column;
    }
    
    public List<String> getParameters() {
        return parameters;
    }
    
    public int getParameterCount() {
        return parameters.size();
    }
    
    public vg_langParser.BlockContext getCodeBlock() {
        return codeBlock;
    }
    
    public boolean isPrivate() {
        return isPrivate;
    }
    
    public int getLine() {
        return line;
    }
    
    public int getColumn() {
        return column;
    }
    
    @Override
    public String toString() {
        return "VGConstructor(" + parameters.size() + " params)";
    }
} 