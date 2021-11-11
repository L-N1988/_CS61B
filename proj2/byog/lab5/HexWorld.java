package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 70;
    private static final int HEIGHT = 70;
    private static int x0 = WIDTH / 2;
    private static int y0 = 0;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    public static void addHexagon(int p, int q, int s, TETile[][] tiles) {
        int height = 2 * s;
        int width = 2 * s;

        TETile symbol = randomTile();
        for (int y = 0; y < height; y += 1) {
            if (y < s) {
                drawUpper(p, q, y, s, tiles, symbol);
            } else {
                drawLower(p, q, y, s, tiles, symbol);
            }
        }
    }

    private static void drawUpper(int p, int q, int y, int s, TETile[][] tiles, TETile symbol) {
        int bound = 2 * s - 1 + y;
        for (int x = 0; x < bound; x += 1) {
            if (x >= s - 1 - y) {
                tiles[x + p][y + q] = symbol;
            }
        }
    }

    private static void drawLower(int p, int q, int y, int s, TETile[][] tiles, TETile symbol) {
        int bound = 4 * s - 2 - y;
        for (int x = 0; x < bound; x += 1) {
            if (x >= y - s) {
                tiles[x + p][y + q] = symbol;
            }
        }
    }

    public static void drawHexagon(int s, TETile[][] tiles) {
        /* 0. */
        x0 -= s;
        addHexagon(x0, y0, s, tiles);
        /* 1-3. */
        addUpperLeft(s, tiles);
        addUpperRight(s, tiles);
        addDownRight(s, tiles);

        /* 4-8. */
        addUpperRight(s, tiles);
        addUpperLeft(s, tiles);
        addUpperLeft(s, tiles);
        addDownLeft(s, tiles);
        addDownLeft(s, tiles);

        /* 9-13. */
        addUpper(s, tiles);
        addUpperRight(s, tiles);
        addUpperRight(s, tiles);
        addDownRight(s, tiles);
        addDownRight(s, tiles);

        /* 14-18. */
        addUpper(s, tiles);
        addUpperLeft(s, tiles);
        addUpperLeft(s, tiles);
        addDownLeft(s, tiles);
        addDownLeft(s, tiles);
    }

    public static void addRight(int s, TETile[][] tiles) {
        int offset_x = 2 * s - 1;
        int offset_y = 0;
        x0 += offset_x;
        y0 += offset_y;
        addHexagon(x0, y0, s, tiles);
    }

    public static void addLeft(int s, TETile[][] tiles) {
        int offset_x = 1 - 2 * s;
        int offset_y = 0;
        x0 += offset_x;
        y0 += offset_y;
        addHexagon(x0, y0, s, tiles);
    }

    public static void addUpper(int s, TETile[][] tiles) {
        int offset_x = 0;
        int offset_y = 2 * s;
        x0 += offset_x;
        y0 += offset_y;
        addHexagon(x0, y0, s, tiles);
    }

    public static void addDown(int s, TETile[][] tiles) {
        int offset_x = 0;
        int offset_y = -2 * s;
        x0 += offset_x;
        y0 += offset_y;
        addHexagon(x0, y0, s, tiles);
    }

    public static void addDownLeft(int s, TETile[][] tiles) {
        int offset_x = 1 - 2 * s;
        int offset_y = -s;
        x0 += offset_x;
        y0 += offset_y;
        addHexagon(x0, y0, s, tiles);
    }

    public static void addDownRight(int s, TETile[][] tiles) {
        int offset_x = 2 * s - 1;
        int offset_y = -s;
        x0 += offset_x;
        y0 += offset_y;
        addHexagon(x0, y0, s, tiles);
    }

    public static void addUpperLeft(int s, TETile[][] tiles) {
        int offset_x = 1 - 2 * s;
        int offset_y = s;
        x0 += offset_x;
        y0 += offset_y;
        addHexagon(x0, y0, s, tiles);
    }

    public static void addUpperRight(int s, TETile[][] tiles) {
        int offset_x = 2 * s - 1;
        int offset_y = s;
        x0 += offset_x;
        y0 += offset_y;
        addHexagon(x0, y0, s, tiles);
    }

    /** Picks a RANDOM tile with a 33% change of being
     *  a wall, 33% chance of being a flower, and 33%
     *  chance of being empty space.
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.SAND;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.MOUNTAIN;
            default: return Tileset.TREE;
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] tiles = new TETile[WIDTH][HEIGHT];
        // initialize tiles
        for (int x0 = 0; x0 < WIDTH; x0 += 1) {
            for (int y0 = 0; y0 < HEIGHT; y0 += 1) {
                tiles[x0][y0] = Tileset.NOTHING;
            }
        }

        drawHexagon(6, tiles);
        ter.renderFrame(tiles);
    }
}
