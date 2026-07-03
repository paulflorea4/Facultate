package org.example.laboratorGUI.utils;

import java.util.*;

public class GraphUtils {
    /**
     * Depth-first search used internally to traverse communities.
     *
     * @param start starting user ID
     * @param adj adjacency list
     * @param visited set of visited user IDs
     */
    public static void dfs(Long start, Map<Long, List<Long>> adj, Set<Long> visited) {
        Stack<Long> st = new Stack<>();
        st.push(start);
        while (!st.isEmpty()) {
            Long u = st.pop();
            if (!visited.contains(u)) {
                visited.add(u);
                for (Long v : adj.getOrDefault(u, List.of())) {
                    if (!visited.contains(v))
                        st.push(v);
                }
            }
        }
    }

    /**
     * Collects all users in a connected component using BFS.
     *
     * @param start starting user ID
     * @param adj adjacency list
     * @param comp list to store component members
     * @param visited set of visited user IDs
     */
    public static void collectComponent(Long start, Map<Long, List<Long>> adj, List<Long> comp, Set<Long> visited) {
        Queue<Long> q = new LinkedList<>();
        q.add(start);
        visited.add(start);
        while (!q.isEmpty()) {
            Long u = q.poll();
            comp.add(u);
            for (Long v : adj.getOrDefault(u, List.of())) {
                if (!visited.contains(v)) {
                    visited.add(v);
                    q.add(v);
                }
            }
        }
    }

    /**
     * Computes the diameter (the longest shortest path) of a component.
     *
     * @param comp list of user IDs in the component
     * @param adj adjacency list
     * @return the diameter of the component
     */
    public static int computeDiameter(List<Long> comp, Map<Long, List<Long>> adj) {
        int diameter = 0;
        Set<Long> compSet = new HashSet<>(comp);
        for (Long u : comp) {
            Map<Long, Integer> dist = bfsDistances(u, adj, compSet);
            int localMax = dist.values().stream().mapToInt(Integer::intValue).max().orElse(0);
            diameter = Math.max(diameter, localMax);
        }
        return diameter;
    }

    /**
     * Performs BFS from a source to compute distances to all reachable nodes in the component.
     *
     * @param source starting user ID
     * @param adj adjacency list
     * @param compSet set of user IDs in the component
     * @return a map from user ID to distance from the source
     */
    private static Map<Long, Integer> bfsDistances(Long source, Map<Long, List<Long>> adj, Set<Long> compSet) {
        Map<Long, Integer> dist = new HashMap<>();
        Queue<Long> q = new LinkedList<>();
        q.add(source);
        dist.put(source, 0);
        while (!q.isEmpty()) {
            Long u = q.poll();
            for (Long v : adj.getOrDefault(u, List.of())) {
                if (compSet.contains(v) && !dist.containsKey(v)) {
                    dist.put(v, dist.get(u) + 1);
                    q.add(v);
                }
            }
        }
        return dist;
    }
}
