package OOD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
/*
思路: 
https://www.1point3acres.com/bbs/thread-970148-1-1.html
1. 记住Scanner的用法: new Scanner(System.in), reader.nextln()
2. 记住关闭Scanner: reader.close();, 最好用try和finally防止内存栈溢出
3. BufferdReader: 
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Enter your Account number: ");
    String accStr = br.readLine();
4. 情况分析, 对于guess中的任意一个字符s只有以下情况: 
    1. target中有s, 且位置与s在guess中的位置一致 -> 直接Green
    2. target中有s, 但位置与s在guess中不一致 -> 分情况: 
        1. 字符s在guess中的数量比s在target中的数量多, 那么一定有一些s是Black, 剩下的是Yellow或者Green
            比如target = "CDAAA", guess = "AAFAA", 如果s是A, 那么要先确保最后两个A, 因为它们是Green, 然后按顺序, 第一个A是yellow, 第二是Black
            所以需要先将位置不对的存下来, 等所有Green的排放好了, 再去排剩下的
        2. 字符s在guess中的数量等于s在target中的数量, 只有Yellow或者Green
        3. 字符s在guess中的数量比s在target中的数量少, 只有Yellow或者Green
    3. target中没有s -> 直接Black
4. 哈希表存每个字符对应的出现频率, 不需要存字符在target中的index, 因为只要把位置匹配的排除了, 剩下的都是不匹配的, 既然不匹配, 那么在target
    中原位置是什么也不重要了
 */

public class WordleGame {
    private int n;
    private String target;
    private HashMap<Character, Integer> mapDup;
    private Scanner reader;

//    public void main() {
//        WordleGame test = new WordleGame();
//        try {
//            this.reader = new Scanner(System.in);
//            System.out.println("Please input target");
//            wordGuess(this.reader.nextLine());
//            for (int i = 0; i < 5; i++) {
//                System.out.println("Please guess");
//                tryItWithDup();
//            }
//        } finally {
//            reader.close();
//        }
//    }

    public WordleGame(String target) {
        // initialize
        this.target = target;
        this.n = target.length();

        // load all characters into map with dup
        this.mapDup = new HashMap<>();
        for (int i = 0; i < n; i++) {
            char ch = target.charAt(i);
            if (mapDup.containsKey(ch)) {
                mapDup.put(ch, mapDup.get(ch) + 1);
            } else {
                mapDup.put(ch, 1);
            }
        }
    }

//    public String getInput() {
//        return this.reader.nextLine();
//    }

    public String[] tryItWithDup(String guess) {
        // initialize
        // String guess = getInput();
        String[] result = new String[this.n];
        HashMap<Character, Integer> mapCopy = new HashMap<>(this.mapDup);
        ArrayList<Integer> remaining = new ArrayList<>();

        // iterate first to finalize Greens and Blacks
        for (int i = 0; i < n; i++) {
            char curChar = guess.charAt(i);
            if (curChar == this.target.charAt(i)) {
                result[i] = "Green";
                mapCopy.put(curChar, mapCopy.get(curChar) - 1);
            } else if (mapCopy.get(curChar) != null && mapCopy.get(curChar) > 0) {
                // 之所以要先将其放入一个list中保存, 因为在4.2.1的情况中, 要先分配Green, 剩下的再分配Yellow的
                remaining.add(i);
            } else {
                result[i] = "Black";
            }
        }

        // iterate duplicate character
        for (int index: remaining) {
            char remainChar = guess.charAt(index);
            if (mapCopy.get(remainChar) > 0) {
                result[index] = "Yellow";
                mapCopy.put(remainChar, mapCopy.get(remainChar) -  1);
            } else {
                result[index] = "Black";
            }
        }
        return result;
    }
}
