package OOD;

import java.util.HashMap;
/*
https://www.1point3acres.com/bbs/thread-970148-1-1.html
https://www.1point3acres.com/bbs/thread-921749-1-1.html
https://www.1point3acres.com/bbs/thread-965119-1-1.html
https://www.1point3acres.com/bbs/thread-953876-1-1.html
 */

public class BoardGame {
    private HashMap<Integer, StringBuilder> board = new HashMap<>();
    private int winLength;

    public BoardGame(int winLength) {
        this.winLength = winLength;
    }

    enum Player {
        PLAYER_WHITE('W'), PLAYER_RED('R');
        char symbol;

        Player(char symbol) {
            this.symbol = symbol;
        }

        public Character toChar() {
            return this.symbol;
        }
    }

    public void makeMove(Player player, int column) {
        if (board.containsKey(column)) {
            board.get(column).append(player.toChar());
        } else {
            StringBuilder tmp = new StringBuilder();
            tmp.append(player.toChar());
            board.put(column, tmp);
        }
        if (checkWinVertical(player, column) == player) {
            System.out.printf("player: %c Wins", player.toChar());
            System.out.println("Game restarted");
            board.clear();
        }
    }

    public Player checkWinHorizontal(Player player, int column) {
        int currentRow = board.get(column).length() - 1;
        for (int i = column - winLength; i <= column; i++) {
            // combine horizontal characters to a StringBuilder if possible
            StringBuilder horizon = new StringBuilder();
            for (int j = 0; j < winLength; j++) {
                if (board.get(i + j).length() < currentRow) {
                    break;
                }
                horizon.append(board.get(i + j).charAt(currentRow));
            }
            // Check horizontal StringBuilder
            boolean flag = true;
            for (int m = 0; m < horizon.length(); m++) {
                if (horizon.charAt(m) != player.symbol) {
                    flag = false;
                }
            }
            if (flag) {
                return player;
            }
        }
        return null;
    }

    public Player checkWinVertical(Player player, int column) {
        StringBuilder pieces = board.get(column);
        int n = pieces.length();
        if (n < 3) {
            return null;
        } else {
            for (int i = n - 1; i > n - 4; i--) {
                if (pieces.charAt(i) != player.toChar()) {
                    return null;
                }
            }
        }
        return player;
    }
}
