package LinkedList;

/*
https://leetcode.cn/problems/swap-nodes-in-pairs/description/?envType=study-plan-v2&envId=top-100-liked

Given a linked list, swap every two adjacent nodes and return its head.
You must solve the problem without modifying the values in the list's nodes (i.e., only nodes themselves may be changed.)

Example 1:
Input: head = [1,2,3,4]
Output: [2,1,4,3]

Example 2:
Input: head = []
Output: []

Example 3:
Input: head = [1]
Output: [1]
 */
public class SwapNodesInPairs {
    /**
     * Definition for singly-linked list. class ListNode { int val; ListNode next; ListNode(int x) {
     * val = x; next = null; } }
     */
    public ListNode swapPairs(ListNode head) {
        // 思路是通过一个helper方程去转换，每一次只调整一个node

        // 创建dummy node
        ListNode dummy = new ListNode();
        dummy.next = head;
        // 创建pre
        ListNode pre = dummy;
        // 遍历
        while (head != null && head.next != null) {
            // 定义next
            ListNode next = head.next;

            /*
            1. 从链表移除
            从
             pre    head  next
            dummy -> [1 -> 2 -> 3 -> 4]
            变成
             head   pre  next
             1   [dummy -> 2 -> 3 -> 4]
             */
            deleteFromLink(pre, head, next);
            /*
            2. 我们想要将1加入2和3之间，所以按照自定义的addToLink方程，需要移动指针
             从
             head   pre  next
              1   [dummy -> 2 -> 3 -> 4]
             变成
             head         pre  next
              1   [dummy -> 2 -> 3 -> 4]
             即：pre和next都往后移动一位
             */
            pre = next;
            next = next.next;
            /*
            3. 加入链表
             从
             head         pre  next
              1   [dummy -> 2 -> 3 -> 4]
             变成
                      pre  head  next
             [dummy -> 2 -> 1 -> 3 -> 4]
             */
            addToLink(pre, head, next);

            /*
            4. 移动到下一个交换点，即3是需要被移动的点
             从
                      pre  head  next
             [dummy -> 2 -> 1 -> 3 -> 4]
             变成
                           pre  head  next
             [dummy -> 2 -> 1 -> 3 -> 4]
             */
            pre = head;
            head = next;
            // next会在下个循环的开始的时候设置
        }
        return dummy.next;
    }

    private void deleteFromLink(ListNode pre, ListNode node, ListNode next) {
        pre.next = next;
        node.next = null;
    }

    private void addToLink(ListNode pre, ListNode node, ListNode next) {
        pre.next = node;
        node.next = next;
    }
}
