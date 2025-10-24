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
4. 同时遍历两个指针，保证他们都不为null，然后相加算余数，并创建新的node加入新的链表
    注意：这道题和MergeTwoSortedList不一样，那道题的循环条件是left != null && right != null，然后再各自遍历直到结束节点
    但如果此题也用同样的方法会很麻烦
    1. 如果我们不用新的链表，直接在l上面修改，那么如果l1更短，就要分情况讨论，当l1遍历结束以后，需要创建新的node，l1更长则不用
    2. 就算用新的链表，还要考虑一个问题，当遍历到最后一位时，如果余数还是不为0，那么还需要进一位，所以还需要额外创建一个node，
        但是一般while循环的退出条件会导致，退出时，刚好处于null，我们没法直接null.next = new ListNode(remaining)，所以
        1. 要么我们修改while循环的条件使其在null前一个就退出，然后额外写同样的代码去计算最后一轮，然后再判断
        2. 或者要么再找个指针，每次记录上一个
        两种都很麻烦
        不如像下面的方式一样，直接把绝对退出条件当成while循环的条件，然后再循环里面分情况判断
5. 退出循环后，有可能这两个链表长度不一样，所以还需要再来一个循环把长的链表剩余的节点都加进去
    但其实这里是两个循环，这样写起来简单一些，不需要考虑究竟哪个链表还有剩余节点

 */
public class AddTwoNumbers {
    /**
     * Definition for singly-linked list. public class ListNode { int val; ListNode next; ListNode()
     * {} ListNode(int val) { this.val = val; } ListNode(int val, ListNode next) { this.val = val;
     * this.next = next; } }
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        // 创建一个新的链表
        ListNode head = new ListNode(-1);
        ListNode ans = head;
        // 创建一个int记录上一轮的余数，也就是会被加入这一轮计算的数
        int carry = 0;
        // 遍历去增加新的链表的node
        while (l1 != null || l2 != null || carry != 0) {
            // 如果l1还没遍历完
            if (l1 != null) {
                carry += l1.val;
                l1 = l1.next;
            }
            // 如果l2还没遍历完
            if (l2 != null) {
                carry += l2.val;
                l2 = l2.next;
            }
            // 添加新的node，移动head
            head = head.next = new ListNode(carry % 10);
            carry /= 10;
        }
        return ans.next;
    }
}
