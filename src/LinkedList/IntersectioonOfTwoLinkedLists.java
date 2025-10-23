package LinkedList;

/*
https://leetcode.cn/problems/intersection-of-two-linked-lists/description/?envType=study-plan-v2&envId=top-100-liked

Given the heads of two singly linked-lists headA and headB, return the node at which the two lists intersect.
If the two linked lists have no intersection at all, return null.

Do not modify the linked list.

思路: 循环法
1. 核心思路：如果a和b相交，因为无法判断在相交前，A和B分别有多长，所以只需要当A和B分别遍历到null的时候，再去遍历一遍对方的链表，
    那么此时两个指针走过的一定相等，且一定会在相交节点相遇
    而如果a和b不相交，但其最终的节点都会指向null，所以只需要最后等待a和b分别走完自己的链表，他们终会在null相遇，然后返回null即可

 */
public class IntersectioonOfTwoLinkedLists {
    /**
     * Definition for singly-linked list. public class ListNode { int val; ListNode next;
     * ListNode(int x) { val = x; next = null; } }
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode a = headA;
        ListNode b = headB;
        while (a != b) {
            a = a == null ? headB : a.next;
            b = b == null ? headA : b.next;
        }
        return a;
    }
}
