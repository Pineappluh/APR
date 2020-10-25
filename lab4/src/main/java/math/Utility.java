package math;

import java.util.Random;

public class Utility {

    public static double getRandomDoubleBetween(double min, double max) {
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }

    public static int getRandomIntBetween(int min, int max) {
        Random r = new Random();
        return min + r.nextInt((max - min) + 1);
    }
}
