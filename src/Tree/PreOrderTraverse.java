package Tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/*
刷题次数: 2
第二次:

https://leetcode.cn/problems/binary-tree-preorder-traversal/

Given the root of a binary tree, return the preorder traversal of its nodes' values.

Input: root = [1,null,2,3]
Output: [1,2,3]

思路1: 递归/DFS
1. 用一个helper函数来做递归, 主函数只需要新建出存储结果的变量即可
2. helper函数:
    1. 先写出口
    2. 因为前序遍历是 根 - 左 - 右, 所以先加入结果, 再递归左, 再递归右
    3. 递归之前检查左右子节点是否存在
时间复杂度: O(N)
空间复杂度: O(k), 递归会用到stack, k代表树高

思路2: 递归/分治
1. 跟思路1整体思路差不多, 不过由于是分治, 所以不需要额外helper函数来帮忙
时间复杂度: O(N)
空间复杂度: O(k), 递归会用到stack, k代表树高

思路3: 无递归/栈
1. 如果不用递归, 由于无法从子节点回到父节点, 那么一定需要一个stack将其经过的父节点都存起来
2. stack是一个FILO的队列, 所以要先将右节点压进去
3. 初始化stack将root压进去
4. 用while循环条件stack是否为空, 类似于BFS里面的queue是否为空
5. 先pop出来一个node, 将此node加入result, 每pop出来一个就将值存进result里面
6. 再检查右节点是否存在, 存在则压进stack, 再检查左子节点是否存在, 存在则压进stack
时间复杂度: O(N)
空间复杂度: O(k), k代表树高

思路4: 无递归/栈
1. 如果不用递归, 由于无法从子节点回到父节点, 那么一定需要一个stack将其经过的父节点都存起来
2. 将一个元组(node, i)压进stack
3. 当i为0, 代表到到下一层, 需要将与i成对的node的左右子节点压进stack
4. 当i为1/2/3时, 将node的值存入result, 1/2/3分别对应前/中/后序遍历
时间复杂度: O(N)
空间复杂度: O(k), k代表树高

思路5: 无递归无栈/Morris算法
1. 无论前中后序遍历, 最大的难点就是到了叶子节点怎么返回父子节点, 比如:
                   1
          2                  3
      4        5         6         7
   8   9   10   11   12   13   14   15
   如果不用额外空间, 那么当把1和2都添加进result后, 我们现在在2的位置, 如何去到3
2. 方法是:
   1. 用now指针记录当前的根节点
   2. 判断根节点now的左子节点是否为空, 如果不为空, 则用一个新的tmp指针指向根节点的左节点, 用while循环一直遍历到该左节点下面最右边的节点, 比如
      now现在在1, 那么tmp为2, 然后while循环让tmp遍历到11
   3. 当tmp指向最右边的节点时, 将now添加进result, 并将tmp.right设为now, 从而达到返回的目的, 比如
      当tmp现在在11, now在2, 那么设tmp.right = 2
   4. 通过now = now.left更迭, 指向下一个左子节点
   5. 重复第2步和第3步, 直到now走到尽头, 比如:
      now在2, tmp为4, tmp遍历到最右边的节点, 即9, 将9的right设为2
      now在4, tmp在8, tmp遍历到最右边的节点, 即8自己, 将8的right设为4
      now在8, 8没有左右子节点
   6. 因为之前已经将所有的子节点和其前一个父节点连接在一起了, 且已经将所有左子节点添加进result, 所以此时需要将此时的now, 即8, 添加进result,
      并且将now更迭为now.right, 因为之前已经设置过8.right = 4了, 所以此时now = 4
   7. 此时now = 4, tmp = 8, 通过if语句判断此时的tmp.right是否等4, 如果等于4, 说明已经遍历过了, 所以一直通过.right方法返回到最上层的1

3. Morris算法利用额外的指针将所有的尾节点都加一个指针返回父子节点, 以达到不用额外空间又能返回父子节点的目的
             难点在于如何返回父节点, 比如

时间复杂度: O(2N), N为节点数量, 为了满足从右子节点回到父节点这一过程, 需要提前遍历一遍所有的节点
空间复杂度: O(1)
 */

public class PreOrderTraverse {
    // DFS / Recursion
    public List<Integer> preOrderTraverse_DFS(TreeNode root) {
        // 定义变量
        List<Integer> result = new ArrayList<Integer>();
        // DFS递归
        recursion(result, root);
        // 返回答案
        return result;
    }

    public void recursion(List<Integer> result, TreeNode root) {
        // 递归出口
        if (root == null) {
            return;
        }
        // 按照前序遍历 根 - 左 - 右 的顺序, 每次都先将root放进答案
        result.add(root.val);
        // 递归遍历左子节点
        recursion(result, root.left);
        // 递归遍历右子节点
        recursion(result, root.right);
    }

    // Divide And Conquer
    public List<Integer> preOrderTraverse_DivideAndConquer(TreeNode root) {
        // 定义变量
        List<Integer> result = new ArrayList<>();
        // corner case
        if (root == null) {
            return result;
        }
        // 分治的分
        List<Integer> left = preOrderTraverse_DivideAndConquer(root.left);
        List<Integer> right = preOrderTraverse_DivideAndConquer(root.right);
        // 分治的治
        // 按照前序遍历 根 - 左 - 右 的顺序, 每次都先将root放进答案
        result.add(root.val);
        // 这里记得一定要用addAll, 因为addAll是将list里面所有的元素放进去, 而不是将list放进去
        result.addAll(left);
        result.addAll(right);
        // 返回答案
        return result;
    }

    // Stack
    public List<Integer> preOrderTraverse_Stack(TreeNode root) {
        // 定义变量
        List<Integer> result = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        // 先检查是否是null, 不然加进stack会报错
        if (root == null) {
            return result;
        }
        // 将root放入stack
        stack.push(root);
        // 开始循环遍历
        while (!stack.isEmpty()) {
            // 将栈顶弹出
            TreeNode node = stack.pop();
            // 按照前序遍历 根 - 左 - 右 的顺序, 每次都先将root放进答案
            result.add(node.val);
            // 这里与递归不一样, 需要先将right放进去, 因为栈是FILO顺序, 所以right先放进去会最后出来
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }
        // 返回答案
        return result;
    }

    public List<Integer> preOrderTraverse_Stack2(TreeNode root) {
        // 定义变量
        List<Integer> result = new ArrayList<>();
        Deque<State> stack = new ArrayDeque<>();
        // 先检查是否是null, 不然加进stack会报错
        if (root == null) {
            return result;
        }
        // 将root放入stack
        stack.push(new State(root, 0));
        // 开始遍历
        while (!stack.isEmpty()) {
            // 将栈顶弹出
            State now = stack.pop();
            TreeNode node = now.node;
            int indicator = now.indicator;
            // 先检查null, 避免后续node.left/node.right出错
            if (node == null) {
                continue;
            }
            // 这是一个通用template, 其本质就是逆向添加进stack, 再一一弹出
            // 对于前序遍历, 其实下面的代码就够了:
            // stack.push(new State(node.right, 0));
            // stack.push(new State(node.left, 0));
            // stack.push(new State(node, 1));
            // 之所以添加其他的, 是因为写成了一个通用公式, 只需要改变添加进result的时候的数值, 就可以做中序/后序遍历
            if (indicator == 0) {
                stack.push(new State(node, 3));
                stack.push(new State(node.right, 0));
                stack.push(new State(node, 2));
                stack.push(new State(node.left, 0));
                stack.push(new State(node, 1));
            }
            // 1代表前序遍历, 2代表中序遍历, 3代表后续遍历
            if (indicator == 1) {
                result.add(node.val);
            }
        }
        return result;
    }

    // Morris
    public List<Integer> preOrderTraverse_Morris(TreeNode root) {
        // 定义变量
        List<Integer> result = new ArrayList<Integer>();
        // 创建一个指向root的新的指针, 这样移动新指针不会导致丢失对root的引用
        TreeNode now = root;
        // 遍历
        while (now != null) {
            // 判断now.left是否存在
            if (now.left != null) {
                // 新设置一个指针指向now.left
                TreeNode tmp = now.left;
                // 通过遍历, 将tmp遍历到当前节点的最右边节点
                while (tmp.right != null && tmp.right != now) {
                    tmp = tmp.right;
                }
                // 如果此时tmp.right == now, 说明我们已经将所有左子节点遍历过且添加进了result, 现在正处于一层层返回父节点的过程中
                // 所以此时只需要将新设置的连接断开, 然后一步步返回
                if (tmp.right == now) {
                    tmp.right = null;
                    now = now.right;
                    // 如果此时tmp.right != now, 说明我们还没有将右子节点和父节点连上, 这是第一次遍历到这个节点
                    // 所以将节点添加进result, 并将tmp.right指向now, 然后now更新为now.left, 继续遍历
                } else {
                    // 因为是前序遍历: 根 - 左 - 右
                    // 所以当第一次遍历到这个节点的时候就需要把它加入result, 因为now即是根, 根要最开始被添加进去
                    result.add(now.val);
                    tmp.right = now;
                    now = now.left;
                }
                // 这里代表最上层的左子树已经全部添加进了result, 现在换右子树
            } else {
                result.add(now.val);
                now = now.right;
            }
        }
        return result;
    }

    // Stack2
    private static class State {
        TreeNode node;
        int indicator;

        public State(TreeNode node, int indicator) {
            this.node = node;
            this.indicator = indicator;
        }
    }
}
