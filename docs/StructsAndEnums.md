# Structs and Enums in VG Language

## Structs

Structs allow you to create custom data types with named fields.

### Defining a Struct

```vg
struct Person {
    name,
    age,
    email
}
```

### Creating Struct Instances

```vg
var john = Person{
    name: "John Doe",
    age: 30,
    email: "john@example.com"
};
```

### Accessing Struct Fields

```vg
print(john.name);  ## Output: John Doe
print(john.age);   ## Output: 30
```

### Modifying Struct Fields

```vg
john.age = 31;
print(john.age);   ## Output: 31
```

### Struct Documentation

You can document structs using special comment blocks:

```vg
/## Represents a person with basic information
# @field name The person's full name
# @field age The person's age in years
# @field email The person's email address
# @author Hussein Abdul-Ameer
##/
struct Person {
    name,
    age,
    email
}
```

## Enums

Enums define a set of named constants.

### Defining an Enum

```vg
enum Color {
    RED,
    GREEN,
    BLUE
}
```

### Using Enum Values

```vg
var selectedColor = Color.RED;

if (selectedColor == Color.RED) {
    print("The color is red");
}
```

### Enums with Custom Values

You can assign specific values to enum constants:

```vg
enum HttpStatus {
    OK = 200,
    NOT_FOUND = 404,
    SERVER_ERROR = 500
}

print(HttpStatus.NOT_FOUND);  ## Output: 404
```

### Enum Documentation

You can document enums using special comment blocks:

```vg
/## HTTP status codes
# @value OK Successful response (200)
# @value NOT_FOUND Resource not found (404)
# @value SERVER_ERROR Internal server error (500)
# @author Hussein Abdul-Ameer
##/
enum HttpStatus {
    OK = 200,
    NOT_FOUND = 404,
    SERVER_ERROR = 500
}
```

## Using Structs and Enums Together

Structs can contain enum fields:

```vg
enum UserRole {
    ADMIN,
    EDITOR,
    VIEWER
}

struct User {
    username,
    role,
    active
}

var user = User{
    username: "admin",
    role: UserRole.ADMIN,
    active: true
};

if (user.role == UserRole.ADMIN) {
    print(user.username + " has admin privileges");
}
```

## Arrays of Structs

You can create arrays of struct instances:

```vg
var users = [
    Person{name: "Alice", age: 25, email: "alice@example.com"},
    Person{name: "Bob", age: 30, email: "bob@example.com"},
    Person{name: "Charlie", age: 35, email: "charlie@example.com"}
];

for (var i = 0; i < 3; i = i + 1) {
    print(users[i].name + " is " + users[i].age + " years old");
}
```

## Practical Examples

### Game Character Example

```vg
enum CharacterClass {
    WARRIOR,
    MAGE,
    ARCHER
}

struct GameCharacter {
    name,
    class,
    level,
    health,
    mana
}

var player = GameCharacter{
    name: "Aragorn",
    class: CharacterClass.WARRIOR,
    level: 10,
    health: 100,
    mana: 20
};

print("Player " + player.name + " is a level " + player.level + " warrior");
```

### Configuration Settings Example

```vg
enum LogLevel {
    DEBUG = 0,
    INFO = 1,
    WARNING = 2,
    ERROR = 3
}

struct AppConfig {
    appName,
    version,
    logLevel,
    maxUsers,
    isDebugMode
}

var config = AppConfig{
    appName: "MyApp",
    version: "1.0.0",
    logLevel: LogLevel.INFO,
    maxUsers: 100,
    isDebugMode: false
};

if (config.logLevel <= LogLevel.DEBUG) {
    print("Debug logging is enabled");
}
``` 