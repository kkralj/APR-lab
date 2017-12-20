package hr.fer.zemris.apr.ga;

import hr.fer.zemris.apr.function.IFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GA {

    private static final double MIN_ERROR = 1e-6;

    private static Random random = new Random();

    private IFunction function;

    private int iterations, variableCount, populationSize, tournamentSize;
    private double mutationProbability, lowerBound, upperBound;
    private boolean printIterations;

    public GA(int iterations, int populationSize, int variableCount, double mutationProbability,
              IFunction function, double lowerBound, double upperBound, int tournamentSize,
              boolean printIterations) {
        this.function = function;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.iterations = iterations;
        this.variableCount = variableCount;
        this.populationSize = populationSize;
        this.tournamentSize = tournamentSize;
        this.printIterations = printIterations;
        this.mutationProbability = mutationProbability;
    }

    public Chromosome run() {
        List<Chromosome> population = Chromosome.getRandomPopulation(
                populationSize, variableCount, function, lowerBound, upperBound
        );

        for (int it = 0; it < iterations; it++) {
            Collections.sort(population);
            if (population.get(0).error < MIN_ERROR) break;

            if (printIterations && it % 1000 == 0) {
                System.out.println(population.get(0).error);
            }

            List<Integer> kRandoms = getSortedRandomInts(tournamentSize, populationSize);
            List<Chromosome> parents = new ArrayList<>();
            for (int i = 0; i < kRandoms.size() - 1; i++) {
                parents.add(population.get(kRandoms.get(i)));
            }

            Chromosome newChild = uniformCrossover(parents.get(0), parents.get(1));
            mutate(newChild, mutationProbability, lowerBound, upperBound);
            newChild.error = function.valueAt(newChild.getValues());

            // replace worst child
            int worstIndex = kRandoms.get(kRandoms.size() - 1);
            population.set(worstIndex, newChild);
        }

        Collections.sort(population);
        return population.get(0);
    }

    public static List<Integer> getSortedRandomInts(int size, int upperBound) {
        List<Integer> numbers = new ArrayList<>();

        int rand;
        for (int i = 0; i < size; i++) {
            do {
                rand = random.nextInt(upperBound);
            } while (numbers.contains(rand));
            numbers.add(rand);
        }

        Collections.sort(numbers);
        return numbers;
    }

    private void mutate(Chromosome child, double mutationProbability, double lowerBound, double upperBound) {
        List<Double> values = child.getValues();
        for (int i = 0; i < values.size(); i++) {
            if (random.nextDouble() < mutationProbability) {
                values.set(i, lowerBound + (upperBound - lowerBound) * random.nextDouble());
            }
        }
    }

    private Chromosome uniformCrossover(Chromosome parent1, Chromosome parent2) {
        List<Double> values1 = parent1.getValues();
        List<Double> values2 = parent2.getValues();

        List<Double> result = new ArrayList<>();

        for (int i = 0; i < values1.size(); i++) {
            double cmin = Math.min(values1.get(i), values2.get(i));
            double cmax = Math.max(values1.get(i), values2.get(i));
            result.add(cmin + random.nextDouble() * (cmax - cmin));

        }
        return new Chromosome(result);
    }
}
