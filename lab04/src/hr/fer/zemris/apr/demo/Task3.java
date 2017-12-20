package hr.fer.zemris.apr.demo;

import hr.fer.zemris.apr.function.Functions;
import hr.fer.zemris.apr.ga.BinaryGA;
import hr.fer.zemris.apr.ga.GA;

public class Task3 {
    public static void main(String[] args) {
        BinaryGA f6 = new BinaryGA(100_000, 4, 50, 3, 0.1,
                Functions.F6, -50, 150, 3);
        System.out.println(f6.run());

        BinaryGA f7 = new BinaryGA(100_000, 4, 50, 3, 0.1,
                Functions.F7, -50, 150, 3);
        System.out.println(f7.run());

        BinaryGA f62 = new BinaryGA(100_000, 4, 50, 6, 0.1,
                Functions.F6, -50, 150, 3);
        System.out.println(f62.run());

        BinaryGA f72 = new BinaryGA(100_000, 4, 50, 6, 0.1,
                Functions.F7, -50, 150, 3);
        System.out.println(f72.run());
    }
}
