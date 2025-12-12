package Interview.Airbnb;

/*
Input: int[] plays = [0, 1, 2, 1, ...]
代表user放checker進哪一個column， 你可以assume 兩個user來回放，board 是7x6的matrix
Implement：
DropDisc(column)
CheckWinner(player)
PrintBoard()

注意：
1. 可以考虑将输入设定为1-based，然后在dropPiece里将其减一，变成0-based，用户界面更有好
2. 可以设置一个isGameOver的flag，这样可以在游戏结束以后，直接返回game over，而不是返回column is full
3. 打印的时候，可以一层一层打印，使用StringBuilder，重置使用sb.setLength(0)
 */

import java.util.Arrays;

public class ConnectFour {
    private static final int ROW = 7;
    private static final int COL = 6;
    private static final int[][] DIRECTIONS = {
            {0, 1},   // Right
            {-1, 0},  // Left
            {1, 0},   // Up
            {-1, 0},  // Down
            {1, 1},   // From left up to right down
            {-1, -1}, // From right down to left up
            {1, -1},  // From right up to left down
            {-1, 1},  // From left down  to right up
    };
    private final int[][] board;
    private int steps;
    private int curUser;
    private final int[] colNext;
    private boolean isGameOver;

    public ConnectFour() {
        this.steps = 0;
        this.colNext = new int[COL];
        Arrays.fill(colNext, ROW - 1);
        this.board = new int[ROW][COL];
    }

    public void dropPiece(int inputCol) {
        // Check if game is over
        if (isGameOver) {
            System.out.println("Game is over.");
            return;
        }
        // Minus 1 to match 0-based index
        int col = inputCol - 1;
        // Check if input is valid
        if (col < 0 || col >= COL) {
            System.out.println("Column " + inputCol + " is invalid. Retry another column.");
            printBoard();
            return;
        }
        // Get current position
        if (colNext[col] < 0) {
            System.out.println("Column " + inputCol + " is Full. Retry another column.");
            printBoard();
            return;
        }
        int row = colNext[col]--;
        // Get current user
        curUser = steps % 2 + 1;
        steps++;
        // Mark board
        board[row][col] = curUser;
        System.out.println("User " + curUser + " dropped a piece on column " + inputCol + ".");
        // Check if there's a winner
        if (hasWinner(row, col)) {
            isGameOver = true;
            System.out.println("You Win! Game Over!");
            System.out.println("Winner is player " + curUser);
            // Check if board is full (game over)
        } else if (isFull()) {
            isGameOver = true;
            System.out.println("Board is full! Tie! Game Over!");
        }
        printBoard();
    }

    private boolean hasWinner(int x, int y) {
        for (int[] dire: DIRECTIONS) {
            int count = 0;
            for (int i = 1; i <= 3; i++) {
                int newX = x + dire[0] * i;
                int newY = y + dire[1] * i;
                if (0 > newX || newX >= ROW || 0 > newY || newY >= COL) continue;
                if (board[newX][newY] != curUser) break;
                count++;
            }
            if (count == 3) return true;
        }
        return false;
    }

    private boolean isFull() {
        for (int i = 0; i < COL; i++) {
            if (colNext[i] >= 0) return false;
        }
        return true;
    }

    private void printBoard() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ROW; i++) {
            sb.setLength(0);
            for (int j = 0; j < COL; j++) {
                sb.append(board[i][j]);
                if (j != COL - 1) sb.append(", ");
            }
            System.out.println(sb.toString());
        }
    }
}

/*
        ConnectFour game = new ConnectFour();
        game.dropPiece(1);
        game.dropPiece(1);
        game.dropPiece(1);
        game.dropPiece(1);
        game.dropPiece(1);
        game.dropPiece(1);
        game.dropPiece(1);

        game.dropPiece(2);
        game.dropPiece(2);
        game.dropPiece(2);
        game.dropPiece(2);
        game.dropPiece(2);
        game.dropPiece(2);
        game.dropPiece(2);
        game.dropPiece(2);

        game.dropPiece(3);
        game.dropPiece(3);
        game.dropPiece(3);
        game.dropPiece(3);
        game.dropPiece(3);
        game.dropPiece(3);
        game.dropPiece(3);
        game.dropPiece(3);
        game.dropPiece(3);

        game.dropPiece(5);
        game.dropPiece(5);
        game.dropPiece(5);
        game.dropPiece(5);
        game.dropPiece(5);
        game.dropPiece(5);
        game.dropPiece(5);

        game.dropPiece(4);
        game.dropPiece(4);
        game.dropPiece(4);
        game.dropPiece(4);
        game.dropPiece(4);
        game.dropPiece(4);
        game.dropPiece(4);
        game.dropPiece(4);

        game.dropPiece(6);
        game.dropPiece(6);
        game.dropPiece(6);
        game.dropPiece(6);
        game.dropPiece(6);
        game.dropPiece(6);
        game.dropPiece(6);

        game.dropPiece(6);
        game.dropPiece(6);
 */