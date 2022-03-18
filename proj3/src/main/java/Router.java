
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        LinkedList<Long> pathNodes = new LinkedList<>();
        GraphDB.Node start = g.getNode(Long.toString(g.closest(stlon, stlat)));
        GraphDB.Node dest = g.getNode(Long.toString(g.closest(destlon, destlat)));
        PriorityQueue<GraphDB.Node> pq = new PriorityQueue<>();
        GraphDB.Node cur = null;
        resetAllNodes(g, dest);
        pq.add(start);
        // set start distance to zero
        g.addEdge(null, g.getNodeID(start), 0);
        while (!pq.isEmpty()) {
            cur = pq.poll();
            String curID = g.getNodeID(cur);
            if (cur.equals(dest)) {
                break;
            }
            for (Long id : g.adjacent(Long.parseLong(g.getNodeID(cur)))) {
                relax(g, Long.toString(id), curID, pq);
            }
        }
        while (cur != null) {
            pathNodes.addFirst(Long.parseLong(g.getNodeID(cur)));
            cur = cur.prev;
        }
        return pathNodes;
    }

    /**
     * reset all nodes distance to infinity, set all previous node to null
     * and calculate all heuristic distance to destination
     */
    private static void resetAllNodes(GraphDB g, GraphDB.Node dest) {
        for (Long id : g.vertices()) {
            g.addEdge(null, Long.toString(id), Double.MAX_VALUE);
            g.getNode(Long.toString(id)).heuristic =
                    g.distance(id, Long.parseLong(g.getNodeID(dest)));
        }
    }

    private static void relax(GraphDB g, String nextID, String curID,
                              PriorityQueue<GraphDB.Node> pq) {
        double newDist = g.getDist(curID)
                + g.distance(Long.parseLong(nextID), Long.parseLong(curID));
        if (g.getDist(nextID) > newDist) {
            // if priority queue does not contain next node, do nothing
            // otherwise, pop out the node to decrease its value
            pq.remove(g.getNode(nextID));
            // decrease previous distance (infinity or finite) to next node
            g.addEdge(curID, nextID, newDist);
            // insert node with new distance
            pq.add(g.getNode(nextID));
        }
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        LinkedList<NavigationDirection> L = new LinkedList<>();
        NavigationDirection cur = new NavigationDirection();
        cur.direction = NavigationDirection.START;
        cur.distance += g.distance(route.get(0), route.get(1));
        cur.way = currentWayName(route.get(0), route.get(1), g);
        for (int i = 1; i < route.size() - 1; i++) {
            // reach the turning point
            if (!currentWayName(route.get(i), route.get(i + 1), g).equals(cur.way)) {
                // System.out.println(g.lon(route.get(i)) + " " + g.lat(route.get(i)));
                L.add(cur);
                System.out.println(g.getNodeID(g.getNode(Long.toString(route.get(i)))));
                // search next way
                cur = new NavigationDirection();
                double prevBearing = g.bearing(route.get(i - 1), route.get(i));
                double curBearing = g.bearing(route.get(i), route.get(i + 1));
                cur.direction = getDir(curBearing - prevBearing);
                // default distance is 0.0, therefore it restarts calculate distance
                cur.distance += g.distance(route.get(i), route.get(i + 1));
                cur.way = currentWayName(route.get(i), route.get(i + 1), g);
                // do not calculate distance again
                continue;
            }
            cur.distance += g.distance(route.get(i), route.get(i + 1));
        }
        L.add(cur);
        return L;
    }

    // must get name from id, instead of store way name directly
    // because two node can share same way name, but has different way id
    // aka, the way is broken to two parts in some sense
    private static String currentWayName(Long cur, Long next, GraphDB g) {
        return g.getWayName(currentWayID(cur, next, g));
    }

    private static String currentWayID(Long cur, Long next, GraphDB g) {
        LinkedList<String> curWayIDs = g.getWayID(cur);
        LinkedList<String> nextWayIDs = g.getWayID(next);
        // assuming all street's turning points are recoded
        for (String wayID : curWayIDs) {
            if (nextWayIDs.contains(wayID)) {
                return wayID;
            }
        }
        return "";
    }

    // get direction basing on relative bearing
    private static int getDir(double bearing) {
        if (bearing < -180.0) {
            bearing += 360.0;
        } else if (bearing > 180) {
            bearing -= 360.0;
        }
        if (bearing >= -15 && bearing <= 15) {
            return NavigationDirection.STRAIGHT;
        } else if (bearing >= -30 && bearing <= 30) {
            if (bearing < 0) {
                return NavigationDirection.SLIGHT_LEFT;
            } else {
                return NavigationDirection.SLIGHT_RIGHT;
            }
        } else if (bearing >= -100 && bearing <= 100) {
            if (bearing < 0) {
                return NavigationDirection.LEFT;
            } else {
                return NavigationDirection.RIGHT;
            }
        } else {
            if (bearing < 0) {
                return NavigationDirection.SHARP_LEFT;
            } else {
                return NavigationDirection.SHARP_RIGHT;
            }
        }
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
