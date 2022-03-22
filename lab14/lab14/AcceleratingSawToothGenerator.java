package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    int state;
    int period;
    double factor;

    public AcceleratingSawToothGenerator(int period, double factor) {
        state = 0;
        this.period = period;
        this.factor = factor;
    }

    public double next() {
        state = state + 1;
        if (state == period) {
            state = state % period;
            period *= factor;
        }
        return normalize();
    }

    private double normalize() {
        return -1.0 + 2.0 / period * state;
    }
}
