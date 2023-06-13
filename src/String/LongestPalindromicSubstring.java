package String;
/*
https://leetcode.cn/problems/longest-palindromic-substring/description/

思路：
1. 中心扩展法，即遍历每个点，以每个点为中点，向两侧扩散检查关于中心点对称的两点是否相等
2. 思路很简单，需要注意的点有：
    1. for循环，不要写(int i = 1; i < len - 1; i++)，因为从1开始，或者从len-2结束(因为是<len-1，即最大为len-2)，会让单字符的情况漏算
    2. 此题奇数偶数在中心扩展时不一样，所以扩展方程要有start和end参数，奇数时start=end=中心点，偶数时start和end是相邻两点
    3. 中心扩展方程的return是：odd[0]是substring的左端，odd[1]是长度
    4. 如果中心扩展方程要return左右两端的下标，那计算长度时候还要重新算长度
 */
public class LongestPalindromicSubstring {
    public String Solution(String s) {
        int maxLength = 0;
        int[] result = new int[2];
        int len = s.length();

        // for循环不要从1开始，也不要写< len- 1
        for (int i = 0; i < len; i++) {
            // 奇数偶数的中心点定义不一样
            // 所以要运行两次，再比较两次的大小取最大
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
            start--;
            end++;
        }
        // 结果是substring的左端下标和长度
        return new int[] {start + 1, end - start - 1};
    }
}
