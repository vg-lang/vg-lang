## VG Library Auto-Import Feature Demo

**NEW FEATURE: Automatic .vglib File Import**

You can now automatically import .vglib files into your project using the IDE!

## How it works:

1. **Go to Libraries → Import .vglib File**
2. **Select any .vglib file** (from libraries/, packages/, or anywhere)
3. **Preview the parsed structure** - see all namespaces and functions
4. **Choose import options:**
   - Overwrite existing libraries
   - Copy .vglib file to project packages folder
5. **Click Import** - automatically creates the library structure

## What gets automatically extracted:

✅ **Library name** (from `library LibraryName {`)
✅ **All namespaces** (from `namespace NamespaceName {`)  
✅ **All functions** (from `function functionName(...)`)
✅ **Proper structure** for project-specific libraries

## Example: Importing Util.vglib

**Parsed Structure:**
```
library Util {
  namespace Type {
    function getType()
  }
  namespace String {
    function toString()
    function indexOf()
    function substring()
    function stringLength()
    function toUpper()
    function fromCharCode()
  }
  // ... etc
}
```

**Result:** Ready to use immediately!
```vg
import Util.String.*;

function main() {
    var str = toString(123);
    var upper = toUpper("hello");
    var index = indexOf("hello world", "world");
}
```

## No more manual entry!

Instead of manually typing each namespace and function, just:
- Select the .vglib file
- Click Import
- Start coding immediately

**The parser handles:**
- Complex nested structures
- Comment removal
- Multiple namespaces
- Duplicate function detection
- Error validation
