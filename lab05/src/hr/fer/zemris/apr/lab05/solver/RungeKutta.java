package hr.fer.zemris.apr.lab05.solver;

import hr.fer.zemris.apr.lab05.matrix.Matrix;

import java.util.ArrayList;
import java.util.List;

public class RungeKutta implements ISolver {

    public List<Matrix> calculate(Matrix A, Matrix B, Matrix x0, double T, double interval) {
        List<Matrix> result = new ArrayList<>();
        result.add(x0);

        Matrix xp = x0;
        for (double total = 1; total <= interval; total += T) {
            Matrix m1 = A.multiply(xp).add(B);
            Matrix m2 = A.multiply(xp.add(m1.multiply(T / 2))).add(B);
            Matrix m3 = A.multiply(xp.add(m2.multiply(T / 2))).add(B);
            Matrix m4 = A.multiply(xp.add(m3.multiply(T))).add(B);

            Matrix msums = m1.add(m2.multiply(2)).add(m3.multiply(2)).add(m4);

            xp = xp.add(msums.multiply(T / 6));

            result.add(xp);
        }

        return result;
    }

}
