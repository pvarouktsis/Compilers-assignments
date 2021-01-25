#!/bin/sh
TEST_PATH="tests/"
TEST_FILENAME="test"

# compile all java files in current directory
javac *.java

for i in {1..7}
do
  echo
  echo -------------------------------------------------------------------------------
  echo Test $i
  echo -------------------------------------------------------------------------------
  java ParserTest3 $TEST_PATH$TEST_FILENAME$i".py"
  echo 
  echo
done

