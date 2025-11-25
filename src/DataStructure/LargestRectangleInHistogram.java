package DataStructure;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

/*
https://leetcode.cn/problems/largest-rectangle-in-histogram/
https://www.lintcode.com/problem/122/
https://www.jiuzhang.com/problem/largest-rectangle-in-histogram/

思路: 单调栈
单调栈的目的: 在暴力解法中, 我们计算方式是: 对每一个数, 再内循环一次找到其可能的最大宽度(向左右遍历直到遇到第一个比自己小的数停止), 即找到左右边界,
    在单调栈中利用递增和存储曾经出现过的数的特性, 完成了暴力解法中的找寻宽度的左右边界的问题, 即:
    每当想要进栈的数小于当前的栈顶, 说明此时我们已经遍历到了当前栈顶的右边界, 所以我们可以开始计算以当前栈顶
    为高的长方形的最长宽度是多少又已知是单调栈且单调递增, 所以当前栈顶的前一个数, 是比当前栈顶小的数里面最大的数, 即左边界, 所以能算出面积
1. 栈里面存储的是数字的下标！
2. 此题最重要的点在于计算宽度: 当一个数过高的时候, 我们知道它的宽大概率为1, 因为无法在原数组中向左右两边延伸形成长方形, 而当一个数
    比较小时, 则宽度就非常重要, 比如1, 理论来讲1的宽度可以横跨整个数组, 它的面积是nums.length * 1, 所以怎么判断某一个数的最大宽度是核心问题
3. 宽度计算公式: i - stack.peek() - 1的具体含义是:
    i是当前循环到原数组的哪个数了, 又因为此数想要进栈, 所以此数一定是当前栈顶的右边界, i = 当前栈顶能形成的长方形的右边界
    stack.peek(), 理论上是栈顶值, 但由于之前我们已经将当前栈顶pop出来了, 所以其实是当前栈顶(top)的前一个数(newTop), 也就是左边界
    - 1, 因为我们计算的时候, 左右边界, 其实是不包含在内的, 即一个开区间(stack.peek(), i)而不是闭区间[stack.peek(), i], 所以-1获得正确的宽度
5. 其次要想清楚的是: 栈顶左右两边的数, 不一定是按原数组中的顺序排列的, 所以计算宽度时, 不是只计算原数组相邻两个数, 而是代表的左边界和右边界
6. 一个例子过一遍:
    数组 [3, 2, 6, 7, 5, 3, 4]
    下标  0  1  2  3  4  5  6
    1. 3的下标0进栈
    2. 2的下标1进栈, 2比3小, 所以3pop出去, 计算面积得1 * 3 = 3, 更新最大面积变量
    3. 6的下标2进栈
    4. 7的下标3进栈
    5. 5的下标4进栈, 5比7小, 所以7pop出去, 计算面积得1 * 7 = 7, 更新最大面积变量
    6. 5也比6小, 所以6继续pop出去, 计算面积得2 * 6 = 12, 更新最大面积变量
    7. 5比2大, 5的下标4成功进栈, 栈内的数是: [1, 4], 因为是下标所以实际的数是: [2, 5]
        这里也证明上面所说: 栈顶左右两边的数, 不一定是按原数组中的顺序排列的
    8. 3的下标5进栈, 3比5小, 所以5pop出去, 计算面积得3 * 5 = 15, 更新最大面积变量
    9. 3比2大, 所以3的下标5成功进栈, 栈内的数是: [1, 5], 因为是下标所以实际的数是: [2, 3]
    10. 4的下标6进栈, 栈内的数是: [1, 5, 6], 因为是下标所以实际的数是: [2, 3, 4]
    11. 此时已经遍历结束, 但还没算完, 所以设置当遍历到最后一个数时, 令i = -1代入进去, -1是根据题来的,
        根本的意思是: 选择一个一定比数组里的任何数都小的数, 将栈内剩余的数pop出栈, 完成计算, 更新最大面积变量
代码需要注意的点:
1. for循环要循环到heights.length, 而不是length-1, 因为最后要用一个绝对小的数将stack里剩余的数pop出来完成计算
2. 相同的数也要将其pop出来, 即while循环的条件之一是 curNum <= heights[stack.peek()]
时间复杂度: O(N)
空间复杂度: O(N), 栈的最坏情况就是原数组也是单调递增的, 那么栈会保存所有的数, 再最后清算面积

为什么不能用DP？可以用, 但时间复杂度不会有优化
可以使用DP, 满足以下任意四个条件之一:
1. 求最大/最小
2. 判断是否可行
3. 计算方案个数
4. 暴力解法时间复杂度是指数级别, DP擅长将指数级(2^N, 3^N, ..)优化到多项式级别(N^2, N^3, ..)
不可以使用DP, 或者说使用DP不能进一步优化时间复杂度, 满足以下任意三个条件之一:
1. 计算具体的方案(比如返回最短的路径[2, 3, 5], 而不是最短的路径有几个)
2. 输入的参数是一个无序集合(DP是通过找寻前后状态然后遍历整个序列计算最后的状态, 如果是无序的集合, 那么肯定没有前后状态)
3. 暴力解法的时间复杂度已经是多项式级别的, DP无法将已经是多项式级别的时间复杂度进一步优化

此题的暴力解法是O(N^2), 即对每一个数, 再内循环一次找到其可能的最大宽度(向右遍历直到遇到第一个比自己小的数停止)
因为已经是多项式级别的时间复杂度了, DP不能将O(N^2)优化到更小, 所以用DP也是O(N^2), 所以没什么用
 */

public class LargestRectangleInHistogram {
    public int largestRectangleInHistogram(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        int maxArea = 0;

        for (int i = 0; i <= heights.length; i++) {
            // if it's the last element,
            // assign a negative number to squeeze out remaining numbers in stack
            int curNum = i == heights.length ? -1 : heights[i];
            while (!stack.isEmpty() && curNum <= heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int weight = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxArea = Math.max(maxArea, height * weight);
            }
            stack.push(i);
        }
        return maxArea;
    }

    public int largestRectangleInHistogram2(int[] heights) {
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
            // 如果stack是空，代表前面没有比当前位置高度更低的数字，直接变成-1，或者就取栈首
            // 变成-1的原因是，如果左侧都比当前位置高，那么当前位置的高度就是能形成的矩形的最高高度了，所以长度可以拉满
            // 之前那些情况之所以需要考虑最左端就是因为他们的高度比当前位置低，所以要根据他们的高度来选长度
            leftMin[i] = stack.isEmpty() ? -1 : stack.peekFirst();
            // 加入栈
            stack.offerFirst(i);
        }

        // 同理，从右往左遍历，找到最右侧的点
        int[] rightMin = new int[n + 1];
        // 重置stack
        stack.clear();
        for (int i = n - 1; i >= 0; i--) {
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
            // 同理，因为我们需要i最大为n，那么我们就需要对i = n的时候特殊处理
            // [1,1,1,5]
            int curHeight = i < n ? heights[i] : -1;
            // 这里的stack.size() > 1是因为我们给了stack一个dummy，即上面的-1
            // 有了dummy和这个条件，我们可以等同于有了leftMin[i] = stack.isEmpty() ? -1 : stack.peekFirst();
            // 这样while循环里面，如果stack里面只有两个元素，我们pollFirst以后，还会剩下dummy，可以用作peekFirst
            while (stack.size() > 1 && heights[stack.peekFirst()] >= curHeight) {
                // 栈首出栈
                int stackFirst = stack.pollFirst();
                /*
                要计算某个位置的面积，需要知道其左端和右端，即第一个比它高度低的位置
                通过上面的条件heights[stack.peekFirst()] >= curHeight，curHeight就是height[i]，所以
                对于stack.peekFirst()（即stackFirst）来说，位置i就是第一个比它高度低的位置
                所以stackFirst的右端就是i
                 */
                int right = i;
                /*
                现在知道了stackFirst的右端是i，那么左端其实就是现在的stack.peekFirst()
                为什么？因为我们的stack是单调递增的，所以栈首一定是最大的，所以stackFirst出栈以后，栈首就是stackFirst的left
                 */
                int left = stack.peekFirst();
                // 计算面积
                maxArea = Math.max(maxArea, heights[stackFirst] * (right - left - 1));
            }
            stack.offerFirst(i);
        }
        return maxArea;
    }
}
