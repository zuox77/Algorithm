package LinkedList;

public class ListNode {
  protected int val;
  protected int key;
  protected ListNode next;

  ListNode() {}

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
