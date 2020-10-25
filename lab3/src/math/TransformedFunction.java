package math;

import java.util.function.Function;

public class TransformedFunction implements Function<Matrix, Double> {

    private double t;

    private Function<Matrix, Double> f;

    private Function<Matrix, Matrix> g;

    private Function<Matrix, Matrix> h;

    public TransformedFunction(Function<Matrix, Double> f, Function<Matrix, Matrix> g, Function<Matrix, Matrix> h, double t) {
        this.f = f;
        this.g = g;
        this.h = h;
        this.t = t;
    }

    @Override
    public Double apply(Matrix o) {
        return f.apply(o) - 1 / t * getGLogSum(o) + t * getHSquareSum(o);
    }

    private double getGLogSum(Matrix o) {
        Matrix g = this.g.apply(o);
        double sum = 0;
        for (int i = 0; i < g.getRows(); i++) {
            double gi = g.getElement(i, 0);
            if (gi < 0) {
                return -Double.POSITIVE_INFINITY;
            } else {
                sum += Math.log(gi);
            }

        }
        return sum;
    }

    private double getHSquareSum(Matrix o) {
        Matrix h = this.h.apply(o);
        double sum = 0;
        for (int i = 0; i < h.getRows(); i++) {
            sum += Math.pow(h.getElement(i, 0), 2);
        }
        return sum;
    }

    public void increaseTransformationFactor() {
        this.t *= 10;
    }
}
