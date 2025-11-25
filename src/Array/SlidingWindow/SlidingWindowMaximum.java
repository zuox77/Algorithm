package Array.SlidingWindow;

/*
刷题次数: 1

https://leetcode.cn/problems/sliding-window-maximum/description/?envType=study-plan-v2&envId=top-100-liked

You are given an array of integers nums, there is a sliding window of size k which is moving from the very left of the array to the very right.
You can only see the k numbers in the window. Each time the sliding window moves right by one position.
Return the max sliding window.

Example 1:
Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
Output: [3,3,5,5,6,7]
Explanation:
Window position                Max
---------------               -----
[1  3  -1] -3  5  3  6  7       3
 1 [3  -1  -3] 5  3  6  7       3
 1  3 [-1  -3  5] 3  6  7       5
 1  3  -1 [-3  5  3] 6  7       5
 1  3  -1  -3 [5  3  6] 7       6
 1  3  -1  -3  5 [3  6  7]      7

Example 2:
Input: nums = [1], k = 1
Output: [1]

时间复杂度: O(N), 遍历一次
空间复杂度: O(1)

思路：不定长滑动窗口
1. 整体思路与定长滑动窗口类似，不同的是：
    1. 只需要targetFreq来记录p的字母的出现次数，当遍历s时，只需在targetFreq中，将当前出现的字母减1
        意义为：targetFreq里面的字母出现的次数都为0时，表明当前滑动窗口里面的字母刚好等于p，反之则不等于
    2. 如果遍历s时，出现了p中不存在的字母，只需要不断的移动左指针left，直到该不存在的字母出现的频率不为负数
        意义为：如果右端点是一个p中不存在的字母，那么要达到不存在的字母出现的频率不为负数条件，只有左端点向右移动，于右端点重合，
        即为抛弃右端点前面所有的字母，因为出现了一个不存在的字母，前面无论有多少符合的字母，就因为这一个不存在的字母，左端点和右端点都需要
        挪到该不存在的字母的下一个位置，然后继续遍历
2.

 */

import java.util.ArrayDeque;
import java.util.Deque;

public class SlidingWindowMaximum {

    public int[] maxSlidingWindow(int[] nums, int k) {
        // 创建一个栈用来记录滑动窗口，栈首（First）代表最大的数，栈尾（Last）代表最小的数，单调递减
        Deque<Integer> stack = new ArrayDeque<>();

        // 创建一个变量记录答案
        int n = nums.length;
        int[] ans = new int[n - k + 1];

        // 遍历nums
        for (int right = 0; right < n; right++) {
            // 如果比栈尾（Last）大，那么需要将所有比nums[right]小的数pop出来，以维护栈的单调性
            while (!stack.isEmpty() && nums[stack.peekLast()] <= nums[right]) {
                stack.removeLast();
            }
            // 加入栈
            stack.addLast(right);
            // 计算left
            /*
            当我们将当前循环的right加入stack以后，且当前的滑动窗口的数量为k时（即left与stack.peekFirst()相等时）
            有两种选择：
            1. 将栈首加入ans然后移除栈首
            2. 将栈首加入ans然后在下一个循环移除栈首
            这两个的区别在于究竟是先加入ans还是先移除栈首。虽然看起来好像我们都需要先加入ans，但是由于有循环，所以实际情况不一样
            如果是第一种情况的话：
            当前滑动窗口数量为k，加入ans，然后移除栈首，这就是先加入ans，后移除栈首
            如果是第二种情况的话：
            当前滑动窗口数量为k，加入ans，在下一个循环移除栈首，那么在写法上，我们其实需要先移除栈首，再加ans。我们需要判断当前是否需要
            移除栈首，且判断条件与第一种情况不一样
            第一种情况：maxSlidingWindow()
            第二种情况：maxSlidingWindow2()
             */

            /*
            如果left < 0，说明当前窗口还没有达到k的长度，后续的操作是
            1. 更新ans
            2. 如果left == stack.peekFirst()，说明此时滑动窗口即将离开stack.peekFirst()的位置，所以将stack.peekFirst()直接出栈
            这两步操作都与left < 0时无关，所以直接continue
             */
            int left = right - k + 1;
            if (left < 0) continue;
            // 更新ans
            ans[left] = nums[stack.peekFirst()];
            /*
            还需要保证当前的right和left不超过k的距离，如果超过，那么栈首出栈
            要做到这点，就需要用栈首的数（即栈首所代表的数的位置）与left比较
             */
            // 如果大于栈首所代表的数的位置，栈首出栈
            if (left >= stack.peekFirst()) stack.removeFirst();
        }
        return ans;
    }

    public int[] maxSlidingWindow2(int[] nums, int k) {
        // 创建一个栈用来记录滑动窗口，栈首代表最大的数，栈尾代表最小的数
        Deque<Integer> stack = new ArrayDeque<>();

        // 创建一个变量记录答案
        int n = nums.length;
        int[] ans = new int[n - k + 1];

        // 遍历nums
        for (int right = 0; right < n; right++) {
            // 如果比栈尾大，那么需要将所有的数pop出来存入新的数，以维护栈的单调性
            while (!stack.isEmpty() && nums[stack.peekLast()] <= nums[right]) {
                stack.removeLast();
            }
            // 加入栈
            stack.addLast(right);
            // 计算left
            // 如果left >= 0，那么就需要记录栈首，保存进ans
            int left = right - k + 1;
            // 需要保证当前的right和left不超过k的距离，如果超过，那么栈首出栈
            // 要做到这点，就需要用栈首的数（即栈首所代表的数的位置）与left比较
            /*
            这里就是和第一种情况不一样的条件，我们需要当left大于栈首位置时，才去remove，
            因为当left大于栈首位置时，代表我们当前的滑动窗口数量为k+1，所以先去除栈首，再加入ans
             */
            if (left > stack.peekFirst()) {
                // 如果大于栈首所代表的数的位置，栈首出栈
                stack.removeFirst();
            }
            // 移除栈首
            if (left >= 0) {
                ans[left] = nums[stack.peekFirst()];
            }
        }
        return ans;
    }
}
