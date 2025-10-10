package DataStructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
刷题次数: 2
第二次: 还记得前缀树大概的思路, 但是忘了具体怎么实现

https://leetcode.cn/problems/remove-sub-folders-from-the-filesystem/description/

Given a list of folders folder, return the folders after removing all sub-folders in those folders.
You may return the answer in any order.
If a folder[i] is located within another folder[j], it is called a sub-folder of it.
The format of a path is one or more concatenated strings of the form: '/' followed by one or more lowercase English letters.
For example, "/leetcode" and "/leetcode/problems" are valid paths while an empty string and "/" are not.

Input: folder = ["/a","/a/b","/c/d","/c/d/e","/c/f"]
Output: ["/a","/c/d","/c/f"]
Input: folder = ["/a","/a/b/c","/a/b/d"]
Output: ["/a"]

思路: 前缀树
1. 前缀树的本质是多叉树
2. 前缀树的思想是将单词拆分成一个个的字母, 单词["ace", "acf", "adg", "adh"]可以保存如下图: 
       a
     /   \
    c     d
   / \   / \
  e  f  g   h
3. 前缀树的目的是节约查找时间, 搜索引擎中的, 输入一个单词就显示出你可能想要输入的下一个单词是什么, 就是通过前缀树实现的
4. 前缀树的实现是通过哈希表, 每个前缀树类型的对象, 都有一个哈希表类型的children, 用来保存下一层的所有前缀树类型的对象
   即Trie(这是a的Trie对象).children -> {"c": Trie(这是c的Trie对象), "d": Trie(这是d的Trie对象)}
     Trie(这是c的Trie对象).children -> {"e": Trie(这是e的Trie对象), "f": Trie(这是f的Trie对象)}
     Trie(这是d的Trie对象).children -> {"g": Trie(这是g的Trie对象), "h: Trie(这是h的Trie对象)}
5. 构建前缀树的时候, 需要定义: 
   1. 一个字典, 且该字典是实例变量, 即每个实例都有一个
   2. 一个整数变量, 记录当前节点的状态, 如果当前节点是结尾, 比如"ace"的e, 那么就记录"ace"在原本folders数组中的位置
      如果当前节点不是结尾, 则默认为初始值-1, 比如"ace"的a和c
6. 前缀树需要两个方法: 
   1. 一个add方法, 输入是一整个单词, 作用是将这整个单词添加进前缀树, 这里主要的思路其实很简单, 就是通过查找字典, 判断是否存在
      如果存在, 则通过更新节点的方法(类似于node = node.next)继续遍历
      如果不存在, 则创建一个新的前缀树, 然后将该字母和前缀树放入字典, 再继续通过更新节点的方法继续遍历
   2. 一个search方法, 没有输入, 输出是一个包含所有完整目录位置的列表, 该方法需要配合一个DFS方法完成
      DFS的出口就是当idx != -1, 即遍历到了目录结尾, 此时添加该idx值, 然后返回
7. 主方法则很简单: 
   1. 遍历folders列表, 将每个单词加入前缀树
   2. 遍历通过search方法得到的列表, 将其位置与实际的目录值对应, 放入result列表

时间复杂度: O(N * M), 其中N是folders列表长度, M是其中的单词的最大长度
时间复杂度: O(N * M), 其中N是folders列表长度, M是其中的单词的最大长度
 */

public class RemoveSubFolder {
    // 定义一个前缀树
    class Trie {
        // 定义一个字典来存储映射关系
        private HashMap<String, Trie> children = new HashMap<>();
        // 定义一个变量来记录该路径是字符串在folder数组的位置, 并且通过-1来表示当前节点不是目录的结尾
        // 比如"/a/b/e" -> 那么对a来说, 不是目录结尾, 所以-1, 对e来说, 就是结尾, 所以记录字符串在folder数组的位置
        private int idx = -1;

        // 定义一个添加的方法
        public void add(String str, int i) {
            // 因为我们想用一个Trie作为根节点, 且每次都是用同样的这个根节点对象, 为了保证对象一致, 所以用一个变量表示自己
            // 比如对于folders = ["/a/c", "/b/d"], 则有结构: 
            // trie(this)) -> children:{"a": trie(a的trie), "b": trie(b的trie)}
            // 再往下
            // trie(a的trie) -> children:{"c": trie(c的trie)}
            // trie(b的trie) -> children:{"d": trie(d的trie)}
            Trie trie = this;
            // 将此字符串通过'/'分割并遍历
            for (String path : str.split("/")) { // "/a/b/e" -> ["", "a", "b", "c"]
                // 排除掉第一个, 因为第一个是空字符串
                if (path == "") {
                    continue;
                }
                // 查询是否在children这个字典中
                // 如果不在
                if (!trie.children.containsKey(path)) {
                    // 创建一个新的trie, 并以{path: trie}的方式存入children
                    trie.children.put(path, new Trie());
                }
                // 如果在, 则说明已经创建过了, 那么就更新trie达到遍历的效果, 类似于node = node.next一样
                // 比如"/a/b"和"/a/c", 在添加"/a/b"时, 已经将a和b都添加进去了, 所以第二次遍历"/a/c"时, 
                // 直接更新trie为a的children, 然后在a的children里面找b, 如果没有就创建
                // 直到遍历到不存在于children中的字符串, 或者遍历结束
                trie = trie.children.get(path);
            }
            // 遍历结束后, 此时的trie应该是处于目录的结尾, 将其位置更新
            trie.idx = i;
        }

        // 定义一个查找的方法
        public List<Integer> search() {
            // 定义一个变量, 记录答案在folders中的index
            List<Integer> resultIdx = new ArrayList<>();
            // 再通过一个DFS来找到遍历每一条路径
            dfs(this, resultIdx);
            // 返回
            return resultIdx;
        }

        // 定义一个DFS的方法
        private void dfs(Trie trie, List<Integer> resultIdx) {
            // 递归出口
            // 如果该trie的idx不是-1, 那么说明此时已经是目录的结尾, 所以记录下来
            if (trie.idx != -1) {
                resultIdx.add(trie.idx);
                return;
            }
            // 遍历
            for (Trie child : trie.children.values()) {
                dfs(child, resultIdx);
            }
        }
    }

    public List<String> solution1(String[] folders) {
        // 定义变量
        List<String> result = new ArrayList<>();
        Trie trie = new Trie();
        // 遍历添加进Trie
        for (int i = 0; i < folders.length; i++) {
            trie.add(folders[i], i);
        }
        // 通过search得到一个答案的index的列表, 然后通过对应到folders中找到真正的目录
        for (int i : trie.search()) {
            result.add(folders[i]);
        }
        return result;
    }
}
