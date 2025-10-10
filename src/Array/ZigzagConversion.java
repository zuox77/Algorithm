package Array;

import java.util.ArrayList;
import java.util.List;
/*
刷题次数: 2
二刷: 忘记考虑rowNums = 1的情况, 解决方案: 要么加一个corner case处理, 要么在更新rowNum的时候, 判断一下numRows - 1 == 0

https://leetcode.cn/problems/zigzag-conversion/description/?orderBy=most_votes

思路: 
1. 既然题目要求将原字符串以Z字型排列, 那么就创建numRows个可变数组, 每个可变数组代表一行, 
2. 然后通过两个变量, 一个记录当前的行数, 一个记录方向, 在特定位置变化方向
3. Java的数组和字符串都比较复杂, 所以要注意用的方式
4. 数组可以用List<StringBuilder>来表示, 并且记住要初始化, 给每一行创建好StringBuilder
5. 如果想用forEach方式遍历字符串, 记得将字符串变成char数组: .toCharArray()
6. 最后建一个StringBuilder, 通过append的方式将每一行加入, 即实现类似于join的功能
7. 最后记得用toString()将StringBuilder转化成String
 */

public class ZigzagConversion {
    public String solution1(String s, int numRows) {
        if (numRows < 2) {
            return s;
        }

        List<StringBuilder> result = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            result.add(new StringBuilder());
        }

        // iterate and use a counter to decide direction
        int rowNum = 0;
        int direction = -1;
        for (char c : s.toCharArray()) {
            result.get(rowNum).append(c);
            if (rowNum == 0 || rowNum == numRows - 1) {
                direction = -direction;
            }
            rowNum += direction;
        }

        // iterate to get final answer
        StringBuilder resultString = new StringBuilder();
        for (StringBuilder row : result) {
            resultString.append(row);
        }
        return resultString.toString();
    }
}
