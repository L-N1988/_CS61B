package byog;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class MapGenerator {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 60;

    private static final long SEED = 20212;
    private static final Random RANDOM = new Random(SEED);

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] randomTiles = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                randomTiles[x][y] = Tileset.NOTHING;
            }
        }

        // fills in a block 14 tiles wide by 4 tiles tall
        fillWithRandomTiles(randomTiles);

        // draws the world to the screen
        ter.renderFrame(randomTiles);
    }

    /**
     * Fills the given 2D array of tiles with RANDOM tiles.
     */
    public static void fillWithRandomTiles(TETile[][] tiles) {
        int x = 5, y = 5;
        int[][] tilesFlag = new int[tiles.length][tiles[0].length];

        for (int cnt = 300; cnt > 0; cnt--) {
            int tileDir = RANDOM.nextInt(2);
            if (tileDir == 0) {
                x = goHor(tiles, x, y, tilesFlag);
            } else {
                y = goVer(tiles, x, y, tilesFlag);
            }
        }
    }

    private static int goHor(TETile[][] tiles, int x, int y, int[][] tilesFlag) {
        int tileDir = RANDOM.nextInt(3);
        if (tileDir == 0) {
            return turnLeft(tiles, x, y, tilesFlag);
        } else {
            return turnRight(tiles, x, y, tilesFlag);
        }
    }

    private static int goVer(TETile[][] tiles, int x, int y, int[][] tilesFlag) {
        int tileDir = RANDOM.nextInt(3);
        if (tileDir == 0) {
            return turnDown(tiles, x, y, tilesFlag);
        } else {
            return turnUp(tiles, x, y, tilesFlag);
        }
    }

    private static int turnUp(TETile[][] tiles, int x, int y, int[][] tilesFlag) {
        int j;
        int tileNum = RANDOM.nextInt(9);
        for (j = y;  j < tiles[0].length && j < y + tileNum; j++) {
            if (j + 1 >= tiles[0].length || tilesFlag[x][j + 1] == 1) {
                break;
            } else {
                tiles[x][j] = Tileset.WALL;
                tilesFlag[x][j] = 1;
            }
        }
//        return (j < tiles[0].length) ? j : tiles[0].length - 1;
        return j;
    }

    private static int turnDown(TETile[][] tiles, int x, int y, int[][] tilesFlag) {
        int j;
        int tileNum = RANDOM.nextInt(9);
        for (j = y;  j > 0 && j > y - tileNum; j--) {
            if (j - 1 <= 0 || tilesFlag[x][j - 1] == 1) {
                break;
            } else {
                tiles[x][j] = Tileset.WALL;
                tilesFlag[x][j] = 1;
            }
        }
//         return (j > 0) ? j : 0;
        return j;
    }

    private static int turnLeft(TETile[][] tiles, int x, int y, int[][] tilesFlag) {
        int i;
        int tileNum = RANDOM.nextInt(9);
        for (i = x;  i > 0 && i > x - tileNum; i--) {
            if (i - 1 <= 0 || tilesFlag[i - 1][y] == 1) {
                break;
            } else {
                tiles[i][y] = Tileset.WALL;
                tilesFlag[i][y] = 1;
            }
        }
//         return (i > 0) ? i : 0;
        return i;
    }

    private static int turnRight(TETile[][] tiles, int x, int y, int[][] tilesFlag) {
        int i;
        int tileNum = RANDOM.nextInt(9);
        // go right tilesNum steps util reach the boundary.
        for (i = x; i < tiles.length && i < x + tileNum; i++) {
            if (i + 1 >= tiles.length || tilesFlag[i + 1][y] == 1) {
                break;
            } else {
                tiles[i][y] = Tileset.WALL;
                tilesFlag[i][y] = 1;
            }
        }
//         return (i < tiles.length) ? i : tiles.length - 1;
        return i;
    }
}
