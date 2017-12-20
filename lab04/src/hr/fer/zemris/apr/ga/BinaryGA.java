package hr.fer.zemris.apr.ga;

import hr.fer.zemris.apr.function.IFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static hr.fer.zemris.apr.ga.GA.getSortedRandomInts;

public class BinaryGA {
    private static final double MIN_ERROR = 1e-6;

    private static Random random = new Random();

    private IFunction function;

    private int iterations;
    private int populationSize;
    private int variableCount;
    private int tournamentSize;

    private double mutationProbability;
    private double lowerBound;
    private double upperBound;

    private int precision;

    public BinaryGA(int iterations, int precision, int populationSize, int variableCount, double mutationProbability,
                    IFunction function, double lowerBound, double upperBound, int tournamentSize) {

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
            newChild.error = function.valueAt(newChild.getRealValues());

            // replace worst child
            int worstIndex = kRandoms.get(kRandoms.size() - 1);
            if (newChild.error < population.get(worstIndex).error) {
                population.set(worstIndex, newChild);
            }
        }

        Collections.sort(population);
        return population.get(0);
    }

    public static void binaryMutate(BinaryChromosome child, double mutationProbability) {
        for (int i = 0; i < child.values.length; i++) {
            for (int j = 0; j < child.getBitCount(); j++) {
                if (random.nextDouble() < mutationProbability) {
                    child.values[i] ^= (1 << j);
                }
            }
        }
    }

    public static BinaryChromosome uniformCrossover(BinaryChromosome parent1, BinaryChromosome parent2) {
        String binaryValues1 = parent1.getBinaryValues();
        String binaryValues2 = parent2.getBinaryValues();

        StringBuilder result = new StringBuilder();
        int variableCount = parent1.variableCount();

        for (int i = 0; i < binaryValues1.length(); i++) {
            result.append(random.nextDouble() < 0.5 ? binaryValues1.charAt(i) : binaryValues2.charAt(i));
        }

        return new BinaryChromosome(BinaryChromosome.convertToIntegers(result.toString(), variableCount),
                parent1.getBitCount(), parent1.getLowerLimit(), parent1.getUpperLimit());
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

        int crossPoint = random.nextInt(binaryValues1.length());

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < crossPoint; i++) {
            result.append(binaryValues1.charAt(i));
        }
        for (int i = crossPoint; i < binaryValues2.length(); i++) {
            result.append(binaryValues2.charAt(i));
        }

        int variableCount = parent1.variableCount();

        return new BinaryChromosome(
                BinaryChromosome.convertToIntegers(result.toString(), variableCount),
                parent1.getBitCount(),
                parent1.getLowerLimit(),
                parent1.getUpperLimit()
        );
    }
}
