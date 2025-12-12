package Interview.Airbnb;

/*
Implement a class that supports the following operations:
set_value(key: str, value: int): Stores the integer value for the given key.
set_sum(key: str, values: List[str]): Defines key as the sum of the values associated with the keys in values.
get_value(key: str) -> int: Returns the integer value associated with key. If the key was defined using set_sum, return the computed sum.
node與node之間是有關聯的，要注意update dependencies，詳細例子如下:
set_value("A", 5): Sets A=5
set_value("B", 10): Sets B=10
set_sum("C", ["A", "B"]): C depends on A and B, so C=15
set_sum("D", ["C", "C", "A"]): D depends on C and A, so D=15+15+5=35
get_value("A"): Returns 5
set_value("B", 10): B already 10, no change needed
get_value("C"): Returns 15
get_value("D"): Returns 35
set_value("A", 100): A changes to 100, triggers updates:
C updates to 100+10=110
D updates to 110+110+100=320
get_value("D"): Returns 320

问清楚要Children里会不会有重复，有就只能用hash table，不然可以用set
以及需不需要做 input validation等等

follow-up
If get_value is not frequently use and set_value is frequently used, how to improve the time complexity for set_value?
答：把calculate sum放进get function，call一次就算一次，或者在额外加一个expire flag，有过修改的就重算一次
 */

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class NodeUpdate {

    public static class Node {
        String id;
        int value;
        Map<Node, Integer> dependencies;
        Map<Node, Integer> revDependencies;

        public Node (String id) {
            this.id = id;
            this.value = 0;
            this.dependencies = new HashMap<>();
            this.revDependencies = new HashMap<>();
        }

        public Node (String id, int value) {
            this.id = id;
            this.value = value;
            this.dependencies = new HashMap<>();
            this.revDependencies = new HashMap<>();
        }
    }

    private final Map<String, Node> nodeMap = new HashMap<>();

    public void setSum(String id, String[] ids) {
        Node root = nodeMap.getOrDefault(id, new Node(id));
        // Deduct root's frequency from all of its revDependencies nodes
        for (Node node: root.dependencies.keySet()) {
            // Deduct 1
            node.revDependencies.merge(node, -1, Integer::sum);
            // Remove if frequency is 1
            if (node.revDependencies.get(root) == 0) node.revDependencies.remove(root);
        }
        // Reset root value
        root.value = 0;
        // Add new dependencies
        for (String dependencyId: ids) {
            if (!nodeMap.containsKey(dependencyId)) return;
            // Get dependency node
            Node node = nodeMap.get(dependencyId);
            // Add node to root's dependency map
            root.dependencies.merge(node, 1, Integer::sum);
            // Add root to node's revDependency map
            node.revDependencies.merge(root, 1, Integer::sum);
            root.value += node.value;
        }
        nodeMap.put(id, root);
    }

    public void setValue(String id, int value) {
        // Update
        int diff = 0;
        Node root;
        if (nodeMap.containsKey(id)) {
            root = nodeMap.get(id);
            diff = value - root.value;
            // If no change
            if (diff == 0) return;
            root.value = value;
        } else {
            root = new Node(id, value);
        }
        nodeMap.put(id, root);
        // Using a queue to update all corresponding nodes
        Deque<Node> queue = new ArrayDeque<>();
        queue.offerFirst(root);
        while (!queue.isEmpty()) {
            Node curNode = queue.pollLast();
            // Traverse all
            for (Map.Entry<Node, Integer> entry: curNode.revDependencies.entrySet()) {
                entry.getKey().value += diff * entry.getValue();
                queue.addFirst(entry.getKey());
            }
        }
    }

    public int getValue(String id) {
        if (nodeMap.containsKey(id)) return nodeMap.get(id).value;
        return -1;
    }
}

/*
        NodeUpdate test = new NodeUpdate();
        test.setValue("A", 5);
        test.setValue("B", 10);
        test.setSum("C", new String[]{"A", "B"});
        test.setSum("D", new String[]{"C", "C", "A"});
        System.out.println("C: " + test.getValue("C"));
        System.out.println("D: " + test.getValue("D"));
        test.setValue("A", 100);
        System.out.println("C: " + test.getValue("C"));
        System.out.println("D: " + test.getValue("D"));
        test.setSum("C", new String[]{"A", "D"});
        System.out.println("C: " + test.getValue("C"));
        System.out.println("D: " + test.getValue("D"));
        test.setValue("B", 15);
        System.out.println("C: " + test.getValue("C"));
 */
