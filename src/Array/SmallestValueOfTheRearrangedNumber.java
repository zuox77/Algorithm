package Array;

/*
刷题次数: 2

https://leetcode.cn/problems/smallest-value-of-the-rearranged-number/?favorite=2cktkvj

You are given an integer num.
Rearrange the digits of num such that its value is minimized, and it does not contain any leading zeros.
Return the rearranged number with minimal value.
Note that the sign of the number does not change after rearranging the digits.

Example 1:
Input: num = 310
Output: 103
Explanation: The possible arrangements for the digits of 310 are 013, 031, 103, 130, 301, 310.
The arrangement with the smallest value that does not contain any leading zeros is 103.

Example 2:
Input: num = -7605
Output: -7650
Explanation: Some possible arrangements for the digits of -7605 are -7650, -6705, -5076, -0567.
The arrangement with the smallest value that does not contain any leading zeros is -7650.

思路:
1. 通过余10（mod）和除10，不断循环，得到每一个数字
2. 将数字和其出现的频率放在一个长度为10的数组上
3. 判断正负：
    正的：
        1. 从小到大遍历数组，找到第一个不是0且频率也不为0的数字，将其加入ans，避免leading zero
        2. 从小到大遍历数组，按照顺序，不断的加入ans，先乘10，再加当前数字，并且更新出现的频率
    负的：
        1. 从大到小遍历数组，找到第一个不是0且频率也不为0的数字，将其加入ans，避免leading zero
        2. 从大到小遍历数组，按照顺序，不断的加入ans，先乘10，再加当前数字，并且更新出现的频率
 */

public class SmallestValueOfTheRearrangedNumber {
    public long smallestNumber(long num) {
        long tmp = num;
        // 检查num是正还是负，如果是正，那么下面的循环都需要从小到大遍历，如果是负，那么反过来，从大到小遍历
        boolean isPositive = true;
        if (tmp < 0) {
            tmp = -tmp;
            isPositive = false;
        }
        // 将每个数字出现的频率保存在一个数组里
        int[] numFreq = new int[10];
        while (tmp != 0) {
            numFreq[(int) (tmp % 10)]++;
            tmp /= 10;
        }

        // 通过正负设置遍历的起始位置和结束位置
        int start = 0;
        int end = 9;
        int incre = 1;
        if (!isPositive) {
            start = 9;
            end = 0;
            incre = -1;
        }

        long ans = 0;
        // 遍历数组，找到第一个非0的数，正数时，从小到大遍历，可以找到最小的非0数，负数时则是找到最大的
        int firstNonZero = 0;
        // 因为i = 0的时候就代表0的次数，所以直接从1开始遍历
        for (int i = start; Math.abs(i - end) >= 0 && i >= 0 && i <= 9; i += incre) {
            if (i == 0) continue;
            if (numFreq[i] != 0) {
                firstNonZero = i;
                break;
            }
        }

        // 将第一个非0的数加入ans，只添加一次，这是为了防止leading zero
        ans += firstNonZero;
        numFreq[firstNonZero]--;

        // Loop to calculate the rest
        for (int i = start; Math.abs(i - end) >= 0 && i >= 0 && i <= 9; i += incre) {
            while (numFreq[i] > 0) {
                ans *= 10;
                ans += i;
                numFreq[i]--;
            }
        }
        return isPositive ? ans : -ans;
    }
}
