package hr.fer.zemris.apr.lab05;

public class LUPSolver {

    private Matrix matrix;
    private double[][] positions;
    private int n;

    public LUPSolver(Matrix matrix) {
        if (matrix.getRows() != matrix.getColumns()) {
            throw new IllegalArgumentException("Can't decompose this matrix.");
        }

        this.matrix = matrix;
        this.n = matrix.getRows();
        this.positions = new double[n][n];

        for (int i = 0; i < n; i++) {
            positions[i][i] = 1;
        }
    }

    public Matrix getPositions() {
        return new Matrix(positions);
    }

    public Matrix decompose() {
        Matrix m = matrix.getCopy();

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
            tmp = positions[current][j];
            positions[current][j] = positions[next][j];
            positions[next][j] = tmp;
        }

        for (int j = 0; j < n; j++) {
            tmp = matrix.getValue(current, j);
            matrix.setValue(current, j, matrix.getValue(next, j));
            matrix.setValue(next, j, tmp);
        }
    }

    public static Matrix forwardSubstitute(Matrix L, Matrix b) {
        if (L.getRows() != L.getColumns() || L.getRows() != b.getRows()) {
            throw new IllegalArgumentException();
        }

        int n = b.getRows();
        double[][] y = new double[n][1];
        for (int i = 0; i < n; i++) {
            y[i][0] = b.getValue(i, 0);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                y[i][0] -= L.getValue(i, j) * y[j][0];
            }
        }

        return new Matrix(y);
    }

    public static Matrix backwardSubstitute(Matrix U, Matrix y) {
        if (U.getRows() != U.getColumns() || U.getRows() != y.getRows()) {
            throw new IllegalArgumentException();
        }

        int n = U.getRows();
        double[][] x = new double[n][1];
        for (int i = 0; i < n; i++) {
            x[i][0] = y.getValue(i, 0);
        }

        for (int i = n - 1; i >= 0; i--) {
            if (Math.abs(U.getValue(i, i)) < 1e-6) {
                throw new IllegalArgumentException("Coefficient is 0.");
            }
            double sum = y.getValue(i, 0);
            for (int j = n - 1; j > i; j--) {
                sum -= U.getValue(i, j) * x[j][0];
            }
            x[i][0] = sum / U.getValue(i, i);
        }

        return new Matrix(x);
    }

}