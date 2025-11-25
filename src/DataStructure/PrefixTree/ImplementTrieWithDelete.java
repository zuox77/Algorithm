package DataStructure.PrefixTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
刷题次数: 1

https://leetcode.cn/problems/implement-trie-prefix-tree/?envType=study-plan-v2&envId=top-100-liked

A trie (pronounced as "try") or prefix tree is a tree data structure used to efficiently store and retrieve keys in a dataset of strings.
There are various applications of this data structure, such as autocomplete and spellchecker.

Implement the Trie class:
Trie() Initializes the trie object.
void insert(String word) Inserts the string word into the trie.
boolean search(String word) Returns true if the string word is in the trie (i.e., was inserted before), and false otherwise.
boolean startsWith(String prefix) Returns true if there is a previously inserted string word that has the prefix, and false otherwise.

Example 1:

Input
["Trie", "insert", "search", "search", "startsWith", "insert", "search"]
[[], ["apple"], ["apple"], ["app"], ["app"], ["app"], ["app"]]
Output
[null, null, true, false, true, null, true]

Explanation
Trie trie = new Trie();
trie.insert("apple");
trie.search("apple");   // return True
trie.search("app");     // return False
trie.startsWith("app"); // return True
trie.insert("app");
trie.search("app");     // return True

思路：
1. 主要是要知道，利用新建class TreeNode，让其每个node实例自带一个map，然后通过map去找
2. 需要一个全局变量或者说实例变量，TreeNode root，去代表根部节点，这样才能每次从根部开始搜索
 */

class ImplementTrieWithDelete {

    private static int count = 0;
    private final TrieNode root;

    public ImplementTrieWithDelete() {
        this.root = new TrieNode();
    }

    private void print(String methodName, String s, String output) {
        System.out.println(count);
        System.out.println("操作: " + methodName + "(" + s + ")");
        System.out.println("结果: " + output);
        System.out.println();
    }

    public void addWord(String s) {
        TrieNode current = root;
        for (char ch : s.toCharArray()) {
            current = current.children.computeIfAbsent(ch, k -> new TrieNode());
        }
        current.isEnd = true;
        print(Thread.currentThread().getStackTrace()[1].getMethodName(), s, null);
    }

    public List<String> search(String s) {
        TrieNode current = root;
        List<String> ans = new ArrayList<>();
        for (char ch : s.toCharArray()) {
            current = current.children.get(ch);
            if (current == null) {
                print(Thread.currentThread().getStackTrace()[1].getMethodName(), s, ans.toString());
                return ans;
            }
        }
        dfsSearch(ans, current, new StringBuilder(s));
        print(Thread.currentThread().getStackTrace()[1].getMethodName(), s, ans.toString());
        return ans;
    }

    private void dfsSearch(List<String> ans, TrieNode node, StringBuilder sb) {
        // Add
        if (node.isEnd) ans.add(sb.toString());
        // Exit
        if (node.children.isEmpty()) return;
        // Traverse all nodes
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            sb.append(entry.getKey());
            dfsSearch(ans, entry.getValue(), sb);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    /*
    这里有三种情况：
    1. 有"app"和"apple"，现在要删除"apple"，需要将"le"一起删除
    2. 只有"apple"，现在要删除"apple"，需要将整个都删除
    3. 有"apples"和"apple"，现在要删除"apple"，只能将字母e的isEnd标记为false
    这三种情况都基于一个核心的逻辑：节点的map为空（无子节点） + 节点的isEnd是false（不是单词） = 可以删除
    1. "app"，最后一个p节点，map不为空但后续所有节点的isEnd都是false（因为"apple"被删掉了），所以p节点以后的都可以删除
    2. "apple"，从第一个节点到最后一个节点，所有节点的isEnd都是false，所以全部删除
    3. "apple"，e节点的children不为空，且后续s节点的isEnd是true，所以不能删除

    某个节点的map是否为空：可以当下就立刻判断
    后续所有节点的isEnd都是false：需要一直往下，直到所有的都是false才能知道。所以这个条件需要回溯的时候才能判断。
    但这里的"后续所有节点"，指的是s之前的，就比如例子1，指的是"app"的最后一个p节点，到"apple"的最后一个e节点，而不是s之后的
    所以，s的最后一个字母，如果它有children，那就说明它后面还有单词，因为每次操作都会把不需要的node去掉

    所以整个dfs的逻辑为：
    1. 退出条件：depth = s时，判断isEnd
    2. 每一层需要做什么：
        1. 找到当前的char
        2. 找到char对应的node A，没有就返回false
        3. depth+1与node A进入下一层，用一个boolean接受结果
    3. 从下往上回溯以后需要做什么：
        1. 通过2.3的结果，可以得知node A是否有子节点
        2. wasDeleted + node A是否有子节点 + !nodeA.isEnd = 是否移除当前节点
        3. 返回wasDeleted
     */
    public boolean delete(String s) {
        boolean ans = dfsDelete(root, s, 0);
        print(Thread.currentThread().getStackTrace()[1].getMethodName(), s, String.valueOf(ans));
        return ans;
    }

    private boolean dfsDelete(TrieNode node, String s, int depth) {
        /*
        退出条件：遍历到s结束，如果此时node.isEnd为true，说明有这个单词，那么将其标记为false，并且返回true（表示找到了且删除了）
        我们不需要考虑s之后的节点/字母，原因是：
        我们写的delete逻辑会每次都删除所有不需要的节点，所以只要s的最后一个字母的children还有元素，说明一定存在其他单词（不需要的
            一定会被删除，留下来的都是需要的）
         */
        // 当长度到了以后开始判断
        if (depth == s.length()) {
            // 如果单词s不存在
            if (!node.isEnd) return false;
            // 如果存在，则将其标记为false
            node.isEnd = false;
            return true;
        }

        // 找到当前字母
        char c = s.charAt(depth);
        // 找到c对应的node;
        TrieNode child = node.children.get(c);
        // 如果不存在直接返回false
        if (child == null) return false;

        // 继续往下遍历
        boolean wasDeleted = dfsDelete(child, s, depth + 1);

        /*
        遍历结束以后，判断是否需要删除
        当把c对应的节点删除以后，判断children还有没有其他元素，如果没有且node的isEnd是false，就说明node也可以被删除
        那么这个信息就会在回溯的时候返回给node的上一个节点，上一个节点就会同样将node删除，以此类推
         */
        if (wasDeleted && child.children.isEmpty() && !child.isEnd) {
            node.children.remove(c);
        }
        // 将wasDeleted一路返回，这是对外接口delete(String s)的结果
        return wasDeleted;
    }

    static class TrieNode {
        Map<Character, TrieNode> children;
        boolean isEnd;

        public TrieNode() {
            this.children = new HashMap<>();
            this.isEnd = false;
        }
    }
}
