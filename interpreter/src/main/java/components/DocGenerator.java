package components;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocGenerator {
    private static final Pattern COMMENT_PATTERN = Pattern.compile("##\\s*(.*)");
    private static final Pattern FUNCTION_PATTERN = Pattern.compile("function\\s+(\\w+)\\s*\\(([^)]*)\\)");
    private static final Pattern NAMESPACE_PATTERN = Pattern.compile("namespace\\s+(\\w+)\\s*\\{");
    private static final Pattern LIBRARY_PATTERN = Pattern.compile("library\\s+(\\w+)\\s*\\{");
    private static final Pattern STRUCT_PATTERN = Pattern.compile("struct\\s+(\\w+)\\s*\\{");
    private static final Pattern IMPORT_PATTERN = Pattern.compile("import\\s+([\\w.]+)");
    private static final Pattern DOC_COMMENT_START_PATTERN = Pattern.compile("/##\\s*(.*)");
    private static final Pattern DOC_COMMENT_LINE_PATTERN = Pattern.compile("#\\s*(.*)");
    private static final Pattern DOC_COMMENT_END_PATTERN = Pattern.compile("##/");
    private static final Pattern DOC_PARAM_PATTERN = Pattern.compile("#\\s*@param\\s+(\\w+)\\s+(.+)");
    private static final Pattern ENUM_PATTERN = Pattern.compile("enum\\s+(\\w+)\\s*\\{");
    private static final Pattern ENUM_VALUE_PATTERN = Pattern.compile("\\s*(\\w+)(?:\\s*=\\s*([^,}]+))?");
    private static final Pattern DOC_FIELD_PATTERN = Pattern.compile("#\\s*@field\\s+(\\w+)\\s+(.+)");
    private static final Pattern DOC_VALUE_PATTERN = Pattern.compile("#\\s*@value\\s+(\\w+)\\s+(.+)");
    private static final Pattern DOC_RETURN_PATTERN = Pattern.compile("#\\s*@return\\s+(.+)");
    private static final Pattern DOC_AUTHOR_PATTERN = Pattern.compile("#\\s*@author\\s+(.+)");

    private String outputDir;
    private Map<String, LibraryDoc> libraries = new HashMap<>();
    private List<ProgramDoc> programs = new ArrayList<>();

    public DocGenerator(String outputDir) {
        this.outputDir = outputDir;
        new File(outputDir).mkdirs();
    }

    public void generateProjectDocs(String projectDir) throws IOException {

        new File(outputDir + "/libraries").mkdirs();
        new File(outputDir + "/programs").mkdirs();
        
        processDirectory(projectDir, ".vglib");
        processDirectory(projectDir, ".vg");
        
        generateIndex();
        for (LibraryDoc lib : libraries.values()) {
            generateLibraryDoc(lib);
        }
        for (ProgramDoc program : programs) {
            generateProgramDoc(program);
        }

        System.out.println("Documentation generated successfully in: " + outputDir);
    }

    private void processDirectory(String directory, String extension) throws IOException {
        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Directory " + directory + " does not exist or is not a directory");
            return;
        }
        

        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(extension));
        if (files == null) return;

        for (File file : files) {
            if (extension.equals(".vglib")) {
                processLibraryFile(file);
            } else if (extension.equals(".vg")) {
                processProgramFile(file);
            }
        }
        
        File[] subdirs = dir.listFiles(File::isDirectory);
        if (subdirs != null) {
            for (File subdir : subdirs) {
                if (!subdir.getAbsolutePath().equals(new File(outputDir).getAbsolutePath())) {
                    processDirectory(subdir.getAbsolutePath(), extension);
                }
            }
        }
    }

    private void processLibraryFile(File file) throws IOException {
        String content = new String(Files.readAllBytes(file.toPath()));
        LibraryDoc libraryDoc = parseLibraryContent(content, file.getName());
        libraries.put(libraryDoc.name, libraryDoc);
    }

    private void processProgramFile(File file) throws IOException {
        String content = new String(Files.readAllBytes(file.toPath()));
        ProgramDoc programDoc = parseProgramContent(content, file.getName());
        programs.add(programDoc);
    }

    private LibraryDoc parseLibraryContent(String content, String filename) {
        LibraryDoc libraryDoc = new LibraryDoc();
        libraryDoc.filename = filename;

        Matcher libMatcher = LIBRARY_PATTERN.matcher(content);
        if (libMatcher.find()) {
            libraryDoc.name = libMatcher.group(1);
        } else {
            libraryDoc.name = filename.replace(".vglib", "");
        }

        String[] lines = content.split("\n");
        StringBuilder description = new StringBuilder();
        for (String line : lines) {
            Matcher commentMatcher = COMMENT_PATTERN.matcher(line);
            if (commentMatcher.find()) {
                description.append(commentMatcher.group(1)).append("\n");
            } else if (line.contains("library")) {
                break;
            }
        }
        libraryDoc.description = description.toString().trim();


        extractNamespacesAndFunctions(content, libraryDoc);

        return libraryDoc;
    }

    private void extractNamespacesAndFunctions(String content, LibraryDoc libraryDoc) {
        Matcher namespaceMatcher = NAMESPACE_PATTERN.matcher(content);
        while (namespaceMatcher.find()) {
            int namespaceStart = namespaceMatcher.start();
            String namespaceName = namespaceMatcher.group(1);
            

            processNamespaceDeclaration(content, namespaceStart, namespaceName, libraryDoc);
        }
        

        Matcher functionMatcher = FUNCTION_PATTERN.matcher(content);
        while (functionMatcher.find()) {
            int functionStart = functionMatcher.start();
            String functionName = functionMatcher.group(1);
            

            boolean isInNamespace = false;
            for (NamespaceDoc namespace : libraryDoc.namespaces) {
                for (FunctionDoc function : namespace.functions) {
                    if (function.name.equals(functionName)) {
                        isInNamespace = true;
                        break;
                    }
                }
                if (isInNamespace) break;
            }
            
            if (!isInNamespace) {

                FunctionDoc functionDoc = processFunctionDeclaration(content, functionStart, functionName);
                libraryDoc.globalFunctions.add(functionDoc);
            }
        }
    }

    private void processNamespaceDeclaration(String content, int namespaceStart, String namespaceName, LibraryDoc libraryDoc) {
        NamespaceDoc namespaceDoc = new NamespaceDoc();
        namespaceDoc.name = namespaceName;
        
        int docStart = content.lastIndexOf("/##", namespaceStart);
        if (docStart >= 0) {
            int docEnd = content.indexOf("##/", docStart);
            if (docEnd > docStart && docEnd < namespaceStart) {
                String docBlock = content.substring(docStart, docEnd);
                namespaceDoc.description = extractDocumentation(docBlock);
            }
        }
        

        int namespaceBodyStart = content.indexOf("{", namespaceStart) + 1;
        int namespaceBodyEnd = findMatchingCloseBrace(content, namespaceBodyStart);
        
        if (namespaceBodyStart > 0 && namespaceBodyEnd > namespaceBodyStart) {
            String namespaceBody = content.substring(namespaceBodyStart, namespaceBodyEnd);
            
            Matcher functionMatcher = FUNCTION_PATTERN.matcher(namespaceBody);
            while (functionMatcher.find()) {
                int functionStart = functionMatcher.start();
                String functionName = functionMatcher.group(1);
                
                FunctionDoc functionDoc = processFunctionDeclaration(namespaceBody, functionStart, functionName);
                namespaceDoc.functions.add(functionDoc);
            }
        }
        
        libraryDoc.namespaces.add(namespaceDoc);
    }

    private FunctionDoc processFunctionDeclaration(String content, int functionStart, String functionName) {
        FunctionDoc functionDoc = new FunctionDoc();
        functionDoc.name = functionName;
        
        Pattern paramListPattern = Pattern.compile("function\\s+" + Pattern.quote(functionName) + "\\s*\\(([^)]*)\\)");
        Matcher paramListMatcher = paramListPattern.matcher(content.substring(functionStart));
        if (paramListMatcher.find()) {
            String paramList = paramListMatcher.group(1);
            
            int docStart = content.lastIndexOf("/##", functionStart);
            if (docStart >= 0) {
                int docEnd = content.indexOf("##/", docStart);
                if (docEnd > docStart && docEnd < functionStart) {
                    String docBlock = content.substring(docStart, docEnd + 3);
                    functionDoc.description = extractDocumentation(docBlock);
                    
                    Matcher returnMatcher = DOC_RETURN_PATTERN.matcher(docBlock);
                    if (returnMatcher.find()) {
                        functionDoc.returnValue = returnMatcher.group(1).trim();
                    }
                    
                    Matcher authorMatcher = DOC_AUTHOR_PATTERN.matcher(docBlock);
                    while (authorMatcher.find()) {
                        functionDoc.authors.add(authorMatcher.group(1).trim());
                    }

                    functionDoc.parameters = parseParameters(paramList, docBlock);
                    
                    return functionDoc;
                }
            }
            
            functionDoc.parameters = parseParameters(paramList, "");
        }
        
        return functionDoc;
    }

    private int findMatchingCloseBrace(String content, int openBracePos) {
        int count = 1;
        for (int i = openBracePos; i < content.length(); i++) {
            if (content.charAt(i) == '{') {
                count++;
            } else if (content.charAt(i) == '}') {
                count--;
                if (count == 0) {
                    return i;
                }
            }
        }
        return content.length();
    }

    private String extractDocumentation(String docBlock) {
        StringBuilder sb = new StringBuilder();
        
        docBlock = docBlock.replaceAll("/##\\s*", "").replaceAll("##/", "");
        
        for (String line : docBlock.split("\n")) {
            Matcher m = DOC_COMMENT_LINE_PATTERN.matcher(line);
            if (m.find()) {
                String content = m.group(1).trim();
                if (!content.startsWith("@param") &&
                    !content.startsWith("@field") && 
                    !content.startsWith("@value") &&
                    !content.startsWith("@return") &&
                    !content.startsWith("@author")) {
                    sb.append(content).append("\n");
                }
            } else {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    sb.append(trimmed).append("\n");
                }
            }
        }
        
        return sb.toString().trim();
    }

    private List<ParameterDoc> parseParameters(String paramString, String docBlock) {
        List<ParameterDoc> parameters = new ArrayList<>();

        if (paramString == null || paramString.trim().isEmpty()) {
            return parameters;
        }
        
        Map<String, String> paramDocs = new HashMap<>();
        if (!docBlock.isEmpty()) {
            Matcher paramDocMatcher = DOC_PARAM_PATTERN.matcher(docBlock);
            while (paramDocMatcher.find()) {
                String paramName = paramDocMatcher.group(1);
                String paramDesc = paramDocMatcher.group(2);
                paramDocs.put(paramName, paramDesc);
            }
        }

        String[] paramNames = paramString.split(",");
        for (String param : paramNames) {
            String trimmedParam = param.trim();
            if (!trimmedParam.isEmpty()) {
                ParameterDoc paramDoc = new ParameterDoc();
                paramDoc.name = trimmedParam;

                if (paramDocs.containsKey(trimmedParam)) {
                    paramDoc.description = paramDocs.get(trimmedParam);
                }

                parameters.add(paramDoc);
            }
        }

        return parameters;
    }

    private ProgramDoc parseProgramContent(String content, String filename) {
        ProgramDoc programDoc = new ProgramDoc();
        programDoc.filename = filename;
        programDoc.name = filename.replace(".vg", "");

        StringBuilder currentDocComment = new StringBuilder();
        Map<String, String> paramDescriptions = new HashMap<>();
        String currentReturnValue = "";
        List<String> currentAuthors = new ArrayList<>();
        boolean inDocComment = false;

        String[] lines = content.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            
            Matcher docStartMatcher = DOC_COMMENT_START_PATTERN.matcher(line);
            if (docStartMatcher.find()) {
                inDocComment = true;
                currentDocComment.append(docStartMatcher.group(1)).append("\n");
                continue;
            }
            
            if (inDocComment) {
                Matcher docLineMatcher = DOC_COMMENT_LINE_PATTERN.matcher(line);
                if (docLineMatcher.find()) {
                    String commentText = docLineMatcher.group(1);
                    
                    Matcher paramMatcher = DOC_PARAM_PATTERN.matcher(line);
                    Matcher fieldMatcher = DOC_FIELD_PATTERN.matcher(line);
                    Matcher valueMatcher = DOC_VALUE_PATTERN.matcher(line);
                    Matcher returnMatcher = DOC_RETURN_PATTERN.matcher(line);
                    Matcher authorMatcher = DOC_AUTHOR_PATTERN.matcher(line);
                    
                    if (paramMatcher.find()) {
                        String paramName = paramMatcher.group(1);
                        String paramDesc = paramMatcher.group(2);
                        paramDescriptions.put(paramName, paramDesc);
                    } else if (fieldMatcher.find()) {
                        String fieldName = fieldMatcher.group(1);
                        String fieldDesc = fieldMatcher.group(2);
                        paramDescriptions.put(fieldName, fieldDesc);
                    } else if (valueMatcher.find()) {
                        String valueName = valueMatcher.group(1);
                        String valueDesc = valueMatcher.group(2);
                        paramDescriptions.put(valueName, valueDesc);
                    } else if (returnMatcher.find()) {
                        currentReturnValue = returnMatcher.group(1).trim();
                    } else if (authorMatcher.find()) {
                        currentAuthors.add(authorMatcher.group(1).trim());
                    } else {
                        currentDocComment.append(commentText).append("\n");
                    }
                    continue;
                }
                
                Matcher docEndMatcher = DOC_COMMENT_END_PATTERN.matcher(line);
                if (docEndMatcher.find()) {
                    inDocComment = false;
                    continue;
                }
            }
            
            Matcher funcMatcher = FUNCTION_PATTERN.matcher(line);
            if (funcMatcher.find()) {
                FunctionDoc functionDoc = new FunctionDoc();
                functionDoc.name = funcMatcher.group(1);
                
                if (currentDocComment.length() > 0) {
                    functionDoc.description = currentDocComment.toString().trim();
                    currentDocComment = new StringBuilder();
                }
                
                functionDoc.returnValue = currentReturnValue;
                functionDoc.authors.addAll(currentAuthors);
                currentReturnValue = "";
                currentAuthors.clear();
                
                String paramsStr = funcMatcher.group(2);
                if (!paramsStr.trim().isEmpty()) {
                    String[] params = paramsStr.split(",");
                    for (String param : params) {
                        ParameterDoc paramDoc = new ParameterDoc();
                        paramDoc.name = param.trim();
                        
                        if (paramDescriptions.containsKey(paramDoc.name)) {
                            paramDoc.description = paramDescriptions.get(paramDoc.name);
                        }
                        
                        functionDoc.parameters.add(paramDoc);
                    }
                }
                
                programDoc.functions.add(functionDoc);
                paramDescriptions.clear();
            }
            
            Matcher importMatcher = IMPORT_PATTERN.matcher(line);
            if (importMatcher.find()) {
                programDoc.imports.add(importMatcher.group(1));
            }
            
            Matcher structMatcher = STRUCT_PATTERN.matcher(line);
            if (structMatcher.find()) {
                StructDoc structDoc = new StructDoc();
                structDoc.name = structMatcher.group(1);
                
                if (currentDocComment.length() > 0) {
                    structDoc.description = currentDocComment.toString().trim();
                    currentDocComment = new StringBuilder();
                }
                
                StringBuilder structBody = new StringBuilder();
                int j = i + 1;
                int braceCount = 1;
                while (j < lines.length && braceCount > 0) {
                    String nextLine = lines[j];
                    if (nextLine.contains("{")) braceCount++;
                    if (nextLine.contains("}")) braceCount--;
                    structBody.append(nextLine).append("\n");
                    j++;
                }
                
                for (Map.Entry<String, String> entry : paramDescriptions.entrySet()) {
                    FieldDoc fieldDoc = new FieldDoc();
                    fieldDoc.name = entry.getKey();
                    fieldDoc.description = entry.getValue();
                    structDoc.fields.add(fieldDoc);
                }
                
                programDoc.structs.add(structDoc);
                paramDescriptions.clear();
            }
            
            Matcher enumMatcher = ENUM_PATTERN.matcher(line);
            if (enumMatcher.find()) {
                EnumDoc enumDoc = new EnumDoc();
                enumDoc.name = enumMatcher.group(1);
                
                if (currentDocComment.length() > 0) {
                    enumDoc.description = currentDocComment.toString().trim();
                    currentDocComment = new StringBuilder();
                }
                
                StringBuilder enumBody = new StringBuilder();
                int j = i + 1;
                int braceCount = 1;
                while (j < lines.length && braceCount > 0) {
                    String nextLine = lines[j];
                    if (nextLine.contains("{")) braceCount++;
                    if (nextLine.contains("}")) braceCount--;
                    enumBody.append(nextLine).append("\n");
                    j++;
                }
                
                for (Map.Entry<String, String> entry : paramDescriptions.entrySet()) {
                    EnumValueDoc valueDoc = new EnumValueDoc();
                    valueDoc.name = entry.getKey();
                    valueDoc.description = entry.getValue();
                    enumDoc.values.add(valueDoc);
                }
                
                programDoc.enums.add(enumDoc);
                paramDescriptions.clear();
            }
        }
        
        return programDoc;
    }


    private EnumDoc processEnumDeclaration(String content, int enumStart, String enumName) {
        EnumDoc enumDoc = new EnumDoc();
        enumDoc.name = enumName;
        
        int docStart = content.lastIndexOf("/##", enumStart);
        if (docStart >= 0) {
            int otherDeclaration = Math.max(
                content.lastIndexOf("function", enumStart - 1),
                Math.max(
                    content.lastIndexOf("struct", enumStart - 1),
                    content.lastIndexOf("enum", enumStart - 1)
                )
            );
            
            if (otherDeclaration < docStart) {
                int docEnd = content.indexOf("##/", docStart);
                if (docEnd > docStart && docEnd < enumStart) {
                    String docBlock = content.substring(docStart, docEnd);
                    enumDoc.description = extractDocumentation(docBlock);
                    

                    Pattern valueDocPattern = Pattern.compile("#\\s*@value\\s+(\\w+)(?:\\s*=\\s*\\d+)?\\s+(.+)");
                    Matcher valueDocMatcher = valueDocPattern.matcher(docBlock);
                    Map<String, String> valueDocs = new HashMap<>();
                    while (valueDocMatcher.find()) {
                        String valueName = valueDocMatcher.group(1);
                        String valueDesc = valueDocMatcher.group(2).trim();
                        valueDocs.put(valueName, valueDesc);
                    }
                    

                    int enumBodyStart = content.indexOf("{", enumStart) + 1;
                    int enumBodyEnd = findMatchingCloseBrace(content, enumBodyStart);
                    
                    if (enumBodyStart > 0 && enumBodyEnd > enumBodyStart) {
                        String enumBody = content.substring(enumBodyStart, enumBodyEnd);
                        String[] values = enumBody.split(",");
                        
                        for (String value : values) {
                            value = value.trim();
                            if (!value.isEmpty()) {
                                Matcher valueMatcher = ENUM_VALUE_PATTERN.matcher(value);
                                if (valueMatcher.find()) {
                                    EnumValueDoc valueDoc = new EnumValueDoc();
                                    valueDoc.name = valueMatcher.group(1);
                                    

                                    if (valueMatcher.group(2) != null) {
                                        valueDoc.value = valueMatcher.group(2).trim();
                                    }
                                    

                                    if (valueDocs.containsKey(valueDoc.name)) {
                                        valueDoc.description = valueDocs.get(valueDoc.name);
                                    }
                                    
                                    enumDoc.values.add(valueDoc);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return enumDoc;
    }


    private StructDoc processStructDeclaration(String content, int structStart, String structName) {
        StructDoc structDoc = new StructDoc();
        structDoc.name = structName;
        

        int docStart = content.lastIndexOf("/##", structStart);
        if (docStart >= 0) {

            int otherDeclaration = Math.max(
                content.lastIndexOf("function", structStart - 1),
                Math.max(
                    content.lastIndexOf("struct", structStart - 1),
                    content.lastIndexOf("enum", structStart - 1)
                )
            );
            
            if (otherDeclaration < docStart) {

                int docEnd = content.indexOf("##/", docStart);
                if (docEnd > docStart && docEnd < structStart) {
                    String docBlock = content.substring(docStart, docEnd);
                    structDoc.description = extractDocumentation(docBlock);
                    

                    Pattern fieldDocPattern = Pattern.compile("#\\s*@field\\s+(\\w+)\\s+(.+)");
                    Matcher fieldDocMatcher = fieldDocPattern.matcher(docBlock);
                    Map<String, String> fieldDocs = new HashMap<>();
                    while (fieldDocMatcher.find()) {
                        String fieldName = fieldDocMatcher.group(1);
                        String fieldDesc = fieldDocMatcher.group(2).trim();
                        fieldDocs.put(fieldName, fieldDesc);
                    }
                    

                    int structBodyStart = content.indexOf("{", structStart) + 1;
                    int structBodyEnd = findMatchingCloseBrace(content, structBodyStart);
                    
                    if (structBodyStart > 0 && structBodyEnd > structBodyStart) {
                        String structBody = content.substring(structBodyStart, structBodyEnd);
                        String[] lines = structBody.split(";");
                        
                        for (String line : lines) {
                            line = line.trim();
                            if (!line.isEmpty()) {

                                String fieldName = line.trim();
                                
                                FieldDoc fieldDoc = new FieldDoc();
                                fieldDoc.name = fieldName;
                                

                                if (fieldDocs.containsKey(fieldName)) {
                                    fieldDoc.description = fieldDocs.get(fieldName);
                                }
                                
                                structDoc.fields.add(fieldDoc);
                            }
                        }
                    }
                }
            }
        }
        
        return structDoc;
    }

    private void generateIndex() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html lang=\"en\">\n");
        sb.append("<head>\n");
        sb.append("    <meta charset=\"UTF-8\">\n");
        sb.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        sb.append("    <title>VG Language Project Documentation</title>\n");
        sb.append("    <link rel=\"stylesheet\" href=\"styles.css\">\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append("    <header>\n");
        sb.append("        <h1>VG Language Project Documentation</h1>\n");
        sb.append("    </header>\n");
        sb.append("    <main>\n");

        sb.append("        <section>\n");
        sb.append("            <h2>Libraries</h2>\n");
        sb.append("            <ul class=\"doc-list\">\n");
        for (LibraryDoc lib : libraries.values()) {
            sb.append("                <li><a href=\"libraries/").append(lib.name).append(".html\">")
                    .append(lib.name).append("</a></li>\n");
        }
        sb.append("            </ul>\n");
        sb.append("        </section>\n");

        sb.append("        <section>\n");
        sb.append("            <h2>Programs</h2>\n");
        sb.append("            <ul class=\"doc-list\">\n");
        
        if (programs.isEmpty()) {
            sb.append("                <li>No programs found</li>\n");
        } else {
            for (ProgramDoc program : programs) {
                System.out.println("Adding program to index: " + program.name);
                sb.append("                <li><a href=\"programs/").append(program.name).append(".html\">")
                        .append(program.name).append("</a></li>\n");
            }
        }
        
        sb.append("            </ul>\n");
        sb.append("        </section>\n");

        sb.append("        <section>\n");
        sb.append("            <h2>How to Document Your Code</h2>\n");
        sb.append("            <p>Use documentation comments to add documentation to your code:</p>\n");
        sb.append("            <h3>Function Documentation</h3>\n");
        sb.append("            <pre><code>/## This is a function description\n");
        sb.append("# This is a continuation of the description\n");
        sb.append("# @param paramName Description of the parameter\n");
        sb.append("# @return Description of the return value\n");
        sb.append("# @author Name of the author\n");
        sb.append("##/\n");
        sb.append("function myFunction(paramName) {\n");
        sb.append("    \n");
        sb.append("}</code></pre>\n");
        sb.append("            <h3>Struct Documentation</h3>\n");
        sb.append("            <pre><code>/## This is a struct description\n");
        sb.append("# @field fieldName Description of the field\n");
        sb.append("##/\n");
        sb.append("struct MyStruct {\n");
        sb.append("    fieldName;\n");
        sb.append("}</code></pre>\n");
        sb.append("            <h3>Enum Documentation</h3>\n");
        sb.append("            <pre><code>/## This is an enum description\n");
        sb.append("# @value VALUE_NAME Description of the enum value\n");
        sb.append("##/\n");
        sb.append("enum MyEnum {\n");
        sb.append("    VALUE_NAME,\n");
        sb.append("    ANOTHER_VALUE = 10\n");
        sb.append("}</code></pre>\n");
        sb.append("        </section>\n");

        sb.append("    </main>\n");
        sb.append("    <footer>\n");
        sb.append("        <p>Generated on ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("</p>\n");
        sb.append("    </footer>\n");
        sb.append("</body>\n");
        sb.append("</html>");

        writeToFile("index.html", sb.toString());

        generateCSS();
    }

    private void generateCSS() throws IOException {
        StringBuilder css = new StringBuilder();
        css.append("* {\n");
        css.append("    box-sizing: border-box;\n");
        css.append("    margin: 0;\n");
        css.append("    padding: 0;\n");
        css.append("}\n\n");

        css.append("body {\n");
        css.append("    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n");
        css.append("    line-height: 1.6;\n");
        css.append("    color: #333;\n");
        css.append("    max-width: 1200px;\n");
        css.append("    margin: 0 auto;\n");
        css.append("    padding: 20px;\n");
        css.append("}\n\n");

        css.append("header {\n");
        css.append("    background-color: #2c3e50;\n");
        css.append("    color: white;\n");
        css.append("    padding: 1rem;\n");
        css.append("    margin-bottom: 2rem;\n");
        css.append("    border-radius: 5px;\n");
        css.append("}\n\n");

        css.append("h1, h2, h3, h4 {\n");
        css.append("    margin-bottom: 0.5rem;\n");
        css.append("}\n\n");

        css.append("h2 {\n");
        css.append("    border-bottom: 2px solid #eee;\n");
        css.append("    padding-bottom: 0.3rem;\n");
        css.append("    margin-top: 2rem;\n");
        css.append("}\n\n");

        css.append("h3 {\n");
        css.append("    margin-top: 1.5rem;\n");
        css.append("    color: #2c3e50;\n");
        css.append("}\n\n");

        css.append("p {\n");
        css.append("    margin-bottom: 1rem;\n");
        css.append("}\n\n");

        css.append("code {\n");
        css.append("    font-family: 'Courier New', Courier, monospace;\n");
        css.append("    background-color: #f5f5f5;\n");
        css.append("    padding: 0.2rem 0.4rem;\n");
        css.append("    border-radius: 3px;\n");
        css.append("    font-size: 0.9rem;\n");
        css.append("}\n\n");

        css.append("pre {\n");
        css.append("    background-color: #f5f5f5;\n");
        css.append("    padding: 1rem;\n");
        css.append("    border-radius: 5px;\n");
        css.append("    overflow-x: auto;\n");
        css.append("    margin-bottom: 1rem;\n");
        css.append("}\n\n");

        css.append("table {\n");
        css.append("    width: 100%;\n");
        css.append("    border-collapse: collapse;\n");
        css.append("    margin-bottom: 1rem;\n");
        css.append("}\n\n");

        css.append("th, td {\n");
        css.append("    padding: 0.5rem;\n");
        css.append("    text-align: left;\n");
        css.append("    border-bottom: 1px solid #ddd;\n");
        css.append("}\n\n");

        css.append("th {\n");
        css.append("    background-color: #f2f2f2;\n");
        css.append("}\n\n");

        css.append("ul, ol {\n");
        css.append("    margin-left: 2rem;\n");
        css.append("    margin-bottom: 1rem;\n");
        css.append("}\n\n");

        css.append(".doc-list {\n");
        css.append("    list-style-type: none;\n");
        css.append("    margin-left: 0;\n");
        css.append("}\n\n");

        css.append(".doc-list li {\n");
        css.append("    margin-bottom: 0.5rem;\n");
        css.append("}\n\n");

        css.append(".doc-list a {\n");
        css.append("    text-decoration: none;\n");
        css.append("    color: #3498db;\n");
        css.append("    font-weight: 500;\n");
        css.append("}\n\n");

        css.append(".doc-list a:hover {\n");
        css.append("    text-decoration: underline;\n");
        css.append("}\n\n");

        css.append("footer {\n");
        css.append("    margin-top: 3rem;\n");
        css.append("    padding-top: 1rem;\n");
        css.append("    border-top: 1px solid #eee;\n");
        css.append("    color: #777;\n");
        css.append("    font-size: 0.9rem;\n");
        css.append("}\n\n");

        css.append(".parameter-name {\n");
        css.append("    font-weight: bold;\n");
        css.append("    color: #2c3e50;\n");
        css.append("}\n\n");

        css.append(".function-signature {\n");
        css.append("    background-color: #f8f9fa;\n");
        css.append("    padding: 0.5rem 1rem;\n");
        css.append("    border-left: 4px solid #3498db;\n");
        css.append("    margin-bottom: 1rem;\n");
        css.append("    font-family: 'Courier New', Courier, monospace;\n");
        css.append("}\n");

        css.append(".return-value {\n");
        css.append("    background-color: #f0f8ff;\n");
        css.append("    padding: 0.5rem 1rem;\n");
        css.append("    border-left: 4px solid #3498db;\n");
        css.append("    margin-bottom: 1rem;\n");
        css.append("}\n\n");
        
        css.append(".author {\n");
        css.append("    color: #666;\n");
        css.append("    font-style: italic;\n");
        css.append("    margin-bottom: 1rem;\n");
        css.append("}\n\n");

        writeToFile("styles.css", css.toString());
    }

    private void generateLibraryDoc(LibraryDoc lib) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html lang=\"en\">\n");
        sb.append("<head>\n");
        sb.append("    <meta charset=\"UTF-8\">\n");
        sb.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        sb.append("    <title>").append(lib.name).append(" Library - VG Documentation</title>\n");
        sb.append("    <link rel=\"stylesheet\" href=\"../styles.css\">\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append("    <header>\n");
        sb.append("        <h1>").append(lib.name).append(" Library</h1>\n");
        sb.append("    </header>\n");
        sb.append("    <nav>\n");
        sb.append("        <a href=\"../index.html\">Back to Index</a>\n");
        sb.append("    </nav>\n");
        sb.append("    <main>\n");

        if (!lib.description.isEmpty()) {
            sb.append("<section>\n");
            sb.append("<p>").append(lib.description.replace("\n", "<br>")).append("</p>\n");
            sb.append("</section>\n");
        }

        new File(outputDir + "/libraries").mkdirs();

        for (NamespaceDoc namespace : lib.namespaces) {
            sb.append("<section>\n");
            sb.append("<h2>Namespace: ").append(namespace.name).append("</h2>\n");

            if (!namespace.description.isEmpty()) {
                sb.append("<p>").append(namespace.description.replace("\n", "<br>")).append("</p>\n");
            }

            for (FunctionDoc function : namespace.functions) {
                sb.append("<div class=\"function\">\n");
                sb.append("<h3 id=\"").append(namespace.name).append("-").append(function.name).append("\">").append(function.name).append("</h3>\n");

                sb.append("<div class=\"function-signature\">");
                sb.append(function.name).append("(");

                List<String> paramStrings = new ArrayList<>();
                for (ParameterDoc param : function.parameters) {
                    paramStrings.add(param.name);
                }
                sb.append(String.join(", ", paramStrings));

                sb.append(")</div>\n");

                if (!function.description.isEmpty()) {
                    sb.append("                <p>").append(function.description.replace("\n", "<br>")).append("</p>\n");
                }
                
                if (!function.authors.isEmpty()) {
                    sb.append("<p class=\"author\"><strong>Author");
                    if (function.authors.size() > 1) {
                        sb.append("s");
                    }
                    sb.append(":</strong> ").append(String.join(", ", function.authors)).append("</p>\n");
                }

                if (!function.parameters.isEmpty()) {
                    sb.append("<h4>Parameters</h4>\n");
                    sb.append("<table>\n");
                    sb.append("<thead>\n");
                    sb.append("<tr>\n");
                    sb.append("<th>Name</th>\n");
                    sb.append("<th>Description</th>\n");
                    sb.append("</tr>\n");
                    sb.append("</thead>\n");
                    sb.append("<tbody>\n");

                    for (ParameterDoc param : function.parameters) {
                        sb.append("<tr>\n");
                        sb.append("<td class=\"parameter-name\">").append(param.name).append("</td>\n");
                        sb.append("<td>").append(param.description.isEmpty() ? "No description available" : param.description).append("</td>\n");
                        sb.append("</tr>\n");
                    }

                    sb.append("</tbody>\n");
                    sb.append("</table>\n");
                }

                if (!function.returnValue.isEmpty()) {
                    sb.append("<h4>Returns</h4>\n");
                    sb.append("<p class=\"return-value\">").append(function.returnValue).append("</p>\n");
                }

                sb.append("</div>\n");
            }

            sb.append("</section>\n");
        }

        if (!lib.globalFunctions.isEmpty()) {
            sb.append("<section>\n");
            sb.append("<h2>Global Functions</h2>\n");

            for (FunctionDoc function : lib.globalFunctions) {
                sb.append("<div class=\"function\">\n");
                sb.append("<h3 id=\"global-").append(function.name).append("\">").append(function.name).append("</h3>\n");

                sb.append("<div class=\"function-signature\">");
                sb.append(function.name).append("(");


                List<String> paramStrings = new ArrayList<>();
                for (ParameterDoc param : function.parameters) {
                    paramStrings.add(param.name);
                }
                sb.append(String.join(", ", paramStrings));

                sb.append(")</div>\n");

                if (!function.description.isEmpty()) {
                    sb.append("<p>").append(function.description.replace("\n", "<br>")).append("</p>\n");
                }

                if (!function.authors.isEmpty()) {
                    sb.append("<p class=\"author\"><strong>Author");
                    if (function.authors.size() > 1) {
                        sb.append("s");
                    }
                    sb.append(":</strong> ").append(String.join(", ", function.authors)).append("</p>\n");
                }

                if (!function.parameters.isEmpty()) {
                    sb.append("<h4>Parameters</h4>\n");
                    sb.append("<table>\n");
                    sb.append("<thead>\n");
                    sb.append("<tr>\n");
                    sb.append("<th>Name</th>\n");
                    sb.append("<th>Description</th>\n");
                    sb.append("</tr>\n");
                    sb.append("</thead>\n");
                    sb.append("<tbody>\n");

                    for (ParameterDoc param : function.parameters) {
                        sb.append("<tr>\n");
                        sb.append("<td class=\"parameter-name\">").append(param.name).append("</td>\n");
                        sb.append(" <td>").append(param.description.isEmpty() ? "No description available" : param.description).append("</td>\n");
                        sb.append(" </tr>\n");
                    }

                    sb.append("</tbody>\n");
                    sb.append("</table>\n");
                }

                if (!function.returnValue.isEmpty()) {
                    sb.append("<h4>Returns</h4>\n");
                    sb.append("<p class=\"return-value\">").append(function.returnValue).append("</p>\n");
                }

                sb.append("</div>\n");
            }

            sb.append("</section>\n");
        }

        sb.append("</main>\n");
        sb.append("<footer>\n");
        sb.append("<p>Generated on ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("</p>\n");
        sb.append("</footer>\n");
        sb.append("</body>\n");
        sb.append("</html>");

        writeToFile("libraries/" + lib.name + ".html", sb.toString());
    }

    private void generateProgramDoc(ProgramDoc program) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n<title>")
                .append(program.name)
                .append("</title>\n<link rel=\"stylesheet\" href=\"../styles.css\">\n</head>\n<body>\n<header>\n<h1>")
                .append(program.name)
                .append("</h1>\n</header>\n<nav>\n<a href=\"../index.html\">Back to Index</a>\n</nav>\n<main>\n");


        if (!program.imports.isEmpty()) {
            sb.append("<section>\n<h2>Imports</h2>\n<ul>\n");
            for (String importPath : program.imports) {
                sb.append("<li>").append(importPath).append("</li>\n");
            }
            sb.append("</ul>\n</section>\n");
        }


        if (!program.structs.isEmpty()) {
            sb.append("<section id=\"structs\">\n")
              .append("<h2>Structs</h2>\n");
            for (StructDoc struct : program.structs) {
                sb.append("<div class=\"struct\">\n")
                  .append("<h3 id=\"").append(struct.name).append("\">").append(struct.name).append("</h3>\n");
                
                if (struct.description != null && !struct.description.isEmpty()) {
                    String cleanDesc = cleanDocText(struct.description);
                    sb.append("<p>").append(cleanDesc).append("</p>\n");
                }
                
                if (!struct.fields.isEmpty()) {
                    sb.append("<h4>Fields</h4>\n")
                      .append("<table class=\"fields\">\n")
                      .append("<thead><tr><th>Name</th><th>Description</th></tr></thead>\n")
                      .append("<tbody>\n");
                    for (FieldDoc field : struct.fields) {
                        sb.append("<tr><td>").append(field.name).append("</td><td>");
                        if (field.description != null && !field.description.isEmpty()) {
                            String cleanDesc = cleanDocText(field.description);
                            sb.append(cleanDesc);
                        } else {
                            sb.append("No description available");
                        }
                        sb.append("</td></tr>\n");
                    }
                    sb.append("</tbody></table>\n");
                }
                
                sb.append("</div>\n");
            }
            sb.append("</section>\n");
        }


        if (!program.enums.isEmpty()) {
            sb.append("<section id=\"enums\">\n")
              .append("<h2>Enums</h2>\n");
            for (EnumDoc enum_ : program.enums) {
                sb.append("<div class=\"enum\">\n")
                  .append("<h3 id=\"").append(enum_.name).append("\">").append(enum_.name).append("</h3>\n");
                
                if (enum_.description != null && !enum_.description.isEmpty()) {
                    String cleanDesc = cleanDocText(enum_.description);
                    sb.append("<p>").append(cleanDesc).append("</p>\n");
                }
                
                if (!enum_.values.isEmpty()) {
                    sb.append("<h4>Values</h4>\n")
                      .append("<table class=\"values\">\n")
                      .append("<thead><tr><th>Name</th><th>Description</th></tr></thead>\n")
                      .append("<tbody>\n");
                    for (EnumValueDoc value : enum_.values) {
                        sb.append("<tr><td>").append(value.name).append("</td><td>");
                        if (value.description != null && !value.description.isEmpty()) {
                            String cleanDesc = cleanDocText(value.description);
                            sb.append(cleanDesc);
                        } else {
                            sb.append("No description available");
                        }
                        sb.append("</td></tr>\n");
                    }
                    sb.append("</tbody></table>\n");
                }
                
                sb.append("</div>\n");
            }
            sb.append("</section>\n");
        }


        if (!program.functions.isEmpty()) {
            sb.append("<section>\n<h2>Functions</h2>\n");
            for (FunctionDoc function : program.functions) {
                sb.append("<div class=\"function\" id=\"function-")
                        .append(function.name)
                        .append("\">\n<h3>")
                        .append(function.name)
                        .append("</h3>\n<pre><code>")
                        .append(function.name)
                        .append("(");
                for (int i = 0; i < function.parameters.size(); i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(function.parameters.get(i).name);
                }
                sb.append(")</code></pre>\n");
                if (!function.description.isEmpty()) {
                    String cleanDesc = cleanDocText(function.description);
                    sb.append("<p>").append(cleanDesc).append("</p>\n");
                }
                if (!function.parameters.isEmpty()) {
                    sb.append("<h4>Parameters</h4>\n<table>\n<thead>\n<tr>\n<th>Name</th>\n<th>Description</th>\n</tr>\n</thead>\n<tbody>\n");
                    for (ParameterDoc param : function.parameters) {
                        sb.append("<tr>\n<td class=\"parameter-name\">")
                                .append(param.name)
                                .append("</td>\n<td>");
                        if (param.description != null && !param.description.isEmpty()) {
                            String cleanDesc = cleanDocText(param.description);
                            sb.append(cleanDesc);
                        } else {
                            sb.append("No description available");
                        }
                        sb.append("</td></tr>\n");
                    }
                    sb.append("</tbody>\n</table>\n");
                }
                if (!function.returnValue.isEmpty()) {
                    sb.append("<h4>Returns</h4>\n");
                    sb.append("<p class=\"return-value\">").append(function.returnValue).append("</p>\n");
                }
                if (!function.authors.isEmpty()) {
                    sb.append("<p class=\"author\"><strong>Author");
                    if (function.authors.size() > 1) {
                        sb.append("s");
                    }
                    sb.append(":</strong> ").append(String.join(", ", function.authors)).append("</p>\n");
                }
                sb.append("</div>\n");
            }
            sb.append("</section>\n");
        }

        sb.append("</main>\n<footer>\n<p>Generated on ")
                .append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .append("</p>\n</footer>\n</body>\n</html>");

        writeToFile("programs/" + program.name + ".html", sb.toString());
    }

    private void writeToFile(String filePath, String content) {
        try {
            File file = new File(outputDir, filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            ErrorHandler.reportError("file not found","file not found");
        }
    }

    static class LibraryDoc {
        String name;
        String filename;
        String description = "";
        List<NamespaceDoc> namespaces = new ArrayList<>();
        List<FunctionDoc> globalFunctions = new ArrayList<>();
    }

    static class NamespaceDoc {
        String name;
        String description = "";
        List<FunctionDoc> functions = new ArrayList<>();
    }

    static class ProgramDoc {
        String name;
        String filename;
        List<String> imports = new ArrayList<>();
        List<StructDoc> structs = new ArrayList<>();
        List<EnumDoc> enums = new ArrayList<>();
        List<FunctionDoc> functions = new ArrayList<>();
    }

    static class FunctionDoc {
        String name;
        String description = "";
        List<ParameterDoc> parameters = new ArrayList<>();
        String returnValue = "";
        List<String> authors = new ArrayList<>();
    }

    static class ParameterDoc {
        String name;
        String description = "";
    }

    static class StructDoc {
        String name;
        String description = "";
        List<FieldDoc> fields = new ArrayList<>();
    }

    static class FieldDoc {
        String name;
        String description = "";
    }

    static class EnumDoc {
        String name;
        String description = "";
        List<EnumValueDoc> values = new ArrayList<>();
    }

    static class EnumValueDoc {
        String name;
        String value = "";
        String description = "";
    }

    private void updateCSS() throws IOException {
        StringBuilder css = new StringBuilder();
        css.append(".view-full-docs{display:inline-block;background-color:#3498db;color:white;padding:0.5rem 1rem;border-radius:4px;text-decoration:none;margin-top:1rem;font-weight:bold;}\n");
        css.append(".view-full-docs:hover{background-color:#2980b9;text-decoration:none;}\n");
        css.append(".quick-nav{background-color:#f8f9fa;padding:1rem;border-radius:5px;margin-bottom:2rem;}\n");
        css.append(".library-description,.imports{margin-bottom:2rem;}\n");
        css.append("nav{margin-bottom:1.5rem;}\n");
        css.append("nav a{text-decoration:none;color:#3498db;font-weight:500;}\n");
        css.append("nav a:hover{text-decoration:underline;}\n");
        css.append(".imported-library{background-color:#e8f4f8;padding:0.5rem 1rem;border-radius:4px;margin-bottom:1rem;}\n");
        css.append(".imported-library h3{color:#2c3e50;margin-bottom:0.5rem;}\n");


        try (FileWriter writer = new FileWriter(outputDir + "/styles.css", true)) {
            writer.write(css.toString());
        }
    }

    public void generateFileDoc(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File not found: " + filePath);
        }


        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".vglib")) {

            processLibraryFile(file);


            for (LibraryDoc lib : libraries.values()) {
                generateLibraryDoc(lib);
            }


            updateIndexWithLibrary(libraries.values().iterator().next());

        } else if (fileName.endsWith(".vg")) {

            processProgramFile(file);


            for (ProgramDoc program : programs) {
                generateProgramDoc(program);
            }


            updateIndexWithProgram(programs.get(0));
        } else {
            throw new IOException("Unsupported file type: " + fileName + ". Only .vg and .vglib files are supported.");
        }

        System.out.println("Documentation generated successfully for " + filePath + " in: " + outputDir);
    }

    private void updateIndexWithLibrary(LibraryDoc lib) throws IOException {
        File indexFile = new File(outputDir, "index.html");

        if (!indexFile.exists()) {

            generateNewIndex();
            indexFile = new File(outputDir, "index.html");
        }

        String content = new String(Files.readAllBytes(indexFile.toPath()));

        if (content.contains("href=\"libraries/" + lib.name + ".html\"")) {
            return;
        }

        int librariesListEnd = content.indexOf("</ul>", content.indexOf("<h2>Libraries</h2>"));
        if (librariesListEnd != -1) {
            StringBuilder newContent = new StringBuilder(content);
            String newLibraryEntry = "                <li><a href=\"libraries/" + lib.name + ".html\">" + lib.name + "</a></li>\n";
            newContent.insert(librariesListEnd, newLibraryEntry);

            int footerStart = newContent.indexOf("<footer>");
            int footerEnd = newContent.indexOf("</footer>", footerStart);
            if (footerStart != -1 && footerEnd != -1) {
                String newFooter = "    <footer>\n        <p>Generated on " +
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "</p>\n    ";
                newContent.replace(footerStart, footerEnd, newFooter);
            }

            Files.write(indexFile.toPath(), newContent.toString().getBytes());
        }
    }

    private void updateIndexWithProgram(ProgramDoc program) throws IOException {
        File indexFile = new File(outputDir, "index.html");

        if (!indexFile.exists()) {

            generateNewIndex();
            indexFile = new File(outputDir, "index.html");
        }


        String content = new String(Files.readAllBytes(indexFile.toPath()));


        if (content.contains("href=\"programs/" + program.name + ".html\"")) {

            return;
        }


        if (!content.contains("<h2>Programs</h2>")) {

            int mainEnd = content.indexOf("</main>");
            if (mainEnd != -1) {
                StringBuilder newContent = new StringBuilder(content);
                String programsSection = "<section>\n<h2>Programs</h2>\n<ul class=\"doc-list\">\n"
                        + "<li><a href=\"programs/" + program.name + ".html\">" + program.name + "</a></li>\n"
                        + "</ul>\n</section>\n";
                newContent.insert(mainEnd, programsSection);

                int footerStart = newContent.indexOf("<footer>");
                int footerEnd = newContent.indexOf("</footer>", footerStart);
                if (footerStart != -1 && footerEnd != -1) {
                    String newFooter = "<footer>\n<p>Generated on " +
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "</p>\n";
                    newContent.replace(footerStart, footerEnd, newFooter);
                }

                Files.write(indexFile.toPath(), newContent.toString().getBytes());
            }
        } else {
            int programsListEnd = content.indexOf("</ul>", content.indexOf("<h2>Programs</h2>"));
            if (programsListEnd != -1) {
                StringBuilder newContent = new StringBuilder(content);
                String newProgramEntry = "<li><a href=\"programs/" + program.name + ".html\">" + program.name + "</a></li>\n";
                newContent.insert(programsListEnd, newProgramEntry);

                int footerStart = newContent.indexOf("<footer>");
                int footerEnd = newContent.indexOf("</footer>", footerStart);
                if (footerStart != -1 && footerEnd != -1) {
                    String newFooter = "<footer>\n<p>Generated on " +
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "</p>\n";
                    newContent.replace(footerStart, footerEnd, newFooter);
                }

                Files.write(indexFile.toPath(), newContent.toString().getBytes());
            }
        }
    }

    private void generateNewIndex() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n")
                .append("<html lang=\"en\">\n")
                .append("<head>\n")
                .append("<meta charset=\"UTF-8\">\n")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
                .append("<title>VG Language Project Documentation</title>\n")
                .append("<link rel=\"stylesheet\" href=\"styles.css\">\n")
                .append("</head>\n")
                .append("<body>\n")
                .append("<header>\n")
                .append("<h1>VG Language Project Documentation</h1>\n")
                .append("</header>\n")
                .append("<main>\n")
                .append("<section>\n")
                .append("<h2>Libraries</h2>\n")
                .append("<ul class=\"doc-list\"></ul>\n")
                .append("</section>\n")
                .append("<section>\n")
                .append("<h2>How to Document Your Code</h2>\n")
                .append("<p>Use documentation comments to add documentation to your code:</p>\n")
                .append("<h3>Function Documentation</h3>\n")
                .append("<pre><code>/## This is a function description\n");
        sb.append("# This is a continuation of the description\n");
        sb.append("# @param paramName Description of the parameter\n");
        sb.append("# @return Description of the return value\n");
        sb.append("# @author Name of the author\n");
        sb.append("##/\n");
        sb.append("function myFunction(paramName) {\n");
        sb.append("    \n");
        sb.append("}</code></pre>\n");
        sb.append("            <h3>Struct Documentation</h3>\n");
        sb.append("            <pre><code>/## This is a struct description\n");
        sb.append("# @field fieldName Description of the field\n");
        sb.append("##/\n");
        sb.append("struct MyStruct {\n");
        sb.append("    fieldName;\n");
        sb.append("}</code></pre>\n");
        sb.append("            <h3>Enum Documentation</h3>\n");
        sb.append("            <pre><code>/## This is an enum description\n");
        sb.append("# @value VALUE_NAME Description of the enum value\n");
        sb.append("##/\n");
        sb.append("enum MyEnum {\n");
        sb.append("    VALUE_NAME,\n");
        sb.append("    ANOTHER_VALUE = 10\n");
        sb.append("}</code></pre>\n");
        sb.append("        </section>\n");

        sb.append("    </main>\n");
        sb.append("    <footer>\n");
        sb.append("        <p>Generated on ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("</p>\n");
        sb.append("    </footer>\n");
        sb.append("</body>\n");
        sb.append("</html>");

        writeToFile("index.html", sb.toString());


        File cssFile = new File(outputDir, "styles.css");
        if (!cssFile.exists()) {
            generateCSS();
        }
    }

    private String cleanDocText(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return text.replaceAll("/#|#/|@\\w+", "").trim();
    }
}
