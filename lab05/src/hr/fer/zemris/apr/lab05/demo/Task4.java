package hr.fer.zemris.apr.lab05.demo;

import hr.fer.zemris.apr.lab05.matrix.Matrix;
import hr.fer.zemris.apr.lab05.solver.ISolver;
import hr.fer.zemris.apr.lab05.solver.RungeKutta;
import hr.fer.zemris.apr.lab05.solver.Trapezni;

import java.util.List;

public class Task4 {
    public static void main(String[] args) {
        Matrix A = new Matrix(new double[][]{
                {0, 1},
                {-200, -102}
        });

        Matrix B = new Matrix(new double[][]{
                {0},
                {0}
        });

        Matrix x0 = new Matrix(new double[][]{
                {1},
                {-2}
        });

        ISolver rungeKutta = new RungeKutta();
        List<Matrix> rungeKuttaResults = rungeKutta.calculate(A, B, x0, 0.025, 2);

        ISolver trapezni = new Trapezni();
        List<Matrix> trapezniResults = trapezni.calculate(A, B, x0, 0.025, 2);
    }
}
