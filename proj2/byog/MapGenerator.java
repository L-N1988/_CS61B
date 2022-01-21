package byog;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;
import java.util.LinkedList;

public class MapGenerator {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 60;

    private static final long SEED = 2213;
    private static final Random RANDOM = new Random(SEED);

    private static class Position {
        int x;
        int y;

        public Position(int X, int Y) {
            x = X;
            y = Y;
        }
    }
    private static class Room {
        Position start;
        int width;
        int height;
        public Room(Position o, int W, int H) {
            start = o;
            width = W;
            height = H;
        }
    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] randomRooms = new TETile[WIDTH][HEIGHT];
        TETile[][] world = new TETile[WIDTH][HEIGHT];

        LinkedList<Room> roomList;
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.WALL;
                randomRooms[x][y] = Tileset.NOTHING;
            }
        }
        initialWorld(world);
        roomList = generateRooms(randomRooms);
        mergeMaps(world, randomRooms);

        // dfs route in world map.
        generateHallway(world);
        // draws the world to the screen
        ter.renderFrame(world);
    }

    private static void generateHallway(TETile[][] world) {
        Position origin = pickOrigin();
        LinkedList<Position> posList = new LinkedList<>();
        posList.addLast(origin);
        world[origin.x][origin.y] = Tileset.FLOOR;
        // default value is false.
        boolean[][] visited = new boolean[WIDTH][HEIGHT];
        dfsHallway(posList, world, origin, visited);
    }

    private static void dfsHallway(LinkedList<Position> posList, TETile[][] world, Position curPos, boolean[][] visited) {
        if (posList.isEmpty()) {
            return;
        }
        Position nextPos = null;
        int randDir = RANDOM.nextInt(4);
        switch (randDir) {
            case 0 -> nextPos = new Position(curPos.x, curPos.y + 2);
            case 1 -> nextPos = new Position(curPos.x, curPos.y - 2);
            case 2 -> nextPos = new Position(curPos.x - 2, curPos.y);
            case 3 -> nextPos = new Position(curPos.x + 2, curPos.y);
            default -> {
            }
        }
        if (inRoom(nextPos, new Room(new Position(-1, -1), WIDTH, HEIGHT)) && !visited[nextPos.x][nextPos.y]) {
            visited[nextPos.x][nextPos.y] = true;
            posList.addLast(nextPos);
            dfsHallway(posList, world, nextPos, visited);
        } else {
            while (isDeadEnd(curPos, visited) && !posList.isEmpty()) {
                Position prevPos = posList.getLast();
                posList.removeLast();
                world[curPos.x][curPos.y] = Tileset.FLOOR;
                world[prevPos.x][prevPos.y] = Tileset.FLOOR;
                world[(curPos.x + prevPos.x) / 2][(curPos.y + prevPos.y) / 2] = Tileset.FLOOR;
                curPos = prevPos;
            }
            dfsHallway(posList, world, curPos, visited);
        }
    }

    private static boolean isDeadEnd(Position curPos, boolean[][] visited) {
        return (!inRoom(new Position(curPos.x, curPos.y + 2), new Room(new Position(-1, -1), WIDTH, HEIGHT))
                        || visited[curPos.x][curPos.y + 2]) &&
                (!inRoom(new Position(curPos.x, curPos.y - 2), new Room(new Position(-1, -1), WIDTH, HEIGHT))
                        || visited[curPos.x][curPos.y - 2]) &&
                (!inRoom(new Position(curPos.x + 2, curPos.y), new Room(new Position(-1, -1), WIDTH, HEIGHT))
                        || visited[curPos.x + 2][curPos.y]) &&
                (!inRoom(new Position(curPos.x - 2, curPos.y), new Room(new Position(-1, -1), WIDTH, HEIGHT))
                        || visited[curPos.x - 2][curPos.y]);
    }

    private static Position pickOrigin() {
        int x = RANDOM.nextInt(WIDTH);
        int y = RANDOM.nextInt(HEIGHT);
        x = (x % 2 == 0) ? x : x - 1;
        y = (y % 2 == 0) ? y : y - 1;
        return new Position(x, y);
    }

    private static void initialWorld(TETile[][] world) {
        for (int i = 0; i < WIDTH; i += 2) {
            for (int j = 0; j < HEIGHT; j += 2) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    public static boolean inRoom(Position start, Room curRoom) {
        return start.x > curRoom.start.x && start.x < curRoom.start.x + curRoom.width + 1
                && start.y > curRoom.start.y && start.y < curRoom.start.y + curRoom.height + 1;
    }

    public static LinkedList<Room> generateRooms(TETile[][] tiles) {
        int maxRooms = 100;
        LinkedList<Room> roomList = new LinkedList<>();
        for (int i = 0; i < maxRooms; i++) {
            Position start = new Position(RANDOM.nextInt(WIDTH), RANDOM.nextInt(HEIGHT));
            int w = RANDOM.nextInt(WIDTH / 10) + 2;
            int h = RANDOM.nextInt(HEIGHT / 10) + 2;
            start.x = (start.x % 2 == 1) ? start.x : start.x + 1;
            start.y = (start.y % 2 == 1) ? start.y : start.y + 1;
            w = (w % 2 == 0) ? w - 1: w;
            h = (h % 2 == 0) ? h - 1: h;
            if (inBound(start, w, h) && !isOverlap(tiles, start, w, h)) {
                fillRooms(tiles, start, w, h);
                roomList.addLast(new Room(start, w, h));
            }
        }
        return  roomList;
    }

    public static boolean inBound(Position start, int w, int h) {
        return start.x > 0 && start.x + w + 1 < WIDTH && start.y > 0 && start.y + h + 1 < HEIGHT;
    }

    public static boolean isOverlap(TETile[][] tiles, Position start, int w, int h) {
        for (int i = start.x; i < start.x + w + 2; i++) {
            for (int j = start.y; j < start.y + h + 2; j++) {
                if (tiles[i][j].equals(Tileset.WALL)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void fillRooms(TETile[][] tiles, Position start, int w, int h) {
        for (int i = start.x; i < start.x + w + 2; i++) {
            for (int j = start.y; j < start.y + h + 2; j++) {
                if (i == start.x || i == start.x + w + 1 || j == start.y || j == start.y + h + 1) {
                    tiles[i][j] = Tileset.WALL;
                } else {
                    tiles[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    public static void mergeMaps(TETile[][] tiles, TETile[][] rooms) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (!rooms[i][j].equals(Tileset.NOTHING)) {
                    tiles[i][j] = rooms[i][j];
                }
            }
        }
    }
}
