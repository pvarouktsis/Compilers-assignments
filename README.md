# Compilers-assignments

This is a repository for the purpose of Compilers Assignments. We implemented a miniPython "parser" using SableCC.

## Project Structure

- lib: SableCC
- code: Java Source Code
- tests: Tests

## Run

In order to run the tests, you need to compile the source code
```
javac code/*.java
```

and execute the script (from the parent directory of repository)
```
./tests/run_tests.sh
```

In order to run the tests individually, after the source code compilation,
```
java code/ParserTest tests/test*.py
```
where wildcard could be the number of test.

## Built with
[SableCC](https://sablecc.org/)

## Authors
- Alexis Lazellari
- Panagiotis Varouktsis
