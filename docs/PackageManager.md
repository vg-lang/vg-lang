# Package Manager

VG Language includes a package manager (`vgpkg`) that allows you to install, remove, and manage libraries.

## Basic Commands

### Installing a Package

```sh
vgpkg install <PackageName>
```

Example:
```sh
vgpkg install mathlib
```

### Removing a Package

```sh
vgpkg remove <PackageName>
```

Example:
```sh
vgpkg remove mathlib
```

### Listing Installed Packages

```sh
vgpkg list
```

This command displays all currently installed packages and their versions.

### Viewing Available Packages

```sh
vgpkg available
```

This command shows all packages available in the official repository.

## Package Repository

Packages are fetched from the official VG Language package repository at:
`https://github.com/Husseinabdulameer11/vg-lang-packagemanager`

The repository contains a `packages.json` file that lists all available packages, their versions, and download URLs.

## Package Installation Location

Installed packages are stored in the `packages/` directory of your VG Language installation. Each package is stored as a `.vglib` file that can be imported into your programs.

The `packages/` directory is automatically created if it doesn't exist when:
- Running a VG program
- Installing a package
- Using any package manager commands

## Custom Package Location


```sh
vgpkg install mathlib 
```

## Creating Your Own Packages

You can create your own packages by:

1. Creating a `.vglib` file with your library code
2. Adding it to the `packages/` directory manually, or
3. Submitting it to the official repository for others to use

## Package Dependencies

When installing a package, any dependencies it requires will be automatically installed as well. 