import Interview.TradeDesk.BankSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    static void main(String[] args) {
        BankSystem bankSystem = new BankSystem();
        // L1
        //        System.out.println("L1");
        //        System.out.println(bankSystem.createAccount(1, "account1"));
        //        System.out.println(bankSystem.createAccount(2, "account1"));
        //        System.out.println(bankSystem.createAccount(3, "account2"));
        //        System.out.println(bankSystem.deposit(4, "xxx", 2700));
        //        System.out.println(bankSystem.deposit(5, "account1", 2700));
        //        System.out.println(bankSystem.transfer(6, "account1", "account2", 2701));
        //        System.out.println(bankSystem.transfer(7, "account1", "account2", 200));

        // L2
        //        System.out.println("L2");
        //        System.out.println(bankSystem.createAccount(1, "account3"));
        //        System.out.println(bankSystem.createAccount(2, "account2"));
        //        System.out.println(bankSystem.createAccount(3, "account1"));
        //        System.out.println(bankSystem.deposit(4, "account1", 2000));
        //        System.out.println(bankSystem.deposit(5, "account2", 3000));
        //        System.out.println(bankSystem.deposit(6, "account3", 4000));
        //        System.out.println(bankSystem.topSpenders(7, 3));
        //        System.out.println(bankSystem.transfer(8, "account3", "account2", 500));
        //        System.out.println(bankSystem.transfer(9, "account3", "account1", 1000));
        //        System.out.println(bankSystem.transfer(10, "account1", "account2", 2500));
        //        System.out.println(bankSystem.topSpenders(11, 3));

        // L3
        int day = 86400000;
        System.out.println("L3");
        System.out.println(bankSystem.createAccount(1, "account1"));
        System.out.println(bankSystem.createAccount(2, "account2"));
        System.out.println(bankSystem.deposit(3, "account1", 2000));
        System.out.println(bankSystem.pay(4, "account1", 1000));
        System.out.println(bankSystem.pay(100, "account1", 1000));
        System.out.println(bankSystem.getPaymentStatus(101, "xxx", "payment1"));
        System.out.println(bankSystem.getPaymentStatus(102, "account2", "payment1"));
        System.out.println(bankSystem.getPaymentStatus(103, "account1", "payment1"));
        System.out.println(bankSystem.topSpenders(104, 2));
        System.out.println(bankSystem.deposit(3 + day, "account1", 100));
        System.out.println(bankSystem.getPaymentStatus(4 + day, "account1", "payment1"));
        System.out.println(bankSystem.deposit(5 + day, "account1", 100));
        System.out.println(bankSystem.deposit(99 + day, "account1", 100));
        System.out.println(bankSystem.deposit(100 + day, "account1", 100));
    }

    public static int minPrice(int[][] hotels, int guest) {
        int totalCap = 0;
        for (int[] hotel : hotels) {
            totalCap += hotel[2];
        }
        int[] dp = new int[totalCap + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        Arrays.sort(
                hotels,
                (a, b) -> {
                    if (a[2] != b[2]) return a[2] - b[2];
                    if (a[1] != b[1]) return a[1] - b[1];
                    return a[0] - b[0];
                });
        for (int[] hotel : hotels) {
            int id = hotel[0], price = hotel[1], cap = hotel[2];
            for (int i = guest; i >= 0; i--) {
                if (dp[i] == Integer.MAX_VALUE) continue;
                dp[i + cap] = Math.min(dp[i] + price, dp[i + cap]);
            }
        }
        int minPrice = Integer.MAX_VALUE;
        for (int i = guest; i <= totalCap; i++) {
            minPrice = Math.min(minPrice, dp[i]);
        }
        return minPrice == Integer.MAX_VALUE ? -1 : minPrice;
    }

    public static List<String> solution(String[][] menu, String[] wants) {
        int targetNum = wants.length;
        // 把wants保存在map里面,方便之后menu做匹配,检查是否存在
        Map<String, Integer> targetMap = new HashMap<>();
        for (int i = 0; i < targetNum; i++) {
            targetMap.put(wants[i].toLowerCase(), i);
        }
        // 将menu转变成menuState
        int n = menu.length;
        List<Menu> menuList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            double cost = Double.parseDouble(menu[i][0]);
            String[] itemList = menu[i][1].split(",");
            // 检查itemList里面是否有wants里不存在的
            //            boolean existNoWant = false;
            //            for (String item: itemList) {
            //                if (!targetMap.containsKey(item.trim().toLowerCase())) {
            //                    existNoWant = true;
            //                    break;
            //                }
            //            }
            //            if (existNoWant) continue;
            // 设置bitMap
            int bitMap = 0;
            for (String item : itemList) {
                if (!targetMap.containsKey(item.trim().toLowerCase())) continue;
                bitMap |= 1 << targetMap.get(item.trim().toLowerCase());
            }
            // 如果bitMap不为0,那么创建MenuState
            if (bitMap != 0) {
                menuList.add(new Menu(i, bitMap, cost));
            }
        }
        // 用dp方程来计算最小花费,dp[i]代表,i是bitMap,其二进制代表wants里面物品的组合
        State[] dp = new State[1 << targetNum];
        int targetState = (1 << targetNum) - 1;
        Arrays.setAll(dp, i -> new State());
        dp[0].cost = 0.0;
        // 遍历menuList来更新dp
        for (Menu ms : menuList) {
            // 从右往左,更新dp
            for (int i = targetState; i >= 0; i--) {
                // 位置i上面的bitmap
                int curBitMap = i;
                // 与ms结合以后的bitMap,即新的位置
                int newBitMap = i | ms.bitMap;
                // 位置newBitMap上面记录的当前cost
                double curCost = dp[newBitMap].cost;
                // 位置i上面记录的当前cost + ms的cost
                double newCost = dp[i].cost + ms.cost;

                // 如果当前无法抵达（即POSITIVE_INFINITY）或者newCost更大了,直接跳过
                if (dp[curBitMap].cost == Double.POSITIVE_INFINITY || newCost - curCost >= 1e-2)
                    continue;
                // 如果cost更少
                if (newCost < curCost) {
                    // 更新newBitMap的cost
                    dp[newBitMap].cost = newCost;
                    // 清除新位置上的现有comb
                    dp[newBitMap].combList.clear();
                }
                // 当前位置i的每一个comb + 当前ms的id -> 添加到dp[新位置]的comb里面
                for (String comb : dp[i].combList) {
                    String newComb = addItemToPath(comb, ms.id);
                    dp[newBitMap].combList.add(newComb);
                }
            }
        }
        // 获取结果
        State finalState = dp[targetState];
        List<String> result = new ArrayList<>();
        if (finalState.cost == Double.POSITIVE_INFINITY) return result;
        result.addAll(finalState.combList);
        return result;
    }

    private static String addItemToPath(String path, int itemNum) {
        String itemStr = String.valueOf(itemNum);
        if (path.isEmpty()) return itemStr;
        String newPath = path + "+" + itemStr;
        String[] strList = newPath.split("\\+");
        Arrays.sort(strList);
        return String.join("+", strList);
    }

    public static String randomWriter(String s, int n) {
        Map<String, List<String>> map = new HashMap<>();
        String[] wordList = s.split(" ");
        int len = wordList.length;
        for (int i = 0; i < len; i++) {
            String word = wordList[i];
            String nextWord;
            if (i == len - 1) {
                nextWord = wordList[0];
            } else {
                nextWord = wordList[i + 1];
            }
            map.computeIfAbsent(word, k -> new ArrayList<>()).add(nextWord);
        }
        String[] ans = new String[n];
        ans[0] = wordList[(int) (Math.random() * (len - 1))];
        for (int i = 1; i < n; i++) {
            List<String> nextWords = map.get(ans[i - 1]);
            String nextWord = nextWords.get((int) (Math.random() * (nextWords.size() - 1)));
            ans[i] = nextWord;
        }
        return String.join(" ", ans);
    }

    public static String guess(String target, String guess) {
        int n = target.length();
        Map<Character, Integer> targetFreqMap = new HashMap<>();

        char[] ans = new char[n];
        for (int i = 0; i < n; i++) {
            char ch = target.charAt(i);
            targetFreqMap.merge(ch, 1, Integer::sum);
        }
        for (int i = 0; i < n; i++) {
            char guessChar = guess.charAt(i);
            char targetChar = target.charAt(i);
            if (!targetFreqMap.containsKey(guessChar)) {
                ans[i] = 'B';
                continue;
            }
            if (guessChar == targetChar) {
                ans[i] = 'G';
                if (targetFreqMap.get(targetChar) > 0) {
                    targetFreqMap.put(targetChar, targetFreqMap.get(targetChar) - 1);
                } else {
                    targetFreqMap.remove(targetChar);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            if (ans[i] == 'G') continue;
            char guessChar = guess.charAt(i);
            char targetChar = target.charAt(i);
            if (!targetFreqMap.containsKey(guessChar)) {
                ans[i] = 'B';
            } else if (guessChar != targetChar) {
                ans[i] = 'Y';
            }
            if (targetFreqMap.get(targetChar) > 1) {
                targetFreqMap.put(targetChar, targetFreqMap.get(targetChar) - 1);
            } else {
                targetFreqMap.remove(targetChar);
            }
        }
        return new String(ans);
    }

    public static class Menu {
        int id;
        int bitMap;
        double cost;

        public Menu(int id, int bitMap, double cost) {
            this.id = id;
            this.cost = cost;
            this.bitMap = bitMap;
        }
    }

    public static class State {
        double cost;
        Set<String> combList;

        public State() {
            this.cost = Double.POSITIVE_INFINITY;
            this.combList = new HashSet<>();
            combList.add("");
        }
    }
}
