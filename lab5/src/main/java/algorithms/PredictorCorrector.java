package algorithms;

import math.Matrix;

import java.util.function.Function;

public class PredictorCorrector extends Algorithm {

    private Algorithm predictor;

    private CorrectorAlgorithm corrector;

    private int s;

    public PredictorCorrector(Algorithm predictor, CorrectorAlgorithm corrector, int s) {
        this.predictor = predictor;
        this.corrector = corrector;
        this.s = s;
    }

    @Override
    protected Matrix calculateNextValue(double t, double T, Matrix x, Matrix A, Matrix B, Function<Double, Matrix> r) {
        Matrix nextX = predictor.calculateNextValue(t, T, x, A, B, r);
        for (int j = 0; j < s; j++) {
            nextX = corrector.calculateNextValue(t, T, x, A, nextX, B, r);
        }
        return nextX;
    }
}
