package hr.fer.zemris.apr.lab05.demo;

import hr.fer.zemris.apr.lab05.matrix.Matrix;

public class Task2 {
    public static void main(String[] args) {
        Matrix m = new Matrix(new double[][]{
                {4, -5, -2},
                {5, -6, -2},
                {-8, 9, 3}
        });

        System.out.println(m.getInverse());
        System.out.println(m.multiply(m.getInverse()));

        Matrix m2 = new Matrix(new double[][]{
                {1, 0, 0, 1},
                {0, 2, 1, 2},
                {2, 1, 0, 1},
                {2, 0, 1, 4},
        });
        System.out.println(m2.getInverse());
        System.out.println(m2.multiply(m2.getInverse()));
    }
}
