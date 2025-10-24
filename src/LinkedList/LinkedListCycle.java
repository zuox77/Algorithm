package LinkedList;

/*
https://leetcode.cn/problems/linked-list-cycle/?envType=study-plan-v2&envId=top-100-liked

Given head, the head of a linked list, determine if the linked list has a cycle in it.
There is a cycle in a linked list if there is some node in the list that can be reached again by continuously following the next pointer.
Internally, pos is used to denote the index of the node that tail's next pointer is connected to. Note that pos is not passed as a parameter.
Return true if there is a cycle in the linked list. Otherwise, return false.

Example 1:
Input: head = [3,2,0,-4], pos = 1
Output: true
Explanation: There is a cycle in the linked list, where the tail connects to the 1st node (0-indexed).

Example 2:
Input: head = [1,2], pos = 0
Output: true
Explanation: There is a cycle in the linked list, where the tail connects to the 0th node.

Example 3:
Input: head = [1], pos = -1
Output: false
Explanation: There is no cycle in the linked list.

思路: 快慢指针
1. 核心思路：如果有环的情况下，建立两个指针，slow每次移动一步，fast每次移动2步，总有一天fast会超过slow一圈并且相遇。
   此时，同时移动head和slow，每次移动一步，当head和slow相遇时，即是入环点。具体可以看leetcode题解。
2. 此题有几个corner case：
    1. 空链表                  --> fast != null
    2. 只有一个node的链表       --> fast.next != null
    3. 只有两个node且无环的链表  --> fast会直接抵达链表末端的null，所以也是fast != null
    4. 只有两个node且有环的链表  --> fast会直接抵达原点，所以此时直接判断slow == fast

 */
public class LinkedListCycle {
    /**
     * Definition for singly-linked list. class ListNode { int val; ListNode next; ListNode(int x) {
     * val = x; next = null; } }
     */
    public boolean hasCycle(ListNode head) {
        // 快慢指针移动直到相遇
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            // 这个条件不能在while里面判断，因为最开始设定slow和fast就是同时从head开始移动的
            if (slow == fast) return true;
        }
        return false;
    }
}
