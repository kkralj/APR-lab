package hr.fer.zemris.apr.lab05.solver;

import hr.fer.zemris.apr.lab05.matrix.Matrix;

import java.util.List;

public interface ISolver {
    List<Matrix> calculate(Matrix A, Matrix B, Matrix x0, double T, double interval);
}
