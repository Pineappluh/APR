package algorithms;

import math.Matrix;

import java.util.function.Function;

public class RungeKutta extends Algorithm {

    @Override
    protected Matrix calculateNextValue(double t, double T, Matrix x, Matrix A, Matrix B, Function<Double, Matrix> r) {
        Matrix M1 = getM1(A, x, B, r, t);
        Matrix M2 = getM2(A, x, T, M1, B, r, t);
        Matrix M3 = getM3(A, x, T, M2, B, r, t);
        Matrix M4 = getM4(A, x, T, M3, B, r, t);
        return  x.add(M1.add(M2.multiply(2)).add(M3.multiply(2)).add(M4).multiply(T / 6));
    }

    private Matrix getM1(Matrix A, Matrix x, Matrix B, Function<Double, Matrix> r, double t) {
        return getDerivationValue(t, x, A, B, r);
    }

    private Matrix getM2(Matrix A, Matrix x, double T, Matrix M1, Matrix B, Function<Double, Matrix> r, double t) {
        return getDerivationValue(t + T / 2, x.add(M1.multiply(T / 2)), A, B, r);
    }

    private Matrix getM3(Matrix A, Matrix x, double T, Matrix M2, Matrix B, Function<Double, Matrix> r, double t) {
        return getDerivationValue(t + T / 2, x.add(M2.multiply(T / 2)), A, B, r);
    }

    private Matrix getM4(Matrix A, Matrix x, double T, Matrix M3, Matrix B, Function<Double, Matrix> r, double t) {
        return getDerivationValue(t + T, x.add(M3.multiply(T)), A, B, r);
    }
}
