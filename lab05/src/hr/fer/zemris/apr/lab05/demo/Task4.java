package hr.fer.zemris.apr.lab05.demo;

import hr.fer.zemris.apr.lab05.Matrix;
import hr.fer.zemris.apr.lab05.RungeKutta;

public class Task4 {
    public static void main(String[] args) {
        Matrix A = new Matrix(new double[][]{
                {0, 1},
                {-200, -102}
        });

        Matrix x0 = new Matrix(new double[][]{
                {1},
                {-2}
        });

        double T = 0.025;
        double total = 2;

        System.out.println(RungeKutta.calculate(A, x0, T, total));
    }
}
