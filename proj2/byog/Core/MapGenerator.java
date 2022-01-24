package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;
import java.util.LinkedList;

public class MapGenerator {
    static class Position {
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

    public static TETile[][] mapGenerator(long SEED) {
        // initialize tiles
        TETile[][] randomRooms = new TETile[Game.WIDTH][Game.HEIGHT];
        TETile[][] world = new TETile[Game.WIDTH][Game.HEIGHT];

        Random RANDOM = new Random(SEED);

        for (int x = 0; x < Game.WIDTH; x += 1) {
            for (int y = 0; y < Game.HEIGHT; y += 1) {
                world[x][y] = Tileset.WALL;
                randomRooms[x][y] = Tileset.NOTHING;
            }
        }
        initialWorld(world);
        generateRooms(randomRooms, RANDOM);
        mergeMaps(world, randomRooms);

        // dfs route in world map.
        generateHallway(world, RANDOM);
        // smooth map.
        smoothMap(world);
        // draws the world to the screen
        // ter.renderFrame(world);
        return world;
    }

    private static void smoothMap(TETile[][] world) {
        for (int i = 0; i < Game.WIDTH; i++) {
            for (int j = 0; j < Game.HEIGHT; j++) {
                if (world[i][j].equals(Tileset.WATER)) {
                    world[i][j] = Tileset.WALL;
                }
            }
        }
        for (int i = 0; i < Game.WIDTH; i++) {
            for (int j = 0; j < Game.HEIGHT; j++) {
                if (!isBoundWAll(new Position(i, j), world)) {
                    world[i][j] = Tileset.NOTHING;
                }
            }
        }
    }

    private static boolean isBoundWAll(Position position, TETile[][] world) {
        Position pos1 = new Position(position.x + 1, position.y);
        Position pos2 = new Position(position.x + 1, position.y + 1);
        Position pos3 = new Position(position.x + 1, position.y - 1);
        Position pos4 = new Position(position.x, position.y + 1);
        Position pos5 = new Position(position.x, position.y - 1);
        Position pos6 = new Position(position.x - 1, position.y);
        Position pos7 = new Position(position.x - 1, position.y + 1);
        Position pos8 = new Position(position.x - 1, position.y - 1);
        Room screen = new Room(new Position(-1, -1), Game.WIDTH, Game.HEIGHT);
        return (inRoom(pos1, screen) && world[pos1.x][pos1.y].equals(Tileset.FLOOR))
                || (inRoom(pos2, screen) && world[pos2.x][pos2.y].equals(Tileset.FLOOR))
                || (inRoom(pos3, screen) && world[pos3.x][pos3.y].equals(Tileset.FLOOR))
                || (inRoom(pos4, screen) && world[pos4.x][pos4.y].equals(Tileset.FLOOR))
                || (inRoom(pos5, screen) && world[pos5.x][pos5.y].equals(Tileset.FLOOR))
                || (inRoom(pos6, screen) && world[pos6.x][pos6.y].equals(Tileset.FLOOR))
                || (inRoom(pos7, screen) && world[pos7.x][pos7.y].equals(Tileset.FLOOR))
                || (inRoom(pos8, screen) && world[pos8.x][pos8.y].equals(Tileset.FLOOR));
    }

    private static void generateHallway(TETile[][] world, Random RANDOM) {
        Position origin = pickOrigin(RANDOM);
        LinkedList<Position> posList = new LinkedList<>();
        posList.addLast(origin);
        // default value is false.
        boolean[][] visited = new boolean[Game.WIDTH][Game.HEIGHT];
        dfsHallway(posList, world, origin, visited, RANDOM);
        world[origin.x][origin.y] = Tileset.PLAYER;
    }

    private static void dfsHallway(LinkedList<Position> posList, TETile[][] world, Position curPos, boolean[][] visited, Random RANDOM) {
        if (posList.isEmpty()) {
            return;
        }
        Position nextPos = null;
        int randDir = RANDOM.nextInt(4);
        switch (randDir) {
            case 0:
                nextPos = new Position(curPos.x, curPos.y + 2);
                break;
            case 1:
                nextPos = new Position(curPos.x, curPos.y - 2);
                break;
            case 2:
                nextPos = new Position(curPos.x - 2, curPos.y);
                break;
            case 3:
                nextPos = new Position(curPos.x + 2, curPos.y);
                break;
            default:
                break;
        }
        if (inRoom(nextPos, new Room(new Position(4, 4), Game.WIDTH - 10, Game.HEIGHT - 10))
                && !visited[nextPos.x][nextPos.y]) {
            visited[nextPos.x][nextPos.y] = true;
            posList.addLast(nextPos);
            curPos = nextPos;
        } else {
            while (!posList.isEmpty()) {
                Position prevPos = posList.getLast();
                posList.removeLast();
                world[curPos.x][curPos.y] = Tileset.FLOOR;
                if (!isDeadEnd(prevPos, visited)) {
                    posList.addLast(prevPos);
                    curPos = prevPos;
                    break;
                }
                world[prevPos.x][prevPos.y] = Tileset.FLOOR;
                world[(curPos.x + prevPos.x) / 2][(curPos.y + prevPos.y) / 2] = Tileset.FLOOR;
                curPos = prevPos;
            }
        }
        dfsHallway(posList, world, curPos, visited, RANDOM);
    }

    private static boolean isDeadEnd(Position curPos, boolean[][] visited) {
        return  (!inRoom(new Position(curPos.x, curPos.y + 2), new Room(new Position(4, 4), Game.WIDTH - 10, Game.HEIGHT - 10))
                        || visited[curPos.x][curPos.y + 2]) &&
                (!inRoom(new Position(curPos.x, curPos.y - 2), new Room(new Position(4, 4), Game.WIDTH - 10, Game.HEIGHT - 10))
                        || visited[curPos.x][curPos.y - 2]) &&
                (!inRoom(new Position(curPos.x + 2, curPos.y), new Room(new Position(4, 4), Game.WIDTH - 10, Game.HEIGHT - 10))
                        || visited[curPos.x + 2][curPos.y]) &&
                (!inRoom(new Position(curPos.x - 2, curPos.y), new Room(new Position(4, 4), Game.WIDTH - 10, Game.HEIGHT - 10))
                        || visited[curPos.x - 2][curPos.y]);
    }

    private static Position pickOrigin(Random RANDOM) {
        int x = RANDOM.nextInt(Game.WIDTH - 10) + 6;
        int y = RANDOM.nextInt(Game.HEIGHT - 10) + 6;
        x = (x % 2 == 0) ? x : x - 1;
        y = (y % 2 == 0) ? y : y - 1;
        return new Position(x, y);
    }

    private static void initialWorld(TETile[][] world) {
        // generate empty room.
        for (int i = 0; i < Game.WIDTH; i += 2) {
            for (int j = 0; j < Game.HEIGHT; j += 2) {
                world[i][j] = Tileset.NOTHING;
            }
        }
        // generate boundary.
        for (int i = 0; i < 5; i++) {
            for (int j = 0 ; j < Game.HEIGHT; j++) {
                world[i][j] = Tileset.WATER;
            }
        }
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < Game.WIDTH; i++) {
                world[i][j] = Tileset.WATER;
            }
        }
        for (int i = Game.WIDTH - 5; i < Game.WIDTH; i++) {
            for (int j = 0; j < Game.HEIGHT; j++) {
                world[i][j] = Tileset.WATER;
            }
        }
        for (int j = Game.HEIGHT - 5; j < Game.HEIGHT; j++) {
            for (int i = 0; i < Game.WIDTH; i++) {
                world[i][j] = Tileset.WATER;
            }
        }
    }

    public static boolean inRoom(Position start, Room curRoom) {
        return start.x > curRoom.start.x && start.x < curRoom.start.x + curRoom.width + 1
                && start.y > curRoom.start.y && start.y < curRoom.start.y + curRoom.height + 1;
    }

    public static void generateRooms(TETile[][] tiles, Random RANDOM) {
        int maxRooms = 100;
        for (int i = 0; i < maxRooms; i++) {
            Position start = new Position(RANDOM.nextInt(Game.WIDTH - 10) + 5,
                    RANDOM.nextInt(Game.HEIGHT - 10) + 5);
            int w = RANDOM.nextInt(Game.WIDTH / 10) + 3;
            int h = RANDOM.nextInt(Game.HEIGHT / 10) + 3;
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
        return start.x > 4 && start.x + w + 1 < Game.WIDTH -5 && start.y > 4
                && start.y + h + 1 < Game.HEIGHT - 5;
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
        for (int i = 0; i < Game.WIDTH; i++) {
            for (int j = 0; j < Game.HEIGHT; j++) {
                if (!rooms[i][j].equals(Tileset.NOTHING)) {
                    tiles[i][j] = rooms[i][j];
                }
            }
        }
    }
}
