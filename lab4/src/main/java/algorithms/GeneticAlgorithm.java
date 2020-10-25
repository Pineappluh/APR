package algorithms;

import math.CustomFunction;
import math.Matrix;
import math.Utility;
import smile.plot.BoxPlot;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class GeneticAlgorithm {

    private static final int DEFAULT_PRECISION = 7;

    private static final double DEFAULT_MUTATION_PROBABILITY = 0.1;

    private static final int DEFAULT_POPULATION_SIZE = 200;

    private static final int DEFAULT_TOURNAMENT_SIZE = 3;

    private Matrix lowerBound;

    private Matrix upperBound;

    private int populationSize;

    private double mutationProbability;

    private int maxFunctionCalls;

    private CustomFunction<Matrix, Double> f;

    private boolean binary;

    private int precision;

    private int dim;

    private int k;

    private List<Integer> lengths = new ArrayList<>();

    public GeneticAlgorithm(Matrix lowerBound, Matrix upperBound, int populationSize, double mutationProbability,
                            int maxFunctionCalls, CustomFunction<Matrix, Double> f, int k) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.populationSize = populationSize;
        this.mutationProbability = mutationProbability;
        this.maxFunctionCalls = maxFunctionCalls;
        this.f = f;
        this.k = k;
        this.binary = false;
        this.dim = upperBound.getRows();
    }

    public GeneticAlgorithm(Matrix lowerBound, Matrix upperBound, int populationSize, double mutationProbability,
                            int maxFunctionCalls, CustomFunction<Matrix, Double> f, int k, int precision) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.populationSize = populationSize;
        this.mutationProbability = mutationProbability;
        this.maxFunctionCalls = maxFunctionCalls;
        this.f = f;
        this.k = k;
        this.binary = true;
        this.precision = precision;
        this.dim = upperBound.getRows();
        setUpChromosomeLengths();
    }

    private void setUpChromosomeLengths() {
        int dim = upperBound.getRows();
        for (int i = 0; i < dim; i++) {
            double min = lowerBound.getElement(i, 0);
            double max = upperBound.getElement(i, 0);
            double n = Math.log10(Math.floor(1 + (max - min) * Math.pow(10, precision))) / Math.log10(2);
            lengths.add((int) Math.ceil(n));
        }
    }

    private Double getFitness(Matrix x) {
        if (this.binary) {
            return -f.apply(mapBinaryToDouble(x));
        } else {
            return -f.apply(x);
        }
    }

    private String getChromosomeFromValue(Integer x, int length) {
        return String.format("%" + length + "s", Integer.toBinaryString((x))).replace(" ", "0");
    }

    private Integer getValueFromChromosome(String chromosome) {
        return Integer.parseInt(chromosome, 2);
    }

    private Double mapBinaryValueToDouble(Integer b, Double lowerBound, Double upperBound, int length) {
        return lowerBound + b / (Math.pow(2, length) - 1) * (upperBound - lowerBound);
    }

    private Matrix mapBinaryToDouble(Matrix x) {
        return x.map((i, b) -> mapBinaryValueToDouble(b.intValue(), lowerBound.getElement(i, 0), upperBound.getElement(i, 0), lengths.get(i)));
    }

    private Matrix getSinglePointCrossoverOffspring(Matrix parent1, Matrix parent2) {
        double[] child1Values = new double[dim];
        double[] child2Values = new double[dim];

        for (int i = 0; i < dim; i++) {
            int n = lengths.get(i);
            int crossoverPoint = Utility.getRandomIntBetween(0, n - 1);
            double bound = lowerBound.getElement(i, 0);

            String parent1Chrom = getChromosomeFromValue((int) parent1.getElement(i, 0), n);
            String parent2Chrom = getChromosomeFromValue((int) parent2.getElement(i, 0), n);
            String child1Chrom = parent1Chrom.substring(0, crossoverPoint) + parent2Chrom.substring(crossoverPoint, n);
            String child2Chrom = parent2Chrom.substring(0, crossoverPoint) + parent1Chrom.substring(crossoverPoint, n);

            child1Values[i] = getValueFromChromosome(child1Chrom);
            child2Values[i] = getValueFromChromosome(child2Chrom);
        }

        Matrix child1 = new Matrix(1, dim, child1Values);
        Matrix child2 = new Matrix(1, dim, child2Values);

        return getFitness(child1) > getFitness(child2) ? child1 : child2;
    }

    private Matrix getUniformCrossoverOffspring(Matrix parent1, Matrix parent2) {
        double[] childValues = new double[dim];

        for (int i = 0; i < dim; i++) {
            int n = lengths.get(i);
            String parent1Chrom = getChromosomeFromValue((int) parent1.getElement(i, 0), n);
            String parent2Chrom = getChromosomeFromValue((int) parent2.getElement(i, 0), n);

            StringBuilder childChromBuilder = new StringBuilder();
            for (int j = 0; j < n; j++) {
                if (parent1Chrom.charAt(j) == parent2Chrom.charAt(j)) {
                    childChromBuilder.append(parent1Chrom.charAt(j));
                } else {
                    childChromBuilder.append(Utility.getRandomIntBetween(0, 1));
                }
            }
            String childChrom = childChromBuilder.toString();

            childValues[i] = getValueFromChromosome(childChrom);
        }

        return new Matrix(1, dim, childValues);
    }

    private Matrix getArithmeticOffspring(Matrix parent1, Matrix parent2) {
        double a = Utility.getRandomDoubleBetween(0, 1);
        return parent1.multiply(a).add(parent2.multiply(1 - a));
    }

    private boolean outsideBounds(Matrix x) {
        for (int i = 0; i < dim; i++) {
            if (x.getElement(i, 0) < lowerBound.getElement(i, 0) || x.getElement(i, 0) > upperBound.getElement(i, 0)) {
                return true;
            }
        }
        return false;
    }

    private Matrix getHeuristicOffspring(Matrix worseParent, Matrix betterParent) {
        double a = Utility.getRandomDoubleBetween(0, 1);
        Matrix offspring  = betterParent.subtract(worseParent).multiply(a).add(betterParent);

        if (outsideBounds(offspring)) {
            offspring = getArithmeticOffspring(worseParent, betterParent);
        }

        return offspring;
    }

    private Matrix generateFloatIndividualInBounds() {
        double[] elements = new double[dim];
        for (int i = 0; i < dim; i++) {
            elements[i] = Utility.getRandomDoubleBetween(lowerBound.getElement(i, 0), upperBound.getElement(i, 0));
        }
        return new Matrix(1, dim, elements);
    }

    private Matrix generateBinaryIndividualInBounds() {
        double[] elements = new double[dim];
        for (int i = 0; i < dim; i++) {
            elements[i] = Utility.getRandomIntBetween(0, (int) Math.pow(2, lengths.get(i)) - 1);
        }
        return new Matrix(1, dim, elements);
    }

    private Matrix getUniformMutation(Matrix x) {
        return generateFloatIndividualInBounds();
    }

    private Matrix getSimpleMutation(Matrix x) {
        for (int i = 0; i < x.getRows(); i++) {
            int n = lengths.get(i);
            String chromosome = getChromosomeFromValue((int) x.getElement(i, 0), n);
            int pointOfMutation = Utility.getRandomIntBetween(0, n - 1);

            String mutatedChromosome =
                    chromosome.substring(0, pointOfMutation)
                            + (chromosome.charAt(pointOfMutation) == '0' ? '1' : '0')
                            + chromosome.substring(pointOfMutation + 1, n);

            x.setElement(i, 0, getValueFromChromosome(mutatedChromosome));
        }
        return x;
    }

    private List<Matrix> generatePopulation() {
        List<Matrix> population = new ArrayList<>();
        if (this.binary) {
            for (int i = 0; i < populationSize; i++) {
                population.add(generateBinaryIndividualInBounds());
            }
        } else {
            for (int i = 0; i < populationSize; i++) {
                population.add(generateFloatIndividualInBounds());
            }
        }
        return population;
    }

    private Matrix getOffspring(Matrix worseParent, Matrix betterParent) {
        if (this.binary) {
            return getUniformCrossoverOffspring(worseParent, betterParent);
        } else {
            return getHeuristicOffspring(worseParent, betterParent);
        }
    }

    private Matrix getMutation(Matrix offspring) {
        if (this.binary) {
            return getSimpleMutation(offspring);
        } else {
            return getUniformMutation(offspring);
        }
    }


    public double search(boolean printResult) {
        List<Matrix> population = generatePopulation();

        do {
            Collections.shuffle(population);
            List<Matrix> picked = population.subList(0, k);

            picked.sort(Comparator.comparing(this::getFitness));
            population.remove(picked.get(0));

            Matrix worseParent = population.get(k - 2);
            Matrix betterParent = population.get(k - 1);
            Matrix offspring = getOffspring(worseParent, betterParent);

            if (Utility.getRandomDoubleBetween(0., 1.) <= mutationProbability) {
                offspring = getMutation(offspring);
            }

            population.add(offspring);
        } while (f.getFunctionEvaluations() < maxFunctionCalls);

        population.sort(Comparator.comparing(x -> -getFitness(x)));
        Matrix xMin = population.get(0);
        if (this.binary) {
            xMin = mapBinaryToDouble(xMin);
        }
        double min = f.apply(xMin);

        if (printResult) {
            System.out.println(String.format("%s: found min %.10f for x = %s",
                    this.binary ? "Binary" : "Double", min, xMin.transpose()));
        }

        return min;
    }

    public static double searchUsingBinary(int precision, Matrix lowerBound, Matrix upperBound, int populationSize,
                                         double mutationProbability, int maxFunctionEvaluations,
                                         CustomFunction<Matrix, Double> f, boolean printResult, int k) {
        GeneticAlgorithm GA = new GeneticAlgorithm(lowerBound, upperBound, populationSize, mutationProbability, maxFunctionEvaluations, f, k, precision);

        return GA.search(printResult);
    }

    public static double searchUsingBinary(int precision, Matrix lowerBound, Matrix upperBound, int populationSize,
                                           double mutationProbability, int maxFunctionEvaluations,
                                           CustomFunction<Matrix, Double> f, boolean printResult) {
        return searchUsingBinary(precision, lowerBound, upperBound, populationSize, mutationProbability, maxFunctionEvaluations, f, printResult, DEFAULT_TOURNAMENT_SIZE);
    }

    public static double searchUsingDouble(Matrix lowerBound, Matrix upperBound, int populationSize,
                                           double mutationProbability, int maxFunctionEvaluations,
                                           CustomFunction<Matrix, Double> f, boolean printResult, int k) {
        GeneticAlgorithm GA = new GeneticAlgorithm(lowerBound, upperBound, populationSize, mutationProbability, maxFunctionEvaluations, f, k);

        return GA.search(printResult);
    }

    public static double searchUsingDouble(Matrix lowerBound, Matrix upperBound, int populationSize,
                                           double mutationProbability, int maxFunctionEvaluations,
                                           CustomFunction<Matrix, Double> f, boolean printResult) {

        return searchUsingDouble(lowerBound, upperBound, populationSize, mutationProbability, maxFunctionEvaluations, f, printResult, DEFAULT_TOURNAMENT_SIZE);
    }
}
