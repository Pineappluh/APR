package algorithms;

import math.Matrix;

import java.util.function.Function;

public class Euler extends Algorithm {

    @Override
    protected Matrix calculateNextValue(double t, double T, Matrix x, Matrix A, Matrix B, Function<Double, Matrix> r) {
        return x.add(getDerivationValue(t, x, A, B, r).multiply(T));
    }
}
