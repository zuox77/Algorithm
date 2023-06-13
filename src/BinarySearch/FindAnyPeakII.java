package BinarySearch;
/*
https://leetcode.cn/problems/find-a-peak-element-ii/description/
https://leetcode.cn/problems/find-a-peak-element-ii/solutions/1208991/python3-er-fen-qie-pian-shi-jian-fu-za-d-gmd2/?orderBy=most_votes
题目：
A peak element in a 2D grid is an element that is strictly greater than all of its adjacent neighbors to the left, right, top, and bottom.
Given a 0-indexed m x n matrix mat where no two adjacent cells are equal, find any peak element mat[i][j] and return the length 2 array [i,j].
You may assume that the entire matrix is surrounded by an outer perimeter with the value -1 in each cell.
You must write an algorithm that runs in O(m log(n)) or O(n log(m)) time.

思路：二分
1. 时间复杂度要求是O(MlogN)或者O(NlogM)，那要么是排序，要么是二分，要么是堆，要么是二叉树树高
2. 这个题明显只能是二分，因为不需要排序也不需要用堆去找前k个最大/最小，也没有二叉树
3. 二分的核心是可以通过计算出index的中点，判断中点的数和target的关系，从而缩小一半的范围
4. 此题的关键在于peak的定义：peak会比上下左右都大
5. 核心逻辑：
    1. 遍历一列找到此列的最大值colMax，因为是此列的最大值，所以不需要考虑上下
    2. 比较该最大值左右两个数，哪个数比colMax更大，则说明哪边更有希望有极值

步骤：以遍历列为例子
1. 取得矩阵的长宽M和N
    如果要进一步优化，可以判断下M和N的大小，遍历短的那个，让时间复杂度永远保证是O(短log长)，不过优化不明显
2. 定义左右指针，colLeft/colRight
3. while循环，条件是colLeft <= colRight
    如果用colLeft + 1 < colRight或者colLeft < colRight，则退出循环后，还需要再判断一次colLeft和colRight
4. 找到colMid = (colLeft + colRight) / 2
5. 遍历colMid列，找到最大值
6. 判断最大值左右两边的数，得到isLeftLarge和isRightLarge
7. 四种情况：
    1. 如果!isLeftLarge&&!isRightLarge，则当前值就是极值，return
    2. 如果isLeftLarge，左边更大，移动colRight：colRight = colMid
    3. 如果isRightLarge，右边更大，移动colLeft：colLeft = colMid + 1，防止死循环
    4. 如果isLeftLarge&&isRightLarge，当前值是谷底，随便移动哪个都可以，可以归到情况3里面

时间复杂度：O(MlogN)或者O(NlogM)
空间复杂度：O(1)
 */

public class FindAnyPeakII {
    public int[] findPeakGrid(int[][] mat) {
        int m = mat.length;    // 行
        int n = mat[0].length; // 列

        int colLeft = 0, colRight = n - 1;

        // 最好用colLeft<=colRight
        while (colLeft <= colRight) {
            // 找到列的中点
            int colMid = (colLeft + colRight) / 2;
            int rowMax = -1;
            int rowMaxIndex = -1;
            boolean isLeftLarge = false, isRightLarge = false;
            // 遍历整个列，找到最大值和最大值的下标
            for (int row = 0; row < m; row++) {
                if (rowMax < mat[row][colMid]) {
                    rowMaxIndex = row;
                    rowMax = mat[row][colMid];
                    // rowMax = mat[rowMaxIndex][colMid]
                }
            }
            // 中间列的最大值的左边是否大于，判断边界要用colLeft
            isLeftLarge = colMid - 1 >= colLeft && mat[rowMaxIndex][colMid - 1] > rowMax;
            // 中间列的最大值的优边是否大于，判断边界要用colRight
            isRightLarge = colMid + 1 <= colRight && mat[rowMaxIndex][colMid + 1] > rowMax;
            // 如果都比当前值小，则说明当前值已经是峰值
            if (!isLeftLarge && !isRightLarge) {
                return new int[] {rowMaxIndex, colMid};
            } else if (isLeftLarge) {
                colRight = colMid;
            } else {
                colLeft = colMid + 1;
            }
        }

        return new int[] {-1, -1};
    }
}
