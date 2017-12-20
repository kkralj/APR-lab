package hr.fer.zemris.apr.ga;

import hr.fer.zemris.apr.function.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BinaryChromosome implements Comparable<BinaryChromosome> {

    private static Random random = new Random();

    public int[] values;

    private double lowerLimit, upperLimit;

    private int bitCount;

    public double error;

    public BinaryChromosome(int[] values, int bitCount, double lowerLimit, double upperLimit) {
        this.values = values;
        this.bitCount = bitCount;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
    }

    public int getBitCount() {
        return bitCount;
    }

    public double getLowerLimit() {
        return lowerLimit;
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    public BinaryChromosome(int size, int bitCount, double lowerLimit, double upperLimit) {
        this.values = new int[size];
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.bitCount = bitCount;

        for (int i = 0; i < size; i++) {
            values[i] = random.nextInt((int) (Math.pow(2, bitCount) - 1));
        }
    }

    public int variableCount() {
        return values.length;
    }

    public static int getBitCount(int precision, double upperBound, double lowerBound) {
        return (int) Math.ceil(Math.log10(Math.floor(1 + Math.pow(10, precision) * (upperBound - lowerBound))) / Math.log10(2));
    }

    @Override
    public int compareTo(BinaryChromosome chromosome) {
        return Double.compare(error, chromosome.error);
    }

    public static List<BinaryChromosome> getPopulation(int populationSize, int variableCount, int precision,
                                                       Function function, double lowerLimit, double upperLimit) {
        List<BinaryChromosome> result = new ArrayList<>();

        int bitCount = getBitCount(precision, upperLimit, lowerLimit);
        for (int i = 0; i < populationSize; i++) {
            BinaryChromosome child = new BinaryChromosome(variableCount, bitCount, lowerLimit, upperLimit);
            child.error = function.valueAt(child.getValues());
            result.add(child);
        }

        return result;
    }

    public List<Double> getValues() {
        List<Double> results = new ArrayList<>();
        double top = Math.pow(2, bitCount) - 1;
        for (int value : values) {
            results.add(lowerLimit + (upperLimit - lowerLimit) * value / top);
        }
        return results;
    }

    public void setValue(int i, double val) {
        double top = Math.pow(2, bitCount) - 1;
        values[i] = (int) ((val - lowerLimit) / (upperLimit - lowerLimit) * top);
    }

    public int[] getBinaryListValues() {
        return this.values;
    }

    public String getBinaryValues() {
        StringBuilder result = new StringBuilder();

        for (int value : values) {
            StringBuilder binValue = new StringBuilder(Integer.toBinaryString(value));
            while (binValue.length() != bitCount) binValue.insert(0, "0");
            result.append(binValue.toString());
        }

        return result.toString();
    }

    public static int[] convertToIntegers(String binary, int variableCount) {
        int[] result = new int[variableCount];
        int varSize = binary.length() / variableCount;
        for (int i = 0; i * varSize < binary.length(); i++) {
            result[i] = Integer.parseInt(binary.substring(i, i + varSize), 2);
        }

        return result;
    }

    @Override
    public String toString() {
        return getValues() + " error: " + error;
    }
}
