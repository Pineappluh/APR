package algorithms;

import math.CustomFunction;
import math.Matrix;
import util.DivergenceChecker;

import java.util.function.Function;

public class GradientDescent {

    private static final double DEFAULT_PRECISION = 10e-6;

    public static Matrix search(Matrix x0, CustomFunction<Matrix, Double> f) {
        return search(x0, f, DEFAULT_PRECISION);
    }

    public static Matrix search(Matrix x0, CustomFunction<Matrix, Double> f, double e) {
        Matrix x = x0.copy();
        Matrix gradient;

        do {
            gradient = f.getGradient().apply(x);
            x = x.subtract(gradient);

            if (DivergenceChecker.check(f.apply(x))) {
                throw new RuntimeException("         Stopping gradient descent (without optimal step) searching, it's probably diverging.");
            }
        } while (!stopCriterionSatisfied(gradient, e));

        DivergenceChecker.finishedSearch();
        return x;
    }

    private static double findOptimalLambda(Function<Matrix, Double> f, Matrix x, Matrix gradient) {
        return GoldenSection.search(1, lambda -> f.apply(x.subtract(gradient.multiply(lambda))));
    }

    public static Matrix searchUsingGoldenRatio(Matrix x0, CustomFunction<Matrix, Double> f) {
        return searchUsingGoldenRatio(x0, f, DEFAULT_PRECISION);
    }

    public static Matrix searchUsingGoldenRatio(Matrix x0, CustomFunction<Matrix, Double> f,  double e) {
        Matrix x = x0.copy();
        Matrix gradient;

        do {
            gradient = f.getGradient().apply(x);
            double lambda = findOptimalLambda(f, x, gradient);
            x = x.subtract(gradient.multiply(lambda));

            if (DivergenceChecker.check(f.apply(x))) {
                throw new RuntimeException("     Stopping gradient descent searching, it's probably diverging.");
            }
        } while (!stopCriterionSatisfied(gradient, e));

        return x;
    }

    private static boolean stopCriterionSatisfied(Matrix gradient, double e) {
        return gradient.getEuclideanNorm() < e;
    }
}
