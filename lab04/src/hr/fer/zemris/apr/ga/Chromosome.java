package hr.fer.zemris.apr.ga;

import hr.fer.zemris.apr.function.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chromosome implements Comparable<Chromosome> {

    private static final Random random = new Random();

    private List<Double> values;
    public double error;

    public Chromosome(List<Double> values) {
        this.values = values;
    }

    @Override
    public int compareTo(Chromosome chromosome) {
        return Double.compare(error, chromosome.error);
    }

    public static List<Chromosome> getRandomPopulation(int populationSize, int variableCount, Function function,
                                                       double lowerLimit, double upperLimit) {
        List<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            List<Double> result = new ArrayList<>();

            for (int j = 0; j < variableCount; j++) {
                result.add(lowerLimit + (upperLimit - lowerLimit) * random.nextDouble());
            }

            Chromosome child = new Chromosome(result);
            child.error = function.valueAt(result);
            population.add(child);
        }

        return population;
    }


    public List<Double> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return values.toString() + " error: " + error;
    }
}
