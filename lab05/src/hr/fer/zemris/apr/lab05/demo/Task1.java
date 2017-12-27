package hr.fer.zemris.apr.lab05.demo;

import hr.fer.zemris.apr.lab05.Matrix;

public class Task1 {
    public static void main(String[] args) {
        Matrix m = new Matrix(new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        });

        System.out.println(m.getInverse()); // exception -> singular matrix
    }
}
