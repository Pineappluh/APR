package algorithms;

import math.CustomFunction;
import math.Matrix;

public class Simplex {


    private static final double DEFAULT_ALPHA = 1;

    private static final double DEFAULT_BETA = 0.5;

    private static final double DEFAULT_GAMMA = 2;

    private static final double DEFAULT_SIGMA = 0.5;

    private static final double DEFAULT_PRECISION = 10e-6;

    public static Matrix search(Matrix x0, CustomFunction<Matrix, Double> f) {
        return search(x0, f, DEFAULT_ALPHA, DEFAULT_BETA, DEFAULT_GAMMA, DEFAULT_SIGMA, DEFAULT_PRECISION);
    }

    public static Matrix search(Matrix x0, CustomFunction<Matrix, Double> f, double sigma) {
        return search(x0, f, DEFAULT_ALPHA, DEFAULT_BETA, DEFAULT_GAMMA, sigma, DEFAULT_PRECISION);
    }

    public static Matrix search(Matrix x0, CustomFunction<Matrix, Double> f, double alpha, double beta, double gamma, double sigma, double e) {
        Matrix x = createSimplexPoints(x0, sigma);
        Matrix xc;
        do {
            int[] result = findLowAndHigh(x, f);
            int l = result[0];
            int h = result[1];
            Matrix xl = x.getColumn(l);
            Matrix xh = x.getColumn(h);
            xc = getCentroid(x, h);
            Matrix xr = reflection(xc, xh, alpha);
            if (f.apply(xr) < f.apply(xl)) {
                Matrix xe = expansion(xc, xr, gamma);
                if (f.apply(xe) < f.apply(xl)) {
                    x.setColumn(h, xe);
                } else {
                    x.setColumn(h, xr);
                }
            } else {
                if (checkReflectionIsMax(x, xr, h, f)) {
                    if (f.apply(xr) < f.apply(xh)) {
                        x.setColumn(h, xr);
                    }
                    Matrix xk = contraction(xc, xh, beta);
                    if (f.apply(xk) < f.apply(xh)) {
                        x.setColumn(h, xk);
                    } else {
                        movePointsTowardLow(x, xl);
                    }
                } else {
                    x.setColumn(h, xr);
                }
            }
        } while(!checkSimplexStoppingCriterion(x, xc, f, e));
        return xc;
    }

    private static Matrix getCentroid(Matrix x, int h) {
        Matrix Xc = new Matrix(1, x.getRows());
        for (int i = 0; i < x.getRows(); i++) {
            double sum = 0;
            for (int j = 0; j < x.getColumns(); j++) {
                if (j != h) {
                    sum += x.getElement(i, j);
                }
            }
            Xc.setElement(i, 0, sum / x.getRows());
        }
        return Xc;
    }

    private static int[] findLowAndHigh(Matrix x, CustomFunction<Matrix, Double> f) {
        double max = 0, min = 0; // 0 doesn't matter, values will get overwritten in first iteration of loop anyway
        Integer maxIndex = null, minIndex = null;
        for(int j = 0; j < x.getColumns(); j++) {
            double value = f.apply(x.getColumn(j));
            if (minIndex == null || value < min) {
                minIndex = j;
                min = value;
            }
            if (maxIndex == null || value > max) {
                maxIndex = j;
                max = value;
            }
        }
        return new int[] {minIndex, maxIndex};
    }

    private static Matrix reflection(Matrix xc, Matrix xh, double alpha) {
        return xc.multiply(1 + alpha).subtract(xh.multiply(alpha));
    }

    private static Matrix expansion(Matrix xc, Matrix xr, double gamma) {
        return xc.multiply(1 - gamma).add(xr.multiply(gamma));
    }

    private static Matrix contraction(Matrix xc, Matrix xh, double beta) {
        return xc.multiply(1 - beta).add(xh.multiply(beta));
    }

    private static boolean checkReflectionIsMax(Matrix x, Matrix xr, int h, CustomFunction<Matrix, Double> f) {
        for (int j = 0; j < x.getColumns(); j++) {
            if (j != h && f.apply(xr) <= f.apply(x.getColumn(j))) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkSimplexStoppingCriterion(Matrix x, Matrix xc, CustomFunction<Matrix, Double> f, double e) {
        double rootSum = 0;
        for (int j = 0; j < x.getColumns(); j++) {
            rootSum += Math.pow(f.apply(x.getColumn(j)) - f.apply(xc), 2);
        }
        rootSum = Math.pow(1. / x.getColumns() * rootSum, 0.5);
        return rootSum <= e;
    }

    private static void movePointsTowardLow(Matrix x, Matrix xl) {
        for (int j = 0; j < x.getColumns(); j++) {
            x.setColumn(j, x.getColumn(j).add(xl).multiply(0.5)); // arithmetic middle
        }
    }

    private static Matrix createSimplexPoints(Matrix x0, double sigma) {
        Matrix x = new Matrix(x0.getRows() + 1, x0.getRows(), 0);
        Matrix I = Matrix.createIdentityMatrix(x0.getRows()).multiply(sigma);
        x.setColumn(0, x0);
        for (int j = 1; j < x.getColumns(); j++) {
            x.setColumn(j, x0.getColumn(0).add(I.getColumn(j - 1)));
        }
        return x;
    }
}
