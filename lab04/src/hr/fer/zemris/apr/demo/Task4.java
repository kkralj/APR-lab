package hr.fer.zemris.apr.demo;

import hr.fer.zemris.apr.function.Functions;
import hr.fer.zemris.apr.ga.BinaryGA;
import hr.fer.zemris.apr.ga.GA;

public class Task4 {

    private static final int ITERATIONS = 10;

    public static void main(String[] args) {

        System.out.println("Changing population:");
        int[] populations = new int[]{30, 50, 100, 200};

        for (int i = 0; i < populations.length; i++) {
            System.out.print("\"" + populations[i] + "\"" + ((i != populations.length - 1) ? ", " : " "));
        }
        System.out.println();
        
        for (int iter = 0; iter < ITERATIONS; iter++) {
            for (int i = 0; i < populations.length; i++) {
                GA ga = new GA(100_000, populations[i], 6,
                        0.3, Functions.F6, -50, 150, 3);
                System.out.print(ga.run().error + ((i != populations.length - 1) ? ", " : " "));
            }
            System.out.println();
        }

        System.out.println("Changing mutation probability:");
        double[] mutations = new double[]{0.1, 0.3, 0.6, 0.9};

        for (int i = 0; i < mutations.length; i++) {
            System.out.print("\"" + mutations[i] + "\"" + ((i != mutations.length - 1) ? ", " : " "));
        }
        System.out.println();

        for (int iter = 0; iter < ITERATIONS; iter++) {
            for (int i = 0; i < mutations.length; i++) {
                GA ga = new GA(100_000, 30, 6,
                        mutations[i], Functions.F6, -50, 150, 3);
                System.out.print(ga.run().error + ((i != mutations.length - 1) ? ", " : " "));
            }
            System.out.println();
        }

//        System.out.println("Changing population:");
//        for (int i = 0; i < populations.length; i++) {
//            System.out.print("\"" + populations[i] + "\"" + ((i != populations.length - 1) ? ", " : " "));
//        }
//        System.out.println();
//        for (int iter = 0; iter < ITERATIONS; iter++) {
//            for (int i = 0; i < populations.length; i++) {
//                BinaryGA ga = new BinaryGA(100_000, 4, populations[i], 6,
//                        0.3, Functions.F6, -50, 150, 3);
//                System.out.print(ga.run().error + ((i != populations.length - 1) ? ", " : " "));
//            }
//            System.out.println();
//        }
//
//        System.out.println("Changing mutation probability:");
//        for (int i = 0; i < mutations.length; i++) {
//            System.out.print("\"" + mutations[i] + "\"" + ((i != mutations.length - 1) ? ", " : " "));
//        }
//        System.out.println();
//        for (int iter = 0; iter < ITERATIONS; iter++) {
//            for (int i = 0; i < mutations.length; i++) {
//                BinaryGA ga = new BinaryGA(100_000, 4, 200, 6,
//                        mutations[i], Functions.F6, -50, 150, 3);
//                System.out.print(ga.run().error + ((i != mutations.length - 1) ? ", " : " "));
//            }
//            System.out.println();
//        }

    }

}
