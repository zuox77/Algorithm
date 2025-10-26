package Array.PrefixSum;

/*
刷题次数: 1

https://leetcode.cn/problems/product-of-array-except-self/description/?envType=study-plan-v2&envId=top-100-liked

Given an integer array nums, return an array answer such that answer[i] is equal to the product of all the elements of nums except nums[i].
The product of any prefix or suffix of nums is guaranteed to fit in a 32-bit integer.
You must write an algorithm that runs in O(n) time and without using the division operation.

Example 1:
Input: nums = [1,2,3,4]
Output: [24,12,8,6]

Example 2:
Input: nums = [-1,1,0,-3,3]
Output: [0,0,9,0,0]

时间复杂度: O(N), 遍历三次
空间复杂度: O(1)

思路1：前缀和（前缀积）
1. 思路就是，创建两个数组，分别从左到右和从右到左计算前缀/后缀乘积
2. 两个数组都需要使首位（pre[0]和suf[n-1]为1）为1
3. 因为首位为1，所以第二位其实才是第一位的前缀积，即错开一位计算，这样刚好可以保证前缀积和后缀积的乘积就是所需的答案

     1. 创建一个数组记录从左到右的乘积，但是只记录[0, n - 2]
     2. 之后再创建一个数组，记录从右到左的乘积，但是也只记录[n - 1, 1]
        这样的话，两个数组相乘就是答案
     3. 答案应该为：[nums[1]*nums[2]*...*numns[n-1], nums[0]*nums[2]*...*numns[n-1], ... , nums[0]*nums[1]*...*numns[n-2]]
        即，每个位置上的数，都是除了自己以外，其他数的乘积，比如第0个数为nums[1]*nums[2]*...*numns[n-1]，第一个数为：nums[0]*nums[2]*...*numns[n-1]
     4. 按照上面说的方法去记录
        从左到右的数组为：[1, 1*nums[0], 1*nums[0]*nums[1], ... , 1*nums[0]*nums[1]*...*nums[n-2]]
        从右到左的数组为：[nums[1]*nums[2]*...*nums[n-1]*1, nums[2]*...*nums[n-1]*1, ..., nums[n-2]*nums[n-1]*1, nums[n-1]*1, 1]
        两两相乘，刚好就是答案，比如i=1时，为：(1*nums[0]) x (nums[2]*...*nums[n-1]*1)，刚好跳过nums[1]，就是答案nums[0]*nums[2]*...*numns[n-1]
     5. 举个例子[5, 2, 7, 9]
        答案应该为：     [2*7*9,   5*7*9,  5*2*9,  5*2*7]
        从左到右的数组为：[1,       1*5,    1*5*2,  1*5*2*7]
        从右到左的数组为：[2*7*9*1, 7*9*1,  9*1,    1]

思路2：优化
1. 看思路1可以发现，我们一共需要遍历3次，前缀积1次（从左到右），后缀积1次（从右到左），最后遍历这俩数组，从左到右乘起来
2. 那么从左到右的其实可以一起计算
3. 且前缀积可以用一个变量（而非数组）来保存，这样还能节省空间
 */

public class ProductOfArrayExceptSelf {
    public int[] productExceptSelf(int[] nums) {

        int n = nums.length;
        // 前缀积
        int[] pre = new int[n];
        pre[0] = 1;
        for (int i = 1; i < n; i++) {
            pre[i] = pre[i - 1] * nums[i - 1];
        }
        // 后缀积
        int[] suf = new int[n];
        suf[n - 1] = 1;
        for (int i = n - 2; i >= 0; i--) {
            suf[i] = suf[i + 1] * nums[i + 1];
        }

        // 相乘
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = pre[i] * suf[i];
        }
        return ans;
    }

    public int[] productExceptSelf2(int[] nums) {
        int n = nums.length;
        // 创建一个数组，用来保存从n-1到1的后缀积
        int[] suf = new int[n];
        suf[n - 1] = 1;
        for (int i = n - 2; i >= 0; i--) {
            suf[i] = suf[i + 1] * nums[i + 1];
        }
        // 一边计算前缀积一边遍历suf，并更新
        int pre = 1;
        for (int i = 0; i < n; i++) {
            // 一定要先更新suf，因为答案要求没有自己,即不需要nums[i]在乘积里面，如果先更新的话，那么nums[i]就会包括在乘积里面了
            suf[i] *= pre;
            pre *= nums[i];
        }
        return suf;
    }
}
