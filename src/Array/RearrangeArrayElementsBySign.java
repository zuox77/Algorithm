package Array;

/*
https://www.lintcode.com/problem/144/
https://www.jiuzhang.com/problem/interleaving-positive-and-negative-numbers/
https://leetcode.cn/problems/rearrange-array-elements-by-sign/description/

You are given a 0-indexed integer array nums of even length consisting of an equal number of positive and negative integers.
You should return the array of nums such that the array follows the given conditions:
    1. Every consecutive pair of integers have opposite signs.
    2. For all integers with the same sign, the order in which they were present in nums is preserved.
    3. The rearranged array begins with a positive integer.
Return the modified array after rearranging the elements to satisfy the aforementioned conditions.

Example 1:
Input: nums = [3,1,-2,-5,2,-4]
Output: [3,-2,1,-5,2,-4]
Explanation:
The positive integers in nums are [3,1,2]. The negative integers are [-2,-5,-4].
The only possible way to rearrange them such that they satisfy all conditions is [3,-2,1,-5,2,-4].
Other ways such as [1,-2,2,-5,3,-4], [3,1,2,-2,-5,-4], [-2,3,-5,1,-4,2] are incorrect because they do not satisfy one or more conditions.

Example 2:
Input: nums = [-1,1]
Output: [1,-1]
Explanation:
1 is the only positive integer and -1 the only negative integer in nums.
So nums is rearranged to [1,-1].

思路: 双指针
1. 我们需要一正一负的排列数组，所以不如新建一个空数组，然后用一个小数组（int[2]）当前应该放的正数和负数的位置分别是什么
2. 遍历原数组
    当当前数字为正数时，将其放入它该放入的位置，并将位置+2（一正一负排列，所以当前位置放了正数，那么下下个位置才会再次放入正数）
    如果是负数，同理
 */

public class RearrangeArrayElementsBySign {
    public int[] rearrangeArray(int[] nums) {
        // ans: 结果数组
        // pos: 位置指针数组，pos[0]用于正数位置，pos[1]用于负数位置
        int[] ans = new int[nums.length], pos = {0, 1};

        /*
        x >>> 31 代表无符号右移操作，即将x的二进制数，全部右移31位，且右侧溢出的部分抛弃不要，左侧不够的部分用0来补足
        为什么是31？因为java的int是32位的二进制，所以全部右移31位，就是抛弃了这个数的数值本身，只保留其符号位（第32位，即最高位，下标是31）
        比如1000（假设有这么一个四位的二进制），其中的最高位（第四位）是1，其下标为3（从0开始）
        那么将其 >>> 2以后，变成了0010，所以1到了第二位，下标为1，所以 >>> 2其实就是将其下标 - 2
        回到原来的情景中，int是32位的二进制，其最高位为31（从0开始计数），所以 >>> 31，就是将其最高位变成第1位，即下标0
        x >>> 31只会等于0或者1
        而原本最高位为1的时候，代表负数，为0的时候代表正数，我们上面要用pos[0]代表正数
         */
        for (int x : nums) {
            // x >>> 31 完全等同于 x > 0 ? 0 : 1
            ans[pos[x >>> 31]] = x; // 根据数字符号放入对应位置
            pos[x >>> 31] += 2; // 相应位置指针前进2步
        }
        return ans;
    }
}
