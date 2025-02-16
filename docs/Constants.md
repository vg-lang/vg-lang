# Constants in VG Language

Constants are similar to variables, but their values cannot be changed once they are assigned. You declare a constant using the `const` keyword.

## **Declaring Constants**

To declare a constant, use the `const` keyword followed by the name and value:

```vg
const PI = 3.14159;
const MAX_VALUE = 100;
```
# Constant Types
Like variables, constants can have various data types:

* Integers: Whole numbers (e.g., 100, -50)
* Doubles: Decimal numbers (e.g., 3.14, 2.71)
* Strings: Text (e.g., "Hello, world!")
* Booleans: true or false
```vg 
const greeting = "Hello, VG!"; ## String constant
const maxAge = 100;            ## Integer constant
```
# Why Use Constants?
* Constants are useful when you need a value that should remain unchanged throughout the program (e.g., mathematical constants like PI, or configuration values like MAX_VALUE).
* Declaring values as constants helps to prevent accidental changes and makes the code more readable.
