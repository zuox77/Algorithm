package Array;
import java.util.*;
/*
刷题次数: 2

https://leetcode.cn/problems/longest-consecutive-sequence/description/

Given an unsorted array of integers nums, return the length of the longest consecutive elements sequence.
You must write an algorithm that runs in O(n) time.

Input: nums = [100,4,200,1,3,2]
Output: 4
Explanation: The longest consecutive elements sequence is [1, 2, 3, 4]. Therefore its length is 4.

思路: 
1. 题目要求需要O(N)时间复杂度, 那么肯定是遍历一次就能找到
2. 所以对于数组中存在的任意连续数列, 我们规定, 只有遇到了该连续数列的最小值, 我们才开始计数, 例如[100,4,200,1,3,2]
   我们能知道该数组中最长的数列为[1, 2, 3, 4], 当我们从左向右遍历时, 会第一个遇到4, 这时候, 我们判断是否存在比4小1的数, 即3, 如果存在, 则说明
   如果4能够与其他数组成连续数列, 4也不是该连续数列的最小值, 所以在遇到4的时候, 我们跳过, 不计算
   当遇到100时, 能发现100不能与任何数组成队列, 所以100本身就是连续数列[100]的最小值, 也是唯一的值, 所以在用一个变量记录不同数列的初始值时, 
   我们需要初始化为1而不是0
3. 用HashSet去重和查找
4. 三刷的时候: 忘记了负数怎么处理, 也忘记了该怎么记录length
   不应该用一个result来记录并且用Math.max(result, num)来更新, 这样的话比如[-1, 0, 1, 2], 明明length是4, 但却只能记录2
   应该将每一个最小值(最小值的意思是2中的连续数列最小值的意思)放入while循环, 去判断该数的最大长度, 然后最后记录下来, 在for的初始重制这个temp值
 */

public class LongestConsecutiveSequence {
    public int longestConsecutive(int[] nums) {
        // add all numbers to a HashSet to deduplicate
        HashSet<Integer> set = new HashSet<>();
        for (int num: nums) {
            set.add(num);
        }

        // iterate the HashSet to calculate the maximum sequence
        int result = 0;
        for (int num: set) {
            int len = 1; // 初始化为1
            // 如果这个数有sequence, 且这个数不是sequence中最小的, 
            // 那么说明这个数在之前或者之后会被重复计算
            // 我们只考虑如果有sequence且当前的数是sequence中最小的那个, 即起点
            if (set.contains(num - 1)) continue;
            while (set.contains(num + 1)) {
                len++;
                num++;
            }
            result = Math.max(result, len);
        }
        return result;
    }
}
