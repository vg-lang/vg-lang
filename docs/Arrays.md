# Arrays in VG Language

Arrays in VG Language store multiple values in a single variable. They can hold different data types, and they support both **one-dimensional** and **nested arrays**.

## **One-Dimensional Arrays**

A simple array stores elements of any type:

```vg
var numbers = [1, 2, 3, 4, 5];
print(numbers[2]); ## Output: 3

var names = ["Alice", "Bob", "Charlie"];
print(names[0]); ## Output: Alice

var mixed = [10, "Hello", true, 3.14];
print(mixed[1]); ## Output: Hello
```
## Nested Arrays
```vg
var matrix = [
    [1, 2, 3],
    [4, 5, 6],
    [7, 8, 9]
];

print(matrix[1][2]); ## Output: 6

var complexArray = [
    ["Alice", 25, true],
    ["Bob", 30, false]
];

print(complexArray[0][0]); ## Output: Alice
print(complexArray[1][1]); ## Output: 30

```
## Modifying Arrays
```vg
var items = ["apple", "banana", "cherry"];
items[1] = "blueberry";

print(items); ## Output: ["apple", "blueberry", "cherry"]

```