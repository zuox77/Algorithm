package LinkedList;

import java.util.HashMap;
import java.util.Map;

/*
https://leetcode.cn/problems/copy-list-with-random-pointer/description/?envType=study-plan-v2&envId=top-100-liked

A linked list of length n is given such that each node contains an additional random pointer, which could point to any node in the list, or null.
Construct a deep copy of the list. The deep copy should consist of exactly n brand-new nodes,
where each new node has its value set to the value of its corresponding original node.
Both the next and random pointer of the new nodes should point to new nodes in the copied list such that
the pointers in the original list and copied list represent the same list state. None of the pointers in the new list should point to nodes in the original list.
For example, if there are two nodes X and Y in the original list, where X.random --> Y, then for the corresponding two nodes x and y in the copied list, x.random --> y.
Return the head of the copied linked list.
The linked list is represented in the input/output as a list of n nodes. Each node is represented as a pair of [val, random_index] where:
val: an integer representing Node.val
random_index: the index of the node (range from 0 to n-1) that the random pointer points to, or null if it does not point to any node.
Your code will only be given the head of the original linked list.

Example 1:
Input: head = [[7,null],[13,0],[11,4],[10,2],[1,0]]
Output: [[7,null],[13,0],[11,4],[10,2],[1,0]]

Example 2:
Input: head = [[1,1],[2,1]]
Output: [[1,1],[2,1]]

Example 3:
Input: head = [[3,null],[3,0],[3,null]]
Output: [[3,null],[3,0],[3,null]]

思路：哈希表
1. 只需要一个哈希表，将原node和新创建的node建立映射关系即可

思路：链表
1. 这道题本质上只需要知道原node和新创建的node的关系即可，即我们只需要有种方法能从原node找到新创建的node
2. 那么我们将新创建的node插入原链表，形成一个类似于：
    1 -> 1' -> 2 -> 2' -> 3 -> 3'
    这样，我们相当于用node.next去记录了这个映射关系

 */
public class CopyListWithRandomPointer {

    public Node copyRandomList(Node head) {
        // 创建一个新的链表的head
        Node dummy = new Node(-1);
        // 用一个指针保存新链表的head
        Node root = dummy;
        /*
        用一个map去保存原node和新建的copy node的映射关系
        这样的话，原node能到的地方（next或者random），通过这个map，也能抵达新的node所对应的地方
         */
        Map<Node, Node> map = new HashMap<>();
        while (head != null) {
            // 根据map，查询head是否已经有copy node，如果是，获得copy node，如果不是，创建copy node
            Node headCopy = map.getOrDefault(head, new Node(head.val));
            // 无论是否通过head获取到node，都再一次保存这段关系，避免重复创建node
            map.put(head, headCopy);
            // 更新copy node的random，注意null的处理
            if (head.random != null) {
                Node headRandom = head.random;
                Node headCopyRandom = map.getOrDefault(headRandom, new Node(headRandom.val));
                // 同样再次保存，避免重复创建
                map.put(headRandom, headCopyRandom);
                // 更新headCopy的random指针
                headCopy.random = headCopyRandom;
            }
            // 入链
            dummy.next = headCopy;
            // 移动dummy和head
            dummy = dummy.next;
            head = head.next;
        }
        return root.next;
    }

    public Node copyRandomList2(Node head) {
        // 注意：需要单独判断head = null的情况，因为head如果为null，后面Node newHead = head.next;就会报错
        if (head == null) return null;
        // 创建一个node代替head指针
        Node current = head;
        // 遍历原链表创建每个node的copy
        while (current != null) {
            // 创建当前node的copy
            Node copy = new Node(current.val);
            // 插入
            copy.next = current.next;
            current.next = copy;
            // 移动到下一个
            current = copy.next;
        }
        // 再次遍历链表，设置random值
        current = head;
        while (current != null) {
            /*
            current.next即为current的copy
            current.next.random即为current的copy的random
            刚好也是current.random.next
            注意：记得判断null
             */
            current.next.random = current.random != null ? current.random.next : null;
            // 移动current
            current = current.next.next;
        }
        // 分离两个链表
        current = head;
        Node newHead = head.next;
        // 只需要将当前的node指向下下个node即可，无需分辨谁是原链表，谁是新链表，因为它们就是隔一个而存在
        while (current != null) {
            // 记录current.next
            Node next = current.next;
            // 将current指向next.next
            current.next = next.next;
            // 移动current
            current = next;
        }
        return newHead;
    }

    public static class Node {
        int val;
        Node next;
        Node random;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
    }
}
