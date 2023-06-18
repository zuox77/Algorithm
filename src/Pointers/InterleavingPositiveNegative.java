package Pointers;
/*
https://www.lintcode.com/problem/144/
https://www.jiuzhang.com/problem/interleaving-positive-and-negative-numbers/

思路: 双指针
1. 正负数的个数可能不一样, 需要先遍历一遍, 计算正负数各自的个数哪个多, 多的排在后面, 因为之后交换的时候如果个数少的排在后面, 因为一般是从左向右
    遍历, 所以很有可能出现将个数多的交换到右边导致错误的情况, 比如
    [1, 2, 3, 4, 5, 6, -1, -2]
    交换的时候, 肯定是双指针, 一个指向0, 一个指向负数的开端, 即-1的位置, 然后2与-1交换, 4与-2交换, 变成了
    [1, -1, 3, -2, 5, 6, 2, 4]
2. 用正常的快排方式先遍历一遍(没有递归的遍历一遍), 将正负数分割开
 */

public class InterleavingPositiveNegative {
}
