import algorithms.*;
import math.Matrix;
import plot.Plotter;

import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Main {

    private static final Map<String, Algorithm> algorithms;

    static {
        Map<String, Algorithm> map = new HashMap<>();
        map.put("Euler", new Euler());
        map.put("Backward Euler", new BackwardEuler());
        map.put("Trapezoidal", new Trapezoidal());
        map.put("Runge-Kutta", new RungeKutta());
        map.put("PE(CE)\u00B2", new PredictorCorrector(new Euler(), new BackwardEuler(), 2));
        map.put("PECE", new PredictorCorrector(new Euler(), new Trapezoidal(), 1));
        algorithms = Collections.unmodifiableMap(map);
    }

    public static void main(String[] args) {
        // firstTask();

        // secondTask();

        // thirdTask();

        // fourthTask();
        Matrix A = new Matrix(2, 2, 0, -1000, 100, 0);
        Matrix B = new Matrix(2, 2, 100, 0, 0, 0);
        Function<Double, Matrix> r = (t -> new Matrix(1, 2, 1));
        Matrix x0 = new Matrix(1,  2, 0);
        System.out.println(new BackwardEuler().solveWithSteps(0.1, 0.2, A, x0, B, r));
    }

    private static void firstTask() {
        printHyphenLine();
        System.out.println("1st task");
        printHyphenLine();
        Matrix A = new Matrix(2, 2, 0, 1, -1, 0);
        Matrix B = new Matrix(2, 2, 0);
        Function<Double, Matrix> r = (t -> new Matrix(1, 2, 0));
        Matrix x0 = new Matrix(1,  2, 1, 1);
        double T = 0.01;
        double tMax = 10;

        UnaryOperator<Double> f1 = (t -> x0.getElement(0, 0) * Math.cos(t) + x0.getElement(0, 1) * Math.sin(t));
        UnaryOperator<Double> f2 = (t -> x0.getElement(0, 1) * Math.cos(t) - x0.getElement(0, 0) * Math.sin(t));

        System.out.println("Real value in tMax: " + new Matrix(1, 2, f1.apply(tMax), f2.apply(tMax)).transpose());
        printHyphenLine();

        Plotter plotter = new Plotter();

        for (Map.Entry<String, Algorithm> algorithms: algorithms.entrySet()) {
            double totalDiff = 0;
            List<Matrix> obtainedSolutionWithSteps = algorithms.getValue().solveWithSteps(T, tMax, A, x0, B, r);

            for (int i = 0; i <= tMax / T; i++) {
                double t = i * T;
                Matrix realSolution = new Matrix(1, 2, f1.apply(t), f2.apply(t));
                Matrix obtainedSolution = obtainedSolutionWithSteps.get(i);

                double diff = 0;
                for (int j = 0; j < realSolution.getRows(); j++) {
                    diff += Math.abs(realSolution.getElement(0, j) - obtainedSolution.getElement(0, j));
                }
                totalDiff += diff;
            }

            System.out.println(
                    String.format("%15s - obtained value: %s, total difference: %f",
                            algorithms.getKey(),
                            obtainedSolutionWithSteps.get(obtainedSolutionWithSteps.size() - 1).transpose(),
                            totalDiff
                    )
            );

            plotter.addPlot(algorithms.getKey(), obtainedSolutionWithSteps, T);
        }

        plotter.showPlots("1st task");

        printHyphenLine();
        System.out.println("\n");
    }

    private static void secondTask() {
        printHyphenLine();
        System.out.println("2nd task");
        printHyphenLine();
        Matrix A = new Matrix(2, 2, 0, 1, -200, -102);
        Matrix x0 = new Matrix(1,  2, 1, -2);
        Matrix B = new Matrix(2, 2, 0);
        Function<Double, Matrix> r = (t -> new Matrix(1, 2, 0));
        double T = 0.1;
        double tMax = 1;

        printObtainedValues(A, x0, B, r, T, tMax, "2nd task");
        printHyphenLine();
        System.out.println("\n");
    }

    private static void thirdTask() {
        printHyphenLine();
        System.out.println("3rd task");
        printHyphenLine();
        Matrix A = new Matrix(2, 2, 0, -2, 1, -3);
        Matrix x0 = new Matrix(1,  2, 1, 3);
        Matrix B = new Matrix(2, 2, 2, 0, 0, 3);
        Function<Double, Matrix> r = (t -> new Matrix(1, 2, 1, 1));
        double T = 0.01;
        double tMax = 10;

        printObtainedValues(A, x0, B, r, T, tMax, "3rd task");
        printHyphenLine();
        System.out.println("\n");
    }

    private static void fourthTask() {
        printHyphenLine();
        System.out.println("4th task");
        printHyphenLine();
        Matrix A = new Matrix(2, 2, 1, -5, 1, -7);
        Matrix x0 = new Matrix(1,  2, -1, 3);
        Matrix B = new Matrix(2, 2, 5, 0, 0, 3);
        Function<Double, Matrix> r = (t -> new Matrix(1, 2, t, t));
        double T = 0.01;
        double tMax = 1;

        printObtainedValues(A, x0, B, r, T, tMax, "4th task");
        printHyphenLine();
        System.out.println("\n");
    }

    private static void printHyphenLine() {
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    private static void printObtainedValues(Matrix A, Matrix x0, Matrix B, Function<Double, Matrix> r, double T, double tMax, String task) {
        Plotter plotter = new Plotter();

        for (Map.Entry<String, Algorithm> algorithms: algorithms.entrySet()) {
            Matrix obtainedSolution = algorithms.getValue().solve(T, tMax, A, x0, B, r);

            System.out.println(
                    String.format("%15s - obtained value: %s",
                            algorithms.getKey(),
                            obtainedSolution.transpose()
                    )
            );

            List<Matrix> obtainedSolutionWithSteps = algorithms.getValue().solveWithSteps(T, tMax, A, x0, B, r);
            plotter.addPlot(algorithms.getKey(), obtainedSolutionWithSteps, T);
        }

        plotter.showPlots(task);
    }
}
