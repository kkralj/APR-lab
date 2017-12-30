package hr.fer.zemris.apr.lab05.demo;

import hr.fer.zemris.apr.lab05.Matrix;
import hr.fer.zemris.apr.lab05.Trapezni;

public class Task3 {
    public static void main(String[] args) {
        Matrix A = new Matrix(new double[][]{
                {0, 1},
                {-1, 0}
        });

        Matrix x0 = new Matrix(new double[][]{
                {0},
                {1}
        });

        Trapezni.calculate(A, x0,0.1, 5);
    }
}
