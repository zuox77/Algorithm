import Tree.TreeNode;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        sb.append(root.val);
        dfs(root, sb);
        return sb.toString();
    }

    private void dfs(TreeNode root, StringBuilder sb) {
        // 退出条件
        if (root == null) return;
        // 添加进sb
        sb.append(',');
        sb.append(root.val);
        // 继续下一层
        dfs(root.left, sb);
        dfs(root.right, sb);
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {}
}
