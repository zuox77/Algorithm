package Array.Palindrome;

/*
刷题次数: 2

https://leetcode.cn/problems/longest-palindromic-substring/description/

Given a string s, return the longest palindromic substring in s.

Example 1:
Input: s = "babad"
Output: "bab"
Explanation: "aba" is also a valid answer.

Example 2:
Input: s = "cbbd"
Output: "bb"

a b a b c
 cnt

思路:
1. 中心扩展法, 即遍历每个点, 以每个点为中点, 向两侧扩散检查关于中心点对称的两点是否相等
2. 思路很简单, 需要注意的点有:
    1. for循环, 不要写(int i = 1; i < len - 1; i++), 因为从1开始, 或者从len-2结束(因为是<len-1, 即最大为len-2), 会让单字符的情况漏算
    2. 此题奇数偶数在中心扩展时不一样, 所以扩展方程要有start和end参数, 奇数时start=end=中心点, 偶数时start和end是相邻两点
    3. 中心扩展方程的return是: odd[0]是substring的左端, odd[1]是长度
    4. 如果中心扩展方程要return左右两端的下标, 那计算长度时候还要重新算长度
 */
public class LongestPalindromicSubstring {
    public String longestPalindromicSubstring(String s) {
        int maxLength = 0;
        int[] result = new int[2];
        int len = s.length();

        // for循环不要从1开始, 也不要写< len- 1
        for (int i = 0; i < len; i++) {
            // 奇数偶数的中心点定义不一样
            // 所以要运行两次, 再比较两次的最大长度的大小, 取最大
            int[] odd = centerSpread(s, len, i, i);
            int[] even = centerSpread(s, len, i, i + 1);
            int[] longer = odd[1] > even[1] ? odd : even;
            if (longer[1] > maxLength) {
                maxLength = longer[1];
                result = longer;
            }
        }
        return s.substring(result[0], result[0] + result[1]);
    }

    public int[] centerSpread(String s, int len, int start, int end) {
        while (start >= 0 && end < len && s.charAt(start) == s.charAt(end)) {
            // start往左移动, end往右移动, 扩散开来
            start--;
            end++;
        }
        // 结果是substring的左端下标和长度
        return new int[] {start + 1, end - start - 1};
    }

    public String longestPalindromicSubstring2(String s) {
        // Corner case
        if (s.isBlank()) return "";
        int n = s.length();
        if (n == 1) return s;
        // 处理s
        char[] charList = processString(s, n);
        // curPalinCenter代表当前回文串范围最大的中心位置
        // curPalinRight代表当前回文串范围最大的最右端
        // maxRadius代表全局最大的回文串的半径
        // maxCenter代表全局最大的回文串的中心
        int curPalinCenter = 0, curPalinRight = 0, maxRadius = 0, maxCenter = 0;
        int[] palinRadius = new int[2 * n + 3];
        // 遍历charList,因为第一个字符是^,所以跳过
        for (int i = 1; i < 2 * n + 2; i++) {
            // 如果此时i在之前的某个位置的回文串范围内,那么我们直接去找其镜像位置
            // 如果不在的范围的话,就只能从头开始中心扩展法
            if (i < curPalinRight) {
                int mirrorIdx = 2 * curPalinCenter - i;
                // 更新palinRadius[i]
                // 这里要注意,如果镜像位置的回文串范围(palinRadius[mirrorIdx] + mirrorIdx)超过了当前的回文串范围,
                // 那我们不能保证超过的部分依旧是镜像,所以要取二者的最小值,以此为基础开始中心扩展
                palinRadius[i] = Math.min(palinRadius[mirrorIdx], curPalinRight - i);
            }
            // 从palinRadius[i]开始中心扩展法
            while (charList[i + palinRadius[i] + 1] == charList[i - palinRadius[i] - 1]) {
                palinRadius[i]++;
            }
            // 如果i+palinRadius[i]超过了curPalinRight,则更新curPalinCenter和curPalinRight
            if (i + palinRadius[i] > curPalinRight) {
                curPalinRight = i + palinRadius[i];
                curPalinCenter = i;
            }
            // 更新全局最大值
            if (palinRadius[i] > maxRadius) {
                maxRadius = palinRadius[i];
                maxCenter = i;
            }
        }
        // 拿到结果
        // 注意,我们保存的maxCenter和maxRadius都是处理后的字符串的位置和半径,而我们对于字符串的处理,是增加#
        // 例如对于"babad",其结果为"aba",start应该为1,截取的范围为(1, 4)
        // 转化以后为"^#b#a#b#a#d#$",maxCenter=5,maxRadius=3,(5 - 3) / 2 = 1,刚好就是start,截取的范围为(1, 1 + 3) =
        // (1, 4)
        int start = (maxCenter - maxRadius) / 2;
        return s.substring(start, start + maxRadius);
    }

    private char[] processString(String s, int n) {
        // 我们要对每个字母的左右都插入#,一共需要插入n+1
        // 我们还需要对首尾插入^和$,一共需要插入2个
        // 所以新的char[]的长度为2n+3
        char[] charList = new char[2 * n + 3];
        // 手动补上第一个符号
        charList[0] = '^';
        // 对每个字母,在其前面插入#
        for (int i = 0; i < n; i++) {
            charList[i * 2 + 1] = '#';
            charList[i * 2 + 2] = s.charAt(i);
        }
        // 补上最后两位
        charList[n * 2 + 1] = '#';
        charList[n * 2 + 2] = '$';
        return charList;
    }
}
