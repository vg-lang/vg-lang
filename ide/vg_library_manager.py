"""
VG Language Library Manager
Provides utilities for managing user-defined libraries in the VG Language IDE.
"""

import json
import os
from typing import Dict, List, Optional, Tuple
from vg_linter import VGLinter

class VGLibraryManager:
    """Manages user-defined libraries for the VG Language IDE"""
    
    def __init__(self, project_directory: str = None):
        self.project_directory = project_directory or os.getcwd()
        self.standard_config_path = os.path.join(os.path.dirname(__file__), 'config', 'standard_libraries.json')
        self.project_config_path = os.path.join(self.project_directory, 'vg_project_libraries.json')
        self.linter = VGLinter()
        
        # Load project-specific libraries
        if self.project_directory:
            self.linter._load_project_libraries_config(self.project_directory)
    
    def reload_libraries(self):
        """Reload libraries from configuration files"""
        self.linter._load_standard_libraries()
        self.linter._load_project_libraries_config(self.project_directory)
    
    def set_project_directory(self, project_path: str):
        """Set the current project directory"""
        self.project_directory = project_path
        self.project_config_path = os.path.join(self.project_directory, 'vg_project_libraries.json')
        self.reload_libraries()
    
    def create_library(self, library_name: str, description: str = "") -> bool:
        """Create a new empty library in the current project"""
        try:
            if library_name in self.linter.user_libraries:
                return False  # Library already exists
            
            self.linter.add_user_library(library_name, {})
            return self.linter.save_user_libraries(self.project_directory)
        except Exception:
            return False
    
    def add_namespace_to_library(self, library_name: str, namespace_name: str, 
                                functions: List[str]) -> bool:
        """Add a namespace with functions to a library"""
        try:
            self.linter.add_user_library_namespace(library_name, namespace_name, functions)
            return self.linter.save_user_libraries(self.project_directory)
        except Exception:
            return False
    
    def remove_library(self, library_name: str) -> bool:
        """Remove a user library"""
        try:
            self.linter.remove_user_library(library_name)
            return self.linter.save_user_libraries(self.project_directory)
        except Exception:
            return False
    
    def import_from_vglib(self, vglib_path: str) -> Tuple[bool, str]:
        """Import library from .vglib file"""
        try:
            if not os.path.exists(vglib_path):
                return False, "File does not exist"
            
            if not vglib_path.endswith('.vglib'):
                return False, "Not a .vglib file"
            
            success = self.linter.import_library_from_vglib(vglib_path, self.project_directory)
            if success:
                library_name = os.path.basename(vglib_path).replace('.vglib', '')
                return True, f"Successfully imported library '{library_name}'"
            else:
                return False, "Failed to parse .vglib file"
        except Exception as e:
            return False, f"Error importing library: {str(e)}"
    
    def get_all_libraries(self) -> Dict[str, Dict]:
        """Get all available libraries (standard + user)"""
        return {
            "standard": self.linter.standard_libraries,
            "user": self.linter.user_libraries
        }
    
    def add_standard_library(self, library_name: str, namespaces: Dict[str, List[str]]) -> bool:
        """Add a library to the standard libraries (admin function)"""
        try:
            # Load current config
            config = {"standard_libraries": {}, "user_libraries": {}}
            if os.path.exists(self.config_path):
                with open(self.config_path, 'r', encoding='utf-8') as f:
                    config = json.load(f)
            
            # Add to standard libraries
            config["standard_libraries"][library_name] = namespaces
            
            # Save config
            os.makedirs(os.path.dirname(self.config_path), exist_ok=True)
            with open(self.config_path, 'w', encoding='utf-8') as f:
                json.dump(config, f, indent=2)
            
            # Reload linter
            self.reload_libraries()
            return True
        except Exception:
            return False
    
    def remove_standard_library(self, library_name: str) -> bool:
        """Remove a library from standard libraries (admin function)"""
        try:
            # Load current config
            config = {"standard_libraries": {}, "user_libraries": {}}
            if os.path.exists(self.config_path):
                with open(self.config_path, 'r', encoding='utf-8') as f:
                    config = json.load(f)
            
            # Remove from standard libraries
            if library_name in config["standard_libraries"]:
                del config["standard_libraries"][library_name]
                
                # Save config
                with open(self.config_path, 'w', encoding='utf-8') as f:
                    json.dump(config, f, indent=2)
                
                # Reload linter
                self.reload_libraries()
                return True
            return False
        except Exception:
            return False
    
    def get_library_functions(self, library_name: str, namespace_name: str) -> List[str]:
        """Get all functions in a specific namespace of a library"""
        info = self.linter.get_library_info(library_name)
        if info["exists"] and namespace_name in info["namespaces"]:
            return info["namespaces"][namespace_name]
        return []
    
    def validate_library_import(self, import_statement: str) -> Tuple[bool, str]:
        """Validate an import statement and return status"""
        try:
            # Parse import statement
            import_statement = import_statement.strip().rstrip(';')
            
            if import_statement.startswith('import '):
                import_path = import_statement[7:].strip()
                
                # Handle wildcard imports
                if import_path.endswith('.*'):
                    import_path = import_path[:-2]
                
                parts = import_path.split('.')
                if len(parts) >= 2:
                    library_name = parts[0]
                    namespace_name = parts[1]
                    
                    info = self.linter.get_library_info(library_name)
                    if not info["exists"]:
                        return False, f"Library '{library_name}' not found"
                    
                    if namespace_name not in info["namespaces"]:
                        return False, f"Namespace '{namespace_name}' not found in library '{library_name}'"
                    
                    return True, f"Valid import: {len(info['namespaces'][namespace_name])} functions available"
                else:
                    return False, "Invalid import format. Use: import Library.Namespace;"
            else:
                return False, "Import statement must start with 'import'"
        except Exception as e:
            return False, f"Error validating import: {str(e)}"
    
    def suggest_functions(self, library_name: str, namespace_name: str, prefix: str = "") -> List[str]:
        """Get function suggestions for autocomplete"""
        functions = self.get_library_functions(library_name, namespace_name)
        if prefix:
            functions = [f for f in functions if f.startswith(prefix)]
        return sorted(functions)
    
    def export_library_to_vglib(self, library_name: str, output_path: str) -> Tuple[bool, str]:
        """Export a user library to .vglib format"""
        try:
            if library_name not in self.linter.user_libraries:
                return False, f"Library '{library_name}' not found"
            
            library_data = self.linter.user_libraries[library_name]
            
            # Generate .vglib content
            vglib_content = f"library {library_name} {{\n\n"
            
            for namespace_name, functions in library_data.items():
                vglib_content += f"    namespace {namespace_name} {{\n"
                for func_name in functions:
                    vglib_content += f"        function {func_name}() {{\n"
                    vglib_content += f"            ## TODO: Implement {func_name}\n"
                    vglib_content += f"        }}\n\n"
                vglib_content += f"    }}\n\n"
            
            vglib_content += "}\n"
            
            with open(output_path, 'w', encoding='utf-8') as f:
                f.write(vglib_content)
            
            return True, f"Library exported to {output_path}"
        except Exception as e:
            return False, f"Error exporting library: {str(e)}"
    
    def get_library_usage_stats(self) -> Dict[str, int]:
        """Get usage statistics for libraries (placeholder for future IDE integration)"""
        # This could be enhanced to track actual usage from parsed files
        stats = {}
        for lib_name in self.linter.user_libraries:
            stats[lib_name] = 0  # Placeholder
        return stats

# Convenience functions for IDE integration
def create_library_manager() -> VGLibraryManager:
    """Create a library manager instance"""
    return VGLibraryManager()

def quick_add_library(library_name: str, namespace_name: str, functions: List[str]) -> bool:
    """Quick way to add a library with one namespace"""
    manager = VGLibraryManager()
    manager.create_library(library_name)
    return manager.add_namespace_to_library(library_name, namespace_name, functions)
