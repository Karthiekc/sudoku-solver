Sudoku Solver

Goal:

Solve a given Sudoku board.

Techniques:

1. Simple backtracking
2. Minimum Remaining Value (MRV) heuristic
3. MRV with Forward Checking
4. MRV with Constraint Propagation

For most of 9x9 board problems, simple backtracking works fine. As we move to larger board configurations, we see how simple simple backtracking can fail miserably. Techniques like MRV heuristic and Constraint Propagation helps immensely by helping us avoid bad choices along search path.

Code structure:

There are four types of Sudoku solvers. To run the code, pass input file path and the type of solver as command-line arguments. Type of solver can be 0, 1, 2, or 3.

Input file:

Refer sample input files.



