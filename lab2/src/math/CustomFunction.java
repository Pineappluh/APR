package math;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CustomFunction<T, R> implements Function<T, R> {

    private Function<T, R> f;

    private Map<T, R> cache = new HashMap<>();

    private int functionEvaluations = 0;

    public CustomFunction(Function<T, R> f) {
        this.f = f;
    }

    @Override
    public R apply(T o) {
        if (cache.containsKey(o)) {
            return cache.get(o);
        } else {
            R value = f.apply(o);
            cache.put(o, value);
            functionEvaluations++;
            return value;
        }
    }

    public int getFunctionEvaluations() {
        return functionEvaluations;
    }

    public void clearCache() {
        functionEvaluations = 0;
        cache.clear();
    }

    public static CustomFunction<Matrix, Double> createFunction1() {
        return new CustomFunction<>(x -> {
            double x1 = x.getElement(0, 0);
            double x2 = x.getElement(1, 0);
            return 100 * Math.pow(x2 - Math.pow(x1, 2), 2) + Math.pow(1 - x1, 2);
        });
    }

    public static CustomFunction<Matrix, Double> createFunction2() {
        return new CustomFunction<>(x -> {
            double x1 = x.getElement(0, 0);
            double x2 = x.getElement(1, 0);
            return Math.pow(x1 - 4, 2) + 4 * Math.pow(x2 - 2, 2);
        });
    }

    public static CustomFunction<Matrix, Double> createFunction3() {
        return new CustomFunction<>(x -> {
            double sum = 0;
            for(int i = 1; i <= x.getRows(); i++) {
                sum += Math.pow(x.getElement(i - 1, 0) - i, 2);
            }
            return sum;
        });
    }

    public static CustomFunction<Matrix, Double> createFunction4() {
        return new CustomFunction<>(x -> {
            double x1 = x.getElement(0, 0);
            double x2 = x.getElement(1, 0);
            return Math.abs((x1 - x2) * (x1 + x2)) + Math.sqrt(Math.pow(x1, 2) + Math.pow(x2, 2));
        });
    }

    public static CustomFunction<Matrix, Double> createFunction6() {
        return new CustomFunction<>(x -> {
            double sumOfSquares = 0;
            for(int i = 0; i < x.getRows(); i++) {
                sumOfSquares += Math.pow(x.getElement(i, 0), 2);
            }
            return 0.5 + (Math.pow(Math.sin(Math.sqrt(sumOfSquares)), 2) - 0.5) / Math.pow(1 + 0.001 * sumOfSquares, 2);
        });
    }
}
