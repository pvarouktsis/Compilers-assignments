Semantic Analysis

We implemented the Semantic Analysis based on the 7 tests cases.

First of all for Semantic Analysis we used 2 hashtables. One that stores the types of variables and (return) types of functions and a second one that stores the whole function "node".

We, also, implemented two visitors. The first one parses the functions and only their parameters to define them respectively (it does not go through the statements) and both defines and checks the variables (meaning identifiers). The second one parses the expressions-operations (addition, subtraction, multiplication, division, modulo, power) and the function calls. During the regular parse of the Abstrac Syntax Tree, the statements of function definition will not be parsed again, but if the function call matches a function that already has been defined (that means that function is in functable, where we store the whole node of the function), we will parse the function as it is in the functable using the arguments. Lastly, we implemented utils which contains helper methods and the enum type which contains the 3 possible types of a variable/function (NUMBER, STRING, NONE).
