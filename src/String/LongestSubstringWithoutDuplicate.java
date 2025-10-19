package String;
/*
https://leetcode.cn/problems/longest-substring-without-repeating-characters/description/?favorite=2cktkvj&orderBy=most_votes
https://www.lintcode.com/problem/384/
https://www.jiuzhang.com/problem/longest-substring-without-repeating-characters/

思路: 滑动窗口
1. 这个题有个隐藏信息需要首先清楚: 计算最长子字符串, 那么一定是一个区间, 想得到这个区间最长可以是多少, 而区间必然有左端和右端, 
    最开始时, 从下标0开始, 左端即是下标0, 右端一直往右, 直到出现第一个重复字符
    当新的字符是重复字符时, 这个重复字符第一次出现的位置假设为i, 那么从这一刻开始, i之前的所有字符都要抛弃, 或者说计数要从头开始算
    比如"abcadef", 当第二个a出现时, 计数清零并且a第一次出现的位置的下一个位置, 成为新的左端
    比如"abccdef", 当第二个c出现时, 计数清零并且c第一次出现的位置的下一个位置, 成为新的左端
    因为如果还想继续往右计算, 那么必然会将这个重复字符的第二次包括在其中, 比如例子1里面的a, 后续的计算都要包括第二个a, 那只能抛弃第一个a
1. 用哈希表记录字符和下标的映射关系, 当重复出现时, 将左指针更新到max(left, map.get(重复字符) + 1）
    这里之所以用max是因为: 
    比如"abba", 在第二个b出现时, 左指针其实已经更新到了下标2, 即第二个b的位置, 此时下标2之前的字符串都不能使用, 不然就会造成重复, 
    如果没有这个max函数, 当第二个a出现时, 理论上应该将左指针更新到下标1, 即第一个b的位置, 但我们刚刚已经确定了, 下标2之前的所有字符都不能用, 
    不然字符b就会重复, 所以为了避免, 有些字符第一次出现的早但第二次出现的晚的时候, 左指针会往回更新的情况, 要用max
1. 当没有重复时, 将新的字符put进哈希表, 并且更新最大的长度max(maxCount, i - left + 1)
    加1是因为, 比如i在下标1, left在下标0, 他们之间是2个字符

时间复杂度: O(N), 遍历一次
空间复杂度: O(N), 哈希表消耗
 */

import java.util.HashMap;

public class LongestSubstringWithoutDuplicate {
    public int solution1(String s) {
        HashMap<Character, Integer> map = new HashMap<>();
        int maxCount = 0;
        int left = 0;

        for (int i = 0; i < s.length(); i++) {
            char letter = s.charAt(i);
            if (map.containsKey(letter)) {
                // 用哈希表记录字符和下标的映射关系, 当重复出现时, 将左指针更新到max(left, map.get(重复字符) + 1）
                left = Math.max(left, map.get(letter) + 1);
            }
            map.put(letter, i);
            maxCount = Math.max(maxCount, i - left + 1);
        }

        return maxCount;
    }
}
