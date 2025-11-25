import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        return;
    }

    public List<List<String>> solveNQueens(int n) {
        // 创建一个matrix代表记录结果
        int[][] matrix = new int[n][n];
        List<int[][]> intAns = new ArrayList<>();
        // 初始化为0
        Arrays.setAll(matrix, i -> 0);
        dfs(
                n,
                0,
                matrix,
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                intAns);

        // 转化结果
        List<List<String>> ansList = new ArrayList<>();
        for (int[][] grid : intAns) {
            List<String> ans = new ArrayList<>();
            for (int[] row : grid) {
                StringBuilder sb = new StringBuilder();
                for (int cell : row) {
                    if (cell == 0) {
                        sb.append('.');
                    } else {
                        sb.append('Q');
                    }
                }
                ans.add(new String(sb));
            }
            ansList.add(new ArrayList<>(ans));
        }
        return ansList;
    }

    public void dfs(
            int n,
            int count,
            int[][] matrix,
            Set<Integer> visitedRow,
            Set<Integer> visitedCol,
            Set<Integer> visitedRightUp,
            Set<Integer> visitedLeftUp,
            List<int[][]> intAns) {
        // 退出条件
        if (count == n) {
            intAns.add(matrix);
            return;
        }

        // 遍历且修改matrix
        for (int i = 0; i < n; i++) {
            if (visitedRow.contains(i)) continue;
            visitedRow.add(i);
            for (int j = 0; j < n; j++) {
                /*
                1. 每次放置一个Q以后，该行和列都不能再被使用
                2. 检查对角线
                3. 检查是否为'Q'

                对于一个n x n的矩阵来说，每个点都有两条对角线，一个从右上到左下，一个从左上到右下
                右上到左下的对角线：其中的点，他们的x+y都相等
                左上到右下的对角线：其中的点，他们的x-y的绝对值都相等
                 */
                if (visitedCol.contains(j)) continue;
                if (visitedRightUp.contains(i + j)) continue;
                if (visitedLeftUp.contains(Math.abs(i - j))) continue;
                if (matrix[i][j] == 1) continue;
                // 修改matrix
                matrix[i][j] = 1;
                // 加入visited
                visitedCol.add(j);
                visitedRightUp.add(i + j);
                visitedLeftUp.add(Math.abs(i - j));
                // 进入下一层
                dfs(
                        n,
                        count + 1,
                        matrix,
                        visitedRow,
                        visitedCol,
                        visitedRightUp,
                        visitedLeftUp,
                        intAns);
                // 改回来
                matrix[i][j] = 0;
                // 去除visited
                visitedCol.remove(j);
                visitedRightUp.remove(i + j);
                visitedLeftUp.remove(Math.abs(i - j));
            }
            visitedRow.remove(i);
        }
    }
}
