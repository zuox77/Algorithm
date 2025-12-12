package Interview.Airwallex;

/*
/**
 * We have 8 vertices and 5 edges (undirected) which can be represented by the below terms:
 * vertices = [1,2,3,4,5,6,7,8]
 * edges = [[1,2],[2,3],[2,4],[6,7],[7,8]]
 *
 * Find all connected vertices
 * You can assume all vertices values are unique integer.
 * For the example in graph, the expected result is:
 * connected_vertices = [[1,2,3,4],[5],[6,7,8]]
 * (Difference in element order is allowed)
 *
 *
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UndirectedGraph {
    public static List<List<Integer>> getConnected2(int[] vertices, int[][] edges) {
        int n = vertices.length;
        List<Integer>[]connection = new List[n + 1];
        Arrays.setAll(connection, i -> new ArrayList<>());

        for (int[] edge: edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            connection[node1].add(node2);
            connection[node2].add(node1);
        }

        Set<Integer> visited = new HashSet<>();
        Set<Integer> path = new HashSet<>();
        Deque<Integer> queue = new ArrayDeque<>();
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if (!visited.add(i)) continue;
            path.clear();
            path.add(i);
            queue.addFirst(i);
            while (!queue.isEmpty()) {
                for (int child : connection[queue.pollLast()]) {
                    if (path.add(child)) queue.addFirst(child);
                }
            visited.addAll(path);
            result.add(new ArrayList<>(path));
            }
        }
        return result;
    }
}

/*
        int[] vertices = {1,2,3,4,5,6,7,8};
        int[][] edges = {{1,2}, {2,1}, {2,3},{2,4},{6,7},{7,8}};
        System.out.println(UndirectedGraph.getConnected2(vertices, edges));
 */