import java.util.Random;
import java.util.Scanner;

public class Minesweeper {
    private static final int SIZE = 10;
    private static final int MINES = 10;
    private static final char COVERED_CELL = '-';
    private static final char MINE = 'X';
    private static final char EMPTY_CELL = ' ';
    private static final char FLAG = 'F';
    private static final char QUESTION_MARK = '?';

    private char[][] board;
    private char[][] mineBoard;
    private boolean[][] revealed;
    private boolean[][] flagged;
    private boolean[][] marked;

    public Minesweeper() {
        board = new char[SIZE][SIZE];
        mineBoard = new char[SIZE][SIZE];
        revealed = new boolean[SIZE][SIZE];
        flagged = new boolean[SIZE][SIZE];
        marked = new boolean[SIZE][SIZE];

        initializeBoard();
        placeMines();
        calculateNumbers();
    }

    private void initializeBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = COVERED_CELL;
            }
        }
    }

    private void placeMines() {
        Random random = new Random();

        int minesPlaced = 0;
        while (minesPlaced < MINES) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);

            if (mineBoard[row][col] != MINE) {
                mineBoard[row][col] = MINE;
                minesPlaced++;
            }
        }
    }

    private void calculateNumbers() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (mineBoard[row][col] == MINE) {
                    continue;
                }

                int count = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int newRow = row + i;
                        int newCol = col + j;

                        if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE) {
                            if (mineBoard[newRow][newCol] == MINE) {
                                count++;
                            }
                        }
                    }
                }

                if (count > 0) {
                    mineBoard[row][col] = (char) (count + '0');
                } else {
                    mineBoard[row][col] = EMPTY_CELL;
                }
            }
        }
    }

    private void printBoard() {
        System.out.print("  ");
        for (int col = 0; col < SIZE; col++) {
            System.out.print(col + " ");
        }
        System.out.println();

        for (int row = 0; row < SIZE; row++) {
            System.out.print(row + " ");
            for (int col = 0; col < SIZE; col++) {
                if (revealed[row][col]) {
                    System.out.print(mineBoard[row][col] + " ");
                } else if (flagged[row][col]) {
                    System.out.print(FLAG + " ");
                } else if (marked[row][col]) {
                    System.out.print(QUESTION_MARK + " ");
                } else {
                    System.out.print(board[row][col] + " ");
                }
            }
            System.out.println();
        }
    }

    private void revealCell(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || revealed[row][col]) {
            return;
        }

        revealed[row][col] = true;

        if (mineBoard[row][col] == MINE) {
            gameOver();
        } else if (mineBoard[row][col] == EMPTY_CELL) {
            revealNeighbors(row, col);
        }
    }

    private void revealNeighbors(int row, int col) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;

                if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE) {
                    revealCell(newRow, newCol);
                }
            }
        }
    }

    private void toggleFlag(int row, int col) {
        flagged[row][col] = !flagged[row][col];
    }

    private void toggleMark(int row, int col) {
        marked[row][col] = !marked[row][col];
    }

    private void gameOver() {
        System.out.println("Game Over!");
        printBoard();
        System.exit(0);
    }

    private void checkWin() {
        boolean allMinesFlagged = true;
        boolean allNonMinesRevealed = true;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (mineBoard[row][col] != MINE) {
                    if (!revealed[row][col]) {
                        allNonMinesRevealed = false;
                    }
                } else {
                    if (!flagged[row][col]) {
                        allMinesFlagged = false;
                    }
                }
            }
        }

        if (allMinesFlagged || allNonMinesRevealed) {
            System.out.println("Congratulations! You win!");
            printBoard();
            System.exit(0);
        }
    }

    private void play() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printBoard();

            System.out.print("Enter row and column (e.g. 1 2): ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();

            System.out.print("Enter action (R - reveal, F - flag, M - mark): ");
            char action = scanner.next().charAt(0);

            if (action == 'R') {
                revealCell(row, col);
            } else if (action == 'F') {
                toggleFlag(row, col);
            } else if (action == 'M') {
                toggleMark(row, col);
            } else {
                System.out.println("Invalid action!");
            }

            checkWin();
        }
    }

    public static void main(String[] args) {
        Minesweeper game = new Minesweeper();
        game.play();
    }
}