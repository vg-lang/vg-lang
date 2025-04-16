# System Calls in VG Language

VG Language allows controlled access to Java methods through the `VgSystemCall` function. This feature enables interaction with the underlying Java platform while maintaining security through a whitelist mechanism.

## Basic Usage

The general syntax for a system call is:

```vg
VgSystemCall("fully.qualified.ClassName", "methodName", arg1, arg2, ...)
```

Examples:

```vg
## Get current time in milliseconds
var currentTime = VgSystemCall("java.lang.System", "currentTimeMillis");

## Parse a string to an integer
var number = VgSystemCall("java.lang.Integer", "parseInt", "42");
```

## Security

System calls are restricted to a whitelist of allowed classes and methods defined in the configuration file (`Configuration/allowed_configurations.vgenv`). This ensures that VG programs can only access safe and approved functionality.

Attempting to call a method that is not on the whitelist will result in a security exception.

## Common System Calls

### Console Input

```vg
## Create a Scanner object
var scanner = VgSystemCall("java.util.Scanner", "<init>", VgSystemCall("components.IoUtils", "getSystemIn"));

## Read a line of input
var input = VgSystemCall("java.util.Scanner", "nextLine", scanner);
```

### File Operations

```vg
## Read a file
var content = VgSystemCall("components.IoUtils", "readFile", "path/to/file.txt");

## Write to a file
VgSystemCall("components.IoUtils", "writeFile", "path/to/file.txt", "Hello, world!");

## Append to a file
VgSystemCall("components.IoUtils", "appendToFile", "path/to/file.txt", "More content");
```

### GUI Operations

```vg
## Create a window
var window = VgSystemCall("components.MyGUI", "<init>", "Window Title", 800, 600);

## Set background color
VgSystemCall("components.MyGUI", "setWindowBackgroundColor", window, VgSystemCall("java.awt.Color", "decode", "#87CEEB"));

## Show the window
VgSystemCall("components.MyGUI", "launch", window);
```

### Math Operations

```vg
## Get absolute value
var abs = VgSystemCall("java.lang.Math", "abs", -10);

## Get random number
var random = VgSystemCall("java.lang.Math", "random");

## Round a number
var rounded = VgSystemCall("java.lang.Math", "floor", 3.7);
```

### String Operations

```vg
## Get string length
var length = VgSystemCall("components.Util", "stringLength", "Hello");

## Find substring position
var index = VgSystemCall("components.Util", "indexOfString", "Hello World", "World");

## Extract substring
var substring = VgSystemCall("components.Util", "substringString", "Hello World", 0, 5);
```

### Type Conversion

```vg
## Convert to integer
var intValue = VgSystemCall("components.Util", "toInt", "42");

## Convert to double
var doubleValue = VgSystemCall("components.Util", "toDouble", "3.14");

## Convert to string
var stringValue = VgSystemCall("components.Util", "toString", 123);
```

## Using System Calls in Libraries

System calls are commonly used in library implementations to provide a clean API for VG programs:

```vg
library MyLibrary {
    namespace math {
        function abs(x) {
            return VgSystemCall("java.lang.Math", "abs", x);
        }
        
        function random() {
            return VgSystemCall("java.lang.Math", "random");
        }
    }
}
```

This allows users to call `MyLibrary.math.abs(-10)` instead of using the system call directly.

## Constructor Calls

To create a new instance of a Java class, use the special `<init>` method name:

```vg
## Create a new Date object
var date = VgSystemCall("java.util.Date", "<init>");

## Create a Timer with parameters
var timer = VgSystemCall("javax.swing.Timer", "<init>", 1000, callbackFunction);
```

## Allowed Classes and Methods

For security reasons, only specific classes and methods are allowed. The complete list can be found in the `Configuration/allowed_configurations.vgenv` file.

Some commonly used allowed classes include:

- `java.lang.System` - For system operations
- `java.util.Scanner` - For reading input
- `java.lang.Integer`, `java.lang.Double` - For number parsing
- `java.lang.String` - For string operations
- `components.MyGUI` - For GUI operations
- `java.lang.Math` - For mathematical operations
- `components.ArrayUtils` - For array manipulation
- `components.IoUtils` - For file operations
- `components.SQLiteDB` - For database operations
- `java.util.regex.Pattern` and `java.util.regex.Matcher` - For regular expressions

## Best Practices

1. **Use Libraries**: Instead of using system calls directly in your code, use the provided libraries that wrap these calls.

2. **Error Handling**: Always handle potential errors when making system calls, as they can throw exceptions.

3. **Security Awareness**: Remember that system calls are restricted for security reasons. If you need additional functionality, consider requesting it to be added to the allowed configurations.

4. **Documentation**: When creating libraries that use system calls, document the underlying Java methods being used.

## Example: Creating a Custom System Call Wrapper

```vg
function readUserInput(prompt) {
    print(prompt);
    var scanner = VgSystemCall("java.util.Scanner", "<init>", VgSystemCall("components.IoUtils", "getSystemIn"));
    return VgSystemCall("java.util.Scanner", "nextLine", scanner);
}

## Usage
var name = readUserInput("Enter your name: ");
print("Hello, " + name);
``` 