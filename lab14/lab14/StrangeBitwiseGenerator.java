package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    int state;
    int period;
    int weirdState;

    public StrangeBitwiseGenerator(int period) {
        state = 0;
        this.period = period;
        weirdState = 0;
    }

    public double next() {
        state = state + 1;
        weirdState = state & (state >>> 3) % period;
        return normalize();
    }

    private double normalize() {
        return -1.0 + 2.0 / period * weirdState;
    }
}
