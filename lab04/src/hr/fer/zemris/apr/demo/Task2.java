package hr.fer.zemris.apr.demo;

import hr.fer.zemris.apr.function.Functions;
import hr.fer.zemris.apr.ga.GA;

public class Task2 {
    public static void main(String[] args) {
        int[] dimemsions = new int[]{1, 3, 6, 10};

        for (int dimension : dimemsions) {
            System.out.println("Dimension: " + dimension);
            GA f6 = new GA(10_000_000, 40, dimension, 0.2,
                    Functions.F6, -50, 150, 3);
            f6.run();

            GA f7 = new GA(10_000_000, 40, dimension, 0.2,
                    Functions.F7, -50, 150, 3);
            f7.run();
        }
    }
}
