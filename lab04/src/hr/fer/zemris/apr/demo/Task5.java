package hr.fer.zemris.apr.demo;

import hr.fer.zemris.apr.function.Function;
import hr.fer.zemris.apr.function.Functions;
import hr.fer.zemris.apr.ga.GA;

public class Task5 {
    private static final int[] tournamentSizes = new int[]{3, 5, 8, 13, 18};

    public static void main(String[] args) {


        System.out.println("For different tournament sizes:");

        System.out.println("F6:");
        tournamentRun(Functions.F6);
        System.out.println("F7:");
        tournamentRun(Functions.F7);

        System.out.println("For different roulette wheels:");
        // TODO
    }

    private static void tournamentRun(Function f) {
        for (int i = 0; i < 5; i++) {
            for (int tournamentSize : tournamentSizes) {
                GA gf = new GA(1_000_000, 40, 6, 0.2,
                        f, -50, 150, tournamentSize);
                System.out.print(gf.run().error + " ");
            }
            System.out.println();
        }
    }
}
