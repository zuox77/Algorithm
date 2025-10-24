package LinkedList;

/*
https://leetcode.cn/problems/remove-nth-ListNode-from-end-of-list/description/?favorite=2cktkvj

思路: 快慢指针
1. 要建一个dummy, dummy指向head, 目的是: 1. 方便找到head 2. 不需要特殊对待head
2. 快慢指针的初始位置分别是慢->dummy和快->head, 这样如果head是需要被移出的ListNode, 就不会找不到head前面的ListNode
3. 将快指针先往前走n步, 然后快慢指针一起往前走: for(int i = 0; i < n; i++)
4. 快慢指针最后停下来的位置应该是, 慢指针在, 需要移出的ListNode的前一个, 快指针在null停下: while (fast != null)
 */
public class RemoveNthListNodeFromEnd {
    public ListNode solution1(ListNode head, int n) {
        ListNode dummy = new ListNode();
        dummy.next = head;
        ListNode slow = dummy, fast = head;
        for (int i = 0; i < n; i++) {
            fast = fast.next;
        }
        while (fast != null) {
            slow = slow.next;
            fast = fast.next;
        }
        slow.next = slow.next.next;
        return dummy.next;
    }
}
