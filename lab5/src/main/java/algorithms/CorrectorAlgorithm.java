package algorithms;

import math.Matrix;

import java.util.function.Function;

public abstract class CorrectorAlgorithm extends Algorithm {

    protected abstract Matrix calculateNextValue(double t, double T, Matrix x, Matrix A, Matrix prediction, Matrix B, Function<Double, Matrix> r);
}
