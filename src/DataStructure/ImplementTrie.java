package DataStructure;

import java.util.HashMap;
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

class ImplementTrie {

    private final TreeNode root = new TreeNode();

    public ImplementTrie() {}

    public void insert(String word) {
        TreeNode cur = root;
        for (Character ch : word.toCharArray()) {
            // 如果ch不存在cur的map中，创建一个新的node
            // 如果ch已经在cur的map中，拿到已存在的node
            TreeNode node = cur.map.getOrDefault(ch, new TreeNode());
            // 不能在这里将当前node（cur）的end属性改为false
            // 因为如果存在例如"app"和"apple"，那么这两个词，search都应该返回true
            // 如果我们在这里将当前node（cur）的end属性改为false了，那么在添加"app"时，这里的end属性为true，
            // 但是后面在添加"apple"时，就会覆盖"app"的end的属性，会导致search app时出错
            // 将当前的ch和node放入map
            cur.map.put(ch, node);
            // 移动cur到下一个node
            cur = node;
        }
        // 移动结束以后，将其end标记为true
        cur.end = true;
    }

    public boolean search(String word) {
        TreeNode cur = iterate(word);
        // 如果cur是null，那么表示word中有字母不存在
        // 如果cur不是null，那么表示word里所有字母都存在，所以只需要额外判断一下cur的这个node的end属性是否为true
        // 如果是true，那么就说明存在word，search返回true，反之则返回false
        return cur != null && cur.end;
    }

    public boolean startsWith(String prefix) {
        // 如果iterate(prefix)所返回的node是null，那么说明，prefix中有字母不存在，所以返回false
        // 反之则返回true
        return iterate(prefix) != null;
    }

    private TreeNode iterate(String s) {
        TreeNode cur = root;
        for (Character ch : s.toCharArray()) {
            // 如果任意一个ch不存在在map中，返回null
            if (!cur.map.containsKey(ch)) {
                return null;
            }
            // 如果存在，那么移动cur
            cur = cur.map.get(ch);
        }
        return cur;
    }

    private static class TreeNode {
        private final Map<Character, TreeNode> map = new HashMap<>(26, 1);
        boolean end = false;
    }
}
