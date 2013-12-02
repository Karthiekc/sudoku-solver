package sudoku.solver;

import java.util.ArrayList;

public class SudokuSolverWithCP extends SudokuSolverMRV {

	public SudokuSolverWithCP(String inputFileName) {
		super(inputFileName);
	}

	/**
	 * Uses constraint propagation to check if a value can be set for a cell.
	 * Constraint propagation reduces number of wrong choices drastically.
	 * 
	 * @return
	 */
	public boolean solveWithMRVAndCP() {
		recursionDepth++;
		int[] nextBoxToFill = getNextBoxToFill();
		int x = nextBoxToFill[0];
		int y = nextBoxToFill[1];

		if (x == -1 && y == -1) // no more boxes to fill
			return true;

		for (int i = 0; i < N; i++) {
			if (allowed[x][y][i] && canAssignWithCP(x, y, i + 1)) {
				boolean[][][] tempAllowed = new boolean[N][N][N];
				getAllowedCopy(tempAllowed, allowed);
				setValue(i + 1, x, y);
				if (solveWithMRVAndCP()) {
					return true;
				}
				board[x][y] = 0;
				getAllowedCopy(allowed, tempAllowed);
			}
		}
		return false;
	}

	public void solve() {
		if (!solveWithMRVAndCP()) {
			System.out.println("No solution..");
		}
	}

	private ArrayList<Integer> getAllowedValues(boolean[][][] tempAllowed,
			int x, int y) {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for (int i = 0; i < N; i++) {
			if (tempAllowed[x][y][i])
				arr.add(i + 1);
		}
		return arr;
	}

	private boolean canAssignWithCP(int x, int y, int value) {
		boolean[][][] tempAllowed = new boolean[N][N][N];
		getAllowedCopy(tempAllowed, allowed);
		return assign(tempAllowed, x, y, value);
	}

	/**
	 * Returns false if assigning given value for cell (x,y) results in no more
	 * possible values for some cell.
	 */
	private boolean assign(boolean[][][] tempAllowed, int x, int y, int value) {
		for (int i = 0; i < N; i++) {
			if (i != (value - 1) && tempAllowed[x][y][i]
					&& !eliminate(tempAllowed, x, y, i + 1))
				return false;
		}
		return true;
	}

	private boolean eliminate(boolean[][][] tempAllowed, int x, int y,
			int valueToEliminate) {
		int i, j;

		if (!tempAllowed[x][y][valueToEliminate - 1])
			return true; // already eliminated

		tempAllowed[x][y][valueToEliminate - 1] = false;

		ArrayList<Integer> allowedValues = getAllowedValues(tempAllowed, x, y);

		if (allowedValues.size() == 0)
			return false; // removed last value

		if (allowedValues.size() == 1) {
			// check if we can eliminate allowed value from peers of x,y
			// if not return false

			int newValueToEliminate = allowedValues.get(0);

			// try to eliminate from cells in same row and col
			for (i = 0; i < N; i++) {
				if (i != y
						&& !eliminate(tempAllowed, x, i, newValueToEliminate))
					return false;
				if (i != x
						&& !eliminate(tempAllowed, i, y, newValueToEliminate))
					return false;
			}

			// try to eliminate from cells in same sub-square
			int xStart = M * (int) (x / M);
			int yStart = K * (int) (y / K);
			for (i = xStart; i < xStart + M; i++) {
				for (j = yStart; j < yStart + K; j++) {
					if (i != x
							&& j != y
							&& !eliminate(tempAllowed, i, j,
									newValueToEliminate))
						return false;
				}
			}
		}

		return true;
	}
}
