package Array;
import java.util.*;
/*
刷题次数: 2
第二次: 完全忘了, 其实就是按层把每个数加进去

https://leetcode.cn/problems/pascals-triangle/description/

Given an integer numRows, return the first numRows of Pascal's triangle.

Input: numRows = 5
Output: [
    [1],
   [1,1],
  [1,2,1],
 [1,3,3,1],
[1,4,6,4,1]]

思路: 直接遍历
1. 初始化需要将第一层添加进去
2. 循环的时候, 从第二层开始, 即i = 1
3. 每次循环, 重新定义layer
4. 记得深拷贝
 */

public class PascalTriangle {
    public List<List<Integer>> generate(int numRows) {
        // 定义变量
        List<List<Integer>> result = new ArrayList<>();
        // 初始化, 将第一层加进去
        List<Integer> tmp = new ArrayList<>();
        tmp.add(1);
        result.add(new ArrayList<>(tmp));
        // 循环
        for (int i = 1; i < numRows; i++) {
            // 每次重新定义声明layer, 由于每次重新定义声明, 所以添加进result的时候不需要深拷贝
            List<Integer> layer = new ArrayList<>();
            // 添加第一个数, 根据题意, 任意一排的第一个数和最后一个数都是1
            layer.add(1);
            // j=1代表从下标1开始, 即第二个数, 因为第一个数永远是1
            // j < i, i代表层数, 比如是第三层的话, i=2, 同样也代表这一层有i+1个数, 即3个数, 又因为最后一个数是1, 所以遍历中间就行
            // 比如第7层, 一共7个数, i = 6, 第一个数layer[0] = 1, 最后一个数layer[6] = 1, 遍历1-5, 所以j < 6
            for (int j = 1; j < i; j++) {
                layer.add(result.get(i - 1).get(j - 1) + result.get(i - 1).get(j));
            }
            // 添加最后一个数
            layer.add(1);
            // 添加进结果
            result.add(layer);
        }
        return result;
    }
}
