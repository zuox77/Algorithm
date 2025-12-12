package BFSAndDFS;

import java.util.ArrayList;
import java.util.List;

/*
https://leetcode.cn/problems/restore-ip-addresses/description/

A valid IP address consists of exactly four integers separated by single dots.
Each integer is between 0 and 255 (inclusive) and cannot have leading zeros.
For example, "0.1.2.201" and "192.168.1.1" are valid IP addresses,
but "0.011.255.245", "192.168.1.312" and "192.168@1.1" are invalid IP addresses.
Given a string s containing only digits,
return all possible valid IP addresses that can be formed by inserting dots into s. You are not allowed to reorder or remove any digits in s.
You may return the valid IP addresses in any order.

Example 1:
Input: s = "25525511135"
Output: ["255.255.11.135","255.255.111.35"]

Example 2:
Input: s = "0000"
Output: ["0.0.0.0"]

Example 3:
Input: s = "101023"
Output: ["1.0.10.23","1.0.102.3","10.1.0.23","10.10.2.3","101.0.2.3"]

思路: dfs
1. 普通递归做法
2. 开始判断: 如果长度<3或者>13, 那无论如何都凑不成完整的ip地址, 直接return
3. 递归时约束条件: 以"25525511135"为例
    1. 每次递归三次, 分别是"2", "25", "255", 即长度为1、2和3的: for(int len = 1; len < 4; len++)
    2. 检查剩下的长度是否还可以继续切分: if (startIndex + len >= n)
    3. 检查当前字符是否为0, 避免"0x", "0xx"的情况: if (len != 1 && s.charAt(startIndex) == '0')
    4. 检查当前切出来的subString是否大于255: if (len == 3 && Integer.parseInt(subString) > 255)
4. 递归出口:
    1. path填满了: path[3] != null
    2. startIndex >= n, 即字符刚好用完
 */
public class RestoreIPAddress {
    public static List<String> restoreIpAddresses(String s) {
        int n = s.length();
        List<String> result = new ArrayList<>();
        String[] path = new String[4];

        if (n <= 3 || n >= 13) {
            return result;
        }

        dfs(s, 0, 0, n, result, path);
        return result;
    }

    private static void dfs(
            String s, int startIndex, int startPath, int n, List<String> result, String[] path) {
        /*
        出口：当path被填满时，一定要退出（即return）
        但是还需要额外检查一下，只有当startIndex >= n时，才表示所有字母都被使用了，才可以加入result
        比如11110 -> 可以分成1.1.1.1 但还有个0没使用，这时候就不能加入result
         */
        if (path[3] != null && startIndex >= n) {
            result.add(String.join(".", path));
            return;
        } else if (path[3] != null) return;

        /*
        按照长度来遍历，或者说决定在哪个位置分割
        startIndex + len -> 在哪里分割，那么下一个startIndex就是从startIndex + len开始
         */
        for (int len = 1; len < 4; len++) {
            // 检查剩余的长度是否还够用
            if (startIndex + len > n) return;
            // 检查是否有leading zero
            if (len != 1 && s.charAt(startIndex) == '0') continue;
            String subString = s.substring(startIndex, startIndex + len);
            // 检查是否超过255
            if (len == 3 && Integer.parseInt(subString) > 255) return;
            // 将其加入path
            path[startPath] = subString;
            // 进入下一层
            dfs(s, startIndex + len, startPath + 1, n, result, path);
            // 回溯以后，将path改回来
            path[startPath] = null;
        }
    }
}
