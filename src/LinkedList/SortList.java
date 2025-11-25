package LinkedList;

/*
https://leetcode.cn/problems/swap-nodes-in-pairs/description/?envType=study-plan-v2&envId=top-100-liked

Given the head of a linked list, return the list after sorting it in ascending order.

Example 1:
Input: head = [4,2,1,3]
Output: [1,2,3,4]

Example 2:
Input: head = [-1,5,3,4,0]
Output: [-1,0,3,4,5]

Example 3:
Input: head = []
Output: []

思路：分治
1. 先找到中点，记得slow和fast的初始位置，fast要在slow.next，不然的话，当分割长度为2的链表的时候，会无限循环
2. 找到以后，先获取right，right是sortList(mid.next)，获取完以后记得断开连接，即mid.next = null
3. 然后获取left，因为已经断开连接，所以直接sortList(head)
4. 合并left和right，只需要遍历两个链表，创建一个新的node作为新的链表的head，谁小就先排谁去head.next

 */
public class SortList {
    /**
     * Definition for singly-linked list. class ListNode { int val; ListNode next; ListNode(int x) {
     * val = x; next = null; } }
     */
    public ListNode sortList(ListNode head) {
        // 只剩一个或者当前为空，则返回head
        if (head == null || head.next == null) return null;
        // 找中位数
        ListNode mid = findMedian(head);
        /*
        分治中的分
        left = 从head到mid
        right = 从mid.next到结尾
         */
        // 注意：记得将链表从mid切断，而且需要先right后left，因为left从head开始且没有切断，相当于无限循环
        ListNode right = sortList(mid.next);
        mid.next = null;
        ListNode left = sortList(head);
        /*
        分治中的治
        将两个链表合起来
         */
        return mergeTwoLists(left, right);
    }

    private ListNode findMedian(ListNode node) {
        /*
        快慢指针找中节点
        如果是奇数[1, 2, 3] -> slow在2
        如果是偶数[1, 2, 3, 4] -> slow在2
        注意：这里fast必须从node.next开始，因为如果fast和slow都从根节点出发的话
        如果是奇数[1, 2, 3] -> slow会停在2，没问题
        如果是偶数[1, 2] -> slow会停在2
            相当于将[1,2]划分为left = [1, 2]和right = []
            带入sortList的逻辑，right会直接返回，left会无限循环
         */
        ListNode slow = node;
        ListNode fast = node.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    private ListNode mergeTwoLists(ListNode left, ListNode right) {
        // 创建一个新的链表的头
        ListNode node = new ListNode();
        ListNode dummy = node;
        // 遍历两条链表
        while (left != null && right != null) {
            // 通过比较，选择链接哪个节点
            if (left.val < right.val) {
                node.next = left;
                left = left.next;
            } else {
                node.next = right;
                right = right.next;
            }
            node = node.next;
        }
        // 连接剩余的节点
        node.next = left != null ? left : right;
        return dummy.next;
    }
}
