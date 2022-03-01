package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Solver {
    private class searchNode implements Comparable {
        WorldState cur;
        int dist;
        searchNode prev;

        public searchNode(WorldState c, int dist, searchNode prev) {
           cur = c;
           this.dist = dist;
           this.prev = prev;
        }

        // enable priority queue works in A* algorithm
        @Override
        public int compareTo(Object o) {
            return Integer.compare(dist + cur.estimatedDistanceToGoal(),
                    ((searchNode) o).dist + ((searchNode) o).cur.estimatedDistanceToGoal());
        }
    }
    private MinPQ<searchNode> pq;
    private Set<searchNode> pqHelper;
    private searchNode start;
    private Map<String, Integer> nodeDist;              // store every node's distance
    public static int enqueueCountMonitor;

    /*
    * Constructor which solves the puzzle, computing
    * everything necessary for moves() and solution() to
    * not have to solve the problem again. Solves the
    * puzzle using the A* algorithm. Assumes a solution exists.
     */
    public Solver(WorldState initial) {
        enqueueCountMonitor = 0;
        start = new searchNode(initial, 0, null);
        nodeDist = new HashMap<>();
        // map start distance to 0
        nodeDist.put(start.cur.toString(), 0);
        pq = new MinPQ<>();
        pqHelper = new HashSet<>();
        pq.insert(start);
        pqHelper.add(start);
        enqueueCountMonitor += 1;
        while (!pq.isEmpty()) {
            start = pq.delMin();
            pqHelper.remove(start);
            if (start.cur.isGoal()) {
                return;
            }
            // search around edges
            for (WorldState item : start.cur.neighbors()) {
                searchNode node = new searchNode(item, start.dist + 1, start);
                relax(node);
            }
        }
    }

    private void relax(searchNode node) {
        if (!nodeDist.containsKey(node.cur.toString())
                || nodeDist.get(node.cur.toString()) > distance(node)) {
            // update distance of the node
            nodeDist.put(node.cur.toString(), distance(node));
            if (pqHelper.contains(node)) {
                pq = decreaseKey(node);
            } else {
                if (node.cur.equals(node.prev.cur)) {
                    return;
                }
                pq.insert(node);
                pqHelper.add(node);
                enqueueCountMonitor += 1;
            }
        }
    }

    private MinPQ<searchNode> decreaseKey(searchNode node) {
        MinPQ<searchNode> tmp = new MinPQ<>();
        tmp.insert(node);
        enqueueCountMonitor += 1;
        while (!pq.isEmpty()) {
            searchNode item = pq.delMin();
            if (!item.cur.equals(node.cur)) {
                tmp.insert(item);
                enqueueCountMonitor += 1;
            }
        }
        return tmp;
    }

    private int distance(searchNode node) {
        return node.dist + node.cur.estimatedDistanceToGoal();
    }

    /*
    * Returns the minimum number of moves to solve the puzzle starting
    * at the initial WorldState.
     */
    public int moves() {
        int minMoves = 0;
        searchNode tail = start;
        while (tail.prev != null) {
            minMoves += 1;
            tail = tail.prev;
        }
        return minMoves;
    }

    /*
    * Returns a sequence of WorldStates from the initial WorldState
    * to the solution.
     */
    public Iterable<WorldState> solution() {
        Stack<WorldState> ret = new Stack<>();
        searchNode tail = start;
        while (tail != null) {
            ret.push(tail.cur);
            tail = tail.prev;
        }
        return ret;
    }
}
