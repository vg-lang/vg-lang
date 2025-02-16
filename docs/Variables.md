# Variables in VG Language

Variables are used to store data values. In VG Language, you can declare variables and assign them values.

## **Declaring Variables**

To declare a variable, use the `var` keyword:

```vg
var x = 10;
var name = "Alice";
var isActive = true;
```
## Variable Types
VG Language supports various data types for variables:

* Integers: Whole numbers (e.g., 10, -5)
* Doubles: Decimal numbers (e.g., 3.14, -0.99)
* Strings: Text enclosed in double quotes (e.g., "Hello, world!")
* Booleans: true or false
* Arrays: A list of elements (e.g., [1, 2, 3], ["a", "b", "c"])

# Example
```vg
var age = 25;         ## Integer
var temperature = 23.5;  ## Double
var greeting = "Hello, VG!"; ## String
var isActive = true;   ## Boolean
```
# Updating Variables
```vg
var x = 5;
x = x + 3;  ## x is now 8
print(x);  ## Output: 8
```
