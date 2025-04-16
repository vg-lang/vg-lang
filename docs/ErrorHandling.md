# Error Handling in VG Language

VG Language provides mechanisms to handle errors and exceptions in your code.

## Try-Catch Statements

The try-catch statement allows you to handle errors gracefully:

```vg
try {
    ## Code that might cause an error
    var result = someRiskyOperation();
} catch (error) {
    ## Code to handle the error
    print("An error occurred: " + error);
}
```

## Throwing Errors

You can throw your own errors using the `throw` statement:

```vg
function divide(a, b) {
    if (b == 0) {
        throw "Division by zero is not allowed";
    }
    return a / b;
}
```

## Error Types

VG Language has several built-in error types:

- `VGTypeException` - Type-related errors (e.g., trying to perform arithmetic on a string)
- `VGNameException` - Variable or function name errors (e.g., using an undefined variable)
- `VGImportException` - Import-related errors (e.g., trying to import a non-existent library)
- `VGException` - General runtime errors

## Example: Handling File Operations

```vg
import IO.File;

try {
    var content = File.readFile("nonexistent.txt");
    print(content);
} catch (error) {
    print("Could not read file: " + error);
}
```

## Example: Validating User Input

```vg
import IO.Prompt;

function getUserAge() {
    try {
        var input = Prompt.input("Enter your age: ");
        var age = Integer.toInt(input);
        
        if (age < 0 || age > 120) {
            throw "Age must be between 0 and 120";
        }
        
        return age;
    } catch (error) {
        print("Invalid input: " + error);
        return getUserAge();  ## Try again
    }
}

var age = getUserAge();
print("Your age is: " + age);
```

## Finally Block

The `finally` block contains code that will execute regardless of whether an exception was thrown or caught:

```vg
try {
    ## Code that might throw an error
    var file = openFile("data.txt");
    processFile(file);
} catch (error) {
    ## Handle the error
    print("Error processing file: " + error);
} finally {
    ## This code always runs
    closeFile(file);
}
```

## Nested Try-Catch Blocks

You can nest try-catch blocks to handle different types of errors at different levels:

```vg
try {
    try {
        ## Risky operation
        var data = fetchData();
        processData(data);
    } catch (dataError) {
        ## Handle data-specific errors
        print("Data error: " + dataError);
        useBackupData();
    }
    
    saveResults();
} catch (error) {
    ## Handle any other errors
    print("Operation failed: " + error);
}
```

## Best Practices

1. **Be Specific**: Catch only the errors you can handle meaningfully.

2. **Provide Useful Error Messages**: Include information about what went wrong and how to fix it.

3. **Clean Up Resources**: Make sure to close files, database connections, etc., even when errors occur.

4. **Don't Overuse Try-Catch**: Don't wrap everything in try-catch blocks; use them only where errors are likely or critical.

5. **Log Errors**: For debugging purposes, log errors with relevant context information.

```vg
try {
    ## Risky operation
} catch (error) {
    ## Log the error with context
    File.appendToFile("error.log", "Error at " + DateTime.timestamp.currentTimeMillis() + ": " + error);
    ## Show a user-friendly message
    print("An error occurred. Please try again later.");
}
```

## Common Error Scenarios

### Division by Zero

```vg
function safeDivide(a, b) {
    try {
        if (b == 0) {
            throw "Division by zero";
        }
        return a / b;
    } catch (error) {
        print("Error: " + error);
        return null;
    }
}
```

### Array Index Out of Bounds

```vg
function getArrayElement(array, index) {
    try {
        if (index < 0 || index >= array.length) {
            throw "Index out of bounds: " + index;
        }
        return array[index];
    } catch (error) {
        print("Error: " + error);
        return null;
    }
}
```

### Type Conversion Errors

```vg
function parseInteger(str) {
    try {
        return Integer.toInt(str);
    } catch (error) {
        print("Could not convert '" + str + "' to an integer");
        return 0;
    }
}
```

## Error Handling in GUI Applications

In GUI applications, it's important to show user-friendly error messages:

```vg
import Guilibrary.window;

try {
    ## Perform operation that might fail
    saveUserData(userData);
} catch (error) {
    ## Show error dialog to user
    window.showErrorPopup(mainWindow, "Save Failed", "Could not save user data: " + error);
}
``` 