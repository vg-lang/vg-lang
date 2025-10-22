"""
Example IDE Integration for VG Language Library Management
This demonstrates how the IDE would integrate with the library management system.
"""

import os
import sys
sys.path.append('ide')  # Add IDE directory to path

from vg_library_manager import VGLibraryManager
from vg_linter import VGLinter

class VGIDELibraryIntegration:
    """Simulates IDE integration with VG Language library management"""
    
    def __init__(self, project_path: str):
        self.project_path = project_path
        self.library_manager = VGLibraryManager(project_path)
        self.linter = VGLinter()
    
    def add_library_dialog(self, library_name: str, description: str = ""):
        """Simulate IDE dialog for adding a new library"""
        print(f"[IDE] Creating library '{library_name}' in project...")
        success = self.library_manager.create_library(library_name, description)
        if success:
            print(f"[IDE] ✅ Library '{library_name}' created successfully")
            return True
        else:
            print(f"[IDE] ❌ Failed to create library '{library_name}' (may already exist)")
            return False
    
    def add_namespace_dialog(self, library_name: str, namespace_name: str, functions: list):
        """Simulate IDE dialog for adding namespace to library"""
        print(f"[IDE] Adding namespace '{namespace_name}' to '{library_name}'...")
        success = self.library_manager.add_namespace_to_library(library_name, namespace_name, functions)
        if success:
            print(f"[IDE] ✅ Namespace '{namespace_name}' added with {len(functions)} functions")
            return True
        else:
            print(f"[IDE] ❌ Failed to add namespace '{namespace_name}'")
            return False
    
    def get_autocomplete_suggestions(self, library_name: str, namespace_name: str, prefix: str = ""):
        """Get function suggestions for IDE autocomplete"""
        return self.library_manager.suggest_functions(library_name, namespace_name, prefix)
    
    def validate_import_statement(self, import_statement: str):
        """Validate import statement for IDE"""
        valid, message = self.library_manager.validate_library_import(import_statement)
        return valid, message
    
    def get_project_libraries(self):
        """Get all libraries available to this project"""
        return self.library_manager.get_all_libraries()
    
    def lint_file_with_libraries(self, file_path: str):
        """Lint a file with current project libraries"""
        full_path = os.path.join(self.project_path, file_path)
        return self.linter.lint_file(full_path)

# Demonstrate IDE usage
if __name__ == "__main__":
    print("=== VG Language IDE Library Integration Demo ===\\n")
    
    # Simulate opening a project in the IDE
    project_path = "projects"
    ide = VGIDELibraryIntegration(project_path)
    
    print("1. Current project libraries:")
    libs = ide.get_project_libraries()
    for lib_name in libs['user'].keys():
        print(f"   - {lib_name}")
    
    print("\\n2. User adds new library via IDE dialog:")
    ide.add_library_dialog("MobileGameLib", "Mobile game development utilities")
    
    print("\\n3. User adds namespaces via IDE dialog:")
    ide.add_namespace_dialog("MobileGameLib", "touch", ["onTap", "onSwipe", "onPinch"])
    ide.add_namespace_dialog("MobileGameLib", "sensors", ["getAccelerometer", "getGyroscope", "getOrientation"])
    
    print("\\n4. IDE validates import statement:")
    valid, msg = ide.validate_import_statement("import MobileGameLib.touch.*;")
    print(f"   Import valid: {valid}, Message: {msg}")
    
    print("\\n5. IDE provides autocomplete suggestions:")
    suggestions = ide.get_autocomplete_suggestions("MobileGameLib", "touch", "on")
    print(f"   Autocomplete for 'on': {suggestions}")
    
    print("\\n6. Final project libraries:")
    libs = ide.get_project_libraries()
    for lib_name, namespaces in libs['user'].items():
        print(f"   {lib_name}:")
        for ns_name, functions in namespaces.items():
            print(f"     {ns_name}: {', '.join(functions)}")
    
    print("\\n✅ IDE integration working perfectly!")
