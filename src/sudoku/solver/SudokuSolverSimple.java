package sudoku.solver;

public class SudokuSolverSimple extends AbstractSudokuSolver {

	public SudokuSolverSimple(String inputFileName) {
		super(inputFileName);
	}

	/**
	 * Solve by simple backtracking. Start solving from cell (x,y) in the board.
	 * All the cells prior to (x,y) in row-major order are filled before trying
	 * to fill this cell.
	 * 
	 * @param x
	 *            row id of cell
	 * @param y
	 *            column id of cell
	 * @return true of board can be solved at current configuration
	 */
	public boolean backtrackAndSolve(int x, int y) {
		recursionDepth++;
		if (x == N) {// reached end of board. done.
			return true;
		}

		if (board[x][y] == 0) { // if a cell is unassigned
			for (int i = 0; i < N; i++) { // try all possible values
				if (canSet(x, y, i + 1)) { // check if value can be set
					board[x][y] = i + 1; // set value
					// solve for next cell
					if (backtrackAndSolve((y == N - 1) ? x + 1 : x,
							(y == N - 1) ? 0 : y + 1)) {
						return true; // done
					}
					board[x][y] = 0; // reset value
				}
			}
			return false; // no values can be set
		} else { // try to fill next cell
			return backtrackAndSolve((y == N - 1) ? x + 1 : x, (y == N - 1) ? 0
					: y + 1);
		}
	}

	private boolean canSet(int x, int y, int value) {
		// check row
		for (int j = 0; j < N; j++) {
			if (j != y && board[x][j] == value)
				return false;
		}

		// check column
		for (int i = 0; i < N; i++) {
			if (i != x && board[i][y] == value)
				return false;
		}

		// check sub-square
		int xStart = M * (x / M), yStart = K * (y / K);
		for (int i = xStart; i < xStart + M; i++) {
			for (int j = yStart; j < yStart + K; j++) {
				if (i == x && j == y)
					continue;
				if (board[i][j] == value)
					return false;
			}
		}
		return true;
	}

	public void solve() {
		long startTime = System.currentTimeMillis();
		if (!backtrackAndSolve(0, 0)) {
			System.out
					.println("Wrong board configuration. No solution exists.");
		}
		timeTakenToSolve = System.currentTimeMillis() - startTime;
	}
}
