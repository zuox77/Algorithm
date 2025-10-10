package DynamicProgramming;
/*
https://leetcode.cn/problems/decode-ways/description/

A message containing letters from A-Z can be encoded into numbers using the following mapping:
'A' -> "1"
'B' -> "2"
...
'Z' -> "26"
Given a string s containing only digits, return the number of ways to decode it.

To decode an encoded message, all the digits must be grouped then mapped back into letters using the reverse of the mapping above (there may be multiple ways).
For example, "11106" can be mapped into:
"AAJF" with the grouping (1 1 10 6)
"KJF" with the grouping (11 10 6)
Note that the grouping (1 11 06) is invalid because "06" cannot be mapped into 'F' since "6" is different from "06".

Input: s = "226"
Output: 3
Explanation: "226" could be decoded as "BZ" (2 26), "VF" (22 6), or "BBF" (2 2 6).

Input: s = "06"
Output: 0
Explanation: "06" cannot be mapped to "F" because of the leading zero ("6" is different from "06").

思路: DP
1. state: dp[i]表示, 第0到第i个字符, 最多能组成多少个排列
2. 根据题意, 先总结所有的可能性, 因为最大的数是26(对应字母z), 所以需要两个字符一起考虑
   假设现在遍历到下标i的位置, 并假设下标i位置上的字符所对应的数字是current(current = s.charAt(i)), 
   下标i-1位置上的字符所对应的数字是last(last = s.charAt(i-1))
   对于last和current, 有以下几种情况
   last = 0, current = 0-9      -> 因为0和任何数都无法组成合理的数字, 比如00/06等, 都不是合理的数字, 标记为case 4
   last = 1, current = 0        -> 1和0可以组成10, 标记为case 1, 后面详细说
   last = 1, current = 1-9      -> 1和1-9可以共同组成合理的数字, 标记为case 2, 后面详细说
   last = 2, current = 0        -> 2和0可以共同组成20, 与case 1类似, 所以也标记为case 1
   last = 2, current = 1-6      -> 2和1-6可以共同组成合理的数字, 与case 2类似, 所以也标记为case 2
   last = 2, current = 7-9      -> 2和7-9无法共同组成合理的数字, 因为27/28/29都大于26, 所以他们只能单独表示, 标记为case 3
   last = 3 - 9, current = 0    -> 3-9和0无法组成合理的数字, 比如30/50等, 都不是合理的数字, 标记为case 4
   last = 3 - 9, current = 1-9  -> 3-9和1-9无法共同组成合理的数字, 因为37/56等都大于26, 所以他们只能单独表示, 标记为case 3
   对于case 1, 即((last == 1 || last == 2) && current == 0): 
       此时current只能与前一个数组成字母, 比如[210], 本来1可以与2组成[2,1]和[21], 但由于0加入, 0一定要和1组合, 所以现在只能变成[2,10]
       所以在此情况下, 组合的数量不仅不会增多, 还会减少, 减少到与i - 2的时候一样, 因为i - 1(即last)与i(即current)一定要组合在一起
       所以此时dp[i] = dp[i - 2]
   对于case 2, 即(last == 1 && 1 <= current <= 9 || last == 2 && 1 <= current <= 6)
       此时current既可以与last合起来组成字母, 也可以单独组成字母, 所以此时组合的数量会增加, 增加的规律如下, 以[1,1,2,3,5]举例: 
       对于[1],         一共有 1 种排列组合: [1]
       对于[1,1],       一共有 2 种排列组合: [1, 1] [11]
       对于[1,1,2],     一共有 3 种排列组合: [1, 1, 2] [1, 12] [11, 2]
       对于[1,1,2,3],   一共有 5 种排列组合: [1, 1, 2, 3] [1, 12, 3] [1, 1, 23] [11, 2, 3] [11, 23]
       对于[1,1,2,3,5], 一共有 8 种排列组合: [1, 1, 2, 3, 5] [1, 12, 3, 5] [1, 12, 35] [1, 1, 23, 5] [1, 1, 2, 35] [11, 2, 3, 5] [11, 23, 5] [11, 2, 35]
       所以此时dp[i] = dp[i - 1] + dp[i - 2]
   对于case 3, 即(last == 2 && 7 <= current <= 9 || 3 <= last <= 9 && 1 <= current <= 9)
       此时current无法与last合起来组成字母, 但他们可以各自单独组成字母, 比如29 -> [2,9], 58 -> [5,8]
       所以此时组合的数量不会增加
       所以此时dp[i] = dp[i - 1]
   对于case 4, 即(3 <= last <= 9 && current == 0)
       注意: 如果此时last是0的情况, 一般由于dp会将第一个数作为初始化, 所以我们会提前看第一个数, 将其分开来判断, 所以这里不讨论last = 0的情况
       对于case 4的其他情况, 根据题目提供的example可以发现, 如果是06则直接返回0
       所以此时直接return 0
3. 总结上面的规律可以发现, dp最多只和dp[i - 2]和dp[i - 1]有关, 所以既可以用滚动数组/变量来记录dp的值, 也可以用dp数组
4. 由于dp可能会和dp[i - 2]有关, 所以如果是用dp数组的话, 且如果当i=1的时候就需要用到dp[i - 2]怎么办呢？
   1. 可以用额外一个位置用来存放dp[-1]
      比如如果是'10', 那么当遍历到0的时候, 即下标1的时候, 根据公式, dp[1] = dp[0] + dp[-1], 所以需要额外一个位置
      但我们又不可能用dp[-1], 所以只能用i + 1来代表现在
   2. 更好的方式是用一个判断语句, 因为dp[i - 2]只有当i = 1的时候需要考虑边界问题(即dp[-1]不存在-1下标), 所以可以用一个三元表达式
      dp[i - 2] = i - 2 < 0 ? 1 : dp[i - 2]
5. initialize: dp[0] = 1, 按照dp的定义应该很好理解, 无论如何第一个数都只可能有一种排列方式
4. answer: dp[-1]

思路1: DP + dp数组
1. 用dp数组来记录dp的值
2. 有i - 2的地方需要额外判断一次
3. 详见solution1

思路2: DP + 滚动数组/变量
 */

public class DecodeWays {
    public int solution1(String s) {
        // corner case
        if (s.charAt(0) == '0') {
            return 0;
        }
        // 声明变量
        int n = s.length();
        int[] dp = new int[n];
        // 初始化
        dp[0] = 1;
        // 遍历
        for (int i = 1; i < n; i++) {
            int last = s.charAt(i - 1) - '0';
            int current = s.charAt(i) - '0';
            // 根据情况写迭代方程
            // case 1
            if (current == 0 && (last == 1 || last == 2)) {
                // 额外判断一下
                dp[i] = i - 2 < 0 ? 1 : dp[i - 2];
                // case 2
            } else if (last == 1 && (1 <= current && current <= 9) || last == 2 && (1 <= current && current <= 6)) {
                // 额外判断一下
                dp[i] = i - 2 < 0 ? 1 : dp[i - 2];
                dp[i] += dp[i - 1];
                // case 3
            } else if (last == 2 && (7 <= current && current <= 9) || (3 <= last || last <= 9) && (1 <= current && current <= 9)) {
                dp[i] = dp[i - 1];
                // case 4
            } else {
                return 0;
            }
        }
        // 返回最后一个数
        return dp[n - 1];
    }

    public int solution2(String s) {
        // corner case
        if (s.charAt(0) == '0') {
            return 0;
        }
        // 声明变量
        int n = s.length();
        // dpLast是i-2, dpCurrent是i-1, 每次遍历时, 将dpCurrent用tmp存下来, 将dpCurrent更新为i, 最后将tmp赋予dpLast, 这样dpLast就是i-1, dpCurrent就是i, 然后开始遍历i+1
        int dpLast = 1, dpCurrent = 1;
        // 遍历
        for (int i = 1; i < n; i++) {
            int last = s.charAt(i - 1) - '0';
            int current = s.charAt(i) - '0';
            // 用一个变量记录一下dpCurrent, 因为做完所有判断以后, dpLast就会更新为当前的dpCurrent, 而dpCurrent会根据判断更新为新的值
            int tmp = dpCurrent;
            // 根据情况写迭代方程
            // case 1
            if (current == 0 && (last == 1 || last == 2)) {
                // 额外判断一下
                dpCurrent = i - 2 < 0 ? 1 : dpLast;
                // case 2
            } else if (last == 1 && (1 <= current && current <= 9) || last == 2 && (1 <= current && current <= 6)) {
                // 额外判断一下
                dpCurrent = i - 2 < 0 ? 1 : dpLast;
                dpCurrent += tmp;
                // case 3
            } else if (last == 2 && (7 <= current && current <= 9) || (3 <= last || last <= 9) && (1 <= current && current <= 9)) {
                dpCurrent = tmp;
                // case 4
            } else {
                return 0;
            }
            // 更新dpLast
            dpLast = tmp;
        }
        // 返回最后一个数
        return dpCurrent;
    }
}
