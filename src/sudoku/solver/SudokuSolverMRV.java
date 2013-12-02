package sudoku.solver;

public class SudokuSolverMRV extends AbstractSudokuSolver {

	public SudokuSolverMRV(String inputFileName) {
		super(inputFileName);
	}

	/**
	 * Returns the position of cell that has minimum remaining allowed values.
	 * 
	 * @return - int[2] representing x and y positions of cell
	 */
	protected int[] getNextBoxToFill() {
		int[] pos = new int[2];
		int minX = -1, minY = -1;
		int minAllowedVals = N + 1, noOfAllowedVals;

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (board[i][j] == 0) {
					noOfAllowedVals = 0;
					for (int k = 0; k < N; k++) {
						if (allowed[i][j][k])
							noOfAllowedVals++;
					}
					if (noOfAllowedVals < minAllowedVals) {
						minAllowedVals = noOfAllowedVals;
						minX = i;
						minY = j;
					}
				}
			}
		}

		pos[0] = minX;
		pos[1] = minY;
		return pos;
	}

	/**
	 * Uses Minimum Remaining Value heuristic to solve given Sudoku board.
	 * 
	 * @return true if the problem can be solved at given configuration
	 */
	public boolean backtrackAndSolveWithMRV() {
		recursionDepth++;

		// choose the cell which has minimum allowed values
		int[] nextBoxToFill = getNextBoxToFill();
		int x = nextBoxToFill[0];
		int y = nextBoxToFill[1];
		if (x == -1 && y == -1) // no more cells to fill
			return true;

		for (int i = 0; i < N; i++) { // try all values
			if (allowed[x][y][i]) {
				boolean[][][] tempAllowed = new boolean[N][N][N]; // backup
																	// current
																	// allowed
																	// values
				getAllowedCopy(tempAllowed, allowed);
				setValue(i + 1, x, y); // sets board[x][y] and updates allowed
										// values
				if (backtrackAndSolveWithMRV()) {
					return true;
				}
				board[x][y] = 0;
				getAllowedCopy(allowed, tempAllowed); // restore allowed values
			}
		}

		return false; // no more values to set
	}

	public void solve() {
		long startTime = System.currentTimeMillis();
		if (!backtrackAndSolveWithMRV()) {
			System.out.println("No solution..");
		}
		timeTakenToSolve = System.currentTimeMillis() - startTime;
	}
}
