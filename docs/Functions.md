# Functions in VG Language

Functions allow you to group code that performs a specific task. They can take parameters and return values.

## Defining Functions

To define a function, use the `function` keyword followed by the function name and parameters:

```vg
function add(a, b) {
    return a + b;
}
```

## Calling Functions

To call a function, use its name followed by parentheses containing any arguments:

```vg
var result = add(5, 3);
print(result);  ## Output: 8
```

## Return Statement

The `return` statement specifies the value to be returned from a function:

```vg
function multiply(a, b) {
    return a * b;
}

var product = multiply(4, 5);  ## Product = 20
```

Functions without a `return` statement or with an empty `return` statement return `null`.

## Parameters

Functions can have multiple parameters, separated by commas:

```vg
function greet(firstName, lastName, age) {
    return "Hello, " + firstName + " " + lastName + "! You are " + age + " years old.";
}

var greeting = greet("John", "Doe", 30);
```

## Function Documentation

You can document your functions using special comment blocks:

```vg
/## Calculates the sum of two numbers
# @param a The first number
# @param b The second number
# @return The sum of a and b
# @author John Doe
##/
function add(a, b) {
    return a + b;
}
```

## Functions in Libraries

Functions can be organized into libraries and namespaces:

```vg
library MathUtils {
    namespace basic {
        function add(a, b) {
            return a + b;
        }
        
        function subtract(a, b) {
            return a - b;
        }
    }
}
```

To use these functions, import them:

```vg
import MathUtils.basic;

var sum = basic.add(5, 3);
```

## Recursive Functions

Functions can call themselves (recursion):

```vg
function factorial(n) {
    if (n <= 1) {
        return 1;
    }
    return n * factorial(n - 1);
}

var result = factorial(5);  ## result = 120
```

## Scope

Variables declared inside a function are only accessible within that function:

```vg
function test() {
    var localVar = 10;
    print(localVar);  ## Works fine
}

test();
print(localVar);  ## Error: localVar is not defined
```

However, functions can access variables from their containing scope:

```vg
var globalVar = 20;

function test() {
    print(globalVar);  ## Works fine, accesses the global variable
}

test();
```

## Best Practices

1. **Use Descriptive Names**: Choose function names that clearly describe what the function does.

2. **Keep Functions Small**: Each function should do one thing and do it well.

3. **Document Your Functions**: Use documentation comments to explain what the function does, its parameters, and return values.

4. **Avoid Side Effects**: Functions should ideally not modify variables outside their scope.

5. **Return Early**: Return as soon as you have the result to make your code more readable.

```vg
function isEven(number) {
    if (number % 2 == 0) {
        return true;
    }
    return false;
}
``` 