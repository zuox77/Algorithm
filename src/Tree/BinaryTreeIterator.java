package Tree;

import java.util.ArrayDeque;
import java.util.Deque;

/*

https://leetcode.cn/problems/binary-search-tree-iterator/description/

Implement the BSTIterator class that represents an iterator over the in-order traversal of a binary search tree (BST):
1. BSTIterator(TreeNode root) Initializes an object of the BSTIterator class.
2. The root of the BST is given as part of the constructor.
3. The pointer should be initialized to a non-existent number smaller than any element in the BST.
boolean hasNext() Returns true if there exists a number in the traversal to the right of the pointer, otherwise returns false.
int next() Moves the pointer to the right, then returns the number at the pointer.
Notice that by initializing the pointer to a non-existent smallest number, the first call to next() will return the smallest element in the BST.
You may assume that next() calls will always be valid. That is, there will be at least a next number in the in-order traversal when next() is called.

Input
["BSTIterator", "next", "next", "hasNext", "next", "hasNext", "next", "hasNext", "next", "hasNext"]
[[[7, 3, 15, null, null, 9, 20]], [], [], [], [], [], [], [], [], []]
Output
[null, 3, 7, true, 9, true, 15, true, 20, false]

Explanation
BSTIterator bSTIterator = new BSTIterator([7, 3, 15, null, null, 9, 20]);
bSTIterator.next();    // return 3
bSTIterator.next();    // return 7
bSTIterator.hasNext(); // return True
bSTIterator.next();    // return 9
bSTIterator.hasNext(); // return True
bSTIterator.next();    // return 15
bSTIterator.hasNext(); // return True
bSTIterator.next();    // return 20
bSTIterator.hasNext(); // return False

思路：与InOrderTraverse的stack做法一致
1. 定义声明实例变量stack和current
2. 构造方法里面将current指向root
3. 在next方法里面,用InOrderTraverse的方法,先一路遍历到最左边的节点,并将所有遍历过的节点都push到stack,然后pop出第一个,并
   用另外一个变量node标注,将current移到current.right,返回刚刚pop出的变量node.val
4. 在hasNext()方法里,通过判断stack非空和current非空来返回boolean
 */

public class BinaryTreeIterator {
    private final Deque<TreeNode> stack;

    public BinaryTreeIterator(TreeNode root) {
        this.stack = new ArrayDeque<>();
        while (root != null) {
            stack.addFirst(root);
            root = root.left;
        }
    }

    public int next() {
        TreeNode root = stack.pollFirst();
        int rootVal = root.val;
        root = root.right;
        while (root != null) {
            stack.addFirst(root);
            root = root.left;
        }
        return rootVal;
    }

    public boolean hasNext() {
        return !stack.isEmpty();
    }
}
