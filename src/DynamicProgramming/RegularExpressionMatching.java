package DynamicProgramming;

/*
https://leetcode.cn/problems/regular-expression-matching/description/?favorite=2cktkvj
https://leetcode.cn/problems/regular-expression-matching/solutions/296114/shou-hui-tu-jie-wo-tai-nan-liao-by-hyj8/?orderBy=most_votes

思路: DP
1. state: dp[i][j]代表: s的前i个字符(即下标0到i - 1)和p的前j个字符(即下标0到j - 1)能否匹配上
2. function:
    1. 当s的第i个字符和p的第j个字符, 能不通过'*'匹配上时, 即s.charAt(i - 1) == p.charAt(j - 1) || p.charAt(j - 1) == '.':
        就让这两个字符匹配上, 检查s和p都往前回退一步时的匹配情况: dp[i][j] = dp[i - 1][j - 1]
    2. 当s的第i个字符和p的第j个字符, 只能通过匹配'*'上时, 即第j个字符一定是'*'时, p.charAt(j - 1) == '*'
        1. 匹配0个: dp[i][j] = dp[i][j - 2]
            p利用'*'可以当成0个数的特性, 将第j个字符(下标j - 1)和第j - 1个字符(下标j - 2)消除, 检查p的第j - 2个字符(下标j - 3)去和s的第i个字符:
            dp[i][j] = dp[i][j - 2]
            此时又分为两种情况, 即p可以匹配上但可以选择故意不匹配的情况和p真的无法匹配上, 比如:
            s = 'a', p = 'aa*', 此时p可以匹配上但可以选择利用'*'将'a*'变为0个, 再让'a*'之前的'a'去匹配s的'a'
            s = 'b', p = 'ba*', 此时p无法匹配上, 只能选择让'a*'之前的'b'去匹配s的'b'
        2. 匹配1个或者多个: dp[i][j] = dp[i - 1][j]
            p的第j个字符(下标j - 1)和第j - 1个字符(下标j - 2), 刚好能匹配上s的第i个字符(下标i - 1)
            dp[i][j] = dp[i - 1][j - 2], 但由于但s也可能有多个a可以匹配, 所以p不变, 让s继续往回检查
            最终方程是: dp[i][j] = dp[i - 1][j]
3. initialize:
    1. boolean[][] dp = new boolean[s.length() + 1][s.length() + 1], 额外的1是为了方便处理第0个的情况
    2. dp[0][0] = true, 因为s的前0个字符和p的前0个字符都是"", 所以一定能匹配上
    3. 二维dp都要处理dp[i][0]和dp[0][j]的情况:
        1. dp[i][0], 即p是"", 因为p是匹配字符, 如果匹配字符都没有, 那一定无法匹配, 所以不需要额外处理, 因为默认为false
        2. dp[0][j], 即s是"", 理论来将也不需要处理, 但因为'*'可以表示0个数, 所以如果p = "a*b*c*"时, 可以匹配s = ""的情况,
           所以要初始化dp[0][j] = dp[0][j - 2]
4. answer: dp[s.length()][s.length()]
 */
public class RegularExpressionMatching {
  public boolean isMatch(String s, String p) {
    int sLen = s.length();
    int pLen = p.length();
    boolean[][] dp = new boolean[sLen + 1][pLen + 1]; // 默认是false

    // initialize
    // dp代表: s的前i个字符(即下标0到i - 1)和p的前j个字符(即下标0到j - 1)能否匹配上
    // dp[0][0]代表: s的前0个字符和p的前0个字符, 即双方都为空字符串时, 那么一定能匹配, 所以初始化为true
    dp[0][0] = true;
    // 当p为""时, 一定无法匹配任何字符串, 所以全为false, 又因为默认是false, 所以不需要额外处理
    // 当s为""时, 因为*能表示0个或者多个, 所以如果p里面全是"a*b*c*", 可以将他们全部看作0个, 此时也可能是true
    // 所以下面就是为了判断这个:
    for (int j = 1; j < pLen + 1; j++) {
      if (p.charAt(j - 1) == '*') { // j - 1即是第j个字符, 即想要判断的是dp[0][j]的状态如何被先前的数据转化的
        dp[0][j] = dp[0][j - 2];
      }
    }
    for (int i = 1; i < sLen + 1; i++) {
      for (int j = 1; j < pLen + 1; j++) {
        // 如果二者的尾位字符能够匹配上或者p的尾位字符是'.'
        if (s.charAt(i - 1) == p.charAt(j - 1) || p.charAt(j - 1) == '.') {
          dp[i][j] = dp[i - 1][j - 1];
          // 如果匹配不上, 但p的第j个字符(下标j - 1)是'*'
        } else if (p.charAt(j - 1) == '*') {
          // 如果p的第j个字符(下标j - 1)是'*', 有两种种情况:
          // 1. dp[i][j - 2]: 即匹配0个的时候: p的第j个字符(下标j - 1)和第j - 1个字符(下标j - 2)是'b*'但是让'*'表示0个,
          // 从而消除'b*', 再让p的第j - 2个字符(下标j - 3)去和s的第i个字符做匹配
          // 也可能是p是'a*', 能匹配上s的'a', 但故意不匹配, 让p再之前的字符去试试匹配a
          // 2. dp[i - 1][j]: 即匹配1个或者多个的时候, p的第j个字符(下标j - 1)和第j - 1个字符(下标j - 2)是'a*',
          // 刚好能匹配上s的第i个字符(下标i - 1), 但s也可能有多个a可以匹配, 所以p的charAt不变,
          // i往前挪一位, 看看前面如果有多个a, 是否也能一起匹配
          if (s.charAt(i - 1) == p.charAt(j - 2) || p.charAt(j - 2) == '.') {
            dp[i][j] =
                dp[i][j - 2] // 即情况1, 但是是能匹配上, 也故意不匹配
                    || dp[i - 1][j]; // 即情况2
          } else { // 即情况1
            dp[i][j] = dp[i][j - 2];
          }
        }
      }
    }
    return dp[sLen][pLen];
  }
}
