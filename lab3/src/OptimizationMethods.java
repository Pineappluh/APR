import algorithms.Box;
import algorithms.GradientDescent;
import algorithms.MixedTransformation;
import algorithms.NewtonRaphson;
import math.CachingFunction;
import math.CustomFunction;
import math.Matrix;
import math.TransformedFunction;
import sun.rmi.runtime.NewThreadAction;

import java.util.function.Function;

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

        CustomFunction<Matrix, Double> f3 = CustomFunction.createFunction3();
        Matrix x0 = new Matrix(1, 2, 0, 0);

        System.out.println("For f3 with x0: " + x0.transpose());
        printHyphenLine();
        try {
            printGradientDescentWithoutFindingStepResult(GradientDescent.search(x0, f3), f3);
        } catch(RuntimeException ex) {
            System.err.println(ex.getMessage());
        }
        try {
            printGradientDescentResult(GradientDescent.searchUsingGoldenRatio(x0, f3), f3);
        } catch(RuntimeException ex) {
            System.err.println(ex.getMessage());
        }

        System.out.println("\n\n");
    }

    private static void secondTask() {
        System.out.println("2. zad");
        printHyphenLine();

        CustomFunction<Matrix, Double> f1 = CustomFunction.createFunction1();
        Matrix x01 = new Matrix(1, 2, -1.9, 2);
        CustomFunction<Matrix, Double> f2 = CustomFunction.createFunction2();
        Matrix x02 = new Matrix(1, 2, 0.1, 0.3);

        System.out.println("For f1 with x0: " + x01.transpose());
        printHyphenLine();
        printGradientDescentResult(GradientDescent.searchUsingGoldenRatio(x01, f1), f1);
        printNewthonRaphsonResult(NewtonRaphson.searchUsingGoldenRatio(x01, f1), f1);
        printHyphenLine();


        System.out.println("For f2 with x0: " + x02.transpose());
        printHyphenLine();
        printGradientDescentResult(GradientDescent.searchUsingGoldenRatio(x02, f2), f2);
        printNewthonRaphsonResult(NewtonRaphson.searchUsingGoldenRatio(x02, f2), f2);
        printHyphenLine();

        System.out.println("\n\n");
    }

    private static void thirdTask() {
        System.out.println("3. zad");
        printHyphenLine();

        CustomFunction<Matrix, Double> f1 = CustomFunction.createFunction1();
        Matrix x01 = new Matrix(1, 2, -1.9, 2);
        CustomFunction<Matrix, Double> f2 = CustomFunction.createFunction2();
        Matrix x02 = new Matrix(1, 2, 0.1, 0.3);
        Function<Matrix, Double> g1 = x -> x.getElement(1, 0) - x.getElement(0, 0);
        Function<Matrix, Double> g2 = x -> 2 - x.getElement(0, 0);
        CustomFunction<Matrix, Matrix> g = new CustomFunction<>(
                new CachingFunction<>(x -> new Matrix(1, 2, g1.apply(x), g2.apply(x)))
        );
        Matrix xd = new Matrix(1, 2, -100, -100);
        Matrix xg = new Matrix(1, 2, 100, 100);

        System.out.println("For f1 with x0: " + x01.transpose());
        printHyphenLine();
        printBoxResult(Box.search(x01, xd, xg, f1, g), f1);
        printHyphenLine();

        System.out.println("For f2 with x0: " + x02.transpose());
        printHyphenLine();
        printBoxResult(Box.search(x02, xd, xg, f2, g), f2);
        printHyphenLine();

        System.out.println("\n\n");
    }

    private static void fourthTask() {
        System.out.println("4. zad");
        printHyphenLine();

        CustomFunction<Matrix, Double> f1 = CustomFunction.createFunction1();
        Matrix x01 = new Matrix(1, 2, -1.9, 2);
        CustomFunction<Matrix, Double> f2 = CustomFunction.createFunction2();
        Matrix x02 = new Matrix(1, 2, 0.1, 0.3);
        Function<Matrix, Double> g1 = x -> x.getElement(1, 0) - x.getElement(0, 0);
        Function<Matrix, Double> g2 = x -> 2 - x.getElement(0, 0);
        CustomFunction<Matrix, Matrix> g = new CustomFunction<>(
                new CachingFunction<>(x -> new Matrix(1, 2, g1.apply(x), g2.apply(x)))
        );
        CustomFunction<Matrix, Matrix> h = new CustomFunction<>(
                new CachingFunction<>(x -> new Matrix(0, 0, 0))
        );

        System.out.println("For f1 with x0: " + x01.transpose());
        printHyphenLine();
        printMixedTransformationResult(MixedTransformation.search(x01, f1, g, h), f1);
        printHyphenLine();

        System.out.println("For f2 with x0: " + x02.transpose());
        printHyphenLine();
        printMixedTransformationResult(MixedTransformation.search(x02, f2, g, h), f2);
        printHyphenLine();

        System.out.println("\n\n");
    }

    private static void fifthTask() {
        System.out.println("5. zad");
        printHyphenLine();

        CustomFunction<Matrix, Double> f = CustomFunction.createFunction4();
        Matrix x0 = new Matrix(1, 2, 5, 5);
        Function<Matrix, Double> g1 = x -> 3 - x.getElement(0, 0) - x.getElement(1, 0);
        Function<Matrix, Double> g2 = x -> 3 + 1.5 * x.getElement(0, 0) - x.getElement(1, 0);
        CustomFunction<Matrix, Matrix> g = new CustomFunction<>(
                new CachingFunction<>(x -> new Matrix(1, 2, g1.apply(x), g2.apply(x)))
        );
        Function<Matrix, Double> h1 = x -> x.getElement(1, 0) - 1;
        CustomFunction<Matrix, Matrix> h = new CustomFunction<>(
                new CachingFunction<>(x -> new Matrix(1, 1, h1.apply(x)))
        );

        System.out.println("For f4 with x0: " + x0.transpose());
        printHyphenLine();
        printMixedTransformationResult(MixedTransformation.search(x0, f, g, h), f);
        printHyphenLine();
    }

    private static void printHyphenLine() {
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    private static void printGradientDescentResult(Matrix result, CustomFunction<Matrix, Double> f) {
        printAlgorithmResult("Gradient descent", result, f);
    }

    private static void printGradientDescentWithoutFindingStepResult(Matrix result, CustomFunction<Matrix, Double> f) {
        printAlgorithmResult("Gradient descent without optimal step", result, f);
    }

    private static void printNewthonRaphsonResult(Matrix result, CustomFunction<Matrix, Double> f) {
        printAlgorithmResult("Newton-Raphson", result, f);
    }

    private static void printBoxResult(Matrix result, CustomFunction<Matrix, Double> f) {
        printAlgorithmResult("Box", result, f);
    }

    private static void printMixedTransformationResult(Matrix result, CustomFunction<Matrix, Double> f) {
        printAlgorithmResult("Mixed transofrmation", result, f);
    }

    private static void printAlgorithmResult(String algorithm, Matrix result, CustomFunction<Matrix, Double> f) {
        System.out.println(
                String.format("%25s xmin: %15s, f(xmin)=%f, f eval: %d, gradient eval: %d, hessian eval: %d",
                        algorithm,
                        result.transpose(),
                        f.apply(result),
                        f.getFunctionEvaluations(),
                        f.getGradient().getFunctionEvaluations(),
                        f.getHessianMatrix().getFunctionEvaluations()
                )
        );
        f.clearCache();
        f.getGradient().clearCache();
        f.getHessianMatrix().clearCache();
    }
}
