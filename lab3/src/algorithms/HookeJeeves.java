package algorithms;

import math.Matrix;

import java.util.function.Function;

public class HookeJeeves {

    private static final double DEFAULT_DX = 0.5;

    private static final double DEFAULT_PRECISION = 10e-6;

    public static Matrix search(Matrix x0, Function<Matrix, Double> f) {
        return search(x0, f, DEFAULT_DX, DEFAULT_PRECISION);
    }

    public static Matrix search(Matrix x0, Function<Matrix, Double> f, double dx, double e) {
        Matrix xp = x0.copy();
        Matrix xb = x0.copy();
        do {
            Matrix xn = investigate(xp, f, dx);
            if (f.apply(xn) < f.apply(xb)) {
                xp = xn.multiply(2).subtract(xb);
                xb = xn;
            } else {
                dx /= 2;
                xp = xb;
            }
        } while (!(dx <= e));
        return xb;
    }

    private static Matrix investigate(Matrix xp, Function<Matrix, Double> f, double dx) {
        Matrix x = xp.copy();
        for (int i = 0; i < x.getRows(); i++) {
            double p = f.apply(x);
            x.addElement(i, 0, dx);
            double n = f.apply(x);
            if (n > p) {
                x.subtractElement(i, 0, 2 * dx);
                n = f.apply(x);
                if (n > p) {
                    x.addElement(i, 0, dx);
                }
            }
        }
        return x;
    }
}
