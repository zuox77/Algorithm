package Tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/*
https://leetcode.cn/problems/binary-tree-right-side-view/description/?orderBy=most_votes
Given the root of a binary tree, imagine yourself standing on the right side of it,
return the values of the nodes you can see ordered from top to bottom

思路：BFS
1. 层级遍历，用一个queue去存储每一层的所有node
2. 将每一层的第一个node存储在result中
3. 关键点在于如何知道每一层是怎么结束的以及怎么知道每一层的最后一个node是哪个：
    1. 用for循环，每次判断完左右以后，用queue.size获得当前层的node数量，再for这个数量
    2. 用两个变量记录，一个变量child随着queue.add增加，另一个则在所有的node加入以后更新size = child
时间复杂度：O(N)
空间复杂度：O(N)

思路：DFS
1. 遍历顺序：根 -> 右 -> 左
2. 通过一个变量depth去记录当前层级
3. 通过result.size去判断当前层级，已知每一层只会有一个，所以result里面有几个数就是几层
   根节点的depth是0，因为最开始的时候result里面是0个数，所以初始化depth为0更方便
时间复杂度：O(N)
空间复杂度：O(N)
 */

public class RightView {
    // BFS
    public List<Integer> solution(Node root) {
        Queue<Node> queue = new LinkedList<>();
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        queue.add(root);
        while (!queue.isEmpty()) {
            int len = queue.size();
            for (int i = 0; i < len; i++) {
                Node node = queue.poll();
                if (i == len - 1) {
                    result.add(node.val);
                }
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
        }

        return result;
    }

    // DFS
    public List<Integer> solution2(Node root) {
        List<Integer> result = new ArrayList<>();
        dfs(root, 0, result);
        return result;
    }

    public void dfs(Node root, int depth, List<Integer> result) {
        // recursion exit
        if (root == null) {
            return;
        }
        // check layer
        if (depth == result.size()) {
            result.add(root.val);
        }
        dfs(root.right, depth, result);
        dfs(root.left, depth, result);
    }
}
