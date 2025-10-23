package LinkedList;

/*
https://leetcode.cn/problems/reverse-linked-list/description/?envType=study-plan-v2&envId=top-100-liked

Given the head of a singly linked list, reverse the list, and return the reversed list.

Example 1:
Input: head = [1,2,3,4,5]
Output: [5,4,3,2,1]

Example 2:
Input: head = [1,2]
Output: [2,1]

Example 3:
Input: head = []
Output: []

思路: 双指针
1. 一前一后双指针
    1. 记录right的下一个node
    2. 将right的下一个指向left
    3. left = right
    4. right = 第一步记录的node
2. 需要注意的是：Leetcode判题时，会看有没有null作为结束节点，如果left从head开始，那么就没有结束节点，会被判定为有环
    所以left要从null开始

 */
public class ReverseLinkedList {
    /**
     * Definition for singly-linked list. public class ListNode { int val; ListNode next;
     * ListNode(int x) { val = x; next = null; } }
     */
    public ListNode reverseList(ListNode head) {
        ListNode left = null;
        ListNode right = head;
        while (right != null) {
            ListNode rightNext = right.next;
            right.next = left;
            left = right;
            right = rightNext;
        }
        return left;
    }
}
