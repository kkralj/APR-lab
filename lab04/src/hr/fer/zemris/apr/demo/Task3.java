package hr.fer.zemris.apr.demo;

import hr.fer.zemris.apr.function.Functions;
import hr.fer.zemris.apr.function.IFunction;
import hr.fer.zemris.apr.ga.BinaryEliminationGA;
import hr.fer.zemris.apr.ga.BinaryGA;
import hr.fer.zemris.apr.ga.GA;

public class Task3 {
    public static void main(String[] args) {
        IFunction[] functions = new IFunction[]{Functions.F6, Functions.F7};
        int[] dimensions = new int[]{3, 6};

        for (IFunction f : functions) {
            for (int dimension : dimensions) {
                System.out.println("Binary GA");

                BinaryEliminationGA bga = new BinaryEliminationGA(f, 20_000, 50, dimension,
                        0.1, -50, 150, 4, 5, false);
                System.out.println(bga.run());

                BinaryGA bga2 = new BinaryGA(100_000, 4, 50, dimension,
                        0.2, f, -50, 150, 3, false);
                System.out.println(bga2.run());

                System.out.println("Double precision GA");
                GA dga = new GA(100_000, 200, dimension, 0.3,
                        f, -50, 150, 3, false);
                System.out.println(dga.run());

            }
            System.out.println();
        }
    }
}
