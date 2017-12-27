package hr.fer.zemris.apr.demo;

import hr.fer.zemris.apr.function.Functions;
import hr.fer.zemris.apr.ga.GA;

public class Task2 {
    public static void main(String[] args) {
        int[] dimensions = new int[]{1, 3, 6, 10, 20};

        for (int dimension : dimensions) {
            System.out.println("Dimension: " + dimension);

            GA f6 = new GA(100_000, 200, dimension, 0.3,
                    Functions.F6, -50, 150, 3,false);
            System.out.println(f6.run());

            GA f7 = new GA(100_000, 200, dimension, 0.3,
                    Functions.F7, -50, 150, 3,false);
            System.out.println(f7.run());
        }
    }
}
