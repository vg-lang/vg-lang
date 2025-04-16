# Documentation Generator

VG Language includes a documentation generator that can create documentation from your code comments.

## Comment Format

VG Language supports special documentation comments that can be processed by the documentation generator:

```vg
/## This is a documentation comment.
# It can span multiple lines.
# @param name Description of the parameter
# @return Description of the return value
# @author Author name
##/
function myFunction(name) {
    return "Hello, " + name;
}
```

## Running the Documentation Generator

The documentation generator can be run using:

```sh
vg --doc myfile.vg
```

This will generate documentation in Markdown format based on the documentation comments in your code.

To generate documentation for an entire directory:

```sh
vg --doc myproject/
```

## Output Format

By default, documentation is generated in Markdown format. The output files are created in a `docs/` directory.

## Supported Tags

The documentation generator recognizes the following special tags:

- `@param <name> <description>` - Documents a function parameter
- `@return <description>` - Documents the return value
- `@author <name>` - Specifies the author of the code
- `@field <name> <description>` - Documents a struct field
- `@value <name> <description>` - Documents an enum value

## Example

Here's an example of a well-documented function:

```vg
/## Calculates the factorial of a number.
# This function uses recursion to calculate the factorial.
#
# @param n The number to calculate the factorial of
# @return The factorial of n
# @author Hussein Abdul-Ameer
##/
function factorial(n) {
    if (n <= 1) {
        return 1;
    }
    return n * factorial(n - 1);
}
```

The generated documentation would include:

- Function name and description
- Parameter details
- Return value information
- Author attribution

## Library Documentation

When documenting libraries, use documentation comments for each namespace and function:

```vg
library MathUtils {
    /## Provides advanced mathematical operations.
    # This namespace contains functions for complex calculations.
    # @author Hussein Abdul-Ameer
    ##/
    namespace advanced {
        /## Calculates the factorial of a number.
        # @param n The number to calculate the factorial of
        # @return The factorial of n
        ##/
        function factorial(n) {
            if (n <= 1) {
                return 1;
            }
            return n * factorial(n - 1);
        }
    }
}
``` 