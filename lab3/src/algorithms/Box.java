package algorithms;

import math.CustomFunction;
import math.Matrix;
import util.DivergenceChecker;

public class Box {

    private static final double DEFAULT_PRECISION = 10e-6;

    private static final double DEFAULT_ALPHA = 1.3;

    public static Matrix search(Matrix x0, Matrix xd, Matrix xg,
                                CustomFunction<Matrix, Double> f, CustomFunction<Matrix, Matrix> g) {
        return search(x0, xd, xg, f, g, DEFAULT_ALPHA, DEFAULT_PRECISION);
    }

    public static Matrix search(Matrix x0, Matrix xd, Matrix xg, CustomFunction<Matrix, Double> f,
                                CustomFunction<Matrix, Matrix> g, double alpha, double e) {
        if (outOfImplicitBounds(x0, g) || outOfExplicitBounds(x0, xd, xg)) {
            System.err.println(String.format("X0: %s is not within bounds.", x0.transpose().toString()));
            return x0;
        }
        Matrix xc = x0.copy();
        Matrix x = new Matrix(2 * x0.getRows(),  x0.getRows(), 0);
        int n = x0.getRows();

        for (int t = 0; t < 2 * n; t++) {
            for (int i = 0; i < n; i++) {
                double R = Math.random();
                x.setElement(i, t, xd.getElement(i, 0) + R * (xg.getElement(i, 0) - xd.getElement(i, 0)));
            }

            while (outOfImplicitBounds(x.getColumn(t), g)) {
                x.setColumn(t, x.getColumn(t).add(xc).multiply(0.5));
            }
            xc = getCentroid(x, t + 1);
        }

        int h, h2;
        do {
            int[] result = findHighAndSecondHigh(x, f);
            h = result[0];
            h2 = result[1];
            xc = getCentroidWithoutH(x, h);
            Matrix xr = reflection(xc, x.getColumn(h), alpha);

            for (int i = 0; i < n; i++) {
                if (xr.getElement(i, 0) < xd.getElement(i, 0)) {
                    xr.setElement(i, 0, xd.getElement(i, 0));
                } else if (xr.getElement(i, 0) > xg.getElement(i, 0)) {
                    xr.setElement(i, 0, xg.getElement(i, 0));
                }
            }


            while (outOfImplicitBounds(xr, g)) {
                xr = xr.add(xc).multiply(0.5);
            }
            if (f.apply(xr) > f.apply(x.getColumn(h2))) {
                xr = xr.add(xc).multiply(0.5);
            }

            x.setColumn(h, xr);

            if (DivergenceChecker.check(f.apply(xc))) {
                throw new RuntimeException("     Stopping box searching, it's probably diverging.");
            }
        } while (!stopCriterionSatisfied(x.getColumn(h), xc, e));

        DivergenceChecker.finishedSearch();
        return xc;
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

    private static boolean outOfExplicitBounds(Matrix col, Matrix xd, Matrix xg) {
        for (int i = 0; i < col.getRows(); i++) {
            if (col.getElement(i, 0) < xd.getElement(i, 0) || col.getElement(i, 0) > xg.getElement(i, 0)) {
                return true;
            }
        }

        return false;
    }

    private static Matrix getCentroid(Matrix x, int columnsFilled) {
        Matrix xc = new Matrix(1, x.getRows());

        for (int i = 0; i < x.getRows(); i++) {
            double sum = 0;
            for (int j = 0; j < columnsFilled; j++) {
                sum += x.getElement(i, j);
            }
            xc.setElement(i, 0, sum / columnsFilled);
        }

        return xc;
    }

    private static Matrix getCentroidWithoutH(Matrix x, int h) {
        Matrix xc = new Matrix(1, x.getRows());

        for (int i = 0; i < x.getRows(); i++) {
            double sum = 0;
            for (int j = 0; j < x.getColumns(); j++) {
                if (j != h) {
                    sum += x.getElement(i, j);
                }
            }
            xc.setElement(i, 0, sum / (x.getColumns() - 1));
        }

        return xc;
    }

    private static int[] findHighAndSecondHigh(Matrix x, CustomFunction<Matrix, Double> f) {
        double max = 0, secondMax = 0;
        Integer maxIndex = null, secondMaxIndex = null;
        for(int j = 0; j < x.getColumns(); j++) {
            double value = f.apply(x.getColumn(j));
            if (maxIndex == null || value > max) {
                if (maxIndex != null) {
                    secondMaxIndex = maxIndex;
                    secondMax = max;
                }
                maxIndex = j;
                max = value;
            }
            else if (secondMaxIndex == null || value > secondMax) {
                secondMaxIndex = j;
                secondMax = value;
            }
        }
        return new int[] {maxIndex, secondMaxIndex};
    }

    private static Matrix reflection(Matrix xc, Matrix xh, double alpha) {
        return xc.multiply(1 + alpha).subtract(xh.multiply(alpha));
    }

    private static boolean stopCriterionSatisfied(Matrix xh, Matrix xc, double e) {
        return xh.subtract(xc).getEuclideanNorm() < e;
    }
}
