package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.io.*;

public class Game {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        StdDraw.enableDoubleBuffering();
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = null;
        MapGenerator.Position org;
        drawGreeter();

        if (StdDraw.hasNextKeyTyped()) {
            char c = StdDraw.nextKeyTyped();
            if (c == 'l' || c == 'L') {
                world = loadWorld();
            } else if (c == 'n' || c == 'N') {
                world = mapGen(readInSEED());
            } else if (c == 'q' || c == 'Q') {
                System.exit(0);
            }
        }
        org = findOrg(world);
        ter.renderFrame(world);
        drawProcess(ter, world, org);
    }

    private void drawProcess(TERenderer ter, TETile[][] world, MapGenerator.Position org) {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                // clear previous player location.
                world[org.x][org.y] = Tileset.FLOOR;
                char c = StdDraw.nextKeyTyped();
                switch (c) {
                    case 'q':
                    case 'Q':
                        saveWorld(world);
                        System.exit(0);
                        break;
                    case 'w':
                    case 'W':
                       org = goUP(world, org);
                       break;
                    case 's':
                    case 'S':
                        org = goDown(world, org);
                        break;
                    case 'l':
                    case 'L':
                        org = goLeft(world, org);
                        break;
                    case 'r':
                    case 'R':
                        org = goRight(world, org);
                        break;
                    default:
                        break;
                }
            }
            // set next step play position.
            world[org.x][org.y] = Tileset.PLAYER;
            ter.renderFrame(world);
        }
    }

    private long readInSEED() {
        long seed = 0;
        while (StdDraw.hasNextKeyTyped()
                && (StdDraw.nextKeyTyped() != 's' || StdDraw.nextKeyTyped() != 'S')) {
            seed = seed * 10 + (long)StdDraw.nextKeyTyped();
        }
        return seed;
    }


    private static void drawGreeter() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(0.5, 0.8, "Press a to add square.");
        StdDraw.text(0.5, 0.85, "Press q to quit and save.");
        StdDraw.text(0.5, 0.9, "Delete world.ser to go back to a blank canvas.");
        StdDraw.show();
        StdDraw.pause(100);
    }

    private static TETile[][] loadWorld() {
        File f = new File("./world.ser");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                TETile[][] loadWorld = (TETile[][]) os.readObject();
                os.close();
                return loadWorld;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }

        /* In the case no World has been saved yet, we return a new one. */
        return new TETile[Game.WIDTH][Game.HEIGHT];
    }


    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(Game.WIDTH, Game.HEIGHT);

        int i = getSEED(input);
        TETile[][] orgMap = mapGen(Long.parseLong(input.substring(1, i)));
        if (i != input.length() - 1) {
            nextSteps(orgMap, input.substring(i + 1));
        }
        // draws the world to the screen
        ter.renderFrame(orgMap);
        return orgMap;
    }

    private void nextSteps(TETile[][] orgMap, String steps) {
        MapGenerator.Position org =  findOrg(orgMap);
        for (int i = 0; i < steps.length(); i++) {
            if (steps.charAt(i) == 'q' || steps.charAt(i) == 'Q') {
                saveWorld(orgMap);
                System.exit(0);
                break;
            } else if (steps.charAt(i) == ':') {
                continue;
            }
            // clear previous player location.
            orgMap[org.x][org.y] = Tileset.FLOOR;
            switch (steps.charAt(i)) {
                case 'w':
                case 'W':
                    org = goUP(orgMap, org);
                    break;
                case 's':
                case 'S':
                    org = goDown(orgMap, org);
                    break;
                case 'a':
                case 'A':
                    org = goLeft(orgMap, org);
                    break;
                case 'd':
                case 'D':
                    org = goRight(orgMap, org);
                    break;
                default:
                    break;
            }
            orgMap[org.x][org.y] = Tileset.PLAYER;
        }
    }

    private void saveWorld(TETile[][] w) {
        File f = new File("./worldMap.ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(w);
            os.close();
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private MapGenerator.Position goRight(TETile[][] orgMap, MapGenerator.Position org) {
        if (orgMap[org.x + 1][org.y].equals(Tileset.WALL)) {
            return org;
        } else {
            return new MapGenerator.Position(org.x + 1, org.y);
        }
    }

    private MapGenerator.Position goLeft(TETile[][] orgMap, MapGenerator.Position org) {
        if (orgMap[org.x - 1][org.y].equals(Tileset.WALL)) {
            return org;
        } else {
            return new MapGenerator.Position(org.x - 1, org.y);
        }
    }

    private MapGenerator.Position goDown(TETile[][] orgMap, MapGenerator.Position org) {
        if (orgMap[org.x][org.y - 1].equals(Tileset.WALL)) {
            return org;
        } else {
            return new MapGenerator.Position(org.x, org.y - 1);
        }
    }

    private MapGenerator.Position goUP(TETile[][] orgMap, MapGenerator.Position org) {
        if (orgMap[org.x][org.y + 1].equals(Tileset.WALL)) {
            return org;
        } else {
            return new MapGenerator.Position(org.x, org.y + 1);
        }
    }

    private MapGenerator.Position findOrg(TETile[][] orgMap) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (orgMap[i][j].equals(Tileset.PLAYER)) {
                    return new MapGenerator.Position(i, j);
                }
            }
        }
        return null;
    }


    private TETile[][] mapGen(long seed) {
        return MapGenerator.mapGenerator(seed);
    }

    private int getSEED(String input) {
        // illegal input string.
        if (input.charAt(0) != 'n' && input.charAt(0) != 'N') {
            return 0;
        }
        int i;
        for (i = 0; i < input.length(); i++) {
            if (input.charAt(i) == 's' || input.charAt(i) == 'S') {
                break;
            }
        }
        return i;
    }
}
