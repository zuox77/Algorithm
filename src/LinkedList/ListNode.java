package LinkedList;

public class ListNode {
    public int val;
    public int key;
    public ListNode next;

    public ListNode() {}

    ListNode(int val) {
        this.val = val;
        this.next = null;
    }

    ListNode(int val, int key) {
        this.val = val;
        this.key = key;
        this.next = null;
    }

    ListNode(int val, ListNode node) {
        this.val = val;
        this.next = node;
    }
}
