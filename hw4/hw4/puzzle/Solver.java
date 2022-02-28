package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
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

        @Override
        public int compareTo(Object o) {
            return Integer.compare(dist, ((searchNode) o).dist);
        }
    }
    private searchNode start;
    private searchNode prev;
    private MinPQ<searchNode> sol;
    private Set<WorldState> pathSet;

    /*
    * Constructor which solves the puzzle, computing
    * everything necessary for moves() and solution() to
    * not have to solve the problem again. Solves the
    * puzzle using the A* algorithm. Assumes a solution exists.
     */
    public Solver(WorldState initial) {
        start = new searchNode(initial, 0, null);
        sol = new MinPQ<>();
        pathSet = new HashSet<>();
        sol.insert(start);
        int curDist = 0;
        while (!sol.isEmpty()) {
            start = sol.delMin();
            pathSet.add(start.cur);
            if (start.cur.isGoal()) {
                System.out.println();
                return;
            }
            MinPQ<searchNode> tmpPQ = new MinPQ<>();
            for (WorldState item : start.cur.neighbors()) {
                searchNode node = new searchNode(item, curDist + item.estimatedDistanceToGoal(), start);
                if (!pathSet.contains(item)) {
                    tmpPQ.insert(node);
                }
            }
            searchNode next = tmpPQ.delMin();
            if (start.prev != null) {
                for (WorldState prevItem : start.prev.cur.neighbors()) {
                    if (prevItem.equals(next.cur)) {
                        pathSet.remove(start.cur);
                        curDist -= 1;
                    }
                }
            }
            sol.insert(next);
            System.out.println(next.cur + " " + next.cur.estimatedDistanceToGoal());
            curDist += 1;
        }
    }

    /*
    * Returns the minimum number of moves to solve the puzzle starting
    * at the initial WorldState.
     */
    public int moves() {
        return pathSet.size() - 1;
    }

    /*
    * Returns a sequence of WorldStates from the initial WorldState
    * to the solution.
     */
    public Iterable<WorldState> solution() {
        return pathSet;
    }
}
