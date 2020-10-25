package algorithms;

import math.CustomFunction;
import math.Matrix;

public class CoordinateDescent {

    private static final double DEFAULT_PRECISION = 10e-6;

    private static double findMinimumLambda(CustomFunction<Matrix, Double> f, Matrix x, Matrix e_i) {
        return GoldenSection.search(1, lambda -> f.apply(x.add(e_i.multiply(lambda))));
    }

    public static Matrix search(Matrix x0, CustomFunction<Matrix, Double> f) {
        return search(x0, f, DEFAULT_PRECISION);
    }

    public static Matrix search(Matrix x0, CustomFunction<Matrix, Double> f, double e) {
        Matrix x = x0.copy();
        Matrix e_i = new Matrix(1, x0.getRows(), 0);
        Matrix xs;
        do {
            xs = x.copy();
            for (int i = 1; i <= x0.getRows(); i++) {
                e_i.setElement(i - 1, 0, 1);
                double lambda = findMinimumLambda(f, x, e_i);
                x = x.add(e_i.multiply(lambda));
                e_i.setElement(i - 1, 0, 0);
            }
        } while(!x.equals(xs, e));
        return x;
    }
}
