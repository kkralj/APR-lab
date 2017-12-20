package hr.fer.zemris.apr.demo;

import hr.fer.zemris.apr.function.Functions;
import hr.fer.zemris.apr.ga.GA;

public class Task1 {
    public static void main(String[] args) {
        GA f1 = new GA(10_000_000, 40, 2, 0.2,
                Functions.F1, -50, 150 ,3);
        System.out.println(f1.run());

        GA f3 = new GA(10_000_000, 40, 5, 0.2,
                Functions.F3, -50, 150, 3);
        System.out.println(f3.run());

        GA f6 = new GA(10_000_000, 40, 2, 0.2,
                Functions.F6, -50, 150, 3);
        System.out.println(f6.run());

        GA f7 = new GA(10_000_000, 40, 2, 0.2,
                Functions.F7, -50, 150, 3);
        System.out.println(f7.run());
    }
}
