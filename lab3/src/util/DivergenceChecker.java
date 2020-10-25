package util;

public class DivergenceChecker {

    private static int badIterations = 0;

    private static Double lastValue;

    public static boolean check(double value) {
        if (lastValue != null) {
            if (Double.compare(value, lastValue) >= 0) {
                badIterations++;
            } else {
                badIterations = 0;
            }

            if (badIterations == 100) {
                finishedSearch();
                return true;
            }
        }

        lastValue = value;
        return false;
    }

    public static void finishedSearch() {
        badIterations = 0;
        lastValue = null;
    }
}
