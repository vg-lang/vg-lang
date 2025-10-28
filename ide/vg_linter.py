"""
VG Language Linter
Provides static analysis and code quality checks for VG Language files.
"""

import re
import os
import json
from typing import List, Dict, Tuple, Optional

class LintMessage:
    """Represents a linter message"""
    def __init__(self, line: int, column: int, severity: str, message: str, rule: str):
        self.line = line
        self.column = column
        self.severity = severity  # 'error', 'warning', 'info'
        self.message = message
        self.rule = rule
    
    def __str__(self):
        return f"Line {self.line}, Col {self.column}: {self.severity.upper()}: {self.message} ({self.rule})"

class VGLinter:
    """VG Language static code analyzer"""
    
    def __init__(self):
        self.messages = []
        
        # VG Language keywords from grammar
        self.keywords = {
            'var', 'const', 'function', 'if', 'else', 'while', 'for', 'do', 
            'return', 'break', 'continue', 'try', 'catch', 'finally', 'throw',
            'true', 'false', 'this', 'new', 'switch', 'case', 'default', 
            'struct', 'enum', 'class', 'extends', 'public', 'private', 'static',
            'import', 'library', 'namespace', 'constructor', 'as'
        }
        
        self.builtin_types = {
            'int', 'double', 'string', 'boolean', 'array', 'object'
        }
        
        # Built-in functions available globally
        self.builtin_functions = {
            'print', 'length', 'toString', 'parseInt', 'parseDouble'
        }
        
        # Libraries loaded from configuration
        self.standard_libraries = {}
        self.user_libraries = {}
        
        # Track imported libraries and namespaces
        self.imported_libraries = set()
        self.imported_namespaces = set()
        
        # Track declared enums and their values
        self.declared_enums = set()
        self.enum_values = set()
        self.declared_structs = set()
        
        # Load all libraries from configuration files
        self._load_standard_libraries()
        self._load_project_libraries_config()
        
        # Load user libraries from libraries directory (.vglib files)
        self._load_user_libraries()
    
    def _load_standard_libraries(self):
        """Load standard libraries from global configuration (read-only)"""
        config_path = os.path.join(os.path.dirname(__file__), 'config', 'standard_libraries.json')
        if os.path.exists(config_path):
            try:
                with open(config_path, 'r', encoding='utf-8') as f:
                    config = json.load(f)
                
                # Load standard libraries
                if 'standard_libraries' in config:
                    self.standard_libraries = config['standard_libraries']
                    
            except Exception as e:
                # If loading fails, use empty libraries
                self.standard_libraries = {}
        else:
            # Create default standard libraries file if it doesn't exist
            self._create_default_standard_libraries()
    
    def _load_project_libraries_config(self, project_path: str = None):
        """Load project-specific user libraries from project directory"""
        if project_path:
            # Look for project-specific libraries
            config_path = os.path.join(project_path, 'vg_project_libraries.json')
        else:
            # Default to current directory
            config_path = os.path.join(os.getcwd(), 'vg_project_libraries.json')
        
        if os.path.exists(config_path):
            try:
                with open(config_path, 'r', encoding='utf-8') as f:
                    config = json.load(f)
                
                # Load project-specific user libraries
                if 'project_libraries' in config:
                    self.user_libraries = config['project_libraries']
                    
            except Exception as e:
                # If loading fails, use empty libraries
                self.user_libraries = {}
        else:
            # Start with empty user libraries for this project
            self.user_libraries = {}
    
    def _create_default_standard_libraries(self):
        """Create default standard libraries configuration file"""
        config_path = os.path.join(os.path.dirname(__file__), 'config', 'standard_libraries.json')
        
        # Ensure config directory exists
        os.makedirs(os.path.dirname(config_path), exist_ok=True)
        
        default_config = {
            "standard_libraries": {}
        }
        
        try:
            with open(config_path, 'w', encoding='utf-8') as f:
                json.dump(default_config, f, indent=2)
        except Exception:
            pass  # Silently ignore if we can't create the file
    
    def _load_project_libraries(self, filepath: str):
        """Load project-specific libraries based on the source file location"""
        # Look for packages directory relative to the source file
        file_dir = os.path.dirname(filepath)
        packages_dir = os.path.join(file_dir, 'packages')
        
        # Also check parent directories for packages
        current_dir = file_dir
        for _ in range(3):  # Check up to 3 levels up
            packages_dir = os.path.join(current_dir, 'packages')
            if os.path.exists(packages_dir):
                for filename in os.listdir(packages_dir):
                    if filename.endswith('.vglib'):
                        self._parse_library_file(os.path.join(packages_dir, filename))
                break
            current_dir = os.path.dirname(current_dir)
    
    def _load_user_libraries(self):
        """Load user-defined libraries from .vglib files"""
        # Load from main libraries directory
        libraries_dir = os.path.join(os.path.dirname(__file__), '..', 'libraries')
        if os.path.exists(libraries_dir):
            for filename in os.listdir(libraries_dir):
                if filename.endswith('.vglib'):
                    self._parse_library_file(os.path.join(libraries_dir, filename))
        
        # Also load from packages directory (for project-specific libraries)
        packages_dir = os.path.join(os.path.dirname(__file__), '..', 'packages')
        if os.path.exists(packages_dir):
            for filename in os.listdir(packages_dir):
                if filename.endswith('.vglib'):
                    self._parse_library_file(os.path.join(packages_dir, filename))
    
    def _parse_library_file(self, filepath: str):
        """Parse a .vglib file to extract library structure"""
        try:
            with open(filepath, 'r', encoding='utf-8') as f:
                content = f.read()
            
            library_name = os.path.basename(filepath).replace('.vglib', '')
            
            # Parse library structure using the VG Language grammar
            namespaces = {}
            current_namespace = None
            
            # Remove comments first
            content = re.sub(r'/\*\*.*?\*\*/', '', content, flags=re.DOTALL)  # Doc comments
            content = re.sub(r'/##.*?##/', '', content, flags=re.DOTALL)      # VG block comments
            content = re.sub(r'/#.*?#/', '', content, flags=re.DOTALL)       # Block comments
            content = re.sub(r'##.*?$', '', content, flags=re.MULTILINE)     # Line comments
            
            for line in content.split('\n'):
                line = line.strip()
                
                # Look for namespace declarations: namespace Name {
                namespace_match = re.match(r'namespace\s+(\w+)\s*\{', line)
                if namespace_match:
                    current_namespace = namespace_match.group(1)
                    namespaces[current_namespace] = []
                
                # Look for function declarations: function name(params) {
                func_match = re.match(r'function\s+(\w+)\s*\(', line)
                if func_match and current_namespace:
                    namespaces[current_namespace].append(func_match.group(1))
            
            if namespaces:
                # Override or update standard library definitions with actual parsed data
                if library_name in self.standard_libraries:
                    self.standard_libraries[library_name].update(namespaces)
                else:
                    self.user_libraries[library_name] = namespaces
                
        except Exception as e:
            # Silently ignore library parsing errors
            pass
    
    def _load_user_library_config(self):
        """Load user libraries from JSON configuration"""
        config_path = os.path.join(os.path.dirname(__file__), 'config', 'user_libraries.json')
        if os.path.exists(config_path):
            try:
                with open(config_path, 'r', encoding='utf-8') as f:
                    config = json.load(f)
                
                if 'user_libraries' in config:
                    self.user_libraries.update(config['user_libraries'])
            except Exception as e:
                # Silently ignore configuration loading errors
                pass
    
    def lint_file(self, filepath: str) -> List[LintMessage]:
        """Lint a VG Language file and return list of issues"""
        self.messages = []
        
        try:
            with open(filepath, 'r', encoding='utf-8') as f:
                content = f.read()
            
            # Get project directory from file path
            project_dir = os.path.dirname(filepath)
            
            # Load project-specific libraries from the file's directory
            self._load_project_libraries_config(project_dir)
            self._load_project_libraries(filepath)
            
            lines = content.split('\n')
            self.lint_content(lines, filepath)
            
        except Exception as e:
            self.messages.append(LintMessage(1, 1, 'error', f"Failed to read file: {e}", 'file-error'))
        
        return self.messages
    
    def lint_content(self, lines: List[str], filepath: str = '<string>') -> List[LintMessage]:
        """Lint VG Language content and return list of issues"""
        self.messages = []
        
        # Track state across lines
        in_function = False
        in_comment_block = False
        declared_variables = set()
        used_variables = set()
        brace_stack = []
        self.imported_libraries = set()  # Reset imported libraries
        self.declared_enums = set()  # Reset enums for this file
        self.enum_values = set()  # Reset enum values for this file
        self.declared_structs = set()  # Reset structs for this file
        
        # Pre-scan to collect all function declarations for forward reference support
        function_names = set()
        for line_num, line in enumerate(lines, 1):
            line = line.strip()
            # Look for function declarations
            func_match = re.match(r'^\s*function\s+(\w+)', line)
            if func_match:
                function_names.add(func_match.group(1))
        
        # Track current context
        in_enum_block = False
        brace_depth = 0
        
        for line_num, line in enumerate(lines, 1):
            original_line = line
            line = line.strip()
            
            # Skip empty lines
            if not line:
                continue
            
            # Handle VG Language block comments: /## ##/
            if '/##' in line and '##/' in line:
                # Single line doc comment
                pass
            elif '/##' in line:
                in_comment_block = True
                continue
            elif '##/' in line:
                in_comment_block = False
                continue
            elif in_comment_block:
                continue
            
            # Handle regular block comments: /# #/
            if '/#' in line and '#/' in line:
                # Single line block comment
                pass
            elif '/#' in line:
                in_comment_block = True
                continue
            elif '#/' in line:
                in_comment_block = False
                continue
            elif in_comment_block:
                continue
            
            # Skip VG Language single line comments
            if line.startswith('##'):
                continue
            
            # Remove inline VG Language comments for analysis
            if '##' in line:
                # Find the ## that's not inside a string by scanning character by character
                in_string = False
                quote_char = None
                i = 0
                while i < len(line) - 1:  # -1 because we need to check for ##
                    char = line[i]
                    
                    # Handle string delimiters
                    if char in ['"', "'"]:
                        if i == 0 or line[i-1] != '\\':  # Not escaped
                            if not in_string:
                                in_string = True
                                quote_char = char
                            elif char == quote_char:
                                in_string = False
                                quote_char = None
                    
                    # Check for comment start when not in string
                    elif not in_string and char == '#' and i < len(line) - 1 and line[i+1] == '#':
                        line = line[:i].rstrip()
                        break
                    
                    i += 1
            
            line = line.strip()
            if not line:
                continue
            
            # Track enum context
            if 'enum' in line and '{' in line:
                in_enum_block = True
            if in_enum_block:
                brace_depth += line.count('{') - line.count('}')
                if brace_depth <= 0:
                    in_enum_block = False
                    brace_depth = 0
            
            # Check for import statements first
            self._check_import_statement(line_num, line)
            
            # Check for library declarations
            self._check_library_declaration(line_num, line)
            
            # Check for namespace declarations
            self._check_namespace_declaration(line_num, line)
            
            # Check for enum declarations
            self._check_enum_declaration(line_num, line, in_enum_block)
            
            # Check for struct declarations
            self._check_struct_declaration(line_num, line)
            
            # Check loop constructs
            self._check_for_loop(line_num, line)
            self._check_while_loop(line_num, line)
            self._check_do_while_loop(line_num, line)
            self._check_foreach_loop(line_num, line)
            
            # Check various lint rules
            self._check_line_length(line_num, original_line)
            self._check_trailing_whitespace(line_num, original_line)
            self._check_indentation(line_num, original_line)
            self._check_semicolons(line_num, line, in_enum_block)
            self._check_braces(line_num, line, brace_stack)
            self._check_variable_declaration(line_num, line, declared_variables)
            self._check_function_declaration(line_num, line, function_names, declared_variables)
            self._check_variable_usage(line_num, line, declared_variables, used_variables, function_names)
            self._check_function_calls(line_num, line, function_names)
            self._check_string_quotes(line_num, line)
            self._check_operators(line_num, line)
            self._check_common_mistakes(line_num, line)
        
        # Check for unused variables
        unused_vars = declared_variables - used_variables
        for var in unused_vars:
            self.messages.append(LintMessage(1, 1, 'warning', f"Variable '{var}' declared but never used", 'unused-variable'))
        
        # Check for unclosed braces
        if brace_stack:
            self.messages.append(LintMessage(len(lines), 1, 'error', f"Unclosed braces: {len(brace_stack)} remaining", 'unclosed-braces'))
        
        return self.messages
    
    def _check_import_statement(self, line_num: int, line: str):
        """Check and process import statements according to VG grammar"""
        # Skip lines that don't start with import
        stripped_line = line.strip()
        if not stripped_line.startswith('import '):
            return
        
        # VG Language import syntax: 
        # 1. import IDENTIFIER.IDENTIFIER ('as' IDENTIFIER)? ';'
        # 2. import "path/to/file.vg" ';'  
        # 3. import IDENTIFIER.IDENTIFIER.*;
        # 4. import IDENTIFIER.IDENTIFIER.FUNCTION as ALIAS;
        
        # Wildcard import: import MyLib.MyNamespace.*;
        wildcard_import_match = re.match(r'^\s*import\s+([\w\.]+)\.\*\s*;\s*$', line)
        if wildcard_import_match:
            import_path = wildcard_import_match.group(1)
            parts = import_path.split('.')
            
            if len(parts) >= 2:
                library_name = parts[0]
                namespace_name = parts[1]
                
                # Add to imported libraries and namespaces
                self.imported_libraries.add(library_name)
                self.imported_namespaces.add(namespace_name)
                
                # For wildcard imports, import all functions from that namespace
                if library_name in self.user_libraries:
                    library_data = self.user_libraries[library_name]
                    if namespace_name in library_data:
                        # Add all functions from this namespace to builtin functions
                        namespace_functions = library_data[namespace_name]
                        for func_name in namespace_functions:
                            self.builtin_functions.add(func_name)
                
                # Check if library exists in user-imported libraries only
                if library_name not in self.user_libraries:
                    self.messages.append(LintMessage(line_num, 1, 'error',
                                                   f"Library '{library_name}' not imported. Use the Library Manager to import it first.", 'library-not-imported'))
                
                # Check if namespace exists in library
                elif library_name in self.user_libraries and namespace_name not in self.user_libraries[library_name]:
                    # Try to get namespace info from .vglib file if available
                    vglib_info = self._try_load_vglib_file(library_name)
                    if vglib_info and namespace_name in vglib_info.get('namespaces', {}):
                        # Namespace exists in .vglib file, update user_libraries
                        self.user_libraries[library_name] = vglib_info['namespaces']
                    else:
                        self.messages.append(LintMessage(line_num, 1, 'error',
                                                       f"Unknown namespace '{namespace_name}' in library '{library_name}'", 'unknown-namespace'))
            else:
                self.messages.append(LintMessage(line_num, 1, 'error',
                                               "Invalid wildcard import: must be in format 'import Library.Namespace.*;'", 'invalid-import-syntax'))
            return
        
        # Library import: import MyLib.MyNamespace as Alias; OR import MyLib.MyNamespace.Function as Alias;
        lib_import_match = re.match(r'^\s*import\s+([\w\.]+)(?:\s+as\s+(\w+))?\s*;\s*$', line)
        if lib_import_match:
            import_path = lib_import_match.group(1)
            alias = lib_import_match.group(2)
            
            parts = import_path.split('.')
            if len(parts) >= 2:
                library_name = parts[0]
                namespace_name = parts[1]
                
                # Add to imported libraries
                self.imported_libraries.add(library_name)
                self.imported_namespaces.add(namespace_name)
                
                if alias:
                    self.imported_namespaces.add(alias)
                
                # Check if library exists in user-imported libraries only
                if library_name not in self.user_libraries:
                    self.messages.append(LintMessage(line_num, 1, 'error',
                                                   f"Library '{library_name}' not imported. Use the Library Manager to import it first.", 'library-not-imported'))
                
                # Check if namespace exists in library
                elif library_name in self.user_libraries:
                    # Try to get namespace info from .vglib file if available
                    vglib_info = self._try_load_vglib_file(library_name)
                    if vglib_info:
                        # Use .vglib info for validation
                        if namespace_name not in vglib_info.get('namespaces', {}):
                            self.messages.append(LintMessage(line_num, 1, 'error',
                                                           f"Unknown namespace '{namespace_name}' in library '{library_name}'", 'unknown-namespace'))
                    elif namespace_name not in self.user_libraries[library_name]:
                        self.messages.append(LintMessage(line_num, 1, 'error',
                                                       f"Unknown namespace '{namespace_name}' in library '{library_name}'", 'unknown-namespace'))
                
            else:
                self.messages.append(LintMessage(line_num, 1, 'error',
                                               "Invalid import: must be in format 'import Library.Namespace;' or 'import Library.Namespace as Alias;'", 'invalid-import-syntax'))
            return
        
        # File import: import "path/to/file.vg";
        file_import_match = re.match(r'^\s*import\s+"([^"]+)"\s*;\s*$', line)
        if file_import_match:
            file_path = file_import_match.group(1)
            # Check if file exists (relative to current file)
            # For now, just mark as valid import
            return
        
        # If we get here, the import statement doesn't match any valid pattern
        # Check for common mistakes
        if not stripped_line.endswith(';'):
            self.messages.append(LintMessage(line_num, len(stripped_line) + 1, 'error',
                                           "Import statement missing semicolon", 'missing-semicolon'))
        elif 'import ;' in stripped_line:
            self.messages.append(LintMessage(line_num, 1, 'error',
                                           "Empty import statement", 'invalid-import-syntax'))
        elif re.match(r'^\s*import\s+\w+\s*;\s*$', line):  # Single identifier
            self.messages.append(LintMessage(line_num, 1, 'error',
                                           "Invalid import: must specify library and namespace (e.g., 'import Library.Namespace;')", 'invalid-import-syntax'))
        else:
            self.messages.append(LintMessage(line_num, 1, 'error',
                                           "Invalid import syntax. Valid formats: 'import Library.Namespace;', 'import Library.Namespace.*;', 'import Library.Namespace as Alias;'", 'invalid-import-syntax'))
    
    def _check_line_length(self, line_num: int, line: str):
        """Check for overly long lines"""
        if len(line) > 120:
            self.messages.append(LintMessage(line_num, len(line), 'warning', 
                                           f"Line too long ({len(line)} characters, max 120)", 'line-length'))
    
    def _check_trailing_whitespace(self, line_num: int, line: str):
        """Check for trailing whitespace"""
        if line.rstrip() != line and line.strip():
            self.messages.append(LintMessage(line_num, len(line.rstrip()) + 1, 'info',
                                           "Trailing whitespace", 'trailing-whitespace'))
    
    def _check_indentation(self, line_num: int, line: str):
        """Check for inconsistent indentation"""
        if line.strip() and line.startswith(' '):
            # Count leading spaces
            spaces = len(line) - len(line.lstrip(' '))
            if spaces % 4 != 0:
                self.messages.append(LintMessage(line_num, 1, 'warning',
                                               f"Inconsistent indentation: {spaces} spaces (use multiples of 4)", 'indentation'))
    
    def _check_semicolons(self, line_num: int, line: str, in_enum_block: bool = False):
        """Check for missing semicolons"""
        original_line = line
        line = line.strip()
        
        # Skip certain lines that don't need semicolons
        if (line.endswith('{') or line.endswith('}') or 
            line.endswith('[') or line.endswith(']') or  # Array literals
            line.endswith(',') or  # Array/object elements
            line.endswith('(') or  # Multi-line function calls/expressions
            line.endswith('=') or  # Multi-line assignments
            line.endswith('+') or line.endswith('-') or line.endswith('*') or line.endswith('/') or  # Multi-line operations
            line.startswith('if') or line.startswith('else') or
            line.startswith('while') or line.startswith('for') or
            line.startswith('function') or line.startswith('struct') or
            line.startswith('enum') or line.startswith('do') or
            line.startswith('##') or  # VG Language comments
            not line):  # Empty lines
            return
        
        # Skip array/object elements that end with comma
        if re.match(r'^\s*".*",?\s*$', line) or re.match(r'^\s*\w+\s*,\s*$', line):
            return
        
        # Skip enum values entirely when inside enum block
        if in_enum_block:
            # Lines inside enum blocks don't need semicolons (they use commas)
            if re.match(r'^\s*\w+(\s*=\s*.*?)?\s*,?\s*$', line):
                return
        
        # Skip enum values outside of explicit enum context (backward compatibility)
        if re.match(r'^\s*\w+(\s*=\s*.*?)?\s*,?\s*$', line) and not ('var ' in line or 'const ' in line):
            # This could be an enum value, skip semicolon checking
            return
        
        # Skip lines that are clearly continuation lines (indented parameters, etc.)
        if re.match(r'^\s+\w+\s*\*?\s*[A-Z_][A-Z_0-9]*', line):  # Indented parameters
            return
        
        # Check if line should end with semicolon - enhanced detection
        var_match = re.match(r'^\s*(var|const|return|break|continue)', line)
        import_match = re.match(r'^\s*import\s+', line)
        has_assignment = ('=' in line and not line.endswith(','))
        func_call_match = re.match(r'^\s*\w+\s*\(.*\)\s*$', line)  # Function calls
        
        # Additional patterns that need semicolons:
        expression_stmt = re.match(r'^\s*\w+(\s*[\+\-\*\/\%]\s*\w+)*\s*$', line)  # Simple expressions
        array_access = re.match(r'^\s*\w+\[\w+\]\s*(=.*)?$', line)  # Array access/assignment
        property_access = re.match(r'^\s*\w+\.\w+.*$', line)  # Property access
        increment_decrement = re.match(r'^\s*\w+(\+\+|--)\s*$', line)  # i++, i--
        assignment_op = re.match(r'^\s*\w+\s*[\+\-\*\/\%]?=', line)  # +=, -=, *=, /=, %=
        
        # Exclude function declarations (they don't need semicolons)
        is_function_decl = re.match(r'^\s*function\s+\w+\s*\(.*\)\s*\{?', line)
        
        if ((var_match or import_match or has_assignment or func_call_match or expression_stmt or 
            array_access or property_access or increment_decrement or assignment_op) and 
            not is_function_decl):
            if not line.endswith(';'):
                self.messages.append(LintMessage(line_num, len(line) + 1, 'error',
                                               "Missing semicolon", 'missing-semicolon'))
    
    def _check_braces(self, line_num: int, line: str, brace_stack: List[int]):
        """Check for balanced braces"""
        for char in line:
            if char == '{':
                brace_stack.append(line_num)
            elif char == '}':
                if not brace_stack:
                    self.messages.append(LintMessage(line_num, line.index(char) + 1, 'error',
                                                   "Unmatched closing brace", 'unmatched-brace'))
                else:
                    brace_stack.pop()
    
    def _check_variable_declaration(self, line_num: int, line: str, declared_variables: set):
        """Check variable declarations"""
        # Check for for-loop variable declarations: for(var i = 0; ...)
        for_var_match = re.search(r'for\s*\(\s*(var|const)\s+(\w+)', line)
        if for_var_match:
            var_name = for_var_match.group(2)
            # For-loop variables are allowed to redeclare existing variables in their scope
            declared_variables.add(var_name)
            
            # Check naming convention
            if not re.match(r'^[a-z][a-zA-Z0-9]*$', var_name):
                self.messages.append(LintMessage(line_num, 1, 'info',
                                               f"Variable '{var_name}' should use camelCase", 'naming-convention'))
            return
        
        # Match multiple variable declarations in a single line: var x = 40; var y = 50; var z = 40;
        # Split line by semicolon and process each declaration
        statements = [stmt.strip() for stmt in line.split(';') if stmt.strip()]
        for stmt in statements:
            var_match = re.match(r'^\s*(var|const)\s+(\w+)', stmt)
            if var_match:
                var_name = var_match.group(2)
                
                # Check for complete variable declaration syntax: var name = value;
                # Must have assignment operator and value
                complete_var_match = re.match(r'^\s*(var|const)\s+(\w+)\s*=\s*(.+)$', stmt)
                if not complete_var_match:
                    # Check if it's just the variable name without assignment
                    incomplete_var_match = re.match(r'^\s*(var|const)\s+(\w+)\s*$', stmt)
                    if incomplete_var_match:
                        self.messages.append(LintMessage(line_num, 1, 'error',
                                                       f"Incomplete variable declaration '{stmt}'. Missing assignment operator and value", 'incomplete-variable-declaration'))
                    else:
                        # Check if it has assignment operator but missing value
                        missing_value_match = re.match(r'^\s*(var|const)\s+(\w+)\s*=\s*$', stmt)
                        if missing_value_match:
                            self.messages.append(LintMessage(line_num, 1, 'error',
                                                           f"Incomplete variable declaration '{stmt}'. Missing value after assignment operator", 'incomplete-variable-declaration'))
                    return  # Don't process further if syntax is incomplete
                
                if var_name in declared_variables:
                    self.messages.append(LintMessage(line_num, 1, 'warning',
                                                   f"Variable '{var_name}' already declared", 'redeclared-variable'))
                else:
                    declared_variables.add(var_name)
                # Check naming convention
                if not re.match(r'^[a-z][a-zA-Z0-9]*$', var_name):
                    self.messages.append(LintMessage(line_num, 1, 'info',
                                                   f"Variable '{var_name}' should use camelCase", 'naming-convention'))
    
    def _check_variable_usage(self, line_num: int, line: str, declared_variables: set, used_variables: set, function_names: set):
        """Check variable usage"""
        # Remove string literals to avoid false positives
        line_without_strings = self._remove_string_literals(line)
        
        # Special handling for for-loop variable declarations
        # Extract variables declared in this line first
        line_declared_vars = set()
        
        # Check for for-loop variable declarations: for(var i = 0; ...)
        for_var_match = re.search(r'for\s*\(\s*(var|const)\s+(\w+)', line_without_strings)
        if for_var_match:
            line_declared_vars.add(for_var_match.group(2))
        
        # Check for regular variable declarations: var name = value;
        var_match = re.match(r'^\s*(var|const)\s+(\w+)', line_without_strings)
        if var_match:
            line_declared_vars.add(var_match.group(2))
        
        # Check for catch parameter declarations: catch (e) {
        catch_match = re.search(r'catch\s*\(\s*(\w+)\s*\)', line_without_strings)
        if catch_match:
            catch_param = catch_match.group(1)
            line_declared_vars.add(catch_param)
            declared_variables.add(catch_param)  # Also add to global scope
        
        # Find all identifiers in the line (excluding those in strings)
        identifiers = re.findall(r'\b([a-zA-Z_]\w*)\b', line_without_strings)
        
        for identifier in identifiers:
            if identifier not in self.keywords:
                used_variables.add(identifier)
                
                # Skip if this identifier is declared in the same line
                if identifier in line_declared_vars:
                    continue
                
                # Check if variable is used before declaration
                if (identifier not in declared_variables and 
                    identifier not in self.imported_libraries and 
                    identifier not in self.imported_namespaces and
                    identifier not in self.builtin_functions and
                    identifier not in self.declared_enums and
                    identifier not in self.enum_values and
                    identifier not in self.declared_structs and
                    identifier not in function_names and
                    not identifier.startswith(('function', 'struct', 'enum', 'class'))):
                    
                    # Check if it's a qualified identifier (Namespace.function)
                    if self._is_valid_qualified_identifier(line_without_strings, identifier):
                        continue
                    
                    # Check if it's a library function
                    if self._is_library_function(identifier):
                        continue
                    
                    self.messages.append(LintMessage(line_num, 1, 'error',
                                                   f"Variable '{identifier}' used before declaration", 'undefined-variable'))
    
    def _remove_string_literals(self, line: str) -> str:
        """Remove string literals from a line to avoid parsing their contents"""
        # Remove double-quoted strings
        line = re.sub(r'"[^"]*"', '""', line)
        # Remove single-quoted strings (if VG supports them)
        line = re.sub(r"'[^']*'", "''", line)
        return line
    
    def _is_valid_qualified_identifier(self, line: str, identifier: str) -> bool:
        """Check if identifier is part of a valid qualified identifier"""
        # Look for patterns like Namespace.function or Library.Namespace.function
        qualified_pattern = rf'\b\w+\.{re.escape(identifier)}\b'
        return bool(re.search(qualified_pattern, line))
    
    def _is_library_function(self, identifier: str) -> bool:
        """Check if identifier is a known library function"""
        # Check standard libraries
        for lib_name, namespaces in self.standard_libraries.items():
            if lib_name in self.imported_libraries:
                for namespace_name, functions in namespaces.items():
                    if namespace_name in self.imported_namespaces and identifier in functions:
                        return True
        
        # Check user libraries
        for lib_name, namespaces in self.user_libraries.items():
            if lib_name in self.imported_libraries:
                for namespace_name, functions in namespaces.items():
                    if namespace_name in self.imported_namespaces and identifier in functions:
                        return True
        
        return False
    
    def _check_library_declaration(self, line_num: int, line: str):
        """Check library declarations"""
        lib_match = re.match(r'^\s*library\s+(\w+)\s*\{', line)
        if lib_match:
            library_name = lib_match.group(1)
            
            # Check naming convention
            if not library_name[0].isupper():
                self.messages.append(LintMessage(line_num, 1, 'info',
                                               f"Library '{library_name}' should start with uppercase", 'naming-convention'))
    
    def _check_namespace_declaration(self, line_num: int, line: str):
        """Check namespace declarations"""
        namespace_match = re.match(r'^\s*namespace\s+(\w+)\s*\{', line)
        if namespace_match:
            namespace_name = namespace_match.group(1)
            
            # Check naming convention
            if not namespace_name[0].isupper():
                self.messages.append(LintMessage(line_num, 1, 'info',
                                               f"Namespace '{namespace_name}' should start with uppercase", 'naming-convention'))
    
    def _check_enum_declaration(self, line_num: int, line: str, in_enum_block: bool = False):
        """Check enum declarations and track enum values"""
        # Check for enum declaration: enum EnumName { VALUE1, VALUE2, ... }
        enum_match = re.match(r'^\s*enum\s+(\w+)\s*\{(.*)?\}?', line)
        if enum_match:
            enum_name = enum_match.group(1)
            enum_body = enum_match.group(2) or ""
            
            # Add enum to declared enums
            self.declared_enums.add(enum_name)
            
            # Check naming convention for enum
            if not enum_name[0].isupper():
                self.messages.append(LintMessage(line_num, 1, 'info',
                                               f"Enum '{enum_name}' should start with uppercase", 'naming-convention'))
            
            # Parse enum values if they're on the same line
            if enum_body:
                self._parse_enum_values(enum_body, line_num)
            return
        
        # Check for enum values on separate lines (inside enum block)
        if in_enum_block:
            # Look for standalone identifiers that could be enum values
            enum_value_match = re.match(r'^\s*(\w+)(?:\s*=\s*.*?)?\s*,?\s*$', line)
            if enum_value_match:
                potential_enum_value = enum_value_match.group(1)
                if potential_enum_value not in self.keywords:
                    self.enum_values.add(potential_enum_value)
            else:
                # Check for comma-separated enum values: RED, GREEN, BLUE
                # Remove any assignments and split by comma
                values_line = re.sub(r'\s*=\s*[^,]*', '', line)  # Remove assignments
                values = [v.strip() for v in values_line.split(',') if v.strip()]
                for value in values:
                    # Extract just the identifier (in case of complex assignments)
                    value_match = re.match(r'^(\w+)', value)
                    if value_match:
                        enum_value = value_match.group(1)
                        if enum_value not in self.keywords and enum_value != '}':
                            self.enum_values.add(enum_value)
    
    def _parse_enum_values(self, enum_body: str, line_num: int):
        """Parse enum values from enum body"""
        # Remove any incomplete parts and split by comma
        values = re.findall(r'(\w+)(?:\s*=\s*[^,]*)?', enum_body)
        for value in values:
            self.enum_values.add(value)
            
            # Check naming convention for enum values
            if not value.isupper():
                self.messages.append(LintMessage(line_num, 1, 'info',
                                               f"Enum value '{value}' should be UPPERCASE", 'naming-convention'))
    
    def _check_struct_declaration(self, line_num: int, line: str):
        """Check struct declarations"""
        struct_match = re.match(r'^\s*struct\s+(\w+)\s*\{', line)
        if struct_match:
            struct_name = struct_match.group(1)
            self.declared_structs.add(struct_name)
            
            # Check naming convention
            if not struct_name[0].isupper():
                self.messages.append(LintMessage(line_num, 1, 'info',
                                               f"Struct '{struct_name}' should start with uppercase", 'naming-convention'))
    
    def add_user_library(self, library_name: str, namespaces: dict):
        """Add a user-defined library to the linter"""
        self.user_libraries[library_name] = namespaces
    
    def add_user_library_namespace(self, library_name: str, namespace_name: str, functions: list):
        """Add a namespace to an existing user library or create new library"""
        if library_name not in self.user_libraries:
            self.user_libraries[library_name] = {}
        self.user_libraries[library_name][namespace_name] = functions
    
    def remove_user_library(self, library_name: str):
        """Remove a user-defined library"""
        if library_name in self.user_libraries:
            del self.user_libraries[library_name]
    
    def save_user_libraries(self, project_path: str = None):
        """Save user libraries to project-specific JSON configuration file"""
        if project_path:
            config_path = os.path.join(project_path, 'vg_project_libraries.json')
        else:
            config_path = os.path.join(os.getcwd(), 'vg_project_libraries.json')
        
        config = {"project_libraries": self.user_libraries}
        
        try:
            with open(config_path, 'w', encoding='utf-8') as f:
                json.dump(config, f, indent=2)
            return True
        except Exception as e:
            return False
    
    def validate_library_structure(self, library_data: dict) -> bool:
        """Validate that library data has correct structure"""
        if not isinstance(library_data, dict):
            return False
        
        for namespace_name, functions in library_data.items():
            if not isinstance(namespace_name, str) or not isinstance(functions, list):
                return False
            
            for func_name in functions:
                if not isinstance(func_name, str):
                    return False
        
        return True
    
    def import_library_from_vglib(self, vglib_path: str, project_path: str = None) -> bool:
        """Import a library from a .vglib file and add it to user libraries"""
        try:
            from vg_library_parser import VGLibraryParser
            parser = VGLibraryParser()
            
            # Parse the .vglib file using the proper parser
            result = parser.parse_vglib_file(vglib_path)
            if not result:
                return False
                
            library_name = result.get('library_name')
            namespaces = result.get('namespaces', {})
            
            if library_name and namespaces:
                # Add to user libraries
                self.user_libraries[library_name] = namespaces
                
                # Save to configuration with proper project path
                return self.save_user_libraries(project_path)
            
            return False
        except Exception as e:
            return False
    
    def get_library_info(self, library_name: str) -> dict:
        """Get information about a specific library (only user-imported libraries)"""
        info = {
            "exists": False,
            "type": None,
            "namespaces": {}
        }
        
        # Only check user-imported libraries (from project configuration)
        # Do not automatically allow standard libraries - they must be explicitly imported
        if library_name in self.user_libraries:
            # First try to get up-to-date info from .vglib files
            vglib_info = self._try_load_vglib_file(library_name)
            if vglib_info:
                info["exists"] = True
                info["type"] = "vglib"
                info["namespaces"] = vglib_info["namespaces"]
            else:
                # Fall back to project configuration
                info["exists"] = True
                info["type"] = "user"
                info["namespaces"] = self.user_libraries[library_name]
        
        return info
    
    def get_available_libraries(self) -> dict:
        """Get all available libraries (standard + user-defined)"""
        all_libraries = {}
        all_libraries.update(self.standard_libraries)
        all_libraries.update(self.user_libraries)
        return all_libraries
    
    def _try_load_vglib_file(self, library_name: str) -> dict:
        """Try to load and parse a .vglib file for the given library"""
        try:
            from vg_library_parser import VGLibraryParser
            parser = VGLibraryParser()
            
            # Get file path safely
            file_path = getattr(self, 'file_path', None)
            
            # Search for .vglib files in multiple locations
            search_paths = [
                os.path.join(os.path.dirname(file_path or ''), 'packages'),
                os.path.join(os.path.dirname(__file__), 'packages'),
                os.path.join(os.path.dirname(__file__), '..', 'libraries'),
                os.path.join(os.path.dirname(__file__), '..', 'packages'),
                'packages',
                'libraries'
            ]
            
            for search_path in search_paths:
                vglib_path = os.path.join(search_path, f"{library_name}.vglib")
                if os.path.exists(vglib_path):
                    result = parser.parse_vglib_file(vglib_path)
                    if result and result.get('library_name') == library_name:
                        return result
            
            return None
        except Exception:
            return None
    
    def get_summary(self) -> str:
        """Get a summary of the linting results"""
        if not self.messages:
            return "✅ No issues found! Code looks good."
        
        errors = sum(1 for msg in self.messages if msg.severity == 'error')
        warnings = sum(1 for msg in self.messages if msg.severity == 'warning')
        info = sum(1 for msg in self.messages if msg.severity == 'info')
        
        summary_parts = []
        if errors > 0:
            summary_parts.append(f"{errors} error{'s' if errors != 1 else ''}")
        if warnings > 0:
            summary_parts.append(f"{warnings} warning{'s' if warnings != 1 else ''}")
        if info > 0:
            summary_parts.append(f"{info} info message{'s' if info != 1 else ''}")
        
        return f"Found {', '.join(summary_parts)}"
    
    def _check_function_declaration(self, line_num: int, line: str, function_names: set, declared_variables: set):
        """Check function declarations and extract parameters"""
        
        # First, check for invalid function syntax patterns
        self._check_invalid_function_syntax(line_num, line)
        
        # Valid function pattern: function name(params) {
        func_match = re.match(r'^\s*function\s+(\w+)\s*\((.*?)\)', line)
        if func_match:
            func_name = func_match.group(1)
            params_str = func_match.group(2).strip()
            
            # Function name was already added in pre-scan, but check for duplicates in actual declaration
            # Note: We don't add to function_names here since it's pre-populated
            
            # Validate parameter syntax
            self._validate_function_parameters(line_num, params_str)
            
            # Extract parameters and add to declared variables
            if params_str:
                # Split parameters by comma and extract parameter names
                params = [p.strip() for p in params_str.split(',') if p.strip()]
                for param in params:
                    # Handle typed parameters like "param: type" or just "param"
                    param_match = re.match(r'^(\w+)(?:\s*:\s*\w+)?', param)
                    if param_match:
                        param_name = param_match.group(1)
                        declared_variables.add(param_name)
            
            # Check naming convention
            if not re.match(r'^[a-z][a-zA-Z0-9]*$', func_name):
                self.messages.append(LintMessage(line_num, 1, 'info',
                                               f"Function '{func_name}' should use camelCase", 'naming-convention'))
            
            # Check if function declaration has opening brace
            if not '{' in line:
                self.messages.append(LintMessage(line_num, len(line) + 1, 'error',
                                               "Function declaration missing opening brace '{'", 'invalid-function-syntax'))
            return
        
        # Also check for shortened function syntax without full match
        func_short_match = re.match(r'^\s*function\s+(\w+)', line)
        if func_short_match:
            func_name = func_short_match.group(1)
            # Check naming convention
            if not re.match(r'^[a-z][a-zA-Z0-9]*$', func_name):
                self.messages.append(LintMessage(line_num, 1, 'info',
                                               f"Function '{func_name}' should use camelCase", 'naming-convention'))
    
    def _validate_function_parameters(self, line_num: int, params_str: str):
        """Validate function parameter syntax"""
        if not params_str:
            return  # Empty parameter list is valid
        
        # Check for trailing comma: function a(ab,)
        if params_str.endswith(','):
            self.messages.append(LintMessage(line_num, 1, 'error',
                                           "Invalid parameter list: trailing comma not allowed", 'invalid-parameter-syntax'))
            return
        
        # Check for leading comma: function a(,ab)
        if params_str.startswith(','):
            self.messages.append(LintMessage(line_num, 1, 'error',
                                           "Invalid parameter list: leading comma not allowed", 'invalid-parameter-syntax'))
            return
        
        # Check for consecutive commas: function a(ab,,cd)
        if ',,' in params_str:
            self.messages.append(LintMessage(line_num, 1, 'error',
                                           "Invalid parameter list: consecutive commas not allowed", 'invalid-parameter-syntax'))
            return
        
        # Check for comma without whitespace followed by parameter: function a(ab,cd)
        # This is actually valid, but let's check for other invalid patterns
        
        # Split by comma and validate each parameter
        params = [p.strip() for p in params_str.split(',')]
        for i, param in enumerate(params):
            if not param:  # Empty parameter after split
                self.messages.append(LintMessage(line_num, 1, 'error',
                                               "Invalid parameter list: empty parameter not allowed", 'invalid-parameter-syntax'))
                continue
            
            # Check parameter naming pattern
            if not re.match(r'^[a-zA-Z_]\w*(?:\s*:\s*\w+)?$', param):
                self.messages.append(LintMessage(line_num, 1, 'error',
                                               f"Invalid parameter '{param}': must be a valid identifier", 'invalid-parameter-syntax'))
    
    def _check_invalid_function_syntax(self, line_num: int, line: str):
        """Check for invalid function syntax patterns"""
        stripped_line = line.strip()
        
        # Check for anonymous functions: function () { or function(){
        if re.match(r'^\s*function\s*\(', stripped_line):
            self.messages.append(LintMessage(line_num, 1, 'error',
                                           "Anonymous functions are not allowed. Function must have a name: 'function functionName() {'", 'invalid-function-syntax'))
            return
        
        # Check for function keyword without proper syntax
        if stripped_line.startswith('function '):
            # Check for missing parentheses: function name {
            if re.match(r'^\s*function\s+\w+\s*\{', stripped_line):
                self.messages.append(LintMessage(line_num, 1, 'error',
                                               "Function declaration missing parentheses. Use: 'function name() {'", 'invalid-function-syntax'))
                return
            
            # Check for malformed function: function name( without closing paren
            if re.match(r'^\s*function\s+\w+\s*\([^)]*$', stripped_line) and ')' not in stripped_line:
                self.messages.append(LintMessage(line_num, 1, 'error',
                                               "Function declaration missing closing parenthesis ')'", 'invalid-function-syntax'))
                return
            
            # Check for function keyword alone or with invalid identifier
            if re.match(r'^\s*function\s*$', stripped_line):
                self.messages.append(LintMessage(line_num, 1, 'error',
                                               "Incomplete function declaration. Use: 'function functionName() {'", 'invalid-function-syntax'))
                return
            
            # Check for function with invalid name (starts with number, special chars, etc.)
            func_name_match = re.match(r'^\s*function\s+([^\s\(]+)', stripped_line)
            if func_name_match:
                func_name = func_name_match.group(1)
                if not re.match(r'^[a-zA-Z_][a-zA-Z0-9_]*$', func_name):
                    self.messages.append(LintMessage(line_num, 1, 'error',
                                                   f"Invalid function name '{func_name}'. Function names must start with a letter or underscore", 'invalid-function-syntax'))
                    return
    
    def _check_function_calls(self, line_num: int, line: str, function_names: set):
        """Check function calls"""
        # Remove string literals to avoid false positives
        line_without_strings = self._remove_string_literals(line)
        
        # Find function calls: identifier(args) or Namespace.identifier(args)
        func_calls = re.findall(r'\b(?:(\w+)\.)?(\w+)\s*\(', line_without_strings)
        
        for namespace, func_name in func_calls:
            # Skip if it's a built-in function
            if func_name in self.builtin_functions:
                continue
            
            # Skip if it's a declared function
            if func_name in function_names:
                continue
            
            # Skip keywords
            if func_name in self.keywords:
                continue
            
            # Check if it's a valid library function call
            if namespace and self._is_valid_library_call(namespace, func_name):
                continue
            
            # Check if it's a valid imported library function
            if self._is_imported_library_function(func_name):
                continue
            
            self.messages.append(LintMessage(line_num, 1, 'warning',
                                           f"Function '{func_name}' called but not defined", 'undefined-function'))
    
    def _is_valid_library_call(self, namespace: str, func_name: str) -> bool:
        """Check if namespace.function is a valid library call"""
        if namespace in self.imported_namespaces:
            # Check standard libraries
            for lib_name, namespaces in self.standard_libraries.items():
                if lib_name in self.imported_libraries and namespace in namespaces:
                    if func_name in namespaces[namespace]:
                        return True
            
            # Check user libraries
            for lib_name, namespaces in self.user_libraries.items():
                if lib_name in self.imported_libraries and namespace in namespaces:
                    if func_name in namespaces[namespace]:
                        return True
        
        return False
    
    def _is_imported_library_function(self, func_name: str) -> bool:
        """Check if function is from an imported library"""
        # Check if any imported namespace contains this function
        for lib_name, namespaces in self.standard_libraries.items():
            if lib_name in self.imported_libraries:
                for ns_name, functions in namespaces.items():
                    if ns_name in self.imported_namespaces and func_name in functions:
                        return True
        
        for lib_name, namespaces in self.user_libraries.items():
            if lib_name in self.imported_libraries:
                for ns_name, functions in namespaces.items():
                    if ns_name in self.imported_namespaces and func_name in functions:
                        return True
        
        return False
    
    def _check_string_quotes(self, line_num: int, line: str):
        """Check string quote consistency"""
        # Remove VG Language comments first to avoid false positives
        line_without_comments = line
        
        # Remove VG Language single-line comments (##)
        comment_pos = line.find('##')
        if comment_pos != -1:
            # Make sure ## is not inside a string
            before_comment = line[:comment_pos]
            # Count unescaped quotes before the comment
            in_string = False
            quote_char = None
            for i, char in enumerate(before_comment):
                if char in ['"', "'"]:
                    if i == 0 or before_comment[i-1] != '\\':
                        if not in_string:
                            in_string = True
                            quote_char = char
                        elif char == quote_char:
                            in_string = False
                            quote_char = None
            
            # Only remove comment if we're not inside a string
            if not in_string:
                line_without_comments = before_comment.strip()
        
        # Skip quote checking for lines that are likely part of multi-line constructs
        # if the line ends with a comma or contains array/object syntax
        if line_without_comments.rstrip().endswith(',') or '[' in line_without_comments or line_without_comments.strip() == '':
            return
        
        # Check for unmatched quotes in the remaining line
        single_quotes = line_without_comments.count("'") - line_without_comments.count("\\'")
        double_quotes = line_without_comments.count('"') - line_without_comments.count('\\"')
        
        # Only report unmatched quotes if there's an actual issue and it's not a multi-line construct
        if single_quotes % 2 != 0 and "'" in line_without_comments:
            self.messages.append(LintMessage(line_num, 1, 'error',
                                           "Unmatched single quotes", 'unmatched-quotes'))
        if double_quotes % 2 != 0 and '"' in line_without_comments:
            self.messages.append(LintMessage(line_num, 1, 'error',
                                           "Unmatched double quotes", 'unmatched-quotes'))
    
    def _check_operators(self, line_num: int, line: str):
        """Check operator spacing and usage"""
        # Check for spacing around operators
        operators = ['=', '==', '!=', '<', '>', '<=', '>=', '+', '-', '*', '/', '%']
        for op in operators:
            if op in line:
                # Simple check for spacing (basic heuristic)
                pattern = f'\\w{re.escape(op)}\\w'
                if re.search(pattern, line):
                    self.messages.append(LintMessage(line_num, 1, 'info',
                                                   f"Consider adding spaces around operator '{op}'", 'operator-spacing'))
    
    def _check_for_loop(self, line_num: int, line: str):
        """Check for-loop syntax validation"""
        # Check for for-each syntax first: for (var item : collection)
        if re.search(r'for\s*\([^:)]*:\s*[^)]*\)', line):
            # This is a for-each loop, not a traditional for-loop
            # Let _check_foreach_loop handle it
            return
        
        # Basic for-loop pattern: for (init; condition; update) {
        for_match = re.match(r'^\s*for\s*\(([^)]*)\)\s*\{?', line)
        if for_match:
            for_content = for_match.group(1).strip()
            
            # Check if parentheses are present
            if not line.strip().startswith('for ('):
                if line.strip().startswith('for('):
                    self.messages.append(LintMessage(line_num, 1, 'info',
                                                   "Consider adding space after 'for' keyword", 'spacing'))
                else:
                    self.messages.append(LintMessage(line_num, 1, 'error',
                                                   "For-loop missing parentheses", 'invalid-loop-syntax'))
                    return
            
            # Check if opening brace is present
            if '{' not in line:
                self.messages.append(LintMessage(line_num, len(line) + 1, 'error',
                                               "For-loop missing opening brace '{'", 'invalid-loop-syntax'))
            
            # Validate for-loop structure (init; condition; update)
            if for_content:
                parts = for_content.split(';')
                if len(parts) != 3:
                    self.messages.append(LintMessage(line_num, 1, 'error',
                                                   "For-loop must have exactly three parts separated by semicolons: for (init; condition; update)", 'invalid-loop-syntax'))
                else:
                    # Check each part
                    init_part = parts[0].strip()
                    condition_part = parts[1].strip()
                    update_part = parts[2].strip()
                    
                    # Init part validation (can be empty, variable declaration, or assignment)
                    if init_part and not re.match(r'^\s*(var|const)\s+\w+\s*=|^\s*\w+\s*=', init_part):
                        # Could be a simple identifier or expression
                        if not re.match(r'^\s*\w+\s*$', init_part):
                            self.messages.append(LintMessage(line_num, 1, 'warning',
                                                           "For-loop initialization should be a variable declaration or assignment", 'loop-init-style'))
                    
                    # Condition part should be a boolean expression (can be empty for infinite loop)
                    if condition_part and not self._is_valid_condition(condition_part):
                        self.messages.append(LintMessage(line_num, 1, 'warning',
                                                       "For-loop condition should be a boolean expression", 'loop-condition-style'))
                    
                    # Update part validation (can be empty, assignment, or increment/decrement)
                    if update_part and not re.match(r'^\s*\w+(\+\+|--|[\+\-\*\/\%]?=)', update_part):
                        # Check for simple identifier (like 'i' which might be missing operator)
                        if re.match(r'^\s*\w+\s*$', update_part):
                            self.messages.append(LintMessage(line_num, 1, 'warning',
                                                           "For-loop update expression incomplete - missing operator (e.g., i++, i += 1)", 'loop-update-style'))
            else:
                self.messages.append(LintMessage(line_num, 1, 'error',
                                               "Empty for-loop parentheses", 'invalid-loop-syntax'))
            return
        
        # Check for incomplete for-loop syntax
        if line.strip().startswith('for ') and '(' not in line:
            self.messages.append(LintMessage(line_num, 1, 'error',
                                           "Incomplete for-loop: missing parentheses", 'invalid-loop-syntax'))
        elif line.strip().startswith('for(') and ')' not in line:
            self.messages.append(LintMessage(line_num, 1, 'error',
                                           "Incomplete for-loop: missing closing parenthesis", 'invalid-loop-syntax'))
    
    def _check_foreach_loop(self, line_num: int, line: str):
        """Check for-each loop syntax validation"""
        # For-each pattern: for (var item : collection) {
        foreach_match = re.match(r'^\s*for\s*\(\s*(var|const)?\s*(\w+)\s*:\s*([^)]+)\)\s*\{?', line)
        if foreach_match:
            var_keyword = foreach_match.group(1)
            item_var = foreach_match.group(2)
            collection = foreach_match.group(3).strip()
            
            # Check if var keyword is present (recommended)
            if not var_keyword:
                self.messages.append(LintMessage(line_num, 1, 'warning',
                                               "For-each loop should declare variable with 'var' keyword", 'loop-variable-declaration'))
            
            # Check if opening brace is present
            if '{' not in line:
                self.messages.append(LintMessage(line_num, len(line) + 1, 'error',
                                               "For-each loop missing opening brace '{'", 'invalid-loop-syntax'))
            
            # Check variable naming convention
            if not re.match(r'^[a-z][a-zA-Z0-9]*$', item_var):
                self.messages.append(LintMessage(line_num, 1, 'info',
                                               f"For-each variable '{item_var}' should use camelCase", 'naming-convention'))
            
            # Basic collection validation
            if not collection:
                self.messages.append(LintMessage(line_num, 1, 'error',
                                               "For-each loop missing collection expression", 'invalid-loop-syntax'))
            
            return
        
        # Check if it's a for-each attempt but with wrong syntax
        if re.search(r'for\s*\([^)]*:\s*[^)]*\)', line):
            # This looks like a for-each but didn't match the pattern above
            if 'var ' not in line and 'const ' not in line:
                self.messages.append(LintMessage(line_num, 1, 'error',
                                               "For-each loop must declare variable: for (var item : collection)", 'invalid-loop-syntax'))
    
    def _check_while_loop(self, line_num: int, line: str):
        """Check while-loop syntax validation"""
        # While-loop pattern: while (condition) {
        while_match = re.match(r'^\s*while\s*\(([^)]*)\)\s*\{?', line)
        if while_match:
            condition = while_match.group(1).strip()
            
            # Check if parentheses are present and properly spaced
            if not line.strip().startswith('while ('):
                if line.strip().startswith('while('):
                    self.messages.append(LintMessage(line_num, 1, 'info',
                                                   "Consider adding space after 'while' keyword", 'spacing'))
                else:
                    self.messages.append(LintMessage(line_num, 1, 'error',
                                                   "While-loop missing parentheses", 'invalid-loop-syntax'))
                    return
            
            # Check if opening brace is present
            if '{' not in line:
                self.messages.append(LintMessage(line_num, len(line) + 1, 'error',
                                               "While-loop missing opening brace '{'", 'invalid-loop-syntax'))
            
            # Check condition
            if not condition:
                self.messages.append(LintMessage(line_num, 1, 'error',
                                               "While-loop missing condition", 'invalid-loop-syntax'))
            elif not self._is_valid_condition(condition):
                self.messages.append(LintMessage(line_num, 1, 'warning',
                                               "While-loop condition should be a boolean expression", 'loop-condition-style'))
            
            # Check for potential infinite loops
            if condition == 'true' or condition == '1':
                self.messages.append(LintMessage(line_num, 1, 'info',
                                               "Potential infinite loop - ensure there's a break condition inside", 'infinite-loop-warning'))
            return
        
        # Check for incomplete while-loop syntax
        if line.strip().startswith('while ') and '(' not in line:
            self.messages.append(LintMessage(line_num, 1, 'error',
                                           "Incomplete while-loop: missing parentheses", 'invalid-loop-syntax'))
        elif line.strip().startswith('while(') and ')' not in line:
            self.messages.append(LintMessage(line_num, 1, 'error',
                                           "Incomplete while-loop: missing closing parenthesis", 'invalid-loop-syntax'))
    
    def _check_do_while_loop(self, line_num: int, line: str):
        """Check do-while loop syntax validation"""
        # Do-while opening: do {
        if re.match(r'^\s*do\s*\{', line):
            # Basic syntax check for do block opening
            if not line.strip().startswith('do {'):
                if line.strip().startswith('do{'):
                    self.messages.append(LintMessage(line_num, 1, 'info',
                                                   "Consider adding space after 'do' keyword", 'spacing'))
            return
        
        # Do-while closing: } while (condition);
        while_closing_match = re.match(r'^\s*\}\s*while\s*\(([^)]*)\)\s*;?', line)
        if while_closing_match:
            condition = while_closing_match.group(1).strip()
            
            # Check for proper spacing
            if not re.search(r'\}\s+while\s+\(', line):
                self.messages.append(LintMessage(line_num, 1, 'info',
                                               "Consider proper spacing: '} while ('", 'spacing'))
            
            # Check if semicolon is present
            if not line.rstrip().endswith(';'):
                self.messages.append(LintMessage(line_num, len(line) + 1, 'error',
                                               "Do-while statement missing semicolon", 'missing-semicolon'))
            
            # Check condition
            if not condition:
                self.messages.append(LintMessage(line_num, 1, 'error',
                                               "Do-while loop missing condition", 'invalid-loop-syntax'))
            elif not self._is_valid_condition(condition):
                self.messages.append(LintMessage(line_num, 1, 'warning',
                                               "Do-while condition should be a boolean expression", 'loop-condition-style'))
            
            return
        
        # Check for incomplete do-while syntax
        if line.strip().startswith('do ') and '{' not in line:
            self.messages.append(LintMessage(line_num, 1, 'error',
                                           "Incomplete do-while: missing opening brace", 'invalid-loop-syntax'))
        elif line.strip() == 'do':
            self.messages.append(LintMessage(line_num, 1, 'error',
                                           "Incomplete do-while: missing opening brace", 'invalid-loop-syntax'))
    
    def _is_valid_condition(self, condition: str) -> bool:
        """Check if a condition looks like a valid boolean expression"""
        if not condition.strip():
            return False
        
        # Common boolean expressions
        boolean_patterns = [
            r'\btrue\b|\bfalse\b',  # Boolean literals
            r'\w+\s*[<>=!]+\s*\w+',  # Comparisons
            r'\w+\s*&&\s*\w+',  # Logical AND
            r'\w+\s*\|\|\s*\w+',  # Logical OR
            r'!\s*\w+',  # Logical NOT
            r'\w+\s*\(\s*\)',  # Function calls (might return boolean)
            r'\w+',  # Simple variable (might be boolean)
        ]
        
        for pattern in boolean_patterns:
            if re.search(pattern, condition):
                return True
        
        return False
    
    def _check_common_mistakes(self, line_num: int, line: str):
        """Check for common programming mistakes"""
        # Assignment in condition (single = not preceded or followed by = or comparison operators)
        if re.search(r'if\s*\([^)]*[^!<>=]=(?!=)[^=]', line):
            self.messages.append(LintMessage(line_num, 1, 'warning',
                                           "Assignment in condition (did you mean ==?)", 'assignment-in-condition'))
        
        # Empty catch/if blocks
        if re.search(r'(if|else|while|for)\s*\([^)]*\)\s*{\s*}', line):
            self.messages.append(LintMessage(line_num, 1, 'warning',
                                           "Empty control block", 'empty-block'))
        
        # Unreachable code after return
        if 'return' in line and not line.strip().startswith('//') and not line.strip().startswith('#'):
            # This is a simple check - would need more sophisticated analysis for real unreachable code detection
            pass
    
    def get_messages_by_severity(self) -> Dict[str, List[LintMessage]]:
        """Group messages by severity"""
        grouped = {'error': [], 'warning': [], 'info': []}
        for msg in self.messages:
            grouped[msg.severity].append(msg)
        return grouped
    
    def get_summary(self) -> str:
        """Get a summary of lint results"""
        grouped = self.get_messages_by_severity()
        errors = len(grouped['error'])
        warnings = len(grouped['warning'])
        info = len(grouped['info'])
        
        if errors + warnings + info == 0:
            return "No issues found ✓"
        
        parts = []
        if errors > 0:
            parts.append(f"{errors} error{'s' if errors != 1 else ''}")
        if warnings > 0:
            parts.append(f"{warnings} warning{'s' if warnings != 1 else ''}")
        if info > 0:
            parts.append(f"{info} style issue{'s' if info != 1 else ''}")
        
        return ", ".join(parts)
