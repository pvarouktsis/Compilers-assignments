Semantic Analysis

First of all for Semantic Analysis we used 2 Hashtables:
symtable: stores the types of variables and functions
functable: stores the functions

We also have two visitors:
The first visitor parses the functions and only their parameters (it does not go through the statements). Also, defines and checks the variables.

The second visitor parses the operations (addition, subtraction, multiplication, division, modulo, power) and the function calls. So, the function definition will not be parsed again during the regular parsing of the Abstract Syntax Tree, but if the function call matches a function that already has been defined (that means that function is in functable, where we store the whole node of the function), we will parse the function using the node from the functable. 

The utils which contains helper methods.

The enum type which contains types of variable/function (NUMBER, STRING, NONE).
