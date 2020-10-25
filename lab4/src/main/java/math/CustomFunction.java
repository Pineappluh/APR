package math;

import java.util.function.Function;

public class CustomFunction<T, R> implements Function<T, R> {

    private CachingFunction<T, R> f;

    private CachingFunction<T, T> gradient;

    private CachingFunction<T, T> hessian;

    public CustomFunction(CachingFunction<T, R> f) {
        this.f = f;
    }

    public CustomFunction(CachingFunction<T, R> f, CachingFunction<T, T> gradient) {
        this.f = f;
        this.gradient = gradient;
    }

    public CustomFunction(CachingFunction<T, R> f, CachingFunction<T, T> gradient, CachingFunction<T, T> hessian) {
        this.f = f;
        this.gradient = gradient;
        this.hessian = hessian;
    }

    @Override
    public R apply(T o) {
        return f.apply(o);
    }

    public int getFunctionEvaluations() {
        return f.getFunctionEvaluations();
    }

    public void clearCache() {
        f.clearCache();
    }

    public CachingFunction<T, T> getGradient() {
        if (gradient == null) {
            throw new RuntimeException("Gradient is not defined.");
        }
        return gradient;
    }

    public CachingFunction<T, T> getHessianMatrix() {
        if (hessian == null) {
            throw new RuntimeException("Hessian matrix is not defined.");
        }
        return hessian;
    }

    public static CustomFunction<Matrix, Double> createFunction1() {
        CachingFunction<Matrix, Double> f1 = new CachingFunction<>(x -> {
            double x1 = x.getElement(0, 0);
            double x2 = x.getElement(1, 0);
            return 100 * Math.pow(x2 - Math.pow(x1, 2), 2) + Math.pow(1 - x1, 2);
        });

        Function<Matrix, Double> fpx1 = x -> {
            double x1 = x.getElement(0, 0);
            double x2 = x.getElement(1, 0);
            return -400 * x1 * (x2 - Math.pow(x1, 2)) - 2 * (1 - x1);
        };

        Function<Matrix, Double> fpx2 = x -> {
            double x1 = x.getElement(0, 0);
            double x2 = x.getElement(1, 0);
            return 200 * (x2 - Math.pow(x1, 2));
        };

        CachingFunction<Matrix, Matrix> gradient = new CachingFunction<>(
                x -> new Matrix(1, 2, fpx1.apply(x), fpx2.apply(x))
        );


        Function<Matrix, Double> fpx11 = x -> {
            double x1 = x.getElement(0, 0);
            double x2 = x.getElement(1, 0);
            return - 400 * (x2 - 3 * Math.pow(x1, 2)) + 2;
        };
        Function<Matrix, Double> fpx12 = x -> -400 * x.getElement(0, 0);
        Function<Matrix, Double> fpx21 = x -> -400 * x.getElement(0, 0);
        Function<Matrix, Double> fpx22 = x -> 200.;

        CachingFunction<Matrix, Matrix> hessian = new CachingFunction<>(x -> new Matrix(
                2, 2,
                fpx11.apply(x), fpx21.apply(x),
                fpx12.apply(x), fpx22.apply(x)
        ));

        return new CustomFunction<>(f1, gradient, hessian);
    }

    public static CustomFunction<Matrix, Double> createFunction2() {
        CachingFunction<Matrix, Double> f2 = new CachingFunction<>(x -> {
            double x1 = x.getElement(0, 0);
            double x2 = x.getElement(1, 0);
            return Math.pow(x1 - 4, 2) + 4 * Math.pow(x2 - 2, 2);
        });

        Function<Matrix, Double> fpx1 = x -> {
            double x1 = x.getElement(0, 0);
            return 2 * (x1 - 4);
        };

        Function<Matrix, Double> fpx2 = x -> {
            double x2 = x.getElement(1, 0);
            return 8 * (x2 - 2);
        };

        CachingFunction<Matrix, Matrix> gradient = new CachingFunction<>(
                x -> new Matrix(1, 2, fpx1.apply(x), fpx2.apply(x))
        );


        Function<Matrix, Double> fpx11 = x -> 2.;
        Function<Matrix, Double> fpx12 = x -> 0.;
        Function<Matrix, Double> fpx21 = x -> 0.;
        Function<Matrix, Double> fpx22  = x -> 8.;

        CachingFunction<Matrix, Matrix> hessian = new CachingFunction<>(x -> new Matrix(
                2, 2,
                fpx11.apply(x), fpx21.apply(x),
                fpx12.apply(x), fpx22.apply(x)
        ));

        return new CustomFunction<>(f2, gradient, hessian);
    }

    public static CustomFunction<Matrix, Double> createFunction3() {
        CachingFunction<Matrix, Double> f3 = new CachingFunction<>(x -> {
            double sum = 0;
            for(int i = 1; i <= x.getRows(); i++) {
                sum += Math.pow(x.getElement(i - 1, 0) - i, 2);
            }
            return sum;
        });

        return new CustomFunction<>(f3);
    }

    public static CustomFunction<Matrix, Double> createFunction4() {
        CachingFunction<Matrix, Double> f4 = new CachingFunction<>(x -> {
            double x1 = x.getElement(0, 0);
            double x2 = x.getElement(1, 0);
            return Math.pow(x1 - 3, 2) + Math.pow(x2, 2);
        });

        Function<Matrix, Double> fpx1 = x -> {
            double x1 = x.getElement(0, 0);
            return 2 * (x1 - 3);
        };

        Function<Matrix, Double> fpx2 = x -> {
            double x2 = x.getElement(1, 0);
            return 2 * x2;
        };

        CachingFunction<Matrix, Matrix> gradient = new CachingFunction<>(
                x -> new Matrix(1, 2, fpx1.apply(x), fpx2.apply(x))
        );


        Function<Matrix, Double> fpx11 = x -> 2.;
        Function<Matrix, Double> fpx12 = x -> 0.;
        Function<Matrix, Double> fpx21 = x -> 0.;
        Function<Matrix, Double> fpx22  = x -> 2.;

        CachingFunction<Matrix, Matrix> hessian = new CachingFunction<>(x -> new Matrix(
                2, 2,
                fpx11.apply(x), fpx21.apply(x),
                fpx12.apply(x), fpx22.apply(x)
        ));

        return new CustomFunction<>(f4, gradient, hessian);
    }

    public static CustomFunction<Matrix, Double> createFunction6() {
        CachingFunction<Matrix, Double> f6 = new CachingFunction<>(x -> {
            double sumOfSquares = 0;
            for(int i = 0; i < x.getRows(); i++) {
                sumOfSquares += Math.pow(x.getElement(i, 0), 2);
            }
            return 0.5 + (Math.pow(Math.sin(Math.sqrt(sumOfSquares)), 2) - 0.5) / Math.pow(1 + 0.001 * sumOfSquares, 2);
        });

        return new CustomFunction<>(f6);
    }

    public static CustomFunction<Matrix, Double> createFunction7() {
        CachingFunction<Matrix, Double> f7 = new CachingFunction<>(x -> {
            double sumOfSquares = 0;
            for(int i = 0; i < x.getRows(); i++) {
                sumOfSquares += Math.pow(x.getElement(i, 0), 2);
            }
            return Math.pow(sumOfSquares, 0.25) * (1 + Math.pow(Math.sin(50 * Math.pow(sumOfSquares, 0.1)), 2));
        });

        return new CustomFunction<>(f7);
    }
}
