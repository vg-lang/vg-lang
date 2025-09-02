package components;

import java.util.List;

public class VGMethod {
    private String name;
    private List<String> parameters;
    private vg_langParser.BlockContext codeBlock;
    private boolean isPrivate;
    private boolean isStatic;
    private boolean isConst;
    private int line;
    private int column;
    
    public VGMethod(String name, List<String> parameters, vg_langParser.BlockContext codeBlock, 
                 boolean isPrivate, boolean isStatic, boolean isConst, int line, int column) {
        this.name = name;
        this.parameters = parameters;
        this.codeBlock = codeBlock;
        this.isPrivate = isPrivate;
        this.isStatic = isStatic;
        this.isConst = isConst;
        this.line = line;
        this.column = column;
    }
    
    public String getName() {
        return name;
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
    
    public boolean isStatic() {
        return isStatic;
    }
    
    public boolean isConst() {
        return isConst;
    }
    
    public int getLine() {
        return line;
    }
    
    public int getColumn() {
        return column;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isPrivate) sb.append("private ");
        if (isStatic) sb.append("static ");
        if (isConst) sb.append("const ");
        sb.append("method ").append(name).append("(").append(parameters.size()).append(" params)");
        return sb.toString();
    }
} 