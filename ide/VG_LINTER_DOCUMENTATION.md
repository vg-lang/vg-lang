# VG Language Linter Documentation

## Overview

The VG Language Linter is a static code analyzer that provides real-time code quality checks, syntax validation, and library import verification for VG Language files.

## Features

### ✅ Grammar-Based Analysis
- Follows the official VG Language grammar
- Supports all VG Language constructs (variables, functions, classes, structs, enums)
- Proper import statement parsing
- Library and namespace declarations

### ✅ Built-in Library Support
- **IO**: File operations and user input (Prompt, File namespaces)
- **Math**: Mathematical operations and constants
- **Arrays**: Array manipulation functions
- **Random**: Random number generation
- **DateTime**: Date and time utilities
- **Util**: Type conversion and string manipulation
- **OsLib**: Operating system utilities
- **Mathlib**: Advanced mathematical functions

### ✅ User Library Extension
- Load custom libraries from `.vglib` files
- JSON configuration for additional libraries
- Dynamic library registration

### ✅ Code Quality Checks
- Undefined variable detection
- Function call validation
- Import verification
- Syntax error detection
- Code style warnings

## Usage

### Basic Linting
```python
from vg_linter import VGLinter

linter = VGLinter()
messages = linter.lint_file("your_file.vg")

for message in messages:
    print(message)
```

### Adding Custom Libraries

#### Method 1: JSON Configuration
Edit `ide/config/user_libraries.json`:

```json
{
  "user_libraries": {
    "MyGameLib": {
      "Graphics": ["drawSprite", "loadTexture"],
      "Audio": ["playSound", "loadMusic"]
    },
    "DatabaseLib": {
      "Connection": ["connect", "disconnect"],
      "Query": ["select", "insert", "update"]
    }
  }
}
```

#### Method 2: VG Library Files
Create `.vglib` files in the `libraries/` directory:

```vg
library MyCustomLib {
    namespace Utils {
        function formatString(str) {
            // Implementation
        }
        
        function validateInput(input) {
            // Implementation
        }
    }
    
    namespace Database {
        function connect(connectionString) {
            // Implementation
        }
    }
}
```

#### Method 3: Programmatic Registration
```python
linter = VGLinter()
linter.add_user_library("MyLib", {
    "Utils": ["helper", "formatter"],
    "Data": ["serialize", "deserialize"]
})
```

## VG Language Import Syntax

The linter supports the official VG Language import syntax:

```vg
// Library imports
import IO.Prompt;           // Import Prompt namespace from IO library
import Math.Basic;          // Import Basic namespace from Math library
import IO.Prompt as Input;  // Import with alias

// File imports
import "path/to/file.vg";   // Import from file
```

## Available Standard Libraries

### IO Library
```vg
import IO.Prompt;
import IO.File;

var name = Prompt.input("Enter name: ");
var content = File.readFile("data.txt");
```

### Util Library
```vg
import Util.Integer;
import Util.String;

var num = Integer.toInt("42");
var upper = String.toUpper("hello");
```

### Math Libraries
```vg
import Math.Basic;
import Mathlib.arithmetic;

var result = Basic.sqrt(16);
var sum = arithmetic.add(5, 3);
```

## Error Types

### Error Severity Levels
- **Error**: Syntax errors, undefined variables, type mismatches
- **Warning**: Potential issues, unused variables, questionable patterns
- **Info**: Style suggestions, naming conventions

### Common Error Messages
- `Variable 'x' used before declaration` - Use variables only after declaring them
- `Function 'func' called but not defined` - Import required libraries or define functions
- `Unknown library 'LibName'` - Check library name or add to configuration
- `Assignment in condition (did you mean ==?)` - Use comparison operators in conditions

## Integration with IDE

The linter integrates seamlessly with the VG Language IDE:

1. **Real-time Analysis**: Code is analyzed as you type
2. **Error Highlighting**: Issues are highlighted in the editor
3. **Library Autocomplete**: Imported libraries provide autocomplete suggestions
4. **Quick Fixes**: Some issues offer automatic fixes

## Configuration

### Customizing Lint Rules
Edit the linter configuration to enable/disable specific checks:

```python
linter = VGLinter()
# Add custom validation rules
linter.add_custom_rule(my_custom_rule)
```

### Library Paths
The linter searches for libraries in:
1. `../libraries/` (standard VG libraries)
2. `config/user_libraries.json` (JSON configuration)
3. User-defined paths

## Best Practices

### Library Organization
- Group related functions in namespaces
- Use descriptive library and namespace names
- Follow VG Language naming conventions

### Code Style
- Declare variables before use
- Import only needed namespaces
- Use meaningful variable names
- Handle errors appropriately

### Performance
- The linter caches library definitions
- Large projects benefit from selective imports
- Consider splitting large libraries into smaller modules

## Troubleshooting

### Common Issues

**Q: My custom library isn't recognized**
A: Check that:
- The `.vglib` file is in the libraries directory
- The library syntax follows VG Language grammar
- The JSON configuration is valid

**Q: False positive errors**
A: Ensure:
- Required libraries are imported
- Variable names don't conflict with keywords
- String literals are properly quoted

**Q: Performance issues**
A: Try:
- Reducing the number of imported libraries
- Splitting large files into smaller modules
- Using selective imports

## Contributing

To extend the linter:
1. Add new rules to the `VGLinter` class
2. Update library definitions as VG Language evolves
3. Improve grammar parsing for new language features
4. Add unit tests for new functionality

---

The VG Language Linter provides professional-grade static analysis to help you write better VG Language code. It follows the official grammar, supports the standard library ecosystem, and can be extended for your specific needs.
