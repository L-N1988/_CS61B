package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Game {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world;
        MapGenerator.Position org;

        drawGreeter();
        world = readInWorld();
        ter.initialize(WIDTH, HEIGHT);
        org = findPlayer(world);
        ter.renderFrame(world);
        drawProcess(ter, world, org);
    }

    private TETile[][] readInWorld() {
        TETile[][] world;

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 'l' || c == 'L') {
                    world = loadWorld();
                    break;
                } else if (c == 'n' || c == 'N') {
                    world = mapGen(readInSEED());
                    break;
                } else if (c == 'q' || c == 'Q') {
                    System.exit(0);
                }
            }
        }
        return world;
    }

    private void drawProcess(TERenderer ter, TETile[][] world, MapGenerator.Position org) {
        StringBuilder s = new StringBuilder();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                // clear previous player location.
                world[org.x][org.y] = Tileset.FLOOR;
                char c = StdDraw.nextKeyTyped();
                s.append(c);
                switch (c) {
                    case 'q':
                    case 'Q':
                        // do not clear player's position before save world map.
                        world[org.x][org.y] = Tileset.PLAYER;
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
                    case 'a':
                    case 'A':
                        org = goLeft(world, org);
                        break;
                    case 'd':
                    case 'D':
                        org = goRight(world, org);
                        break;
                    default:
                        break;
                }
            }
            // set next step play position.
            world[org.x][org.y] = Tileset.PLAYER;
            drawHUD(world);
            //  Add some neat easter eggs or cheat codes to your game which do something fun.
            // type in string "adads" will show all map for seconds.
            if (s.length() > 4
                    && s.substring(s.length() - 5).equals("adads")) {
                ter.renderFrame(world);
                StdDraw.pause(1000);
                // insert a meaningless character
                // to avoid re-enter if statement many times.
                s.append('o');
            }
            blindSight(world);
        }
    }

    /*
     * Create a system so that the game only displays tiles on the screen
     * that are within the line of sight of the player
     */
    private void blindSight(TETile[][] world) {
        MapGenerator.Position playerPos = findPlayer(world);
        int sightRad = 8;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (i > playerPos.x - sightRad && i < playerPos.x + sightRad
                        && j > playerPos.y - sightRad && j < playerPos.y + sightRad) {
                    world[i][j].draw(i, j);
                } else {
                    Tileset.NOTHING.draw(i, j);
                }
            }
        }
        StdDraw.show();
    }

    /*
     * display head up message about tiles.
     */
    private void drawHUD(TETile[][] world) {
        StdDraw.setPenColor(Color.WHITE);
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
            return;
        }
        if (world[(int) x][(int) y].equals(Tileset.FLOOR)) {
            StdDraw.text(3, HEIGHT - 3, "Floor");
        } else if (world[(int) x][(int) y].equals(Tileset.PLAYER)) {
            StdDraw.text(3, HEIGHT - 3, "Player");
        } else if (world[(int) x][(int) y].equals(Tileset.LOCKED_DOOR)) {
            StdDraw.text(3, HEIGHT - 3, "Locked door");
        } else if (world[(int) x][(int) y].equals(Tileset.NOTHING)) {
            StdDraw.text(3, HEIGHT - 3, "Nothing");
        } else if (world[(int) x][(int) y].equals(Tileset.WALL)) {
            StdDraw.text(3, HEIGHT - 3, "Wall");
        }
        StdDraw.show();
    }

    private long readInSEED() {
        // initialize some original frame variable.
        StdDraw.clear(StdDraw.BLACK);
        Font smallFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(smallFont);

        long seed = 0;
        StringBuilder s = new StringBuilder();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                StdDraw.clear(StdDraw.BLACK);
                StdDraw.setPenColor(StdDraw.WHITE);
                char c = StdDraw.nextKeyTyped();
                if (c == 's' || c == 'S') {
                    break;
                }
                seed = seed * 10 + (long) c - 48;
                s.append(c);
                StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2, "Your input seed: ");
                StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 2, s.toString());
                StdDraw.show();
            }
        }
        return seed;
    }


    private void drawGreeter() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);

        Font bigFont = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(bigFont);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT * 3 / 4, "CS61B: THE GAME");

        Font smallFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(smallFont);

        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2, "New Game (N)");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 2, "Load Game (L)");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 4, "Quit (Q)");
        StdDraw.show();
    }

    private static TETile[][] loadWorld() {
        File f = new File("./world.txt");
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
        TETile[][] orgMap;
        int i = getSEED(input);
        if (input.charAt(0) == 'l' || input.charAt(0) == 'L') {
            orgMap = loadWorld();
        } else {
            orgMap = mapGen(Long.parseLong(input.substring(1, i)));
        }
        if (i != input.length() - 1) {
            nextSteps(orgMap, input.substring(i + 1));
        }
        return orgMap;
    }

    private void nextSteps(TETile[][] orgMap, String steps) {
        MapGenerator.Position org =  findPlayer(orgMap);
        for (int i = 0; i < steps.length(); i++) {
            if (steps.charAt(i) == 'q' || steps.charAt(i) == 'Q') {
                saveWorld(orgMap);
                System.exit(0);
                break;
            } else if (steps.charAt(i) == ':') {
                continue;
            } else if (steps.charAt(i) == 'l' || steps.charAt(i) == 'L') {
                orgMap = loadWorld();
                org = findPlayer(orgMap);
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
        File f = new File("./world.txt");
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
        if (!orgMap[org.x + 1][org.y].equals(Tileset.FLOOR)) {
            return org;
        } else {
            return new MapGenerator.Position(org.x + 1, org.y);
        }
    }

    private MapGenerator.Position goLeft(TETile[][] orgMap, MapGenerator.Position org) {
        if (!orgMap[org.x - 1][org.y].equals(Tileset.FLOOR)) {
            return org;
        } else {
            return new MapGenerator.Position(org.x - 1, org.y);
        }
    }

    private MapGenerator.Position goDown(TETile[][] orgMap, MapGenerator.Position org) {
        if (!orgMap[org.x][org.y - 1].equals(Tileset.FLOOR)) {
            return org;
        } else {
            return new MapGenerator.Position(org.x, org.y - 1);
        }
    }

    private MapGenerator.Position goUP(TETile[][] orgMap, MapGenerator.Position org) {
        if (!orgMap[org.x][org.y + 1].equals(Tileset.FLOOR)) {
            return org;
        } else {
            return new MapGenerator.Position(org.x, org.y + 1);
        }
    }

    private MapGenerator.Position findPlayer(TETile[][] orgMap) {
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
