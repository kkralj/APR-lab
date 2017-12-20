package hr.fer.zemris.apr.ga;

import hr.fer.zemris.apr.function.IFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static hr.fer.zemris.apr.ga.BinaryGA.binaryMutate;

public class BinaryEliminationGA {
    private static final double MIN_ERROR = 1e-6;

    private static Random random = new Random();

    private IFunction function;

    private int iterations, variableCount, populationSize, precision, elitism;
    private double mutationProbability, lowerBound, upperBound;
    private boolean printIterations;

    public BinaryEliminationGA(IFunction function, int iterations, int populationSize, int variableCount,
                               double mutationProbability, double lowerBound, double upperBound, int precision,
                               int elitism, boolean printIterations) {
        this.elitism = elitism;
        this.function = function;
        this.precision = precision;
        this.iterations = iterations;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.variableCount = variableCount;
        this.populationSize = populationSize;
        this.printIterations = printIterations;
        this.mutationProbability = mutationProbability;
    }

    public BinaryChromosome run() {
        List<BinaryChromosome> population = BinaryChromosome.getPopulation(populationSize, variableCount,
                precision, function, lowerBound, upperBound);

        for (int generation = 1; generation <= iterations; generation++) {
            Collections.sort(population);
            if (population.get(0).error < MIN_ERROR) break;

            if (printIterations && generation % 1000 == 0) {
                System.out.println(population.get(0).error);
            }

            List<BinaryChromosome> nextPopulation = new ArrayList<>();
            for (int i = 0; i < elitism; i++) {
                nextPopulation.add(population.get(i));
            }

            for (int i = nextPopulation.size(); i < populationSize; i++) {
                List<BinaryChromosome> parents = proportionalSelection(population, 2);
                BinaryChromosome child = uniformCrossover(parents.get(0), parents.get(1));
                binaryMutate(child, mutationProbability);
                child.error = function.valueAt(child.getRealValues());
                nextPopulation.add(child);
            }

            population = nextPopulation;
        }

        Collections.sort(population);

        return population.get(0);
    }

    private static BinaryChromosome uniformCrossover(BinaryChromosome parent1, BinaryChromosome parent2) {
        int[] values1 = parent1.values;
        int[] values2 = parent2.values;

        int[] result = new int[values1.length];

        for (int i = 0; i < values1.length; i++) {
            int cmin = Math.min(values1[i], values2[i]);
            int cmax = Math.max(values1[i], values2[i]);
            result[i] = (int) (cmin + random.nextDouble() * (cmax - cmin));

        }
        return new BinaryChromosome(result, parent1.getBitCount(), parent1.getLowerLimit(), parent1.getUpperLimit());
    }

    private List<BinaryChromosome> proportionalSelection(List<BinaryChromosome> population, int howMany) {
        List<BinaryChromosome> parents = new ArrayList<>();

        double totalFitness = 0;
        for (BinaryChromosome ch : population) {
            totalFitness += ch.getFitness();
        }

        for (int parentIndex = 0; parentIndex < howMany; parentIndex++) {
            double limit = random.nextDouble() * totalFitness;

            int chosen = 0;
            double upperLimit = population.get(chosen).getFitness();
            while (limit > upperLimit && chosen + 1 < population.size()) {
                chosen++;
                upperLimit += population.get(chosen).getFitness();
            }

            parents.add(population.get(chosen));
        }

        return parents;
    }
}
