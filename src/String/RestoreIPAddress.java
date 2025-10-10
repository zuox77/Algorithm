package String;

import java.util.ArrayList;
import java.util.List;

/*
https://leetcode.cn/problems/restore-ip-addresses/description/

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
    public List<String> restoreIpAddresses(String s) {
        int n = s.length();
        List<String> result = new ArrayList<>();
        String[] path = new String[4];

        if (n <= 3 || n >= 13) {
            return result;
        }

        recursion(s, 0, 0, n, result, path);
        return result;
    }

    public void recursion(String s, int startIndex, int startPath, int n, List<String> result, String[] path) {
        // recursion exit
        if (path[3] != null) {
            if (startIndex >= n) {
                result.add(String.join(".", path));
            }
            return;
        }

        // iterate
        for (int len = 1; len < 4; len++) {
            // remaining chars are not enough
            if (startIndex + len > n) {
                return;
            }
            // case of "0x", "0xx"
            if (len != 1 && s.charAt(startIndex) == '0') {
                return;
            }
            String subString = s.substring(startIndex, len + startIndex);
            // case of any substring larger than 255
            if (len == 3 && Integer.parseInt(subString) > 255) {
                return;
            }
            path[startPath] = subString;
            // 在递归时候, 如果要直接用startPath作为参数传入下一层, 那不能用startPath++, 因为之后返回时, startPath也比以前多1, 
            // 用startPath + 1最好
            recursion(s, startIndex + len, startPath + 1, n, result, path);
            path[startPath] = null;
        }
    }
}
