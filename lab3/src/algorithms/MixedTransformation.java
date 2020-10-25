package algorithms;

import math.CustomFunction;
import math.Matrix;
import math.TransformedFunction;
import util.DivergenceChecker;

import java.util.function.Function;

public class MixedTransformation {

    private static final double DEFAULT_PRECISION = 10e-6;

    private static final double DEFAULT_FACTOR = 1.;

    public static Matrix search(Matrix x0, CustomFunction<Matrix, Double> f,
                                CustomFunction<Matrix, Matrix> g, CustomFunction<Matrix, Matrix> h) {
        return search(x0, f, g, h, DEFAULT_FACTOR, DEFAULT_PRECISION);
    }

    public static Matrix search(Matrix x0, CustomFunction<Matrix, Double> f,
                                CustomFunction<Matrix, Matrix> g, CustomFunction<Matrix, Matrix> h,
                                double t, double e) {
        Matrix last;
        TransformedFunction tf = new TransformedFunction(f, g, h, t);

        if (outOfImplicitBounds(x0, g)) {
            x0 = findPointInside(x0, g);
        }

        do {
            last = x0;
            x0 = HookeJeeves.search(x0, tf);
            tf.increaseTransformationFactor();

            if (DivergenceChecker.check(f.apply(x0))) {
                throw new RuntimeException("Stopping mixed transformation searching, it's probably diverging.");
            }
        } while(!stopCriterionSatisfied(last, x0, e));

        DivergenceChecker.finishedSearch();
        return x0;
    }

    private static boolean outOfImplicitBounds(Matrix col, CustomFunction<Matrix, Matrix> g) {
        Matrix values = g.apply(col);

        for (int i = 0; i < values.getRows(); i++) {
            if (values.getElement(i, 0) < 0) {
                return true;
            }
        }

        return false;
    }

    private static Matrix findPointInside(Matrix x0, CustomFunction<Matrix, Matrix> g) {
        Function<Matrix, Double> G = x -> {
            Matrix values = g.apply(x);
            double sum = 0;

            for (int i = 0; i < values.getRows(); i++) {
                if (values.getElement(i, 0) < 0) {
                    sum += values.getElement(i, 0);
                }
            }

            return -sum;
        };

        Matrix newx0 = HookeJeeves.search(x0, G);
        System.out.println(
                String.format("     Using %s as x0 instead of %s because it wasn't inside bounds.",
                        newx0.transpose(),
                        x0.transpose()
                )
        );

        return newx0;
    }

    private static boolean stopCriterionSatisfied(Matrix last, Matrix current, double e) {
        return current.subtract(last).getEuclideanNorm() < e;
    }
}
