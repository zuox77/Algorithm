package LinkedList;

public class Node {
    protected int val;
    protected Node next;

    Node() {
    }

    Node(int val) {
        this.val = val;
        this.next = null;
    }

    Node(int val, Node node) {
        this.val = val;
        this.next = node;
    }
}
