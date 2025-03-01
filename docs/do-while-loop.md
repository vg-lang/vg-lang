# Do-While Loop in VG Language
The do-while loop in VG Language executes a block of code at least once, regardless of the condition.
It checks the condition after executing the loop body, making it different from a regular while loop.

```vg
var x = 0;
do {
    print(x);
    x = x + 1;
} while (x < 5);
```
