package lab11.graphs;

import edu.princeton.cs.algs4.MinPQ;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    private MinPQ<Vertex> pq;

    private class Vertex implements Comparable {
        int s;
        int dist;

        Vertex(int s, int dist) {
            this.s = s;
            this.dist = dist;
        }

        @Override
        public int compareTo(Object o) {
            return Integer.compare(this.dist + h(this.s), ((Vertex) o).dist + h(((Vertex) o).s));
        }

    }

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        pq = new MinPQ<>();
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(v) - maze.toX(t)) + Math.abs(maze.toY(v) - maze.toY(t));
    }

    /** Finds Vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from Vertex s. */
    private void astar(int ver) {
        pq.insert(new Vertex(ver, distTo[ver]));
        while (!pq.isEmpty()) {
            Vertex cur = pq.delMin();
            marked[cur.s] = true;
            announce();
            if (cur.s == t) {
                return;
            }
            for (int w : maze.adj(cur.s)) {
                if (!marked[w]) {
                    distTo[w] = cur.dist + 1;
                    edgeTo[w] = cur.s;
                    pq.insert(new Vertex(w, distTo[w]));
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

