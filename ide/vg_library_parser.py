"""
VG Library Parser - Automatically extract library structure from .vglib files
"""

import re
import os

class VGLibraryParser:
    """Parser for extracting library structure from .vglib files"""
    
    def __init__(self):
        self.library_pattern = r'library\s+(\w+)\s*\{'
        self.namespace_pattern = r'namespace\s+(\w+)\s*\{'
        self.function_pattern = r'function\s+(\w+)\s*\([^)]*\)\s*\{'
        
    def parse_vglib_file(self, file_path):
        """Parse a .vglib file and extract its structure
        
        Returns:
            dict: {
                'library_name': str,
                'namespaces': {
                    'namespace_name': ['function1', 'function2', ...]
                }
            }
        """
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
                
            return self.parse_vglib_content(content)
            
        except Exception as e:
            print(f"Error parsing .vglib file {file_path}: {e}")
            return None
            
    def parse_vglib_content(self, content):
        """Parse .vglib content and extract structure"""
        result = {
            'library_name': None,
            'namespaces': {}
        }
        
        # Remove comments to avoid false matches
        content = self._remove_comments(content)
        
        # Find library name
        library_match = re.search(self.library_pattern, content, re.IGNORECASE)
        if library_match:
            result['library_name'] = library_match.group(1)
        else:
            # Try to extract from filename if no library declaration found
            return None
            
        # Find all namespaces and their functions
        namespaces = self._extract_namespaces(content)
        result['namespaces'] = namespaces
        
        return result
        
    def _remove_comments(self, content):
        """Remove VG language comments from content"""
        # Remove single-line comments (##)
        content = re.sub(r'##.*', '', content)
        
        # Remove multi-line comments (/## ... ##/)
        content = re.sub(r'/\#\#.*?\#\#/', '', content, flags=re.DOTALL)
        
        return content
        
    def _extract_namespaces(self, content):
        """Extract namespaces and their functions"""
        namespaces = {}
        
        # Find namespace declarations and track braces manually
        lines = content.split('\n')
        current_namespace = None
        brace_count = 0
        namespace_content = []
        
        for line in lines:
            line = line.strip()
            
            # Check for namespace declaration
            ns_match = re.search(r'namespace\s+(\w+)\s*\{', line, re.IGNORECASE)
            if ns_match and brace_count == 0:
                # Save previous namespace if exists
                if current_namespace and namespace_content:
                    functions = self._extract_functions_from_lines(namespace_content)
                    if functions:
                        namespaces[current_namespace] = functions
                
                # Start new namespace
                current_namespace = ns_match.group(1)
                namespace_content = []
                brace_count = line.count('{') - line.count('}')
                continue
                
            # If we're inside a namespace, collect content
            if current_namespace is not None:
                namespace_content.append(line)
                brace_count += line.count('{') - line.count('}')
                
                # If braces are balanced, we've reached the end of the namespace
                if brace_count <= 0:
                    functions = self._extract_functions_from_lines(namespace_content)
                    if functions:
                        namespaces[current_namespace] = functions
                    current_namespace = None
                    namespace_content = []
                    brace_count = 0
                    
        # Handle last namespace if file doesn't end cleanly
        if current_namespace and namespace_content:
            functions = self._extract_functions_from_lines(namespace_content)
            if functions:
                namespaces[current_namespace] = functions
                
        return namespaces
        
    def _extract_functions_from_lines(self, lines):
        """Extract function names from a list of lines"""
        functions = []
        content = '\n'.join(lines)
        
        # Find all function declarations
        for match in re.finditer(self.function_pattern, content, re.IGNORECASE):
            function_name = match.group(1)
            if function_name not in functions:
                functions.append(function_name)
                
        return sorted(functions)
        
    def validate_library_structure(self, library_data):
        """Validate that the parsed library structure is valid"""
        if not library_data:
            return False, "Failed to parse library file"
            
        if not library_data.get('library_name'):
            return False, "No library name found"
            
        if not library_data.get('namespaces'):
            return False, "No namespaces found"
            
        # Check that all namespaces have at least one function
        empty_namespaces = [ns for ns, funcs in library_data['namespaces'].items() if not funcs]
        if empty_namespaces:
            return False, f"Empty namespaces found: {', '.join(empty_namespaces)}"
            
        return True, "Library structure is valid"

# Test function
def test_parser():
    """Test the parser with a sample .vglib file"""
    parser = VGLibraryParser()
    
    # Test with an actual library file
    test_file = "libraries/Util.vglib"
    if os.path.exists(test_file):
        result = parser.parse_vglib_file(test_file)
        if result:
            print(f"Parsed library: {result['library_name']}")
            for namespace, functions in result['namespaces'].items():
                print(f"  {namespace}: {', '.join(functions)}")
        else:
            print("Failed to parse library file")
    else:
        print(f"Test file {test_file} not found")

if __name__ == "__main__":
    test_parser()
