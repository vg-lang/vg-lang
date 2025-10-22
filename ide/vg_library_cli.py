#!/usr/bin/env python3
"""
VG Language Library CLI Manager
Command-line tool for managing VG Language libraries.
"""

import argparse
import json
import os
import sys
from vg_library_manager import VGLibraryManager

def main():
    parser = argparse.ArgumentParser(description='VG Language Library Manager')
    parser.add_argument('--project', '-p', default='.', 
                       help='Project directory (default: current directory)')
    subparsers = parser.add_subparsers(dest='command', help='Commands')
    
    # List libraries command
    list_parser = subparsers.add_parser('list', help='List all libraries')
    list_parser.add_argument('--type', choices=['standard', 'project', 'all'], default='all',
                           help='Type of libraries to list')
    
    # Add library command
    add_parser = subparsers.add_parser('add', help='Add a new project library')
    add_parser.add_argument('name', help='Library name')
    add_parser.add_argument('--description', default='', help='Library description')
    
    # Add namespace command
    namespace_parser = subparsers.add_parser('add-namespace', help='Add namespace to library')
    namespace_parser.add_argument('library', help='Library name')
    namespace_parser.add_argument('namespace', help='Namespace name')
    namespace_parser.add_argument('functions', nargs='+', help='Function names')
    
    # Remove library command
    remove_parser = subparsers.add_parser('remove', help='Remove a project library')
    remove_parser.add_argument('name', help='Library name to remove')
    
    # Import from .vglib command
    import_parser = subparsers.add_parser('import', help='Import library from .vglib file')
    import_parser.add_argument('path', help='Path to .vglib file')
    
    # Validate import command
    validate_parser = subparsers.add_parser('validate', help='Validate import statement')
    validate_parser.add_argument('import_statement', help='Import statement to validate')
    
    args = parser.parse_args()
    
    if not args.command:
        parser.print_help()
        return
    
    manager = VGLibraryManager(args.project)
    print(f"Working with project: {os.path.abspath(args.project)}")
    
    if args.command == 'list':
        libraries = manager.get_all_libraries()
        
        if args.type in ['standard', 'all']:
            print("Standard Libraries:")
            for lib_name, namespaces in libraries['standard'].items():
                print(f"  {lib_name}:")
                for ns_name, functions in namespaces.items():
                    print(f"    {ns_name}: {', '.join(functions)}")
        
        if args.type in ['project', 'all']:
            print("\\nProject Libraries:")
            for lib_name, namespaces in libraries['user'].items():
                print(f"  {lib_name}:")
                for ns_name, functions in namespaces.items():
                    print(f"    {ns_name}: {', '.join(functions)}")
    
    elif args.command == 'add':
        success = manager.create_library(args.name, args.description)
        if success:
            print(f"Created library '{args.name}' in project")
        else:
            print(f"Failed to create library '{args.name}' (may already exist)")
            sys.exit(1)
    
    elif args.command == 'add-namespace':
        success = manager.add_namespace_to_library(args.library, args.namespace, args.functions)
        if success:
            print(f"Added namespace '{args.namespace}' to library '{args.library}'")
        else:
            print(f"Failed to add namespace")
            sys.exit(1)
    
    elif args.command == 'remove':
        success = manager.remove_library(args.name)
        if success:
            print(f"Removed library '{args.name}'")
        else:
            print(f"Failed to remove library '{args.name}' (may not exist)")
            sys.exit(1)
    
    elif args.command == 'import':
        success, message = manager.import_from_vglib(args.path)
        print(message)
        if not success:
            sys.exit(1)
    
    elif args.command == 'validate':
        valid, message = manager.validate_library_import(args.import_statement)
        print(f"Valid: {valid}")
        print(f"Message: {message}")
        if not valid:
            sys.exit(1)

if __name__ == '__main__':
    main()
