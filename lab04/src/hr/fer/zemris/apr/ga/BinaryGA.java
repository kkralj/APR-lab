package hr.fer.zemris.apr.ga;

import hr.fer.zemris.apr.function.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BinaryGA {
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

    private int precision;

    public BinaryGA(int iterations, int precision, int populationSize, int variableCount, double mutationProbability,
                    Function function, double lowerBound, double upperBound, int tournamentSize) {

        this.function = function;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.iterations = iterations;
        this.variableCount = variableCount;
        this.populationSize = populationSize;
        this.tournamentSize = tournamentSize;
        this.mutationProbability = mutationProbability;
        this.precision = precision;
    }

    public BinaryChromosome run() {
        List<BinaryChromosome> population = BinaryChromosome.getPopulation(
                populationSize, variableCount, precision, function, lowerBound, upperBound);

        for (int it = 0; it < iterations; it++) {
            Collections.sort(population);
            if (population.get(0).error < MIN_ERROR) break;

            List<Integer> kRandoms = getSortedRandomInts(tournamentSize, populationSize);

            List<BinaryChromosome> parents = new ArrayList<>();
            for (int i = 0; i < kRandoms.size() - 1; i++) {
                parents.add(population.get(kRandoms.get(i)));
            }

            BinaryChromosome newChild = onePointCrossover(parents.get(0), parents.get(1));
            binaryMutate(newChild, mutationProbability);
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

    private void binaryMutate(BinaryChromosome child, double mutationProbability) {
        for (int i = 0; i < child.values.length; i++) {
            for (int j = 0; j < child.getBitCount(); j++) {
                if (random.nextDouble() < mutationProbability) {
                    child.values[i] ^= (1 << j);
                }
            }
        }
    }

    private BinaryChromosome twoPointCrossover(BinaryChromosome parent1, BinaryChromosome parent2) {
        String binaryValues1 = parent1.getBinaryValues();
        String binaryValues2 = parent2.getBinaryValues();

        StringBuilder result = new StringBuilder();

        int crossPoint1 = random.nextInt(binaryValues1.length());
        int crossPoint2 = crossPoint1 + random.nextInt(binaryValues1.length() - crossPoint1);

        for (int i = 0; i < crossPoint1; i++) {
            result.append(binaryValues1.charAt(i));
        }
        for (int i = crossPoint1; i < crossPoint2; i++) {
            result.append(binaryValues2.charAt(i));
        }
        for (int i = crossPoint2; i < binaryValues1.length(); i++) {
            result.append(binaryValues1.charAt(i));
        }

        return new BinaryChromosome(BinaryChromosome.convertToIntegers(result.toString(), parent1.variableCount()),
                parent1.getBitCount(), parent1.getLowerLimit(), parent1.getUpperLimit());
    }

    private BinaryChromosome onePointCrossover(BinaryChromosome parent1, BinaryChromosome parent2) {
        String binaryValues1 = parent1.getBinaryValues();
        String binaryValues2 = parent2.getBinaryValues();

        if (binaryValues1.length() != binaryValues2.length()) {
            throw new IllegalArgumentException();
        }

        if (parent1.variableCount() != parent2.variableCount()) {
            throw new IllegalArgumentException();
        }

        if (parent1.getBitCount() != parent2.getBitCount()) {
            throw new IllegalArgumentException();
        }

        StringBuilder result = new StringBuilder();

        int variableCount = parent1.variableCount();
        int crossPoint = random.nextInt(binaryValues1.length());

        for (int i = 0; i < crossPoint; i++) {
            result.append(binaryValues1.charAt(i));
        }
        for (int i = crossPoint; i < binaryValues2.length(); i++) {
            result.append(binaryValues2.charAt(i));
        }

        return new BinaryChromosome(BinaryChromosome.convertToIntegers(result.toString(), variableCount), parent1.getBitCount(),
                parent1.getLowerLimit(), parent1.getUpperLimit());
    }
}
