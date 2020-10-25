package main;

import algorithms.GeneticAlgorithm;
import math.CustomFunction;
import math.Matrix;
import smile.plot.BoxPlot;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MainProgram {

    public static void main(String[] args) {
        // firstTask();

        // secondTask();

        // thirdTask();

        fourthTask();

        // fifthTask();
    }

    private static void printHyphenLine() {
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");
    }

    private static void firstTask() {
        System.out.println("1st task");
        printHyphenLine();

        System.out.println("For f1: ");
        printHyphenLine();
        GeneticAlgorithm.searchUsingDouble(
                new Matrix(1, 2, -50),
                new Matrix(1, 2, 150),
                200,
                0.1,
                50000,
                CustomFunction.createFunction1(),
                true
        );
        GeneticAlgorithm.searchUsingBinary(
                6,
                new Matrix(1, 2, -50),
                new Matrix(1, 2, 150),
                300,
                0.1,
                20000,
                CustomFunction.createFunction1(),
                true
        );
        printHyphenLine();

        System.out.println("For f3 with 5 variables: ");
        printHyphenLine();
        GeneticAlgorithm.searchUsingDouble(
                new Matrix(1, 5, -50),
                new Matrix(1, 5, 150),
                200,
                0.1,
                50000,
                CustomFunction.createFunction3(),
                true
        );
        GeneticAlgorithm.searchUsingBinary(
                6,
                new Matrix(1, 5, -50),
                new Matrix(1, 5, 150),
                300,
                0.1,
                20000,
                CustomFunction.createFunction3(),
                true
        );
        printHyphenLine();

        System.out.println("For f6 with 2 variables: ");
        printHyphenLine();
        GeneticAlgorithm.searchUsingDouble(
                new Matrix(1, 2, -50),
                new Matrix(1, 2, 150),
                500,
                0.3,
                100000,
                CustomFunction.createFunction6(),
                true
        );
        GeneticAlgorithm.searchUsingBinary(
                6,
                new Matrix(1, 2, -50),
                new Matrix(1, 2, 150),
                500,
                0.3,
                20000,
                CustomFunction.createFunction6(),
                true
        );
        printHyphenLine();

        System.out.println("For f7 with 2 variables: ");
        printHyphenLine();
        GeneticAlgorithm.searchUsingDouble(
                new Matrix(1, 2, -50),
                new Matrix(1, 2, 150),
                500,
                0.3,
                100000,
                CustomFunction.createFunction7(),
                true
        );
        GeneticAlgorithm.searchUsingBinary(
                6,
                new Matrix(1, 2, -50),
                new Matrix(1, 2, 150),
                500,
                0.3,
                20000,
                CustomFunction.createFunction7(),
                true
        );
        printHyphenLine();
    }

    private static void secondTask() {
        System.out.println("2nd task");
        printHyphenLine();

        for (Integer dim: Arrays.asList(1, 3, 6, 10)) {
            System.out.println(String.format("For f6 with %d variables: ", dim));
            printHyphenLine();
            GeneticAlgorithm.searchUsingDouble(
                    new Matrix(1, dim, -50),
                    new Matrix(1, dim, 150),
                    500,
                    0.3,
                    10000 * dim,
                    CustomFunction.createFunction6(),
                    true
            );
            GeneticAlgorithm.searchUsingBinary(
                    7,
                    new Matrix(1, dim, -50),
                    new Matrix(1, dim, 150),
                    500,
                    0.3,
                    5000 * dim,
                    CustomFunction.createFunction6(),
                    true
            );
            printHyphenLine();

            System.out.println(String.format("For f7 with %d variables: ", dim));
            printHyphenLine();
            GeneticAlgorithm.searchUsingDouble(
                    new Matrix(1, dim, -50),
                    new Matrix(1, dim, 150),
                    500,
                    0.3,
                    10000 * dim,
                    CustomFunction.createFunction7(),
                    true
            );
            GeneticAlgorithm.searchUsingBinary(
                    7,
                    new Matrix(1, dim, -50),
                    new Matrix(1, dim, 150),
                    500,
                    0.3,
                    5000 * dim,
                    CustomFunction.createFunction7(),
                    true
            );
            printHyphenLine();
        }
    }

    private static double getMedian(List<Double> values) {
        List<Double> valuesCopy = new ArrayList<>(values);
        Collections.sort(valuesCopy);
        if (valuesCopy.size() % 2 == 0)
            return (valuesCopy.get(valuesCopy.size() / 2) + valuesCopy.get(values.size() / 2 - 1)) / 2;
        else
            return valuesCopy.get(valuesCopy.size() / 2);
    }

    private static void thirdTask() {
        System.out.println("3rd task");
        printHyphenLine();

        for (Integer dim: Arrays.asList(3, 6)) {
            int numberOfHits = 0;
            List<Double> results = new ArrayList<>();
            System.out.println(String.format("For f6 with %d variables: ", dim));
            printHyphenLine();

            for (int i = 0; i < 10; i++) {
                double result = GeneticAlgorithm.searchUsingDouble(
                        new Matrix(1, dim, -50),
                        new Matrix(1, dim, 150),
                        1000,
                        0.3,
                        60000,
                        CustomFunction.createFunction6(),
                        false
                );
                if (Math.abs(result) < 1e-6) {
                    numberOfHits += 1;
                }
                results.add(result);
            }
            System.out.println(String.format("Double: median = %f, hits = %d", getMedian(results), numberOfHits));

            results.clear();
            numberOfHits = 0;
            for (int i = 0; i < 10; i++) {
                double result = GeneticAlgorithm.searchUsingBinary(
                        4,
                        new Matrix(1, dim, -50),
                        new Matrix(1, dim, 150),
                        1000,
                        0.3,
                        60000,
                        CustomFunction.createFunction6(),
                        false
                );
                if (Math.abs(result) < 1e-6) {
                    numberOfHits += 1;
                }
                results.add(result);
            }
            System.out.println(String.format("Binary: median = %f, hits = %d", getMedian(results), numberOfHits));
            printHyphenLine();

            System.out.println(String.format("For f7 with %d variables: ", dim));
            printHyphenLine();

            results.clear();
            numberOfHits = 0;
            for (int i = 0; i < 10; i++) {
                double result = GeneticAlgorithm.searchUsingDouble(
                        new Matrix(1, dim, -50),
                        new Matrix(1, dim, 150),
                        1000,
                        0.3,
                        60000,
                        CustomFunction.createFunction7(),
                        false
                );
                if (Math.abs(result) < 1e-6) {
                    numberOfHits += 1;
                }
                results.add(result);
            }
            System.out.println(String.format("Double: median = %f, hits = %d", getMedian(results), numberOfHits));

            results.clear();
            numberOfHits = 0;
            for (int i = 0; i < 10; i++) {
                double result = GeneticAlgorithm.searchUsingBinary(
                        4,
                        new Matrix(1, dim, -50),
                        new Matrix(1, dim, 150),
                        1000,
                        0.3,
                        60000,
                        CustomFunction.createFunction7(),
                        false
                );
                if (Math.abs(result) < 1e-6) {
                    numberOfHits += 1;
                }
                results.add(result);
            }
            System.out.println(String.format("Binary: median = %f, hits = %d", getMedian(results), numberOfHits));
            printHyphenLine();
        }
    }

    private static void fourthTask() {
        System.out.println("4th task");
        printHyphenLine();
        System.out.println("Finding optimal f6 parameters for double");

        List<Integer> populationParams = Arrays.asList(30, 50, 100, 200);
        List<Double> mutationProbabilityParams = Arrays.asList(0.1, 0.3, 0.6, 0.9);
        double defaultMutationProbability = mutationProbabilityParams.get(0);

        List<Double> populationMedianResults = new ArrayList<>();
        List<List<Double>> populationResults = new ArrayList<>();
        for (Integer population: populationParams) {
            List<Double> results = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                double result = GeneticAlgorithm.searchUsingDouble(
                        new Matrix(1, 3, -50),
                        new Matrix(1, 3, 150),
                        population,
                        defaultMutationProbability,
                        100000,
                        CustomFunction.createFunction7(),
                        false
                );
                results.add(result);
            }
            populationResults.add(results);
            populationMedianResults.add(getMedian(results));
        }
        int optimalPopulation = populationParams.get(
                populationMedianResults.indexOf(Collections.min(populationMedianResults))
        );
        printHyphenLine();
        System.out.println("-> Population param");
        System.out.println("Possible params: " + populationParams);
        System.out.println("Median results for those params: " + populationMedianResults);
        System.out.println("Optimal param: " + optimalPopulation);

        List<Double> mutationProbabilityMedianResults = new ArrayList<>();
        for (Double mutationProbability: mutationProbabilityParams) {
            List<Double> results = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                double result = GeneticAlgorithm.searchUsingDouble(
                        new Matrix(1, 3, -50),
                        new Matrix(1, 3, 150),
                        optimalPopulation,
                        mutationProbability,
                        100000,
                        CustomFunction.createFunction7(),
                        false
                );
                results.add(result);
            }
            mutationProbabilityMedianResults.add(getMedian(results));
        }
        double optimalMutationProbability = mutationProbabilityParams.get(
                mutationProbabilityMedianResults.indexOf(Collections.min(mutationProbabilityMedianResults))
        );
        printHyphenLine();
        System.out.println("-> Mutation probability param");
        System.out.println("Possible params: " + mutationProbabilityParams);
        System.out.println("Median results for those params: " + mutationProbabilityMedianResults);
        System.out.println("Optimal param: " + optimalMutationProbability);
        printHyphenLine();
        System.out.println("Showing box-plot for population params...");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Results");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setPreferredSize(new Dimension(800, 800));
                double[][] data = new double[populationResults.size()][];
                for (int i = 0; i < populationResults.size(); i++) {
                    List<Double> singleResult = populationResults.get(i);
                    double[] array = new double[singleResult.size()];
                    for (int j = 0; j < array.length; j++) {
                        array[j] = singleResult.get(j);
                    }
                    data[i] = array;
                }
                String[] labels = populationParams
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.toList())
                        .toArray(new String[populationParams.size()]);
                frame.add(BoxPlot.plot(data, labels));
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    private static void fifthTask() {
        System.out.println("5th task");
        printHyphenLine();

        for (Integer k: Arrays.asList(3, 4, 5, 7, 10, 15, 25, 50)) {
            System.out.println("Function f7 for k = " + k);
            GeneticAlgorithm.searchUsingDouble(
                    new Matrix(1, 2, -50),
                    new Matrix(1, 2, 150),
                    500,
                    0.2,
                    100000,
                    CustomFunction.createFunction7(),
                    true,
                    k
            );
            printHyphenLine();
        }
    }
}
