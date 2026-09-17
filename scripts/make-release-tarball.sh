#!/usr/bin/env bash

set -euo pipefail

if [ $# -ne 1 ]; then
    echo "Usage: $0 <version>   e.g. $0 1.5.1"
    exit 1
fi
VERSION="$1"

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
DIST_DIR="$REPO_ROOT/dist"
STAGE="$DIST_DIR/vg-lang-$VERSION"

VG_JAR="$REPO_ROOT/interpreter/target/vg.jar"
VGPKG_JAR="$REPO_ROOT/packagemanager/target/vgpkg.jar"
LIBRARIES="$REPO_ROOT/libraries"
CONFIG="$REPO_ROOT/installer/config"

for f in "$VG_JAR" "$VGPKG_JAR" "$LIBRARIES" "$CONFIG"; do
    if [ ! -e "$f" ]; then
        echo "Missing: $f"
        echo "Did you run 'mvn package' first?"
        exit 1
    fi
done

rm -rf "$STAGE"
mkdir -p "$STAGE"
cp "$VG_JAR" "$STAGE/vg.jar"
cp "$VGPKG_JAR" "$STAGE/vgpkg.jar"
cp -r "$LIBRARIES" "$STAGE/libraries"
mkdir -p "$STAGE/config"
cp "$CONFIG/allowed_configurations.vgenv" "$STAGE/config/allowed_configurations.vgenv"

cd "$DIST_DIR"
TARBALL="vg-lang-$VERSION.tar.gz"
tar -czf "$TARBALL" "vg-lang-$VERSION"

echo ""
echo "Built: $DIST_DIR/$TARBALL"
echo ""
echo "sha256:"
shasum -a 256 "$TARBALL" 2>/dev/null || sha256sum "$TARBALL"
echo ""
echo "Next steps:"
echo "  1. Create a GitHub Release tagged v$VERSION on vg-lang"
echo "  2. Upload $TARBALL as a release asset"
echo "  3. Update Formula/vg-lang.rb in your tap: bump the url version and"
echo "     paste the sha256 above"
