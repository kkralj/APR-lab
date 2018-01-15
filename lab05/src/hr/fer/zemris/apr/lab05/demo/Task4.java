package hr.fer.zemris.apr.lab05.demo;

import hr.fer.zemris.apr.lab05.Util;
import hr.fer.zemris.apr.lab05.matrix.Matrix;
import hr.fer.zemris.apr.lab05.solver.ISolver;
import hr.fer.zemris.apr.lab05.solver.RungeKutta;
import hr.fer.zemris.apr.lab05.solver.Trapezni;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.FileNotFoundException;
import java.util.List;

public class Task4 {
    public static void main(String[] args) throws FileNotFoundException {
        ISolver trapezni = new Trapezni();
        ISolver rungeKutta = new RungeKutta();

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

        System.out.println("runge-kutta-unstable");
        List<Matrix> result = rungeKutta.calculate(A, B, x0, 0.1, 10);
        Util.writeResults(result, "data/task4-runge-kutta-unstable.txt");
        Util.print(result, 100);

        System.out.println("trapezni-unstable");
        result = trapezni.calculate(A, B, x0, 0.1, 10);
        Util.writeResults(result, "data/task4-trapezni-unstable.txt");
        Util.print(result, 100);

        System.out.println("runge-kutta");
        result = rungeKutta.calculate(A, B, x0, 0.025, 10);
        Util.writeResults(result, "data/task4-runge-kutta.txt");
        Util.print(result, 100);

        System.out.println("trapezni");
        result = trapezni.calculate(A, B, x0, 0.0025, 10);
        Util.writeResults(result, "data/task4-trapezni.txt");
        Util.print(result, 1000);
    }
}
