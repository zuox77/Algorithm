package Recursion;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
/*
Given a N * M board, each position is either 1 or 0, need to flip all 1 to 0

 */

public class FlipBoard {
    private int[][] directions = new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

    public int solution(int[][] matrix) {
        HashSet<int[]> visited = new HashSet<>();
        Deque<int[]> queue = new LinkedList<>();

        int count = 0;
        int n = matrix.length;
        int m = matrix[0].length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // not 1
                if (matrix[i][j] != 1) {
                    continue;
                }
                queue.addFirst(new int[]{i, j});
                // add click number
                count++;
                while (!queue.isEmpty()) {
                    int[] coordinate = queue.pollLast();
                    // flip
                    matrix[coordinate[0]][coordinate[1]] = 0;
                    // find next 1s
                    for (int[] direction : directions) {
                        int new_i = coordinate[0] + direction[0];
                        int new_j = coordinate[1] + direction[1];
                        if (validator(new_i, new_j, n, m, matrix)) {
                            queue.addFirst(new int[]{new_i, new_j});
                        }
                    }
                }
            }
        }
        return count;
    }

    public boolean validator(int i, int j, int n, int m, int[][] matrix) {
        if (0 <= i && i < n && 0 <= j && j < m && matrix[i][j] == 1) {
            return true;
        }
        return false;
    }
}
