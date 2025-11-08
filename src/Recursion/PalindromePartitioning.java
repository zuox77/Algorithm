package Recursion;

import java.util.ArrayList;
import java.util.List;

/*
https://leetcode.cn/problems/palindrome-partitioning/description/?envType=study-plan-v2&envId=top-100-liked

Given a string s, partition s such that every substring of the partition is a palindrome.
Return all possible palindrome partitioning of s.

Example 1:
Input: s = "aab"
Output: [["a","a","b"],["aa","b"]]

Example 2:
Input: s = "a"
Output: [["a"]]

思路: DFS/回溯/双指针
1. 主要要考虑清楚每次循环需要做什么事情以及准备怎么来做
2. 要想每个substring都是回文串，那肯定就需要分割，所以这个问题就变成了，如何分割s，且每个分割出来的substring都是回文串
3. 需要记住：dfs和回溯本身并不是时间复杂度的优化器，只是一种解决问题的方式，或者说，它基本能和迭代（遍历）等同，所以其实就是一种枚举的方式
4. 所以我们现在的问题变成了：枚举每个可能的分割的位置，并检查是否为回文串，如果是，则加入答案，然后继续枚举
5. 那么基本思路就很清晰了，用双指针来辅助，左指针和右指针分别代表两个位置，中间的部分即想要加入答案的substring

1. 如果不用for loop，即partition()方法，那么就需要考虑几个情况
    1. 右指针已经大于s的长度了，这就是退出条件，直接将其放入ans
    2. 右指针最大的不分割的地方在哪里，因为每次只有两个操作，分割或者不分割，所以我们需要设定，当右指针小于某个值的时候，才能选择不分割
        即end < s.length() - 1时。因为当end = s.length() - 1，end已经处于s的最后一个字母了，那么此时不存在分割或者不分割选项了，
        只能分割，即，将s整个都当成一个回文串去检查，并根据结果看是否放入ans
2. 如果用for loop，那么就将右指针当成iterator，左指针只需要在找到回文串的时候，跟右指针一起移动就行，具体可以看partition2()

 */
public class PalindromePartitioning {
    public List<List<String>> partition(String s) {
        // 创建一个list保存答案
        List<List<String>> ans = new ArrayList<>();
        recursion(s, 0, 0, ans, new ArrayList<String>());
        return ans;
    }

    // 把start当做是substring的起始位置，把end当做是substring的结束位置，
    // 或者说考虑两个指针，都从0开始，end指针不断往右挪动，然后每次只有两种情况：分割或者不分割
    // 如果不分割，那么end指针往右挪动1，继续往下
    // 如果分割，那么代表我们想将从start到end这一段的substring当成回文串加入ans，所以此时还需要再检查一下是否是回文串
    // 如果是，则加入，然后start和end指针都移动到end + 1的位置（因为end之前的字母都已经分割+检查结束了）
    // 如果不是，那么什么都不发生，因为我们要分割出来的每一个都是回文串
    public void recursion(
            String s, int start, int end, List<List<String>> ans, List<String> substring) {
        // 退出条件
        // 因为end代表下标，所以end如果等于s.length()，那代表其实已经遍历结束了，所以退出
        if (end == s.length()) {
            ans.add(new ArrayList<>(substring));
            return;
        }
        // 因为end最多等于s.length() - 1，假设每次都不分割，那么最多就是全选，将整个s作为substring，即start=0，end=s.length() - 1
        // 那么此时，当end=s.length() - 1，就必须分割
        // 所以选择分割的最大值为end < s.length() - 1
        if (end < s.length() - 1) {
            // 不分割，那么右指针继续往右移动
            recursion(s, start, end + 1, ans, substring);
        }
        // 如果选择分割，那么就要检查是否为回文串
        if (isPalindrome(s, start, end)) {
            // 如果确实为回文串，那么加入substring
            substring.add(s.substring(start, end + 1));
            // 移动所有指针
            recursion(s, end + 1, end + 1, ans, substring);
            // 返回以后移除substring的最后一位
            substring.removeLast();
        }
    }

    public List<List<String>> partition2(String s) {
        // 创建一个list保存答案
        List<List<String>> ans = new ArrayList<>();
        recursion(s, 0, ans, new ArrayList<String>());
        return ans;
    }

    public void recursion(String s, int start, List<List<String>> ans, List<String> substring) {
        // 退出条件
        // 这里需要稍微多想一下，好像我们应该检查start == s.length() 作为退出条件才对，因为end一定比start先到终点
        // 但原因是：因为我们只用了一个指针在方程里面传递，所以每次传进去的int start，其实不仅代表start指针，也代表end指针会从这里开始遍历
        // 这其实就是利用了“如果找到了一个回文串，那么下一个回文串，左右指针从当前回文串结束位置+1的地方开始”这个条件
        // 每次找到，左右指针都会移动到相同的位置，所以我们才可以只用一个变量去传递新的位置
        // 所以这里的退出条件看似是start == s.length()，实则也是end == s.length()，所以没问题
        if (start == s.length()) {
            ans.add(new ArrayList<>(substring));
            return;
        }

        // 枚举结束的位置，所以for loop里面是end指针（右指针）
        // 上面讲过，如果找到了一个回文串，那么下一个需要左右指针从当前回文串结束位置+1的地方开始，所以需要用start = end这个条件，
        // 不然for loop用start = 0开始的话，就会重复遍历
        for (int end = start; end < s.length(); end++) {
            if (isPalindrome(s, start, end)) {
                // 如果确实为回文串，那么加入substring
                substring.add(s.substring(start, end + 1));
                // 移动所有指针，从当前回文串结束位置+1的地方开始继续枚举，end+1就是新的start指针位置
                recursion(s, end + 1, ans, substring);
                // 返回以后移除substring的最后一位
                substring.removeLast();
            }
        }
    }

    public boolean isPalindrome(String s, int start, int end) {
        while (start < end) {
            if (s.charAt(start++) != s.charAt(end--)) {
                return false;
            }
        }
        return true;
    }
}
