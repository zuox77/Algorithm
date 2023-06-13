package Array;
/*
https://leetcode.cn/problems/median-of-two-sorted-arrays/
https://www.lintcode.com/problem/65/
https://www.jiuzhang.com/problem/median-of-two-sorted-arrays/

这道题可以扩展为：
1. 2个数组中找第k个数
2. k个数组中找第k个数

思路1：分治法
注意很容易搞错的点：
1. 数组的第几个数和数组内的下标是不同的，找第k个数，在数组内的下标是找k-1
2. 在nums[]的括号里面的都是下标，都要k - 1
3. 传递进入递归的参数，都不用k - 1
思路：
1. 正常分治法去做，主要要分奇偶性来做，因为下面的方法其实是一个找第k个数的方法，而根据题意，如果是偶数，需要(median + median+1)/2，
    即下标为median-1和下标为median的两个数，所以要分两次去找
2. 核心的点在于要想清楚一个隐藏条件：如何在O(1)时间内排除某个数组一半的数，这样才能做到每次都以log级别的速度找寻
3. 假设在每个数组里都取前median/2个数出来，那么即是下标median/2-1，即nums1[median/2-1]和nums2[median/2-1]中，最大的那个数最有可能是median
    比如nums1 = [1, 3, 5, 7]，nums2 = [2, 4, 6, 8]
    median = 4，即第4个数，中位数的值是4
    如果每个数组都取median/2 = 2个数，即nums1[1] = 3，nums2[1] = 4
    一定是大的那个数是中位数
    所以，每次在median/2-1的位置，将小的数所在的nums的区间给排除
4. 排除掉以后，用递归继续找
5. 递归出口三个：1. nums1遍历完了 2. nums2遍历完了 3. k=1了，即遍历到第k个数了
时间复杂度：其实是O(log(N+M))
空间复杂度：O(1)，没有额外空间，因为不需要像真的归并法一样建立新的数组存储值

思路2：归并法
1. 正常归并法去解，两个指针分别指向两个数组，谁小就移动哪个指针，不需要真的建立一个新的数组取存储
2. 用for比while好，因为可以计算出究竟要多少次遍历
3. for的时候额外多遍历一次，因为结果跟两个数组的总长度的奇偶性有关，如果偶数需要两个数的平均值
4. 用两个变量单独存储具体的值，而不是通过指针去找，因为只能判断两个指针的和是否是median，但很难判断哪个指针先到了median
时间复杂度：其实是O((N+M)/2)，但常量不计算，所以是O(N+M)，因为需要遍历两个数组
空间复杂度：O(1)，没有额外空间，因为不需要像真的归并法一样建立新的数组存储值
 */

public class MedianOfTwoSortedArrays {
    // 分治法
    public static double Solution1(int[] nums1, int[] nums2) {
        int totalLength = nums1.length + nums2.length;
        // check odd or even
        if (totalLength % 2 == 0) {
            return (findKth(nums1, 0, nums2, 0, totalLength / 2) + findKth(nums1, 0, nums2, 0, totalLength / 2 + 1)) / 2.0;
        } else {
            return findKth(nums1, 0, nums2, 0, totalLength / 2 + 1);
        }

    }
    public static int findKth(int[] nums1, int startFirst, int[] nums2, int startSecond, int k) {
        // recursion exit
        if (startFirst >= nums1.length) { // nums1已经遍历完了
            return nums2[startSecond + k - 1]; // k - 1表示的是下标，字面意思是找第k个数
        }
        if (startSecond >= nums2.length) { // nums2已经遍历完了
            return nums1[startFirst + k - 1]; // k - 1表示的是下标，字面意思是找第k个数
        }
        if (k == 1) { // 因为每次排除了k/2个数，所以后面递归的时候，k也会相应减少k/2，最后为1的时候表示找到了
            return Math.min(nums1[startFirst], nums2[startSecond]);
        }

        // get the value of k/2
        // 如果取不出值，比如某个数组已经遍历完了，那么就赋一个最大值来代替繁琐的if
        int halfKthOfFirst = startFirst + k / 2 - 1 < nums1.length ? nums1[startFirst + k / 2 - 1] : Integer.MAX_VALUE;
        int halfKthOfSecond = startSecond + k / 2 - 1 < nums2.length ? nums2[startSecond + k / 2 - 1] : Integer.MAX_VALUE;

        // compare to determine recursion
        if (halfKthOfFirst < halfKthOfSecond) { // 如果nums1的k/2小于nums2的k/2，那么结果大概率在nums2中，所以排除nums1的前k/2个数
            return findKth(nums1, startFirst + k / 2, nums2, startSecond, k - k / 2);
        } else {
            return findKth(nums1, startFirst, nums2, startSecond + k / 2, k - k / 2);
        }
    }

    // 归并法，但并不真正归并成一个新的数组，只是通过归并来找
    public static double Solution2(int[] nums1, int[] nums2) {
        /* 不需要corner case的判断，因为如果nums1 = [], nums2 = [1]，就不能简单的return0
        // corner case
        if (nums1 == null || nums1.length == 0) {
            return 0;
        }
        if (nums2 == null || nums2.length == 0) {
            return 0;
        }
        */

        // define
        int first = 0;
        int second = 0;
        int left = -1; // left和right不是指针，是第median个数和第median+1个数的value
        int right = -1;
        int totalLength = nums1.length + nums2.length;
        int median = totalLength / 2;

        // iterate both array
        // 这里用for比while好，因为知道median是多少，所以一定要遍历那么多次才能找到
        // 之所以是median + 1是因为，遍历median次能刚好找到median，但如果totalLength是偶数，那么根据题目要求，结果是
        // (第median个数 + 第median+1个数) / 2.0，所以这里多遍历一次为了找到后一个数，然后后面通过if判断就行了
        for (int i = 0; i < median + 1; i++) {
            left = right; // 每次更新是因为，当找到了第median个数以后，会去找第median+1个数，这时候用left取保存，而median+1找到后
                          // 因为for的次数到了，会自动退出，所以left保留median个数，right保留median+1个数
            // 下面条件表示：
            // 如果nums1已经遍历完了 || nums2还没有遍历完 && nums2[second]是更小的，即合并数组里面从小到大排列的下一个数
            if (first >= nums1.length || second < nums2.length && nums2[second] < nums1[first]) {
                right = nums2[second++];
            } else {
                right = nums1[first++];
            }
        }

        // check odd or even
        return totalLength % 2 == 1 ? right : (left + right) / 2.0;
    }
}
