#!/bin/sh
TEST_PATH="tests/"
TEST_FILENAME="test"
COMPILER_PATH="code/"
COMPILER_FILENAME="Compiler"

# compile all java files in current directory
# javac code/*.java

for i in {1..7}
do
  echo
  echo -------------------------------------------------------------------------------
  echo Test $i
  echo -------------------------------------------------------------------------------
  java $COMPILER_PATH$COMPILER_FILENAME $TEST_PATH$TEST_FILENAME$i".py"
  echo 
  echo
done
