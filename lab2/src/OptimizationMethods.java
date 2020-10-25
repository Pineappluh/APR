import algorithms.CoordinateDescent;
import algorithms.GoldenSection;
import algorithms.HookeJeeves;
import algorithms.Simplex;
import math.CustomFunction;
import math.Matrix;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OptimizationMethods {

    public static void main(String[] args) {
        firstTask();

        secondTask();

        thirdTask();

        fourthTask();

        fifthTask();
    }

    private static void firstTask() {
        System.out.println("1. zad");
        printHyphenLine();

        for (int x0 = 10; x0 < 40; x0 += 5) {
            CustomFunction<Double, Double> f = new CustomFunction<>(x -> Math.pow(x - 3, 2));
            CustomFunction<Matrix, Double> F = new CustomFunction<>(X -> Math.pow(X.getElement(0, 0) - 3, 2));
            Matrix X0 = new Matrix(1, 1, x0);
            double minimum = GoldenSection.search(10, f, 2);

            System.out.println("For x0: " + x0);
            printHyphenLine();
            printGoldenSectionResult(new Matrix(1, 1, minimum), f.apply(minimum), f.getFunctionEvaluations());
            f.clearCache();
            printAlgorithmResults(F, X0);
            printHyphenLine();
        }

        System.out.println("\n\n");
    }

    private static void secondTask() {
        System.out.println("2. zad");
        printHyphenLine();

        CustomFunction<Matrix, Double> f1 = CustomFunction.createFunction1();
        CustomFunction<Matrix, Double> f2 = CustomFunction.createFunction2();
        CustomFunction<Matrix, Double> f3 = CustomFunction.createFunction3();
        CustomFunction<Matrix, Double> f4 = CustomFunction.createFunction4();
        Matrix x01 = new Matrix(1, 2, -1.9, 2);
        Matrix x02 = new Matrix(1, 2, 0.1, 0.3);
        Matrix x03 = new Matrix(1, 5, 0);
        Matrix x04 = new Matrix(1, 2, 5.1, 1.1);

        List<CustomFunction<Matrix, Double>> functionList = Arrays.asList(f1, f2, f3, f4);
        List<Matrix> x0List = Arrays.asList(x01, x02, x03, x04);

        for (int i = 0; i < functionList.size(); i++) {
            CustomFunction<Matrix, Double> F = functionList.get(i);
            Matrix X0 = x0List.get(i);

            System.out.println("For x0: " + X0.transpose());
            printHyphenLine();
            printAlgorithmResults(F, X0);
            printHyphenLine();
        }

        System.out.println("\n\n");
    }


    private static void thirdTask() {
        System.out.println("3. zad");
        printHyphenLine();

        CustomFunction<Matrix, Double> f4 = CustomFunction.createFunction4();
        Matrix x04 = new Matrix(1, 2, 5, 5);

        Matrix min = Simplex.search(x04, f4);
        printSimplexResult(min, f4.apply(min), f4.getFunctionEvaluations());
        f4.clearCache();

        min = HookeJeeves.search(x04, f4);
        printHookeJeevesResult(min, f4.apply(min), f4.getFunctionEvaluations());
        f4.clearCache();

        printHyphenLine();
        System.out.println("\n\n");
    }


    private static void fourthTask() {
        System.out.println("4. zad");
        printHyphenLine();

        CustomFunction<Matrix, Double> f1 = CustomFunction.createFunction1();
        List<Matrix> x01List = Arrays.asList(new Matrix(1, 2, 0.5, 0.5), new Matrix(1, 2, 20, 20));
        Matrix min;

        for(Matrix x01: x01List) {
            System.out.println("For x0: " + x01.transpose() + ", sigma from 1 to 20");
            printHyphenLine();
            for (int sigma = 1; sigma <= 20; sigma++) {
                min = Simplex.search(x01, f1, sigma);
                printSimplexResult(min, f1.apply(min), f1.getFunctionEvaluations());
                f1.clearCache();
            }
            printHyphenLine();
        }

        System.out.println("\n\n");
    }


    private static void fifthTask() {
        System.out.println("5. zad");
        printHyphenLine();

        Random r = new Random();
        CustomFunction<Matrix, Double> f6 = CustomFunction.createFunction6();
        int maxPoint = 50, minPoint = -50, hits = 0, tries = 100000;
        double allowedError = 10e-4;

        for(int i = 0; i < tries; i++) {
            Matrix x0 = new Matrix(1, 1, r.nextInt((maxPoint - minPoint) + 1) + minPoint);
            double minFound = f6.apply(Simplex.search(x0, f6));
            if (minFound < allowedError) {
                hits++;
            }
        }

        System.out.println(String.format("Probability of finding Schaffer's function's global minimum using simplex: %f%%", 100. * hits / tries));
        printHyphenLine();
    }

    private static void printHyphenLine() {
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    private static void printCoordinateDescentResult(Matrix result, double value, int functionEvaluations) {
        printAlgorithmResult("Coordinate descent", result, value, functionEvaluations);
    }

    private static void printGoldenSectionResult(Matrix result, double value, int functionEvaluations) {
        printAlgorithmResult("Golden section", result, value, functionEvaluations);
    }

    private static void printHookeJeevesResult(Matrix result, double value, int functionEvaluations) {
        printAlgorithmResult("Hooke-Jeeves", result, value, functionEvaluations);
    }

    private static void printSimplexResult(Matrix result, double value, int functionEvaluations) {
        printAlgorithmResult("Simplex", result, value, functionEvaluations);
    }

    private static void printAlgorithmResult(String algorithm, Matrix result, double value, int functionEvaluations) {
        System.out.println(
                String.format("%25s xmin: %15s, f(xmin)=%f, function evaluations: %d",
                        algorithm,
                        result.transpose(),
                        value,
                        functionEvaluations
                )
        );

    }

    private static void printAlgorithmResults(CustomFunction<Matrix, Double> F, Matrix X0) {
        Matrix min = CoordinateDescent.search(X0, F);
        printCoordinateDescentResult(min, F.apply(min), F.getFunctionEvaluations());
        F.clearCache();

        min = Simplex.search(X0, F);
        printSimplexResult(min, F.apply(min), F.getFunctionEvaluations());
        F.clearCache();

        min = HookeJeeves.search(X0, F);
        printHookeJeevesResult(min, F.apply(min), F.getFunctionEvaluations());
        F.clearCache();
    }
}
