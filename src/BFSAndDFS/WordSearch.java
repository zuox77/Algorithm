package BFSAndDFS;

/*
https://leetcode.cn/problems/word-search/description/?envType=study-plan-v2&envId=top-100-liked

Given an m x n grid of characters board and a string word, return true if word exists in the grid.
The word can be constructed from letters of sequentially adjacent cells, where adjacent cells are horizontally or vertically neighboring.
The same letter cell may not be used more than once.

Example 1:
Input: board = [
    ["A","B","C","E"],
    ["S","F","C","S"],
    ["A","D","E","E"]
    ], word = "ABCCED"
Output: true

Example 2:
Input: board = [
    ["A","B","C","E"],
    ["S","F","C","S"],
    ["A","D","E","E"]
    ], word = "SEE"
Output: true

Example 3:
Input: board = [
    ["A","B","C","E"],
    ["S","F","C","S"],
    ["A","D","E","E"]
    ], word = "ABCB"
Output: false

思路: DFS
1. 记住方向的常量怎么创建
2. 记住找到是返回true而不是false，即我们需要写true的条件，而不是写false的条件，因为只有true才能保证，false只能说明当前找的路径不正确，不能保证
    其余没找的路径不正确
 */

public class WordSearch {
    private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    public boolean exist(char[][] board, String word) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (recursion(i, j, 0, board, word)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean recursion(int i, int j, int k, char[][] board, String word) {
        // 退出条件，如果不等于，任意时候直接退出
        if (board[i][j] != word.charAt(k)) {
            return false;
        }
        // 现在，上面这个if筛选完以后，其余时候都是符合的
        // 那么结束就是到终点了
        if (k == word.length() - 1) {
            return true;
        }
        // 每次循环
        // 更新board
        board[i][j] = '1'; // 标记为visited
        for (int[] direction : DIRECTIONS) {
            int newX = i + direction[0];
            int newY = j + direction[1];
            // 之所以这里是if(xxx) {return true;}而不是if(!xxx) {return false;}的原因是：
            // 当我们寻找到符合要求的字母的时候，我们应该直接return，即其余方向上都不用再搜索了
            // 但如果我们选择return false则没有这个效果
            // 听起来好像是我们应该直接false停下，但要注意recursion用的都是return，return代表返回上一层，而不是一般用的break退出循环
            // 但这里，我们找错了还要继续找，直到把周围方向的都找完为止，所以选返回true的写法，而不能写返回false
            if (0 <= newX
                    && newX < board.length
                    && 0 <= newY
                    && newY < board[0].length
                    && recursion(newX, newY, k + 1, board, word)) {
                return true;
            }
        }
        // 返回以后更新board
        board[i][j] = word.charAt(k);
        return false;
    }
}
