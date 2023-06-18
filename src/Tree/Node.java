package Tree;

public class Node {
    int val; // 这些变量都不能是private, 因为在其他类里面就会无法访问
    Node left;
    Node right;
    public Node() {}
    public Node(int val) {
        this.val = val;
    }
    public Node(int val, Node left, Node right) {
        this.val = val;
        this.left = left;
        this.right = right;
        System.out.println();
    }
}
