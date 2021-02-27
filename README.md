# CSP-Graph-Coloring
## An exmple solution for graph coloring problem
In this project backtracking search was used with AC3 which provides constraint propagation. AC3 is invoked each time a variable is assigned a new value. MRV heuristic is used when choosing the next variable. When there are more than 1 variable with the minimum domain size, then the variable is chosen based on the degree of it. LCV heuristic is used to select a value when there are more than 1 remaining value in the domain of a variable.
To start the program:
1. navigate to the project directory
2. type "java -classpath C:\Users\Admin\IdeaProjects\ai_p2\out\production\ai_p2 project2.Main sample_input_1.txt"

Example is given for Windows OS. Please use the correct path for your own OS.

There are 2 sample input files in the project directory.
New input file can be created based on the format of these files.
