package hr.fer.zemris.apr;

public class LUSolver {

    public static Matrix decomposeLU(Matrix m) {
        if (m.getRows() != m.getCols()) {
            throw new IllegalArgumentException("Can't decompose this matrix.");
        }

        int n = m.getRows();
        double x;

        for (int i = 0; i < n - 1; i++) {
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

    public static Matrix forwardSubstitute(Matrix L, Matrix b) {
        if (L.getRows() != L.getCols() || L.getRows() != b.getRows()) {
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
        if (U.getRows() != U.getCols() || U.getRows() != y.getRows()) {
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

