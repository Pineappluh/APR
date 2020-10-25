package math;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CachingFunction<T, R> implements Function<T, R>{

    private Function<T, R> f;

    private Map<T, R> cache = new HashMap<>();

    private int functionEvaluations = 0;

    public CachingFunction(Function<T, R> f) {
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

}
