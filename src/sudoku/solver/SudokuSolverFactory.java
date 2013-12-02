package sudoku.solver;

public class SudokuSolverFactory {

	public static int TYPE_SIMPLE = 0, TYPE_MRV = 1, TYPE_MRV_WITH_FC = 2,
			TYPE_MRV_WITH_CP = 3;

	public static ISudokuSolver getSolver(String inputFile, int type) {
		switch (type) {
		case 1:
			return new SudokuSolverMRV(inputFile);
		case 2:
			return new SudokuSolverMRVWithFC(inputFile);
		case 3:
			return new SudokuSolverWithCP(inputFile);
		default:
			return new SudokuSolverSimple(inputFile);
		}
	}
}
