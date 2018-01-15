package hr.fer.zemris.apr.lab05.solver;

import hr.fer.zemris.apr.lab05.matrix.Matrix;

import java.util.ArrayList;
import java.util.List;

public class Trapezni implements ISolver {

    public List<Matrix> calculate(Matrix A, Matrix B, Matrix x0, double T, double interval) {
        List<Matrix> result = new ArrayList<>();
        result.add(x0);

        Matrix U = Matrix.getIdentity(A.getRows());
        Matrix R = U.subtract(A.multiply(T / 2)).getInverse().multiply(U.add(A.multiply(T / 2)));
        Matrix S = U.subtract(A.multiply(T / 2)).getInverse().multiply(B);

        Matrix xp = x0;
        for (double total = 1; total <= interval; total += T) {
            xp = R.multiply(xp).add(S);
            result.add(xp);
        }

        return result;
    }

}
