package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    private int[] myEdgeTo;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        myEdgeTo = new int[maze.V()];
        // assuming source is 0.
        myEdgeTo[0] = 0;
        distTo[0] = 0;
        marked[0] = true;
    }

    @Override
    public void solve() {
        dfs(0);
    }

    // Helper methods go here
    private void dfs(int s) {
        marked[s] = true;
        announce();
        for (int w : maze.adj(s)) {
            // reach the close point of circle.
            if (w != myEdgeTo[s] && marked[w]) {
                edgeTo[w] = s;
                // search backwards to create circle path.
                int cur, prev;
                cur = w;
                prev = myEdgeTo[w];
                while (prev != w) {
                    edgeTo[cur] = prev;
                    announce();
                    cur = prev;
                    prev = myEdgeTo[cur];
                }
                return;
            }
            if (!marked[w]) {
                myEdgeTo[w] = s;
                distTo[w] = distTo[s] + 1;
                dfs(w);
            }
        }
    }

}
