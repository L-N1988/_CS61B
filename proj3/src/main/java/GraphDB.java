import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Graph for storing all the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    // chaining list to represent graph, string is node's id, list is connected nodes
    private final Map<String, LinkedList<Node>> graph = new HashMap<>();
    // store all node's info, including id, lon, lat, location
    private final Map<String, Node> nodes = new HashMap<>();
    // store all way's info, including nodes, name, highway, max speed?
    private final Map<String, Way> ways = new HashMap<>();

    static class Node implements Comparable {
        private final String id;
        private final String lat;
        private final String lon;
        private String location;
        private final LinkedList<String> wayID;
        Node prev;
        double distTo;
        double heuristic;

        Node(String id, String lat, String lon, double distTo) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            location = null;
            prev = null;
            this.distTo = distTo;
            heuristic = Double.MAX_VALUE;
            wayID = new LinkedList<>();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            Node other = (Node) o;
            return this.id.equals(other.id)
                    && this.lat.equals(other.lat) && this.lon.equals(other.lon);
        }

        @Override
        public int hashCode() {
            return this.id.hashCode();
        }

        @Override
        public int compareTo(Object o) {
            Node other = (Node) o;
            return Double.compare(this.distTo + this.heuristic, other.distTo + other.heuristic);
        }
    }

    static class Way {
        private LinkedList<Node> nodes;
        private String wayID;
        private String name;
        private String highway;
        private String maxSpeed;

        Way(LinkedList<Node> nd, String wayID,
                   String name, String highway, String maxSpeed) {
            nodes = nd;
            this.wayID = wayID;
            this.name = name;
            this.highway = highway;
            this.maxSpeed = maxSpeed;
        }
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    public void addNode(Node n) {
        nodes.put(n.id, n);
    }

    public Node getNode(String id) {
        return nodes.get(id);
    }

    public String getNodeID(Node n) {
        return n.id;
    }

    public void addWay(String id, LinkedList<Node> way) {
        graph.put(id, way);
    }

    public void addEdge(String curId, String nextID, double distTo) {
        Node next = getNode(nextID);
        next.prev = getNode(curId);
        next.distTo = distTo;
    }

    public void addRecordedWay(LinkedList<Node> nd, String wayID,
                               String name, String highway, String maxSpeed) {
        ways.put(wayID, new Way(nd, wayID, name, highway, maxSpeed));
        for (Node item : nd) {
            if (!item.wayID.contains(wayID)) {
                item.wayID.add(wayID);
            }
        }
    }

    // return ways' id, which cross the node, id is node's id
    public LinkedList<String> getWayID(long id) {
        return getNode(Long.toString(id)).wayID;
    }

    public String getWayName(String wayID) {
        return ways.get(wayID).name;
    }

    public LinkedList<Node> getWayNode(String wayID) {
        return ways.get(wayID).nodes;
    }

    public void addLocation(Node n, String loc) {
        n.location = loc;
    }

    public double getDist(String id) {
        return getNode(id).distTo;
    }

    public LinkedList<Node> getWay(String id) {
        return graph.get(id);
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        LinkedList<String> toBeRemoved = new LinkedList<>();
        for (Map.Entry<String, Node> item : nodes.entrySet()) {
            if (graph.get(item.getKey()).isEmpty()) {
                toBeRemoved.add(item.getKey());
            }
        }
        for (String id : toBeRemoved) {
            nodes.remove(id);
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        return nodes.keySet().stream().map(s -> Long.parseLong(s)).collect(Collectors.toSet());
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        LinkedList<Node> adj = graph.get(Long.toString(v));
        LinkedList<Long> retAdj = new LinkedList<>();
        for (Node item : adj) {
            retAdj.add(Long.parseLong(getNodeID(item)));
        }
        return retAdj;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        long closestID = 0;
        double dist = Double.MAX_VALUE;
        for (Long id : vertices()) {
            if (distance(lon(id), lat(id), lon, lat) < dist) {
                closestID = id;
                dist = distance(lon(id), lat(id), lon, lat);
            }
        }
        return closestID;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return Double.parseDouble(nodes.get(Long.toString(v)).lon);
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return Double.parseDouble(nodes.get(Long.toString(v)).lat);
    }
}
