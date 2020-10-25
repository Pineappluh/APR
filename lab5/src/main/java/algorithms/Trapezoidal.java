package algorithms;

import math.Matrix;

import java.util.function.Function;

public class Trapezoidal extends CorrectorAlgorithm {

    @Override
    protected Matrix calculateNextValue(double t, double T, Matrix x, Matrix A, Matrix B, Function<Double, Matrix> r) {
        Matrix I = Matrix.createIdentityMatrix(x.getRows());
        return Matrix.solveWithLUP(
                I.subtract(A.multiply(T / 2)),
                x.add(getDerivationValue(t, x, A, B, r).add(B.multiply(r.apply(t + T))).multiply(T / 2))
        );
    }

    @Override
    protected Matrix calculateNextValue(double t, double T, Matrix x, Matrix A, Matrix prediction, Matrix B, Function<Double, Matrix> r) {
        return x.add(
                getDerivationValue(t, x, A, B, r)
                        .add(getDerivationValue(t + T, prediction, A, B, r))
                        .multiply(T / 2)
        );

    }
}
