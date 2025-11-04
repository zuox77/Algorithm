package DataStructure.Stack;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/*
https://leetcode.cn/problems/largest-rectangle-in-histogram/description/?envType=study-plan-v2&envId=top-100-liked

Given an array of integers heights representing the histogram's bar height where the width of each bar is 1, return the area of the largest rectangle in the histogram.

Example 1:
Input: heights = [2,1,5,6,2,3]
Output: 10
Explanation: The above is a histogram where width of each bar is 1.
The largest rectangle is shown in the red area, which has an area = 10 units.

Example 2:
Input: heights = [2,4]
Output: 4

思路:
1. 核心思路为：遍历heights，且对于每一个i，它能形成的最大的矩形面积为：
    maxArea = heights[i] * (right - left - 1)
    注意：left和right都代表位置（即下标），而不是高度
    left代表：在满足heights[left] < heights[i]的条件下，left是在i左侧，且距离i最近的位置
        且如果i就是数组的最左侧，或者i左侧没有所有位置，它们的高度都比heights[i]小，那么left = -1
    right代表：在满足heights[right] < heights[i]的条件下，right是在i右侧，且距离i最近的位置
        且如果i就是数组的最右侧，或者右侧所有位置，它们的高度都比heights[i]小，那么right = n
    简而言之：left和right代表了当前i所能形成的最大的矩形面积的左边界和右边界，且left和right这两个位置本身并不计入该矩形面积计算
        left+1和right-1才会被计入矩形

    对于宽度计算，假设左侧位置为x，右侧位置为y
    当x和y 不 被计算在内时：宽度 = y - x - 1
    当x和y   被计算在内时：宽度 = y - x + 1
    所以面积公式还可以写成：
    maxArea = heights[i] * ((right - 1) - (left + 1) + 1) = heights[i] * (right - left - 1)

    之所以left可以为-1、right可以为n的原因是，我们计算出的left和right，其实并不是矩形真正的左侧和右侧，
    而是左边界和右边界，即是开区间的，这两个位置，其实是不计入矩形面积的
    比如我们找到left=-1，那么实际进行计算的矩形的左边界其实是left+1，即0，即数组的左端
    同理我们找到right=n，那么实际进行计算的矩形的右边界其实是right-1，即n-1，即数组的右端

    为什么我们要找这两个位置而不直接找能被用于计算矩形面积的左侧和右侧？
    因为：如果某个位置可以被计入矩形面积，我们无法知道这个位置的左侧或者右侧是否同样也满足条件，所以只有去
        寻找第一个不满足条件的位置。
        就类似于FindFirstAndLastPositionOfElementInSortedArray.java
        我们找到了target，但要确定第一个和最后一个的位置，我们只能当找到了第一个和最后一个不满足条件的位置时，才能确定上一个
        位置是第一个或者最后一个

    [3, 2, 6, 6, 7, 4, 5]
    n = 7
    i = 0：高度为3，能形成的最大矩阵为[2]，面积为3
        right = 1，heights[right] = 2，是距离i最近的，且高度小于heights[i]
        left = -1，因为左侧没有数字
        maxArea = heights[i] * (right - left - 1) = 3 * (1 - -1 - 1) = 3 * 1 = 3
    i = 1：高度为2，能形成的最大矩阵为[2,2,2,2,2,2,2]，面积为2 * 7 = 14
        right = n，因为右侧的数都比heights[i]=2大
        left = -1，因为左侧的数也都比heights[i]=2大
        maxArea = heights[i] * (right - left - 1) = 2 * (7 - -1 - 1) = 2 * 7 = 14
    i = 2：高度为6，能形成的最大矩阵为[6,6,6]，面积为18
        right = 5，heights[right] = 4，是距离i最近的，且高度小于heights[i]
        left = 1，heights[left] = 2，是距离i最近的，且高度小于heights[i]
        maxArea = heights[i] * (right - left - 1) = 6 * (5 - 1 - 1) = 6 * 3 = 18
    其余类推
2. 创建一个数组记录每个i所对应的left，再创建一个单调栈，从左到右遍历
    1. heights[i] >= 栈首：将栈中元素一一出栈，直到栈为空或者当前栈首小于
    2. 当前栈为空：即左侧没有比heights[i]更小的位置，或者i就处于数组的最左侧，leftMin[i] = -1
        当前栈不为空：栈首就是left，leftMin[i] = stack.peekFirst()
    3. i进栈

    注意：等于的时候也要出栈，原因刚刚提到过，高度与heights[i]相等的时候，该位置会被计入矩形面积，而我们要找的是第一个不能被计入
    矩形面积的位置，所以等于也不行

    注意：为什么heights[i] >= 栈首时，需要将栈中元素一一出栈？
    因为：我们只想知道 离i最近的且高度小于heights[i] 的位置，那些以前比heights[i]更高的点，比起i来说，对于离后面的点，这些点都不可能
    比i更靠近了

3. 清空刚刚的栈（stack.clear()），创建一个数组记录每个i所对应的right，从右到左遍历，计算出每个i对应的right，条件与第二步一样
4. 遍历刚刚得到的两个数组，算出面积

优化：
1. 在这一步：heights[i] >= 栈首：将栈中元素一一出栈，直到栈为空或者当前栈首小于
    已知heights[i] >= 栈首，那么对于i来说，我能确定在i这个位置，其右侧比heights[i]高度小且距离最近的位置就是栈首的位置
    所以栈首就是i的right，即：rightMin[stack.pollFirst()] = i
    比如[3, 2, 6, 6, 7, 4, 5]
    当我们遍历到i = 1时，heights[i] = 2，栈首 = 3，heights[i] >= 栈首，所以我们需要将栈首出栈
    同时，我们来思考，对于数字3来说，哪个位置是它的right？刚好就是2
    后续哪怕还有比2更小的数，但对right而言，只需要heights[right] < heights[i]即可，不需要找最小的那个，反而我们是需要找最近的那个
    所以我们一定能确定，数字3的right就是数字2，所以rightMin[0] = 1
    所以我们可以在把出栈的时候，顺便把right拿到：rightMin[stack.pollFirst()] = i

2. 记住：因为是用的数组保存left和right，数组创建出来后，默认是全部填0，所以我们还需要对right的初始化额外做一步：
    Arrays.fill(right, n)

优化：
1. 根据上一次的优化，我们可以知道，当栈首需要出栈的时候，我们能得到：栈首的right是i，即rightMin[stack.pollFirst()] = i
    如果此时我们还能得到栈首的left，那么我们同时也有了leftMin[stack.pollFirst()] = i
    那我们此时就可以得到i的最大面积，且我们也不需要额外两个数组了，直接用变量表示即可

    注意：我们已知栈首的right，想要得到栈首的left，这样可以计算出栈首的面积，不是i的面积

2. 当栈首需要出栈的时候，如何的到栈首的left？已知栈首一定是栈里面高度最高的元素，所以如果把栈首推出栈，下一个栈首就是left
    即：
    int stackFirst = stack.pollFirst();  -> 当前栈首
    int left = stack.peekFirst(); -> 当前栈首的left，是当前栈首出栈以后，新的栈首
    int right = i
    可得面积

    注意：
    1.简单思索就知道，当i = 0时，其实我们无法计算面积，我们至少也要等到i = 1的时候才能计算
        所以这个解法里面的for循环，需要i从0一直遍历到n，而不是n - 1
        当i = n时，利用第三步的特殊处理，将所有的stack里面剩余的元素全部poll出来计算面积

    2. stack需要一个dummy，值为-1
        上面我们提到，我们在计算当前栈首的最大面积时，"left = stack.peekFirst(); -> 当前栈首的left，是当前栈首出栈以后，新的栈首"
        所以有一种情况是，当前栈首出栈以后，stack为空的情况
        所以我们需要填入-1，因为当stack为空时，说明当前栈首的左侧的数都比它大，
        这种情况就就相当于leftMin[当前栈首] = stack.isEmpty() ? -1 : stack.peekFirst()

    3. 当i = n时，因为不存在heights[n]这个数，所以要额外特殊处理，将其处理为任意的绝对小的数即可，目的是为了当i = n时，
        能够使其把所有stack里面的数都推出栈，计算面积
        int curHeight = i < n ? heights[i] : Integer.MIN_VALUE
        int curHeight = i < n ? heights[i] : -1

 */

public class LargestRectangleHistogram {
    public int largestRectangleArea(int[] heights) {
        int n = heights.length;
        // 从左往右遍历，找到一个，在i左侧，且比heights[i]小（不包括等于）的，离i最近的坐标
        // 如果没有则为-1
        Deque<Integer> stack = new ArrayDeque<>();
        int[] leftMin = new int[n];
        for (int i = 0; i < n; i++) {
            /*
            当stack保存的数都比heights[i]大或者等于，全部出栈
            因为我们需要找的是
            1. 离i距离最近
            2. 且小于（不能等于）heights[i]
            的位置。这个位置的右侧一位，比如这个位置是j，那么j+1，就是当前heights[i]可以计算面积的最左端
            不能等于的原因是：我们要的其实是这个位置的右侧一位，但如果是等于heights[i]，那么右侧一位就少算了
            例如[2,3,5,5,6,2,3]
            中间的5和5和6，最大面积为3 * 5，所以我们计算的位置应该是在3的位置（即下标1），这样3的位置的右侧一位即是面积的最左端
            为什么不能直接找最左端？因为只有当我们确认：左侧某一个位置上的高度，小于heights[i]的时候，我们才知道哪里是最左端

            对于下面的条件：
            为什么这里要heights[stack.peekFirst()] >= heights[i]而不是heights[stack.peekFirst()] > heights[i]？
            因为：我们要找的是离得最近的那一个比heights[i]小的位置，所以等于的时候，虽然也满足条件，但是不是离得最近的了

            并且：我们想要stack保存的数字是什么？或者说想要stack给我们提供什么样的信息？
            是：当我遍历到后面某一个位置（比如x）时，我需要知道前面的位置里面，离当前这个位置最近的且heights[前面的某个位置] < heights[当前这个位置]的位置在哪里
            假设我现在遍历到x了，那后面的数（比如位置为x+1）有三种情况，（高度）比x大，（高度）比x小，（高度）等于x
            1. （高度）比x大时：对于位置x+1，我需要知道，左侧的位置里面，离x+1最近的且heights[x] < heights[x+1]的位置在哪
                所以，在x之前的那些数，只要是高度比x更大或者等于的，我都不需要知道了，因为在我能保证heights[x] < heights[x+1]的情况下，
                x一定是离x+1最近的那个
            2. （高度）比x小：对于位置x+1，因为heights[x] > heights[x+1]，所以我可以在这个时候计算出位置x+1的最左端，并且把x出栈
                出栈的原因就是第一条原因
            3. （高度）等于x：和第一条的原因类似，等于的时候，哪怕也符合高度小于heights[x+1]，但是距离没有x近，所以也不需要考虑了

            这里其实我们还是在用人为的方式去创建一个单调栈，但因为我们保存的是下标，所以单调的是高度，所以栈本身不是单调的，但其高度一定是单调的
            */
            while (!stack.isEmpty() && heights[stack.peekFirst()] >= heights[i]) {
                stack.pollFirst();
            }
            // 如果stack是空，代表前面没有比当前位置高度更低的数字，直接变成-1，否则为栈首
            leftMin[i] = stack.isEmpty() ? -1 : stack.peekFirst();
            // 加入栈
            stack.offerFirst(i);
        }

        // 同理，从右往左遍历，找到最右侧的点
        int[] rightMin = new int[n];
        // 重置stack
        stack.clear();
        for (int i = n - 1; i >=0; i--) {
            while (!stack.isEmpty() && heights[stack.peekFirst()] >= heights[i]) {
                stack.pollFirst();
            }
            // 对于右侧来说，如果当前位置的高度就是最低点，那么同样，长度拉满，此时应该将其设置为n，原因类似，
            // 左端点找到以后，实际的矩阵最左端是左端点右侧一位，那么右端点找到以后，实际的矩阵最右端是右端点左侧一位，所以n - 1，刚好是rightMin的最右端
            rightMin[i] = stack.isEmpty() ? n : stack.peekFirst();
            stack.offerFirst(i);
        }
        // 最后在遍历一次得到的数组，来计算最大值
        int maxArea = 0;
        for (int i = 0; i < n; i++) {
            maxArea = Math.max(maxArea, (rightMin[i] - leftMin[i] - 1) * heights[i]);
        }
        return maxArea;
    }

    public int largestRectangleArea2(int[] heights) {
        int n = heights.length;
        // stack
        Deque<Integer> stack = new ArrayDeque<>();
        // 用两个数组来保存左端点和右端点
        int[] leftMin = new int[n];
        int[] rightMin = new int[n];
        // 给rightMin设置默认值
        Arrays.fill(rightMin, n);
        // 从左往右遍历
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && heights[stack.peekFirst()] >= heights[i]) {
                // 第一个解法里，我们直接把不需要的元素出栈了
                // 但其实这个元素可以用来判断rightMin，所以我们可以一次遍历就获得两个数组的值
                rightMin[stack.pollFirst()] = i;
            }
            leftMin[i] = stack.isEmpty() ? -1 : stack.peekFirst();
            stack.offerFirst(i);
        }
        // 计算最大面积
        int maxArea = 0;
        for (int i = 0; i < n; i++) {
            maxArea = Math.max(maxArea, heights[i] * (rightMin[i] - leftMin[i] - 1));
        }

        return maxArea;
    }

    public int largestRectangleArea3(int[] heights) {
        int n = heights.length;
        // stack
        Deque<Integer> stack = new ArrayDeque<>();
        // 给stack一个默认值，以此来对应，当leftMin[i] = -1的情况
        stack.offerFirst(-1);

        // 遍历
        int maxArea = 0;
        // 这里记得是i <= n，因为在这个算法下，第一个（i = 0）元素的面积，最快也是在i = 1的时候，才能计算出来
        // 所以说当i = n - 1的面积，需要i = n的时候才能计算出来
        // 且因为我们有dummy在stack里面，所以不怕空栈的情况
        for (int i = 0; i <= n; i++) {
            /*
            [1,1,1,5]
            i = 0，stack = [-1]
                当前i入栈
            i = 1，stack = [0, -1]
                stack.peekFirst() = 0，heights[stack.peekFirst()] = 1，size = 2，curHeight = 1，满足while条件，
                栈首出栈且计算其maxArea，然后当前i入栈
            i = 2，stack = [1, -1]
                stack.peekFirst() = 1，heights[stack.peekFirst()] = 1，size = 2，curHeight = 1，满足while条件，
                栈首出栈且计算其maxArea，然后当前i入栈
            i = 3，stack = [2, -1]
                stack.peekFirst() = 2，heights[stack.peekFirst()] = 1，size = 2，curHeight = 5，不满足while条件，
                当前i入栈
            i = 4, stack = [3, 2, -1]
                stack.peekFirst() = 3，heights[stack.peekFirst()] = 5，size = 2，curHeight = -1，满足while条件

                第一次while循环：stack.peekFirst() = 3，heights[stack.peekFirst()] = 5，size = 2，curHeight = -1
                    栈首出栈且计算其maxArea，stack = [2, -1]
                第二次while循环：stack.peekFirst() = 2，heights[stack.peekFirst()] = 1，size = 2，curHeight = -1
                    栈首出栈且计算其maxArea，stack = [-1]
                第三次while循环：stack.peekFirst() = -1，size = 1，curHeight = -1
                    size不符合条件，heights[-1]不会报错，直接退出循环
                当前i入栈

            在i = 4第一次循环时，将i = 3的面积计算了
            在i = 4第二次循环时，将i = 2的面积计算了
            在i = 2时，将i = 1的面积计算了
            在i = 1时，将i = 0的面积计算了

            至此，所有i都有了面积，且我们一直保存了最大面积
            */
            // 因为我们需要i最大为n，那么我们就需要对i = n的时候特殊处理，当i = n时，我们知道此刻的i已经没有实际意义了，
            // 所以我们需要做的就是将stack里面仍有的数都推出栈，并且计算其面积，所以当i = n时，我们只需要保证curHeight足够小，
            // 就能让其一定进入while循环去计算剩余stack元素的面积
            int curHeight = i < n ? heights[i] : -1;
            // 这里的stack.size() > 1是因为我们给了stack一个dummy，即上面的-1
            // 有了dummy和这个条件，我们可以等同于有了leftMin[i] = stack.isEmpty() ? -1 : stack.peekFirst();
            // 这样while循环里面，如果stack里面只有两个元素，我们pollFirst以后，还会剩下dummy，可以用作peekFirst
            while (stack.size() > 1 && heights[stack.peekFirst()] >= curHeight) {
                // 栈首出栈
                int stackFirst = stack.pollFirst();
                // 通过rightMin[stack.pollFirst()] = i可以得到：栈首的right是i
                int right = i;
                // 栈首的left是下一个栈首，即，将stackFirst推出栈以后，现在的栈首
                int left = stack.peekFirst();
                // 计算面积
                maxArea = Math.max(maxArea, heights[stackFirst] * (right - left - 1));
            }
            stack.offerFirst(i);
        }
        return maxArea;
    }
}
