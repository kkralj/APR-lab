package hr.fer.zemris.apr.ga;

import hr.fer.zemris.apr.function.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GA {

    private static final double MIN_ERROR = 1e-6;

    private static Random random = new Random();

    private Function function;

    private int iterations;
    private int populationSize;
    private int variableCount;
    private int tournamentSize;

    private double mutationProbability;
    private double lowerBound;
    private double upperBound;

    public GA(int iterations, int populationSize, int variableCount, double mutationProbability,
              Function function, double lowerBound, double upperBound, int tournamentSize) {

        this.function = function;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.iterations = iterations;
        this.variableCount = variableCount;
        this.populationSize = populationSize;
        this.tournamentSize = tournamentSize;
        this.mutationProbability = mutationProbability;
    }

    public Chromosome run() {
        List<Chromosome> population = Chromosome.getRandomPopulation(
                populationSize, variableCount, function, lowerBound, upperBound
        );

        for (int it = 0; it < iterations; it++) {
            Collections.sort(population);
            if (population.get(0).error < MIN_ERROR) break;

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

    private List<Integer> getSortedRandomInts(int size, int upperBound) {
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
//            betas[i] = (0.4 + 0.2 * random.nextDouble()) * (b1[i] + b2[i]);
            result.add(cmin + random.nextDouble() * (cmax - cmin));

        }
        return new Chromosome(result);
    }
}
