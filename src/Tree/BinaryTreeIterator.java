package Tree;

import java.util.ArrayDeque;
import java.util.Deque;

/*
刷题次数：2
第二次：忘了
https://leetcode.cn/problems/binary-search-tree-iterator/description/

Implement the BSTIterator class that represents an iterator over the in-order traversal of a binary search tree (BST):
1. BSTIterator(Node root) Initializes an object of the BSTIterator class.
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
  private Deque<Node> stack = new ArrayDeque<>();
  private Node current;

  public BinaryTreeIterator(Node root) {
    // 添加新的指针
    this.current = root;
  }

  public int next() {
    // 与InOrderTraverse的stack解法类似
    // 先遍历到最左边的节点,并将所有节点push到stack里面
    while (this.current != null) {
      this.stack.push(current);
      current = current.left;
    }
    // 这里需要一个新的指针用来返回结果,因为在返回之前要把current移动到current.right
    Node node = stack.pop();
    current = node.right;
    return node.val;
  }

  public boolean hasNext() {
    // 如果stack非空或者current非空
    if (current != null || !stack.isEmpty()) {
      return true;
    }
    return false;
  }
}
