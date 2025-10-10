package BinarySearch;
/*
刷题次数: 2
二刷: 
1. 对我个人而言, 经常搞混淆矩阵坐标, 所以用长宽这个概念来代替 -> int len (故意用len这个错别字区分length方法) = mat[0].lenght; int width = mat.length;
2. 对于该怎么循环, 记住: 外围的while循环是不断减少长(len)的范围, 内侧的for循环是遍历某一列(width)从而找到该列的最大值
3. 找到某一列的最大值以后, 判断左侧右侧的大小时候, 记得: 1. 需要先判断边界条件 2. 再判断大小
4. 判断边界条件时: 记住要以left和right指针作为当前的边界, 而不是以len和width

https://leetcode.cn/problems/find-a-peak-element-ii/description/
https://leetcode.cn/problems/find-a-peak-element-ii/solutions/1208991/python3-er-fen-qie-pian-shi-jian-fu-za-d-gmd2/?orderBy=most_votes

A peak element in a 2D grid is an element that is strictly greater than all of its adjacent neighbors to the left, right, top, and bottom.
Given a 0-indexed m x n matrix mat where no two adjacent cells are equal, find any peak element mat[i][j] and return the length 2 array [i,j].
You may assume that the entire matrix is surrounded by an outer perimeter with the value -1 in each cell.
You must write an algorithm that runs in O(m log(n)) or O(n log(m)) time.

Input: mat = [[1,4],[3,2]]
Output: [0,1]
Explanation: Both 3 and 4 are peak elements so [1,0] and [0,1] are both acceptable answers.

思路: 二分
1. 时间复杂度要求是O(MlogN)或者O(NlogM), 那要么是排序, 要么是二分, 要么是堆, 要么是二叉树树高
2. 这个题明显只能是二分, 因为不需要排序也不需要用堆去找前k个最大/最小, 也没有二叉树
3. 二分的核心是可以通过计算出index的中点, 判断中点的数和target的关系, 从而缩小一半的范围
4. 此题的关键在于peak的定义: peak会比上下左右都大
5. 核心逻辑: 
    1. 遍历一列找到此列的最大值colMax, 因为是此列的最大值, 所以不需要考虑上下
    2. 比较该最大值左右两个数, 哪个数比colMax更大, 则说明哪边更有希望有极值

步骤: 以遍历列为例子
1. 取得矩阵的长宽M和N
   如果要进一步优化, 可以判断下M和N的大小, 遍历短的那个, 让时间复杂度永远保证是O(短log长), 不过优化不明显
2. 定义左右指针, colLeft/colRight
3. while循环, 条件是colLeft <= colRight
   如果用colLeft + 1 < colRight或者colLeft < colRight, 则退出循环后, 还需要再判断一次colLeft和colRight
4. 找到colMid = (colLeft + colRight) / 2
5. 遍历colMid列, 找到最大值
6. 判断最大值左右两边的数, 得到isLeftLarge和isRightLarge
7. 四种情况: 
    1. 如果!isLeftLarge&&!isRightLarge, 则当前值就是极值, return
    2. 如果isLeftLarge, 左边更大, 移动colRight: colRight = colMid
    3. 如果isRightLarge, 右边更大, 移动colLeft: colLeft = colMid + 1, 防止死循环
    4. 如果isLeftLarge&&isRightLarge, 当前值是谷底, 随便移动哪个都可以, 可以归到情况3里面

时间复杂度: O(MlogN)或者O(NlogM)
空间复杂度: O(1)
 */

class FindAnyPeakII {
    public int[] solution1(int[][] mat) {
        // 找到长和宽
        int len = mat[0].length;
        int width = mat.length;
        // 二分法遍历找到中点列
        int left = 0, right = len - 1;
        while (left <= right) {
            // 找到中点列
            int mid = left + (right - left) / 2;
            // 遍历中点列找到该列的最大值
            int colMax = mat[0][mid];
            int colMaxIndex = 0;
            for (int i = 1; i < width; i++) {
                if (colMax < mat[i][mid]) {
                    colMax = mat[i][mid];
                    colMaxIndex = i;
                }
            }
            // 比较mat[colMaxIndex][mid]左右找到最大值
            boolean leftLarger = mid - 1 >= left && mat[colMaxIndex][mid - 1] > colMax;
            boolean rightLarger = mid + 1 <= right && mat[colMaxIndex][mid + 1] > colMax;
            // 分情况判断
            // 当前点比左右都大, 则说明是峰顶, 找到答案直接返回即可
            if (!leftLarger && !rightLarger) {
                return new int[]{colMaxIndex, mid};
                // 当前点处于向右递增, 则右边更有可能有峰顶, 所以left指针向右移动
            } else if (!leftLarger && rightLarger) {
                // 注意这里一定要是mid+1, 详情可以看BinarySearch.md
                left = mid + 1;
                // 当前点处于向左递增, 则左边更有可能有峰顶, 所以right指针向左移动
                // 同时这还包含了峰谷的情况, 即当前点比左右两边都小, 这个情况下, 往哪边移动都可以
            } else {
                right = mid;
            }
        }
        // 如果while循环退出了都没找到, 则说明可能没有峰值
        return new int[]{-1, -1};
    }
}