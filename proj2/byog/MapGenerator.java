package byog;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Arrays;
import java.util.Random;
import java.util.LinkedList;

public class MapGenerator {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 60;

    private static final long SEED = 22213;
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

        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.WALL;
                randomRooms[x][y] = Tileset.NOTHING;
            }
        }
        initialWorld(world);
        generateRooms(randomRooms);
        mergeMaps(world, randomRooms);

        // dfs route in world map.
        generateHallway(world);
        // smooth map.
        smoothMap(world);
        // draws the world to the screen
        ter.renderFrame(world);
    }

    private static void smoothMap(TETile[][] world) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (world[i][j].equals(Tileset.WATER)) {
                    world[i][j] = Tileset.WALL;
                }
            }
        }
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (!boundWAll(new Position(i, j), world)) {
                    world[i][j] = Tileset.NOTHING;
                }
            }
        }
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (isOuter(new Position(i, j), world)) {
                    world[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    private static boolean isOuter(Position position, TETile[][] world) {
        // if (!world[position.x][position.y].equals(Tileset.FLOOR)) {
        //     return false;
        // }
        Position pos1 = new Position(position.x + 1, position.y);
        Position pos2 = new Position(position.x, position.y + 1);
        Position pos3 = new Position(position.x, position.y - 1);
        Position pos4 = new Position(position.x - 1, position.y);
        LinkedList<Position> l1 = new LinkedList<>(Arrays.asList(pos1, pos2, pos3));
        LinkedList<Position> l2 = new LinkedList<>(Arrays.asList(pos1, pos2, pos4));
        LinkedList<Position> l3 = new LinkedList<>(Arrays.asList(pos1, pos3, pos4));
        LinkedList<Position> l4 = new LinkedList<>(Arrays.asList(pos2, pos3, pos4));
        return isFloor(l1, world) || isFloor(l2, world) || isFloor(l3, world) || isFloor(l4, world);
    }

    private static boolean isFloor(LinkedList<Position> l, TETile[][] world) {
        Room screen = new Room(new Position(-1, -1), WIDTH, HEIGHT);
        return (inRoom(l.get(0), screen) && world[l.get(0).x][l.get(0).y].equals(Tileset.FLOOR))
                && (inRoom(l.get(1), screen) && world[l.get(1).x][l.get(1).y].equals(Tileset.FLOOR))
                && (inRoom(l.get(2), screen) && world[l.get(2).x][l.get(2).y].equals(Tileset.FLOOR));
    }

    private static boolean boundWAll(Position position, TETile[][] world) {
        Position pos1 = new Position(position.x + 1, position.y);
        Position pos2 = new Position(position.x + 1, position.y + 1);
        Position pos3 = new Position(position.x + 1, position.y - 1);
        Position pos4 = new Position(position.x, position.y + 1);
        Position pos5 = new Position(position.x, position.y - 1);
        Position pos6 = new Position(position.x - 1, position.y);
        Position pos7 = new Position(position.x - 1, position.y + 1);
        Position pos8 = new Position(position.x - 1, position.y - 1);
        Room screen = new Room(new Position(-1, -1), WIDTH, HEIGHT);
        return (inRoom(pos1, screen) && world[pos1.x][pos1.y].equals(Tileset.FLOOR))
                || (inRoom(pos2, screen) && world[pos2.x][pos2.y].equals(Tileset.FLOOR))
                || (inRoom(pos3, screen) && world[pos3.x][pos3.y].equals(Tileset.FLOOR))
                || (inRoom(pos4, screen) && world[pos4.x][pos4.y].equals(Tileset.FLOOR))
                || (inRoom(pos5, screen) && world[pos5.x][pos5.y].equals(Tileset.FLOOR))
                || (inRoom(pos6, screen) && world[pos6.x][pos6.y].equals(Tileset.FLOOR))
                || (inRoom(pos7, screen) && world[pos7.x][pos7.y].equals(Tileset.FLOOR))
                || (inRoom(pos8, screen) && world[pos8.x][pos8.y].equals(Tileset.FLOOR));
    }

    private static void generateHallway(TETile[][] world) {
        Position origin = pickOrigin();
        LinkedList<Position> posList = new LinkedList<>();
        posList.addLast(origin);
        // default value is false.
        boolean[][] visited = new boolean[WIDTH][HEIGHT];
        dfsHallway(posList, world, origin, visited);
        world[origin.x][origin.y] = Tileset.PLAYER;
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
        if (inRoom(nextPos, new Room(new Position(4, 4), WIDTH - 10, HEIGHT - 10))
                && !visited[nextPos.x][nextPos.y]) {
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
        return  (!inRoom(new Position(curPos.x, curPos.y + 2), new Room(new Position(4, 4), WIDTH - 10, HEIGHT - 10))
                        || visited[curPos.x][curPos.y + 2]) &&
                (!inRoom(new Position(curPos.x, curPos.y - 2), new Room(new Position(4, 4), WIDTH - 10, HEIGHT - 10))
                        || visited[curPos.x][curPos.y - 2]) &&
                (!inRoom(new Position(curPos.x + 2, curPos.y), new Room(new Position(4, 4), WIDTH - 10, HEIGHT - 10))
                        || visited[curPos.x + 2][curPos.y]) &&
                (!inRoom(new Position(curPos.x - 2, curPos.y), new Room(new Position(4, 4), WIDTH - 10, HEIGHT - 10))
                        || visited[curPos.x - 2][curPos.y]);
    }

    private static Position pickOrigin() {
        int x = RANDOM.nextInt(WIDTH - 10) + 6;
        int y = RANDOM.nextInt(HEIGHT - 10) + 6;
        x = (x % 2 == 0) ? x : x - 1;
        y = (y % 2 == 0) ? y : y - 1;
        return new Position(x, y);
    }

    private static void initialWorld(TETile[][] world) {
        // generate empty room.
        for (int i = 0; i < WIDTH; i += 2) {
            for (int j = 0; j < HEIGHT; j += 2) {
                world[i][j] = Tileset.NOTHING;
            }
        }
        // generate boundary.
        for (int i = 0; i < 5; i++) {
            for (int j = 0 ; j < HEIGHT; j++) {
                world[i][j] = Tileset.WATER;
            }
        }
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < WIDTH; i++) {
                world[i][j] = Tileset.WATER;
            }
        }
        for (int i = WIDTH - 5; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.WATER;
            }
        }
        for (int j = HEIGHT - 5; j < HEIGHT; j++) {
            for (int i = 0; i < WIDTH; i++) {
                world[i][j] = Tileset.WATER;
            }
        }
    }

    public static boolean inRoom(Position start, Room curRoom) {
        return start.x > curRoom.start.x && start.x < curRoom.start.x + curRoom.width + 1
                && start.y > curRoom.start.y && start.y < curRoom.start.y + curRoom.height + 1;
    }

    public static void generateRooms(TETile[][] tiles) {
        int maxRooms = 100;
        for (int i = 0; i < maxRooms; i++) {
            Position start = new Position(RANDOM.nextInt(WIDTH - 10) + 5, RANDOM.nextInt(HEIGHT - 10) + 5);
            int w = RANDOM.nextInt(WIDTH / 10) + 3;
            int h = RANDOM.nextInt(HEIGHT / 10) + 3;
            start.x = (start.x % 2 == 1) ? start.x : start.x + 1;
            start.y = (start.y % 2 == 1) ? start.y : start.y + 1;
            w = (w % 2 == 0) ? w - 1: w;
            h = (h % 2 == 0) ? h - 1: h;
            if (inBound(start, w, h) && !isOverlap(tiles, start, w, h)) {
                fillRooms(tiles, start, w, h);
            }
        }
    }

    public static boolean inBound(Position start, int w, int h) {
        return start.x > 4 && start.x + w + 1 < WIDTH -5 && start.y > 4 && start.y + h + 1 < HEIGHT - 5;
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
