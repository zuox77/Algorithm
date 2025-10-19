package DynamicProgramming;

/*
https://leetcode.cn/problems/trapping-rain-water/?envType=study-plan-v2&envId=top-100-liked

Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.

Example 1:
Input: height = [0,1,0,2,1,0,1,3,2,1,2,1]
Output: 6
Explanation: The above elevation map (black section) is represented by array [0,1,0,2,1,0,1,3,2,1,2,1].
In this case, 6 units of rain water (blue section) are being trapped.

Example 2:
Input: height = [4,2,0,3,2,5]
Output: 9

思路1: 普通DP/双指针
1. 根据题意和图，可以得到几个隐藏信息：
    1. 要想装水，只有左右两侧都大于0时，才能装水
    2. 此题的最佳解法肯定是O(N)，即遍历一次/多次，来解决
    3. 当左右两侧均大于0时，能装多少水，取决于左右两侧高度的最小值，以及左右两侧相距的宽度
2. 思路为：
    1. 定义一个指针，从左往右遍历
    2. 每次循环，计算当前下标能装多少水，这样可以不需要考虑宽度的问题
    3. 第二步中，有一个需要注意的点：因为是从左往右遍历，所以其实我们只知道左侧的最小值是多少，该如何判断右侧呢？
        比如[5,4,3,2,1,0,?,?,?,?, ... , ?]，当遍历到4的时候，假设右侧的?中最大的数为6，那么可以装1的水，但如果右侧的?中最大的值
        为2，那么一点水都装不了（因为上面的隐藏信息3说了，要取决于左右两侧高度的最小值，现在最小值为2，所以必须小于2的才能装水）
        该怎么解决这个问题？
        答案：定义另一个指针，从右往左遍历，且左右指针移动的条件是，哪个指针当前位置上的高度小，谁移动（height[left] vs height[right]）
        为什么？
        同样的例子[5,4,3,2,1,0,?,?,?,?, ... , ?]，如果右侧最大的值为6，那么当右指针遍历到6时，根据条件，左指针会一直移动，右指针不变。
        但请注意，我们保持右指针不变，并不是因为右指针此时代表的是，"取决于左右两侧高度的最小值"中右侧的高度，相反，左指针在自己移动的过程中，
        会用一个变量记录左侧的高度，然后利用新的值与这个高度进行比较，从而计算出当下的水量。
        比如[3,0,0,0,3,2,1]，当左指针遍历到第一个0时，假设右指针遍历到第一个1，此时难道左侧能装的水量为1吗？显然不是，它能装3的水。
        所以其实这个条件（哪个指针当前位置上的高度小，谁移动），真实的含义是，让当前高度的最大的那个指针不动，这样另一个指针动的时候，我能保证，
        无论它的高度是多少，都不会比另一个指针的高度更大，所以我可以安心的用自己每次循环记录的最大值去计算水量。
        比如[3,0,1,2,5,0,1]，当左指针开始遍历的时候，发现右指针的高度更低，所以移动右指针，右指针从1移动到0的过程中，我每次都会判断
        当前高度谁打谁小（height[left] vs height[right]），并记录右指针经历过的数的最大值，我虽然不知道右指针的下一个数是大是小，
        但都不妨碍我直接去计算水量，比如到0的时候，右指针经历过的最大的数为1，所以0的位置能装1的水。
        而后右指针指向5，右侧的计算结束，左指针开始移动。左指针遍历到第一个0时，左侧经历过最大的数为3，所以能装的水量为3，右侧是多少不影响计算
3. 步骤：
    1. 循环left < right
    2. 移动左指针：while (height[left] <= height[right] && left < right)。一定要记得将left < right带入，不然左右指针就会越界
    3. 循环中：
        1. 直接先移动一下指针（因为初始化的时候已经将left和right以及它们位置上的高度都算进去了）
        2. 当leftMax > height[left]，计算水
        3. 当leftMax <= height[left]，更新leftMax
    4. 移动右指针：while (height[left] > height[right] && left < right)。一定要记得将left < right带入，不然左右指针就会越界
    5. 循环中：
        1. 直接先移动一下指针（因为初始化的时候已经将left和right以及它们位置上的高度都算进去了）
        2. 当rightMax > height[right]，计算水
        3. 当rightMax <= height[right]，更新rightMax

思路2: 计算双向最大值数组，然后比较
1. 根据题意和图，可以得到几个隐藏信息：
    1. 要想装水，只有左右两侧都大于0时，才能装水
    2. 此题的最佳解法肯定是O(N)，即遍历一次/多次，来解决
    3. 当左右两侧均大于0时，能装多少水，取决于左右两侧高度的最小值，以及左右两侧相距的宽度
2. 思路为：
    1. 从左往右遍历，算出到i的时候，当前最大高度是多少，存为数组（leftMax = [...]）
    2. 从右往左遍历，算出到i的时候，当前最大高度是多少，存为数组（rightMax= [...]）
    3. 从左往右遍历，选出第一步和第二步中，i的位置下，最小的高度（Math.min(leftMax[i], rightMax[i])），再减去当前高度（height[i]）

思路3：单调栈
1. 根据题意和图，可以得到几个隐藏信息：
    1. 要想装水，只有左右两侧都大于0时，才能装水
    2. 此题的最佳解法肯定是O(N)，即遍历一次/多次，来解决
    3. 当左右两侧均大于0时，能装多少水，取决于左右两侧高度的最小值，以及左右两侧相距的宽度
2. 思路为：
    1. 其实跟前两个思路类似，都是通过一些方式，保证当前只有一侧的最大值可以影响水量，从而做到O(N)的时间复杂度
    2. 如果当前是高度在持续下降，那么保存所有的下标，又由于单调栈可以保证顺序（其实这道题并没有用上，这题存储的是下标，只是利用了FILO特性）
        所以栈顶是当前的最小高度（同样的，因为存储的是下标，所以对于高度，其实我们没有用到"单调"这个特性，而是通过条件判断，让高度强行"单调"）
        例如[6,4,2,0,1,3,5]，单调栈会一直遍历，保存[6,4,2,0]（保存的是下标，不是高度本身），直到1的时候，由于1大于栈顶元素（3）所对应的高度（0），
        所以3出栈，开始计算0的水量（因为保存的是下标，所以可以计算宽度）
        然后继续循环，判断栈顶，此时2为栈顶，2大于1，所以没法存水，往下走，走到3的时候，3大于栈顶（2），所以2出栈，然后计算2的水量。
        长话短说：如果一直下降，则一直压进栈，直到出现大于栈顶的元素，然后计算水量（宽度和高度），然后看下一个站立的元素，直到栈顶大于当前高度。

 */

import java.util.ArrayDeque;
import java.util.Deque;

public class TrappingRainWater {

  public int trap(int[] height) {
    int water = 0;
    int left = 0;
    int right = height.length - 1;
    int leftMax = height[left];
    int rightMax = height[right];

    while (left < right) {
      // Move left
      while (height[left] <= height[right] && left < right) { // 一定要记得 left < right
        // 先移动一次，因为是从index 0开始的，而index 0的高度已经作为leftMax，在变量声明和初始化时保存了
        // 相当于先把i = 0保存，然后从i = 1开始
        left++;
        // leftMax表示当前已遍历过的最大高度，在新的最大高度出现之前，可以直接计算当前水量
        // 再次重申，如果疑惑：为什么在不知道右侧高度的情况下可以这么计算，请看上方解答
        // 简而言之：height[left] <= height[right]条件会保证：在此循环中，只有左侧的高度会决定水量，跟右侧无关
        if (leftMax > height[left]) {
          water += leftMax - height[left];
          // 更新左侧最大值
        } else {
          leftMax = height[left];
        }
      }
      // Move right
      while (height[left] > height[right] && left < right) { // 一定要记得 left < right
        // 先移动一次，因为是从index n-1开始的，而index n-1的高度已经作为rightMax，在变量声明和初始化时保存了
        // 相当于先把i = n-1保存，然后从i = n-2开始
        right--;
        // 同理上方
        if (rightMax > height[right]) {
          water += rightMax - height[right];
          // 更新右侧最大值
        } else {
          rightMax = height[right];
        }
      }
    }
    return water;
  }

  public int trap2(int[] height) {
    int n = height.length;

    // 计算从左往右的最大值
    int[] leftMax = new int[n];
    leftMax[0] = height[0];
    for (int i = 1; i < n; i++) {
      leftMax[i] = Math.max(leftMax[i - 1], height[i]);
    }

    // 计算从右往左的最大值
    int[] rightMax = new int[n];
    rightMax[n - 1] = height[n - 1];
    for (int i = n - 2; i >= 0; i--) {
      rightMax[i] = Math.max(rightMax[i + 1], height[i]);
    }

    // 从左往右遍历，每次取两个最大值中最小的，再减去当前高度
    int water = 0;
    for (int i = 0; i < n; i++) {
      water += Math.min(leftMax[i], rightMax[i]) - height[i];
    }
    return water;
  }

  public int trap3(int[] height) {
    int water = 0;
    Deque<Integer> stack = new ArrayDeque<>();

    for (int i = 0; i < height.length; i++) {
      // 当栈顶元素（在此题中，即为上一个位置）的高度如果小于等于当前高度，即为解析中提到的上升时，开始计算
      /*
      为什么等于也要包括进来？
      当等于时，height[stack.peek()] = height[i]，所以prevHeight = height[stack.pop()] = height[i]
      所以在计算waterHeight时：
         1. 如果 Math.min(height[prevPrevIndex], height[i]) = height[i] 时
             那么waterHeight = height[i] - prevHeight = 0，不会计算水量
         2. 如果 Math.min(height[prevPrevIndex], height[i]) = height[prevPrevIndex] 时
             而已知我们通过条件强行将stack变量变成了一个单调栈，所以单调栈里面的元素的高度一定是递增的，即栈顶元素所对应的高度最小
             所以height[prevPrevIndex]一定大于prevHeight，那也没问题，该计算就计算，该增加水量就增加
       所以当等于时，有点类似于计算[4,2,2,2,3,...]这种情况，当遍历到中间那几个2时，会发生等于的情况。
       具体发生了什么：
       1. 当遍历到第一个2时，会直接压进栈，此时stack = [0, 1]（再次提醒：stack存储的是高度的index，而不是高度本身，因为我们需要index来计算宽度）
       2. 当遍历到第二个2时，会进入循环，把第一个2 pop出来，此时相当于我们在计算[4, 2, 2]的第一个2，可以装多少水
             可以明显看出，左侧高度4，右侧高度2，中间高度2，所以waterHeight = 0，不能装水
             最后，把第二个的2 push进去
       3. 当遍历到第三个2时，同理第二个2，记住：每次都会把上一个2 pop出来，然后把当前的2 push进去
       4. 当遍历到3时，注意，虽然前面遍历了三个2，但是此时stack里面只有最后一个2还保留着，即stack = [0, 3]（0代表4，3代表最后一个2）。
             此时相当于我们在计算[4, 2, 3]的2可以装多少水，
             与之前情况不同，此时能装水了，因为waterHeight = 3（左侧高度4，右侧高度3，二者取最小） - 2 = 1
             但是宽度上，是 4（高度3所代表的index）- 0（高度4所代表的index）- 1 = 3，所以能装水 = 3 * 1 = 3
      */
      while (!stack.isEmpty() && height[stack.peek()] <= height[i]) {
        /*
        注意此时：
        栈顶（stack.pop()） = 需要计算水量的位置
        栈顶的下一个元素（stack.pop().peek(，即prevPrevIndex）：左侧的位置
        当前元素（i）：右侧的位置
         */
        // 之所以这里直接将其pop出来而不是peek，因为这个index并不重要，我们已经有左侧位置和右侧位置了，
        // 利用左右侧位置差就可以算出宽度，所以这里只需要pop出来，记录一个最低高度
        // 为什么是最低？因为这个循环的条件就是高度持续下降时，一直压进栈，所以栈顶一定是高度最低的
        int prevHeight = height[stack.pop()];
        // 这里千万别忘了这个if，因为后续需要peek左侧位置，如果栈里已经没有元素了，我们无法得知左侧位置，也就不需要计算了
        if (stack.isEmpty()) {
          break;
        }
        // 找到左侧位置
        int prevPrevIndex = stack.peek();
        // 左侧位置和右侧位置的高度取最小值，减去需要计算水量的位置的高度，即为水量高度
        int waterHeight = Math.min(height[prevPrevIndex], height[i]) - prevHeight;
        // 宽度就是右侧位置-左侧位置-1
        water += waterHeight * (i - prevPrevIndex - 1);
      }
      // 当栈顶元素（在此题中，即为上一个位置）的高度如果小于等于当前高度（即为解析中提到的持续下降时），直接压进栈
      // 或者当前面的计算都结束以后，把当前index压进栈
      stack.push(i);
    }
    return water;
  }
}
