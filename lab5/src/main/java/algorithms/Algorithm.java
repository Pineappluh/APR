package algorithms;

import math.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class Algorithm {

    public List<Matrix> solveWithSteps(double T, double tMax, Matrix A, Matrix x0, Matrix B, Function<Double, Matrix> r) {
        return solveWithSteps(T, tMax, A, x0, B, r, null);
    }

    public List<Matrix> solveWithSteps(double T, double tMax, Matrix A, Matrix x0, Matrix B, Function<Double, Matrix> r, Integer outputFrequency) {
        List<Matrix> points = new ArrayList<>();
        points.add(x0);
        Matrix x = x0.copy();
        for (int i = 0; i < tMax / T; i += 1) {
            double t = i * T;
            printIfNecessary(x, i, outputFrequency, t);
            x = calculateNextValue(t, T, x, A, B, r);
            points.add(x);
        }
        return points;
    }

    public Matrix solve(double T, double tMax, Matrix A, Matrix x0, Matrix B, Function<Double, Matrix> r) {
        return solve(T, tMax, A, x0, B, r, null);
    }

    public Matrix solve(double T, double tMax, Matrix A, Matrix x0, Matrix B, Function<Double, Matrix> r, Integer outputFrequency) {
        Matrix x = x0.copy();
        for (int i = 0; i < tMax / T; i += 1) {
            double t = i * T;
            printIfNecessary(x, i, outputFrequency, t);
            x = calculateNextValue(t, T, x, A, B, r);
        }
        return x;
    }

    private void printIfNecessary(Matrix x, int i, Integer outputFrequency, double t) {
        if (outputFrequency != null && i % outputFrequency == 0) {
            System.out.println(String.format("t = %f: %s", t, x.transpose()));
        }
    }

    Matrix getDerivationValue(double t, Matrix x, Matrix A, Matrix B, Function<Double, Matrix> r) {
        return A.multiply(x).add(B.multiply(r.apply(t)));
    }

    protected abstract Matrix calculateNextValue(double t, double T, Matrix x, Matrix A, Matrix B, Function<Double, Matrix> r);
}
