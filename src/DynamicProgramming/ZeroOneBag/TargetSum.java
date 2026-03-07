package DynamicProgramming.ZeroOneBag;

/*
https://leetcode.cn/problems/target-sum/

You are given an integer array nums and an integer target.
You want to build an expression out of nums by adding one of the symbols '+' and '-' before each integer in nums and then concatenate all the integers.
For example, if nums = [2, 1], you can add a '+' before 2 and a '-' before 1 and concatenate them to build the expression "+2-1".
Return the number of different expressions that you can build, which evaluates to target.

Example 1:
Input: nums = [1,1,1,1,1], target = 3
Output: 5
Explanation: There are 5 ways to assign symbols to make the sum of nums be target 3.
-1 + 1 + 1 + 1 + 1 = 3
+1 - 1 + 1 + 1 + 1 = 3
+1 + 1 - 1 + 1 + 1 = 3
+1 + 1 + 1 - 1 + 1 = 3
+1 + 1 + 1 + 1 - 1 = 3

Example 2:
Input: nums = [1], target = 1
Output: 1

思路: DP的0-1背包
1. 题目咔咔说一大堆,不要被迷惑了,要是去想着如何选择加号或者减号再去插入的话,就很难想了
2. 换个思路:因为只有加号和减号,所以无论加号减号如何选择如何分配,最终结果一定是一些数加起来减去另一些数加起来
    例如[2, 5, 3, 6, 7], 随便插入加号减号,可能得到+2+5-3+6-7,也就是(2+5+6)-(3+7)
    所以也就是一个正数子集加上一个负数子集:(2+5+6)+(-3+-7)
    此时,其实我们就将问题重新转化成了,一个正子集和负子集的和等于某个值,就是0-1背包问题
3. dp[i]代表,当和为i时,有多少种组合,即有多少种不同的子集的和为i
4. 转化方程:
    对于i来说,i此时代表sum为i时,dp[i]代表有多少种不同的组合
    而当我们遍历到num这个数字时,还能发现,对于dp[i - num]来说,dp[i - num]代表,sum为 i-num 时有多少种不同的组合
    此时我们发现,如果将num这个数加入到sum为i-num的子集们,那刚好,那些子集们的和就变成了i
    所以说:dp[i]的更新方程为dp[i] += dp[i - num],即dp[i]本身已经有的组合数,加上,差num就刚好sum为i的组合数,组成了dp[i]的组合数
注意:
1. 此题要稍微做一些数学计算,按照上面的定义,这个问题是一个正子集加上一个负子集的和等于target的时候,有多少种不同的组合
    假设这两个子集的和的绝对值分别为x和y
    那么x + y = sum(遍历nums累加可以得到)
    而我们需要:x - y = target(这是题目的要求)
    两两相加可得:2x = sum + target
    所以x = (sum + target) / 2
    y = x - target = sum/2 - target/2 = (sum - target) / 2
    而sum和target都是给定的数字,所以x也是定下来的,y也是定下来的,那么x和y的值都确定了
    所以最后返回dp[x]
2. 由于我们的定义是一个正子集和一个负子集,所以一定是x比y大,因为我们定义就是x-y=target
3. 我们要找寻的不是题目给的target,即我们不要dp[target],我们需要的是x
    为什么?因为我们对dp的定义是:当和为i时,有多少种组合,即有多少种不同的子集的和为i
    我们的dp并不是直接去找寻两个子集的差刚好等于target的情况,而是去找寻子集的和的不用组合数
4. 只能返回dp[x],不能返回dp[y]
    例子:nums=[100],target=-100,此时sum=100,x=0,y=100
    而因为只有一个数,所以理论上只有一个组合,即[空子集]+[-100],所以答案为1
    但dp = [1,0,0,0, ... ,0],此时dp[x] = 1, dp[y] = 0,但正确答案是1
    当然,如果nums=[-100],target=100是,此时sum=-100,x=0,y=-200
    所以明显不可能
    并且由于题目的规定,nums是不会为负数的,因为我们需要给nums加上加z号和减号
    所以不会出现需要返回dp[y]的情况
    而且,这个的实际意义是,因为我们是从后往前去更新dp的,其结果是一层层累加往后的,所以在所有更新过的dp里面,下标大的一定是最新的结果
    但是上面的例子里,y虽然大于x,但明显,y是出于无法抵达的点,所以x才是"所有更新过的dp种",最大的那个
5. dp长度为sum+1
6. 可以提前判断一下,如果diff > sum,那说明不可能存在,因为两个子集的sum不可能小于两个子集的差值
 */

public class TargetSum {
    public int findTargetSumWays(int[] nums, int target) {
        // 将target换个名字,因为在我们的定义里,这个target其实是两个子集的和
        // 我们真正的target是x或者y,即这两个子集各自的和
        int diff = target;
        // 计算下总共多大
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        // 计算target,即x
        target = (diff + sum) / 2;
        // diff > sum: 两个子集的sum不可能小于两个子集的差值
        // diff + sum % 2是奇数还是偶数:上面的公式给出了,2x = sum + diff,所以如果是奇数,x一定是小数,不成立
        if (diff > sum || (diff + sum) % 2 == 1) return 0;
        int[] dp = new int[sum + 1];
        dp[0] = 1;
        for (int num : nums) {
            for (int i = target; i >= num; i--) {
                dp[i] = dp[i] + dp[i - num];
            }
        }
        for (int n : dp) {
            System.out.println(n);
        }
        return dp[target];
    }
}
