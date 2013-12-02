package sudoku.solver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public abstract class AbstractSudokuSolver implements ISudokuSolver {

	// Dimension of board
	int N;

	// Dimensions of sub-squares (or sub-rectangles)
	int M, K;

	// NxN board
	int[][] board;

	// NxNxN array to maintain allowed values
	// allowed[i][j][k] == false implies that the value k
	// is not allowed on cell (i,j)
	boolean[][][] allowed;

	// for statistics
	long recursionDepth = 0;

	// for statistics; helps to evaluate toughness of problem
	int numOfValuesSet = 0;

	// time to solve the problem
	long timeTakenToSolve = 0;

	protected AbstractSudokuSolver(String inputFileName) {
		readInput(inputFileName);
		initAllowedValues();
	}

	/**
	 * Read board configuration from input file
	 */
	public void readInput(String inputFile) {
		try {
			InputStream in = new FileInputStream(new File(inputFile));
			Scanner scanner = new Scanner(in);
			N = scanner.nextInt();
			M = scanner.nextInt();
			K = scanner.nextInt();

			board = new int[N][N];
			allowed = new boolean[N][N][N];

			String input = scanner.nextLine();
			while (input.isEmpty())
				input = scanner.nextLine();

			String[] arr = input.split(" ");
			int index = 0;
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					int value = Integer.parseInt(arr[index++]);
					if (value != 0) {
						// setValue(value, i, j);
						board[i][j] = value;
						numOfValuesSet++;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void initAllowedValues() {
		allowed = new boolean[N][N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				for (int k = 0; k < N; k++) {
					allowed[i][j][k] = true;
				}
			}
		}

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (board[i][j] != 0)
					updateAllowedValues(i, j, board[i][j]);
			}
		}
	}

	private void updateAllowedValues(int x, int y, int value) {
		// set allowed[][][value-1] = false in rows and cols
		for (int i = 0; i < N; i++) {
			allowed[x][i][value - 1] = false;
			allowed[i][y][value - 1] = false;
		}

		// update allowed values for MxK block
		int xStart = M * (int) (x / M);
		int yStart = K * (int) (y / K);
		for (int i = xStart; i < xStart + M; i++) {
			for (int j = yStart; j < yStart + K; j++) {
				allowed[i][j][value - 1] = false;
			}
		}

		// no values are allowed anymore
		for (int i = 0; i < N; i++)
			allowed[x][y][i] = false;

		allowed[x][y][value - 1] = true;
	}

	public void setValue(int value, int x, int y) {
		board[x][y] = value;
		updateAllowedValues(x, y, value);
	}

	public long getRecursionDepth() {
		return recursionDepth;
	}

	protected void printBoard() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * randomly create a sudoku problem
	 * 
	 * @param numOfValues
	 *            - number of values to be filled initially
	 */
	public void randomGenerate(int numOfValues) {
		int x, y, value;
		boolean isSet;

		for (int i = 0; i < numOfValues; i++) {
			isSet = false;
			while (!isSet) {
				x = (int) (Math.random() * (double) N);
				y = (int) (Math.random() * (double) N);

				if (board[x][y] != 0)
					continue;

				value = (int) (Math.random() * (double) N) + 1;
				while (!allowed[x][y][value - 1]) {
					value = (int) (Math.random() * (double) N) + 1;
				}

				setValue(value, x, y);
				isSet = true;
			}
		}
	}

	/**
	 * Abstract method to be implemented by concrete class that solves the
	 * problem.
	 */
	public abstract void solve();

	/**
	 * Get a deep copy of allowed array
	 * 
	 * @param newBoard
	 * @param oldBoard
	 */
	protected void getAllowedCopy(boolean[][][] newAllowed,
			boolean[][][] oldAllowed) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				for (int k = 0; k < N; k++) {
					newAllowed[i][j][k] = oldAllowed[i][j][k];
				}
			}
		}
	}

	public void printInitialStatistics() {
		System.out.println("Initial configuration:\n ");
		printBoard();
		System.out
				.println("Number of filled squares: " + numOfValuesSet + "\n");
	}

	public void printFinalStatistics() {
		System.out.println("After completing: \n");
		printBoard();
		System.out.println("No. of times backtracked: " + recursionDepth);
		System.out.println("Time taken to solve (in milliseconds): "
				+ timeTakenToSolve);
	}
}
