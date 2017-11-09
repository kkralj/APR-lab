package hr.fer.zemris.apr;

public class LUPSolver {

    private Matrix m;
    private double[][] p;
    private int n;

    public LUPSolver(Matrix m) {
        if (m.getRows() != m.getCols()) {
            throw new IllegalArgumentException("Can't decompose this matrix.");
        }

        this.m = m;
        this.n = m.getRows();
        this.p = new double[n][n];

        for (int i = 0; i < n; i++) {
            p[i][i] = 1;
        }
    }

    public Matrix decompose() {
        Matrix m = this.m.clone();

        double x;
        for (int i = 0; i < n - 1; i++) {
            int pivot = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(m.getValue(pivot, i)) < Math.abs(m.getValue(j, i))) {
                    pivot = j;
                }
            }
            if (pivot != i) {
                swapRows(i, pivot);
            }

            x = m.getValue(i, i);
            if (Math.abs(x) < 1e-6) {
                throw new IllegalArgumentException("Invalid pivot!");
            }
            for (int j = i + 1; j < n; j++) {
                m.setValue(j, i, m.getValue(j, i) / x);
                for (int k = i + 1; k < n; k++) {
                    m.setValue(j, k, m.getValue(j, k) - m.getValue(j, i) * m.getValue(i, k));
                }
            }
        }

        return m;
    }

    private void swapRows(int current, int next) {
        double tmp;
        for (int j = 0; j < n; j++) {
            tmp = p[current][j];
            p[current][j] = p[next][j];
            p[next][j] = tmp;
        }

        for (int j = 0; j < n; j++) {
            tmp = m.getValue(current, j);
            m.setValue(current, j, m.getValue(next, j));
            m.setValue(next, j, tmp);
        }
    }

    public Matrix getMatrix() {
        return m.clone();
    }

    public Matrix getP() {
        return new Matrix(p);
    }
}
