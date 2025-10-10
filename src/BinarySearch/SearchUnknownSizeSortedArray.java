package BinarySearch;
/*
刷题次数: 2
第二次: 基本记得

https://leetcode.cn/problems/search-in-a-sorted-array-of-unknown-size/description/

You have a sorted array of unique elements and an unknown size.
You do not have access to the array, but you can use the ArrayReader interface to access it. You can call ArrayReader.get(i) that:
returns the value at the ith index (0-indexed) of the secret array (i.e., secret[i]), or
returns (2^31 - 1) if the i is out of the boundary of the array.
You are also given an integer target.
Return the index k of the hidden array where secret[k] == target or return -1 otherwise.
You must write an algorithm with O(log n) runtime complexity.

Input: secret = [-1,0,3,5,9,12], target = 9
Output: 4
Explanation: 9 exists in secret and its index is 4.

思路: 二分 + 找边界
1. 此题与一般二分唯一的区别就是没有右边界
2. 没有右边界就去找右边界
   1. 通过题目提供的方法(reader.get())去找, 初始化right = 1, 通过判断当前right与target的大小, 按指数增加, 从而达到O(logN)的时间复杂度
   2. 因为题目里有提到, 如果比2^31-1大, 则定义为超出边界, 直接返回-1, 所以我们直接设置右边界为Integer的最大值, 即Integer.MAX_VALUE
3.其他与二分都一样, 推荐用搭配2

思路1: 二分 + 搭配2 + 找边界
思路2: 二分 + 搭配2(right = mid) + 找边界
思路2: 二分 + 搭配2(right = mid - 1) + 找边界
这道题用哪种都可以
 */

public class SearchUnknownSizeSortedArray {
    // 随便乱写个ArrayReader的类, 避免编译报错
    class ArrayReader {
        public int get(int i) {
            return i;
        }
    }

    public int solution1(ArrayReader reader, int target) {
        // 定义指针
        int left = 0, right = 1; // 也可以直接right = Integer.MAX_VALUE;
        // 找到右边界
        while (reader.get(right) < target) {
            // 因为我们已经知道right此时小于target, 说明从left到right之间, 不存在target, 那么也将left往右移动, 减小时间
            // 这里不移动left也可以
            left = right;
            // 通过指数级别增加从而同样达到二分的效果, 否则的话, 找右边界就变成了O(N)的时间复杂度, 会导致最后整个算法的时间复杂度都变成O(N)
            right <<= 1;
        }
        // 开始二分
        while (left + 1 < right) {
            // 找到中点
            int mid = left + (right - left) / 2;
            // 判断情况
            // 找到target, 则直接返回
            if (reader.get(mid) == target) {
                return mid;
                // 比target小, 则left往右移动
            } else if (reader.get(mid) < target) {
                left = mid;
                // 比target大, 则right往左移动
            } else {
                right = mid;
            }
        }
        // 最后再判断一次
        return reader.get(left) == target ? left : reader.get(right) == target ? right : -1;
    }

    public int soluiton2(ArrayReader reader, int target) {
        // 定义指针
        int left = 0, right = 1;
        // 找到右边界
        while (reader.get(right) < target) {
            // 通过指数级别增加同样达到二分的效果
            left = right;
            right <<= 1;
        }
        // 开始二分
        while (left < right) { // -----> 与思路1的区别
            // 找到中点
            int mid = left + (right - left) / 2;
            // 判断情况
            // 找到target, 则直接返回
            if (reader.get(mid) == target) {
                return mid;
                // 比target小, 则left往右移动
            } else if (reader.get(mid) < target) {
                left = mid;
                // 比target大, 则right往左移动
            } else {
                right = mid; // -----> 与思路1的区别
            }
        }
        // 最后再判断一次
        return reader.get(left) == target ? left : reader.get(right) == target ? right : -1;
    }

    public int soluiton3(ArrayReader reader, int target) {
        // 定义指针
        int left = 0, right = 1;
        // 找到右边界
        while (reader.get(right) < target) {
            // 通过指数级别增加同样达到二分的效果
            left = right;
            right <<= 1;
        }
        // 开始二分
        while (left < right) {
            // 找到中点
            int mid = left + (right - left) / 2;
            // 判断情况
            // 找到target, 则直接返回
            if (reader.get(mid) == target) {
                return mid;
                // 比target小, 则left往右移动
            } else if (reader.get(mid) < target) {
                left = mid;
                // 比target大, 则right往左移动
            } else {
                right = mid - 1; // -----> 与思路2的区别
            }
        }
        // 最后再判断一次
        return reader.get(left) == target ? left : reader.get(right) == target ? right : -1;
    }
}
