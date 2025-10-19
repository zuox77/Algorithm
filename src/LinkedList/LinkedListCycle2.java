package LinkedList;

/*
https://leetcode.cn/problems/linked-list-cycle-ii/description/?envType=study-plan-v2&envId=top-100-liked

Given the head of a linked list, return the node where the cycle begins. If there is no cycle, return null.
There is a cycle in a linked list if there is some node in the list that can be reached again by continuously following the next pointer.
Internally, pos is used to denote the index of the node that tail's next pointer is connected to (0-indexed).
It is -1 if there is no cycle. Note that pos is not passed as a parameter.

Do not modify the linked list.

思路: 快慢指针
1. 核心思路：如果有环的情况下，建立两个指针，slow每次移动一步，fast每次移动2步，总有一天fast会超过slow一圈并且相遇。
   此时，同时移动head和slow，每次移动一步，当head和slow相遇时，即是入环点。具体可以看leetcode题解。
2. 此题有几个corner case：
    1. 空链表                  --> fast != null
    2. 只有一个node的链表       --> fast.next != null
    3. 只有两个node且无环的链表  --> fast会直接抵达链表末端的null，所以也是fast != null
    4. 只有两个node且有环的链表  --> fast会直接抵达原点，所以此时直接判断slow == fast

 */
public class LinkedListCycle2 {
    /**
     * Definition for singly-linked list.
     * class ListNode {
     *     int val;
     *     ListNode next;
     *     ListNode(int x) {
     *         val = x;
     *         next = null;
     *     }
     * }
     */
    public ListNode detectCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                while (slow != head) {
                    slow = slow.next;
                    head = head.next;
                }
                return slow;
            }
        }
        return null;
    }
}
