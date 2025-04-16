# Libraries in VG Language

VG Language comes with a rich set of built-in libraries that provide additional functionality for your programs.

## Importing Libraries

To use a library in your VG program, use the `import` statement:

```vg
import LibraryName.namespace;
```

You can import specific namespaces or functions from a library:

```vg
import MathLib.arithmetic;  ## Import the arithmetic namespace
import Guilibrary.window.create;  ## Import a specific function
```

## Standard Libraries

### MathLib

Mathematical functions and constants.

#### Namespace: arithmetic

Functions for basic arithmetic operations.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `add(a, b)` | Adds two numbers | `a`: First number<br>`b`: Second number | Sum of a and b | `arithmetic.add(5, 10)` → `15` |
| `subtract(a, b)` | Subtracts second number from first | `a`: First number<br>`b`: Second number | Difference of a and b | `arithmetic.subtract(20, 5)` → `15` |
| `multiply(a, b)` | Multiplies two numbers | `a`: First number<br>`b`: Second number | Product of a and b | `arithmetic.multiply(4, 3)` → `12` |
| `divide(a, b)` | Divides first number by second | `a`: First number<br>`b`: Second number | Quotient of a and b | `arithmetic.divide(10, 2)` → `5` |
| `abs(x)` | Gets absolute value | `x`: Number | Absolute value of x | `arithmetic.abs(-15)` → `15` |

#### Namespace: constants

Mathematical constants.

| Constant | Description | Return Value | Example |
|----------|-------------|--------------|---------|
| `pi()` | Returns the value of π | Approximately 3.14159 | `constants.pi()` → `3.14159...` |
| `e()` | Returns the value of e | Approximately 2.71828 | `constants.e()` → `2.71828...` |

#### Usage Example

```vg
import MathLib.arithmetic;
import MathLib.constants;

var pi = constants.pi();
var sum = arithmetic.add(5, 10);
var difference = arithmetic.subtract(20, 5);
var product = arithmetic.multiply(4, 3);
var quotient = arithmetic.divide(10, 2);
var absoluteValue = arithmetic.abs(-15);
```

### Guilibrary

GUI components for creating graphical applications.

#### Namespace: window

Functions for creating and managing windows.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `create(title, width, height)` | Creates a new window | `title`: Window title<br>`width`: Window width<br>`height`: Window height | Window object | `window.create("My App", 800, 600)` |
| `setBackgroundColor(window, color)` | Sets window background color | `window`: Window object<br>`color`: Color (hex code) | None | `window.setBackgroundColor(gameWindow, "#87CEEB")` |
| `addComponentToWindow(window, component)` | Adds a component to window | `window`: Window object<br>`component`: UI component | None | `window.addComponentToWindow(gameWindow, image)` |
| `launch(window)` | Displays the window | `window`: Window object | None | `window.launch(gameWindow)` |

#### Namespace: Image

Functions for creating and manipulating images.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `createImage(path, x, y, width, height)` | Creates an image component | `path`: Image file path<br>`x`: X position<br>`y`: Y position<br>`width`: Width<br>`height`: Height | Image object | `Image.createImage("logo.png", 100, 100, 200, 200)` |

#### Namespace: IOEvents

Functions for handling user input events.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `setOnKeyPress(window, callback)` | Sets key press event handler | `window`: Window object<br>`callback`: Function to call when key is pressed | None | See example below |

#### Namespace: Sound

Functions for playing audio.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `playSound(path)` | Plays a sound file | `path`: Sound file path | None | `Sound.playSound("click.wav")` |

#### Usage Example

```vg
import Guilibrary.window;
import Guilibrary.Image;
import Guilibrary.IOEvents;
import Guilibrary.Sound;

## Create a window
var gameWindow = window.create("My Application", 800, 600);
window.setBackgroundColor(gameWindow, "#87CEEB");

## Add components
var image = Image.createImage("assets/logo.png", 100, 100, 200, 200);
window.addComponentToWindow(gameWindow, image);

## Set up event handling
IOEvents.setOnKeyPress(gameWindow, function(key) {
    if (key == "SPACE") {
        Sound.playSound("assets/click.wav");
    }
});

## Display the window
window.launch(gameWindow);
```

### RegexLib

Regular expression operations.

#### Namespace: regex

Functions for pattern matching and text manipulation.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `findAll(text, pattern)` | Finds all matches of a pattern | `text`: Input text<br>`pattern`: Regex pattern | Array of matches | `regex.findAll("Hello world", "\\w+")` |
| `matches(text, pattern)` | Checks if text matches pattern | `text`: Input text<br>`pattern`: Regex pattern | Boolean | `regex.matches("test@example.com", "^[\\w.]+@[\\w.]+\\.[a-z]{2,}$")` |
| `replaceAll(text, pattern, replacement)` | Replaces all matches | `text`: Input text<br>`pattern`: Regex pattern<br>`replacement`: Replacement text | Modified text | `regex.replaceAll("Hello world", "world", "universe")` |

#### Usage Example

```vg
import RegexLib.regex;

## Find all matches
var matches = regex.findAll("Hello world", "\\w+");

## Check if a string matches a pattern
var isMatch = regex.matches("test@example.com", "^[\\w.]+@[\\w.]+\\.[a-z]{2,}$");

## Replace text
var newText = regex.replaceAll("Hello world", "world", "universe");
```

### SQLiteLib

Database operations with SQLite.

#### Namespace: db

Functions for database operations.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `openDB(path)` | Opens or creates a database | `path`: Database file path | Database connection | `db.openDB("mydata.db")` |
| `runStatement(db, sql)` | Executes SQL statement | `db`: Database connection<br>`sql`: SQL statement | None | `db.runStatement(database, "CREATE TABLE users (id INTEGER, name TEXT)")` |
| `runQuery(db, sql)` | Executes SQL query | `db`: Database connection<br>`sql`: SQL query | Query results | `db.runQuery(database, "SELECT * FROM users")` |
| `closeDB(db)` | Closes database connection | `db`: Database connection | None | `db.closeDB(database)` |

#### Usage Example

```vg
import SQLiteLib.db;

## Open or create a database
var database = db.openDB("mydata.db");

## Create a table
db.runStatement(database, "CREATE TABLE IF NOT EXISTS users (id INTEGER, name TEXT)");

## Insert data
db.runStatement(database, "INSERT INTO users VALUES (1, 'John')");

## Query data
var results = db.runQuery(database, "SELECT * FROM users");
print(results);

## Close the database
db.closeDB(database);
```

### DateTime

Date and time functions.

#### Namespace: timestamp

Functions for working with timestamps.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `currentTimeMillis()` | Gets current time in milliseconds | None | Current timestamp | `timestamp.currentTimeMillis()` |

#### Namespace: date

Functions for working with dates.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `getYear()` | Gets current year | None | Current year | `date.getYear()` |
| `getMonth()` | Gets current month | None | Current month (1-12) | `date.getMonth()` |
| `getDay()` | Gets current day | None | Current day of month | `date.getDay()` |

#### Namespace: time

Functions for working with time.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `getHour()` | Gets current hour | None | Current hour (0-23) | `time.getHour()` |
| `getMinute()` | Gets current minute | None | Current minute (0-59) | `time.getMinute()` |
| `getSecond()` | Gets current second | None | Current second (0-59) | `time.getSecond()` |

#### Usage Example

```vg
import DateTime.timestamp;
import DateTime.date;
import DateTime.time;

## Get current timestamp
var now = timestamp.currentTimeMillis();

## Get date components
var year = date.getYear();
var month = date.getMonth();
var day = date.getDay();

## Get time components
var hour = time.getHour();
var minute = time.getMinute();
var second = time.getSecond();
```

### IO

File and console input/output operations.

#### Namespace: File

Functions for file operations.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `readFile(path)` | Reads file contents | `path`: File path | File contents as string | `File.readFile("input.txt")` |
| `writeFile(path, content)` | Writes to a file | `path`: File path<br>`content`: Content to write | None | `File.writeFile("output.txt", "Hello, world!")` |
| `appendToFile(path, content)` | Appends to a file | `path`: File path<br>`content`: Content to append | None | `File.appendToFile("log.txt", "Program executed")` |

#### Namespace: Prompt

Functions for user input.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `input(prompt)` | Gets user input | `prompt`: Text to display | User input as string | `Prompt.input("Enter your name: ")` |

#### Usage Example

```vg
import IO.File;
import IO.Prompt;

## Read user input
var name = Prompt.input("Enter your name: ");
print("Hello, " + name);

## File operations
File.writeFile("output.txt", "Hello, world!");
var content = File.readFile("input.txt");
File.appendToFile("log.txt", "Program executed at " + DateTime.timestamp.currentTimeMillis());
```

### OSLib

Operating system information and environment variables.

#### Namespace: sys

Functions for system information.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `getOSName()` | Gets operating system name | None | OS name as string | `sys.getOSName()` |
| `getUserHome()` | Gets user home directory | None | Path as string | `sys.getUserHome()` |
| `getUserDir()` | Gets current directory | None | Path as string | `sys.getUserDir()` |
| `getEnv(name)` | Gets environment variable | `name`: Variable name | Variable value | `sys.getEnv("PATH")` |

#### Usage Example

```vg
import OSLib.sys;

## Get OS information
var osName = sys.getOSName();
print("Running on: " + osName);

## Get environment variables
var homePath = sys.getUserHome();
var currentDir = sys.getUserDir();
var envVar = sys.getEnv("PATH");
```

### Arrays

Array manipulation utilities.

#### Namespace: array

Functions for working with arrays.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `getLength(arr)` | Gets array length | `arr`: Array | Length as integer | `array.getLength(myArray)` |
| `push(arr, item)` | Adds item to end of array | `arr`: Array<br>`item`: Item to add | Modified array | `array.push(myArray, 4)` |
| `pop(arr)` | Removes last item from array | `arr`: Array | Removed item | `array.pop(myArray)` |
| `unshift(arr, item)` | Adds item to start of array | `arr`: Array<br>`item`: Item to add | Modified array | `array.unshift(myArray, 0)` |
| `shift(arr)` | Removes first item from array | `arr`: Array | Removed item | `array.shift(myArray)` |

#### Usage Example

```vg
import Arrays.array;

## Create an array
var myArray = [1, 2, 3];

## Get array length
var length = array.getLength(myArray);

## Modify arrays
array.push(myArray, 4);  ## Add to end: [1, 2, 3, 4]
var lastItem = array.pop(myArray);  ## Remove from end: [1, 2, 3]
array.unshift(myArray, 0);  ## Add to beginning: [0, 1, 2, 3]
var firstItem = array.shift(myArray);  ## Remove from beginning: [1, 2, 3]
```

### Util

Utility functions for type conversion and string operations.

#### Namespace: Type

Functions for type checking.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `getType(value)` | Gets type of a value | `value`: Any value | Type as string | `Type.getType("Hello")` → `"string"` |

#### Namespace: String

Functions for string operations.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `toString(value)` | Converts value to string | `value`: Any value | String representation | `String.toString(123)` → `"123"` |
| `stringLength(str)` | Gets string length | `str`: String | Length as integer | `String.stringLength("Hello")` → `5` |
| `indexOf(str, substr)` | Finds position of substring | `str`: String<br>`substr`: Substring to find | Position as integer | `String.indexOf("Hello World", "World")` → `6` |
| `substring(str, start, end)` | Extracts part of string | `str`: String<br>`start`: Start index<br>`end`: End index | Substring | `String.substring("Hello World", 0, 5)` → `"Hello"` |
| `toUpper(str)` | Converts string to uppercase | `str`: String | Uppercase string | `String.toUpper("hello")` → `"HELLO"` |

#### Namespace: Integer

Functions for integer operations.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `toInt(str)` | Converts string to integer | `str`: String | Integer value | `Integer.toInt("42")` → `42` |

#### Namespace: Double

Functions for double operations.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `toDouble(str)` | Converts string to double | `str`: String | Double value | `Double.toDouble("3.14")` → `3.14` |

#### Namespace: Boolean

Functions for boolean operations.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `toBoolean(str)` | Converts string to boolean | `str`: String | Boolean value | `Boolean.toBoolean("true")` → `true` |

#### Usage Example

```vg
import Util.Type;
import Util.String;
import Util.Integer;
import Util.Double;
import Util.Boolean;

## Type checking
var type = Type.getType("Hello");  ## Returns "string"

## Type conversion
var intValue = Integer.toInt("42");
var doubleValue = Double.toDouble("3.14");
var boolValue = Boolean.toBoolean("true");
var strValue = String.toString(123);

## String operations
var length = String.stringLength("Hello");
var position = String.indexOf("Hello World", "World");
var part = String.substring("Hello World", 0, 5);
var upper = String.toUpper("hello");
```

### Random

Random number generation.

#### Namespace: number

Functions for generating random numbers.

| Function | Description | Parameters | Return Value | Example |
|----------|-------------|------------|--------------|---------|
| `getRandomInt(min, max)` | Generates random integer | `min`: Minimum value<br>`max`: Maximum value | Random integer | `number.getRandomInt(1, 100)` |
| `getRandomDouble(min, max)` | Generates random double | `min`: Minimum value<br>`max`: Maximum value | Random double | `number.getRandomDouble(0, 1)` |

#### Usage Example

```vg
import Random.number;

## Generate random numbers
var randomInt = number.getRandomInt(1, 100);  ## Random integer between 1 and 100
var randomDouble = number.getRandomDouble(0, 1);  ## Random double between 0 and 1
```

## Creating Custom Libraries

You can create your own libraries using the `library` keyword:

```vg
library MyLibrary {
    namespace utils {
        function greet(name) {
            return "Hello, " + name + "!";
        }
        
        function farewell(name) {
            return "Goodbye, " + name + "!";
        }
    }
    
    namespace math {
        function square(x) {
            return x * x;
        }
    }
}
```

Save this code in a file with the `.vglib` extension in the `libraries` or `projects/packages` directory to make it available for import in your VG programs.

## Library Documentation

When creating libraries, it's good practice to document your functions using documentation comments:

```vg
library MyLibrary {
    /## Utility functions for common operations
    # This namespace contains helper functions for everyday tasks.
    # @author Your Name
    ##/
    namespace utils {
        /## Greets a person by name
        # @param name The name of the person to greet
        # @return A greeting message
        ##/
        function greet(name) {
            return "Hello, " + name + "!";
        }
    }
}
```

## System Calls in Libraries

Libraries often use system calls to access Java functionality. These calls are restricted to a whitelist of allowed classes and methods defined in the configuration file.

```vg
## Example of a system call in a library function
function currentTimeMillis() {
    return VgSystemCall("java.lang.System", "currentTimeMillis");
}
```

## Library Namespaces

Libraries can contain multiple namespaces to organize related functionality:

```vg
library MyLibrary {
    namespace math {
        ## Math-related functions
    }
    
    namespace string {
        ## String manipulation functions
    }
    
    namespace file {
        ## File handling functions
    }
}
```

To use functions from different namespaces, import each namespace separately:

```vg
import MyLibrary.math;
import MyLibrary.string;

var squared = math.square(5);
var reversed = string.reverse("Hello");
```
