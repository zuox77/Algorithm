package LinkedList;

/*
https://leetcode.cn/problems/merge-two-sorted-lists/description/?envType=study-plan-v2&envId=top-100-liked

You are given the heads of two sorted linked lists list1 and list2.
Merge the two lists into one sorted list. The list should be made by splicing together the nodes of the first two lists.
Return the head of the merged linked list.

Example 1:
Input: list1 = [1,2,4], list2 = [1,3,4]
Output: [1,1,2,3,4,4]

Example 2:
Input: list1 = [], list2 = []
Output: []

Example 3:
Input: list1 = [], list2 = [0]
Output: [0]

思路:
1. 创建一个新的指针，指向dummy node，这就是之后的新链表的起点head
2. 再创建一个指针，指向head，因为head在之后的操作中会移动，所以还需要一个指针保存根节点
3. 创建两个指针，分别指向两个链表的根节点
4. 同时遍历两个指针，保证他们都不为null，然后比较，谁小就把谁加入新链表
5. 退出循环后，有可能这两个链表长度不一样，所以还需要再来一个循环把长的链表剩余的节点都加进去
    但其实这里是两个循环，这样写起来简单一些，不需要考虑究竟哪个链表还有剩余节点

 */
public class MergeTwoSortedLists {
    /**
     * Definition for singly-linked list. public class ListNode { int val; ListNode next; ListNode()
     * {} ListNode(int val) { this.val = val; } ListNode(int val, ListNode next) { this.val = val;
     * this.next = next; } }
     */
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        // 创建一个新的指针，代表新的链表
        ListNode head = new ListNode(-1);
        // 创建另一个指针保存答案，因为head是会移动的，所以还需要一个指针去保存答案
        ListNode ans = head;
        // 创建两个指针，分别指向两个链表
        ListNode left = list1;
        ListNode right = list2;
        // 循环移动指针，将val小的放入新的链表，并移动
        while (left != null && right != null) {
            if (left.val <= right.val) {
                head.next = left;
                left = left.next;
            } else {
                head.next = right;
                right = right.next;
            }
            head = head.next;
        }
        // 将剩余的node加入
        while (left != null) {
            head.next = left;
            left = left.next;
            head = head.next;
        }
        while (right != null) {
            head.next = right;
            right = right.next;
            head = head.next;
        }
        // 返回ans.next，因为head是一个自定义的不存在的节点
        return ans.next;
    }
}
