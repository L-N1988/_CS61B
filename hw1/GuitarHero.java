import edu.princeton.cs.algs4.StdAudio;
import synthesizer.GuitarString;

public class GuitarHero {
    private static final double CONCERT_A = 440.0;
    private static final int NUM_KEY = 37;
    private static String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    private GuitarString[] myPiano = new GuitarString[NUM_KEY];
    private double[] vol = new double[NUM_KEY];

    public GuitarHero() {
        for (int i = 0; i < keyboard.length(); i += 1) {
            myPiano[i] = new GuitarString(CONCERT_A * Math.pow(2, (i - 24) / 12));
            vol[i] = 2.0;
        }
    }

    public static void main(String[] args) {
        GuitarHero myGuitar = new GuitarHero();
        System.out.println("starting performance...");

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (keyboard.indexOf(key) != -1) {
                    myGuitar.myPiano[keyboard.indexOf(key)].pluck();
                    double sample = myGuitar.myPiano[keyboard.indexOf(key)].sample();
                    for (int i = 0; i < 50000; i += 1) {
                        StdAudio.play(sample);
                        myGuitar.myPiano[keyboard.indexOf(key)].tic();
                    }
                }
            }
        }
    }
}
