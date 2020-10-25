package algorithms;

import java.util.function.Function;

public class GoldenSection {

    private static final double GOLDEN_RATIO = 0.5 * (Math.sqrt(5) - 1);

    private static final double DEFAULT_PRECISION = 10e-6;

    private static final double DEFAULT_SEARCH_SHIFT = 1;

    public static double[] findUnimodalInterval(Function<Double, Double> f, double h, double point) {
        double l = point - h;
        double r = point + h;
        double m = point;
        double fl, fm, fr;
        int step = 1;

        fm = f.apply(point);
        fl = f.apply(l);
        fr = f.apply(r);

        if (fm < fr && fm < fl) {
            return new double[] {l, r};
        } else if(fm > fr) {
            do {
                l = m;
                m = r;
                fm = fr;
                r = point + h * (step *= 2);
                fr = f.apply(r);
            } while(fm > fr);
        } else {
            do {
                r = m;
                m = l;
                fm = fl;
                l = point - h * (step *= 2);
                fl = f.apply(l);
            } while(fm > fl);
        }
        return new double[] {l, r};

    }

    public static double search(double start, Function<Double, Double> f) {
        return search(start, f, DEFAULT_SEARCH_SHIFT);
    }

    public static double search(double start, Function<Double, Double> f, double shift) {
        return search(start, f, DEFAULT_PRECISION, shift);
    }

    public static double search(double start, Function<Double, Double> f, double e, double shift) {
        double[] interval = findUnimodalInterval(f, shift, start);
        return search(interval[0], interval[1], f, e);
    }

    public static double search(double a, double b, Function<Double, Double> f) {
        return search(a, b, f, DEFAULT_PRECISION);
    }

    public static double search(double a, double b, Function<Double, Double> f, double e) {
        double c = b - (b - a) * GOLDEN_RATIO;
        double d = a + (b - a) * GOLDEN_RATIO;
        double fc = f.apply(c);
        double fd = f.apply(d);
        // printFormattedGoldenRatioValues(a, f.apply(a), b, f.apply(b), c, fc, d, fd);
        while ((b - a) > e) {
            if (fc < fd) {
                b = d;
                d = c;
                c = b - (b - a) * GOLDEN_RATIO;
                fd = fc;
                fc = f.apply(c);
            } else {
                a = c;
                c = d;
                d = a + (b - a) * GOLDEN_RATIO;
                fc = fd;
                fd = f.apply(d);
            }
            // printFormattedGoldenRatioValues(a, f.apply(a), b, f.apply(b), c, fc, d, fd);
        }
        return (a + b) / 2;
    }


    private static void printFormattedGoldenRatioValues(double a, double fa,
                                                        double b, double fb,
                                                        double c, double fc,
                                                        double d, double fd) {
        System.out.println(String.format("a = %f, f(a) = %f", a, fa));
        System.out.println(String.format("b = %f, f(b) = %f", b, fb));
        System.out.println(String.format("c = %f, f(c) = %f", c, fc));
        System.out.println(String.format("d = %f, f(d) = %f\n", d, fd));
    }
}
