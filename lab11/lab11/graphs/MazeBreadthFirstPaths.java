package lab11.graphs;

import java.util.LinkedList;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    private int s;
    private int t;
    private Queue<Integer> bfsQueue;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        bfsQueue = new LinkedList<>();

        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        marked[s] = true;
        bfsQueue.offer(s);
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        while (!bfsQueue.isEmpty()) {
            int curPos = bfsQueue.poll();
            // mark must after remove/delete.
            marked[curPos] = true;
            announce();
            if (curPos == t) {
                return;
            }
            for (int neighbour : maze.adj(curPos)) {
                if (!marked[neighbour]) {
                    bfsQueue.offer(neighbour);
                    edgeTo[neighbour] = curPos;
                    distTo[neighbour] = distTo[curPos] + 1;
                    announce();
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs();
    }
}

