package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    int state;
    int period;

    public SawToothGenerator(int period) {
        state = 0;
        this.period = period;
    }

    public double next() {
        state = (state + 1) % period;
        return normalize();
    }

    private double normalize() {
        return -1.0 + 2.0 / period * state;
    }
}
