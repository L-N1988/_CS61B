package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(80, 60, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        // Initialize random number generator
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        // Generate random string of letters of length n
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < n; i++) {
            str.append(CHARACTERS[rand.nextInt(CHARACTERS.length)]);
        }
        return str.toString();
    }

    public void drawFrame(String s) {
        // Take the string and display it in the center of the screen
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text((double) width / 2, (double) height / 2, s);
        // If game is not over, display relevant game information at the top of the screen
        StdDraw.text(5, height - 2, "Round: " + round);
        if (!playerTurn) {
            StdDraw.text((double) width / 2, height - 2, "Watch!");
        } else {
            StdDraw.text((double) width / 2, height - 2, "Type!");
        }
        StdDraw.text((double) width * 3 / 4, height - 2, ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)]);
        StdDraw.line(0, height - 3, width - 1, height - 3);

        StdDraw.show();
    }

    public void flashSequence(String letters) throws InterruptedException {
        // Display each character in letters, making sure to blank the screen between letters
        for (int i = 0; i < letters.length(); i++) {
            playerTurn = false;
            drawFrame(String.valueOf(letters.charAt(i)));
            Thread.sleep(1000);
            StdDraw.clear(Color.BLACK);
            Thread.sleep(500);
        }
    }

    public String solicitNCharsInput(int n) {
        // Read n letters of player input
        StringBuilder str = new StringBuilder();
        int i = 0;
        playerTurn = true;
        drawFrame(str.toString());
        while (i < n) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                str.append(c);
                i++;
                drawFrame(str.toString());
            }
        }
        return str.toString();
    }

    public void startGame() throws InterruptedException {
        // Set any relevant variables before the game starts
        round = 1;
        String randStr;
        String inStr;
        gameOver = false;
        playerTurn = false;
        // Establish Game loop
        do {
            randStr = generateRandomString(round);
            flashSequence(randStr);
            // round number equals string length.
            inStr = solicitNCharsInput(round);
            round++;
            Thread.sleep(500);
        } while (randStr.equals(inStr));
        gameOver = true;
        playerTurn = false;
        drawFrame("Game over, the right string is: " + randStr);
        Thread.sleep(1000);
        System.exit(0);
    }

}
