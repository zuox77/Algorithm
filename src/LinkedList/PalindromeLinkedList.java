package LinkedList;

/*
https://leetcode.cn/problems/palindrome-linked-list/description/?envType=study-plan-v2&envId=top-100-liked

Given the head of a singly linked list, return true if it is a palindrome or false otherwise.

Example 1:
Input: head = [1,2,2,1]
Output: true

Example 2:
Input: head = [1,2]
Output: false

思路: 快慢指针 + 翻转链表
1. 快慢指针找到中间节点
    注意：循环条件为fast != null && fast.next != null
    1. 因为fast每次移动2格
    2. 当是奇数个的时候，比如：1 -> 2 -> 3 -> 4 -> 5，中间节点为3，此时slow = 3， fast = 5，此时就该停下，所以要判断 fast.next != null
    3. 当是偶数个的时候，比如：1 -> 2 -> 3 -> 4 -> 5 -> 6，中间节点为3或者4都可以，最好设定为4，
        那么此时slow = 4， fast = null（6的下一个），此时该停下，所以要判断 fast != null
2. 从mid开始，翻转mid之后的链表，并且记录结束节点end（不算null）
3. 循环同时移动end和head指针，条件为end != null
    因为我们在1.3的时候，设定4为中间节点，所以中间节点前面的链表是比中间节点后面的链表更长的，所以end会先到null
4. 当任意一个节点的值不一样时，返回false，否则循环退出后，返回true

 */
public class PalindromeLinkedList {
    /**
     * Definition for singly-linked list. public class ListNode { int val; ListNode next; ListNode()
     * {} ListNode(int val) { this.val = val; } ListNode(int val, ListNode next) { this.val = val;
     * this.next = next; } }
     */
    public boolean isPalindrome(ListNode head) {
        // Corner case
        if (head == null) return false;
        // 用快慢指针找到中间节点
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        // 中间节点
        ListNode mid = slow;
        // 翻转mid后的链表
        ListNode pre = null;
        ListNode cur = mid;
        while (cur != null) {
            ListNode curNext = cur.next;
            cur.next = pre;
            pre = cur;
            cur = curNext;
        }
        // 结束节点
        ListNode end = pre;
        // 同时遍历mid前的链表和翻转后的链表
        while (end != null) {
            if (head.val != end.val) {
                return false;
            }
            head = head.next;
            end = end.next;
        }
        return true;
    }
}
