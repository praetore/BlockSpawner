#!/bin/sh

NAME=BlockSpawner;

# 1. Compile
echo "Compiling with javac..."
mkdir -p bin/;
javac -Xlint:deprecation src/*/*/*.java -d bin -classpath lib/*: -sourcepath src -target 1.7 -g:lines,vars,source -source 1.7

# 2. Build the jar
echo "Creating jar file..."
mkdir -p dist/
jar -cf dist/"$NAME.jar" src/*/*/*.yml -C bin .