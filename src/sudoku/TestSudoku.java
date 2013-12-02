package sudoku;

import sudoku.solver.ISudokuSolver;
import sudoku.solver.SudokuSolverFactory;

public class TestSudoku {
	public static void main(String args[]){
		if(args.length != 2){
			System.out.println("Input file and type of solver not specified as argument\n");
			return;
		}
		
		String inputFile = args[0];
		int solverType = Integer.parseInt(args[1]);
		
		ISudokuSolver solver = SudokuSolverFactory.getSolver(inputFile, solverType);
		solver.printInitialStatistics();
		solver.solve();
		solver.printFinalStatistics();
	}
}
