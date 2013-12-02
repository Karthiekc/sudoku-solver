package sudoku.solver;

public class SudokuSolverMRVWithFC extends SudokuSolverMRV {

	public SudokuSolverMRVWithFC(String inputFileName) {
		super(inputFileName);
	}

	public boolean solveWithMRVAndFC() {
		recursionDepth++;
		int[] nextBoxToFill = getNextBoxToFill();
		int x = nextBoxToFill[0];
		int y = nextBoxToFill[1];
		if (x == -1 && y == -1)
			return true; // no more boxes to fill

		for (int i = 0; i < N; i++) {
			if (allowed[x][y][i] && forwardCheck(x, y, i + 1)) {
				boolean[][][] tempAllowed = new boolean[N][N][N];
				getAllowedCopy(tempAllowed, allowed);
				setValue(i + 1, x, y);
				if (solveWithMRVAndFC()) {
					return true;
				}
				board[x][y] = 0;
				getAllowedCopy(allowed, tempAllowed);
			}
		}
		return false;
	}

	public void solve() {
		long startTime = System.currentTimeMillis();
		if (!solveWithMRVAndFC()) {
			System.out.println("No solution..");
		}
		timeTakenToSolve = System.currentTimeMillis() - startTime;
	}

	private boolean noMoreValuesAllowedForCol(boolean[][][] tempAllowed, int x,
			int y) {
		for (int i = 0; i < N; i++) {
			if (i != x) {
				for (int k = 0; k < N; k++) {
					if (tempAllowed[i][y][k])
						return false;
				}
			}
		}
		return true;
	}

	private boolean noMoreValuesAllowedForRow(boolean[][][] tempAllowed, int x,
			int y) {
		for (int j = 0; j < N; j++) {
			if (j != y) {
				for (int k = 0; k < N; k++) {
					if (tempAllowed[x][j][k])
						return false;
				}
			}
		}
		return true;
	}

	private boolean noMoreValuesAllowedForBox(boolean[][][] tempAllowed, int x,
			int y) {
		for (int k = 0; k < N; k++) {
			if (tempAllowed[x][y][k])
				return false;
		}
		return true;
	}

	private boolean noMoreValuesAllowedForSubSquare(boolean[][][] tempAllowed,
			int x, int y) {
		int xStart = M * (int) (x / M);
		int yStart = K * (int) (y / K);
		for (int i = xStart; i < xStart + M; i++) {
			for (int j = yStart; j < yStart + K; j++) {
				if (i == x && j == y)
					continue;
				if (noMoreValuesAllowedForBox(tempAllowed, i, j))
					return true;
			}
		}
		return false;
	}

	private boolean forwardCheck(int x, int y, int value) {
		boolean[][][] tempAllowed = new boolean[N][N][N];
		getAllowedCopy(tempAllowed, allowed);

		// set allowed[][][value-1] = false in rows and cols
		for (int i = 0; i < N; i++) {
			tempAllowed[x][i][value - 1] = false;
			tempAllowed[i][y][value - 1] = false;
		}

		if (noMoreValuesAllowedForRow(tempAllowed, x, y)
				|| noMoreValuesAllowedForCol(tempAllowed, x, y))
			return false;

		// MxK block
		int xStart = M * (int) (x / M);
		int yStart = K * (int) (y / K);
		for (int i = xStart; i < xStart + M; i++) {
			for (int j = yStart; j < yStart + K; j++) {
				tempAllowed[i][j][value - 1] = false;
			}
		}

		if (noMoreValuesAllowedForSubSquare(tempAllowed, x, y))
			return false;

		return true;
	}
}
