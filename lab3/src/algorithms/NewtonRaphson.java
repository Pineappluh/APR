package algorithms;

import math.CustomFunction;
import math.Matrix;
import util.DivergenceChecker;

import java.util.function.Function;

public class NewtonRaphson {

    private static final double DEFAULT_PRECISION = 10e-6;

    public static Matrix search(Matrix x0, CustomFunction<Matrix, Double> f) {
        return search(x0, f, DEFAULT_PRECISION);
    }

    public static Matrix search(Matrix x0, CustomFunction<Matrix, Double> f, double e) {
        Matrix x = x0.copy();
        Matrix dx;

        do {
            dx = f.getHessianMatrix().apply(x).inverse().multiply(f.getGradient().apply(x));
            x = x.subtract(dx);

            if (DivergenceChecker.check(f.apply(x))) {
                throw new RuntimeException("    Stopping gradient descent searching, it's probably diverging.");
            }
        } while (!stopCriterionSatisfied(dx, e));

        DivergenceChecker.finishedSearch();
        return x;
    }

    private static double findOptimalLambda(Function<Matrix, Double> f, Matrix x, Matrix dx) {
        return GoldenSection.search(1, lambda -> f.apply(x.subtract(dx.multiply(lambda))));
    }

    public static Matrix searchUsingGoldenRatio(Matrix x0, CustomFunction<Matrix, Double> f) {
        return searchUsingGoldenRatio(x0, f, DEFAULT_PRECISION);
    }

    public static Matrix searchUsingGoldenRatio(Matrix x0, CustomFunction<Matrix, Double> f,  double e) {
        Matrix x = x0.copy();
        Matrix dx;

        do {
            dx = f.getHessianMatrix().apply(x).inverse().multiply(f.getGradient().apply(x));
            double lambda = findOptimalLambda(f, x, dx);
            x = x.subtract(dx.multiply(lambda));

            if (DivergenceChecker.check(f.apply(x))) {
                throw new RuntimeException("    Stopping gradient descent searching, it's probably diverging.");
            }
        } while (!stopCriterionSatisfied(dx, e));

        DivergenceChecker.finishedSearch();
        return x;
    }

    private static boolean stopCriterionSatisfied(Matrix dx, double e) {
        return dx.getEuclideanNorm() < e;
    }
}
