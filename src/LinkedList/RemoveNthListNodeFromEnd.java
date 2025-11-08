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
    public ListNode removeNthFromEnd(ListNode head, int n) {
        // 创建两个指针，指向head
        ListNode fast = head;
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode slow = dummy;
        // 先让head走n步
        for (int i = 0; i < n; i++) {
            fast = fast.next;
        }
        // 因为有限制条件：1<=n<=链表长度，所以如果n=链表长度时，即head就是需要被移出的node，而此时head已经到了结束节点的下一个，即null
        // 所以我们需要判断
        // 除开上面的特殊情况，此时正常该做的事情是，因为fast已经走了n步，所以此时fast和slow一起走，最后当fast在null时，slow就在该节点上
        // 但是为了移出该节点，我们还需要知道该节点的上一个节点是什么，所以最好在最开始定义的时候，将slow定义为一个dummy节点，并使其指向head，
        // 即slow在fast的上一个
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        // 移出该节点，不需要担心slow是null，因为fast一定比slow先到null
        slow.next = slow.next.next;
        return dummy.next;
    }
}
