package hr.fer.zemris.apr.lab05;

public class Matrix {
    private double[][] matrix;

    public Matrix(int i, int size) {
        this.matrix = new double[size][1];
        matrix[i][0] = 1;
    }

    public Matrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public Matrix getInverse() {
        if (getRows() != getColumns()) {
            throw new IllegalArgumentException("Cannot find inverse of non-square matrix.");
        }

        LUPSolver solver = new LUPSolver(this);
        Matrix LU = solver.decompose();
        Matrix P = solver.getPositions();

        double[][] result = new double[getRows()][getColumns()];

        for (int i = 0; i < matrix.length; i++) {
            Matrix res = LUPSolver.forwardSubstitute(LU, P.multiply(new Matrix(i, getRows())));
            Matrix solRow = LUPSolver.backwardSubstitute(LU, res);
            for (int j = 0; j < getColumns(); j++) {
                result[j][i] = solRow.getValue(j, 0);
            }
        }

        return new Matrix(result);
    }

    public Matrix multiply(Matrix another) {
        if (matrix[0].length != another.matrix.length) {
            throw new IllegalArgumentException("Incompatible matrices.");
        }

        double[][] result = new double[matrix.length][another.matrix[0].length];

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                for (int k = 0; k < matrix[0].length; k++) {
                    result[i][j] += matrix[i][k] * another.matrix[k][j];
                }
            }
        }

        return new Matrix(result);
    }

    public void setValue(int x, int y, double val) {
        matrix[x][y] = val;
    }

    public double getValue(int x, int y) {
        return matrix[x][y];
    }

    public int getRows() {
        return matrix.length;
    }

    public int getColumns() {
        return matrix[0].length;
    }

    public Matrix getCopy() {
        double[][] m = new double[getRows()][getColumns()];
        for (int i = 0; i < getRows(); i++) {
            System.arraycopy(matrix[i], 0, m[i], 0, getColumns());
        }

        return new Matrix(m);
    }

    public static Matrix getIdentity(int size) {
        double[][] result = new double[size][size];
        for (int i = 0; i < size; i++) {
            result[i][i] = 1;
        }
        return new Matrix(result);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (double[] row : matrix) {
            sb.append("[ ");
            for (double el : row) {
                sb.append(String.format("%8f ", el));
            }
            sb.append("]\n");
        }
        return sb.toString();
    }

    public Matrix add(double v) {
        Matrix result = getCopy();
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                result.matrix[i][j] += v;
            }
        }
        return result;
    }

    public Matrix add(Matrix other) {
        if (getRows() != other.getRows() || getColumns() != other.getColumns()) {
            throw new IllegalArgumentException("Matrices are not compatible.");
        }

        Matrix result = getCopy();
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                result.matrix[i][j] += other.matrix[i][j];
            }
        }

        return result;
    }

    public Matrix multiply(double v) {
        Matrix result = getCopy();
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                result.matrix[i][j] *= v;
            }
        }
        return result;
    }

    public Matrix subtract(Matrix other) {
        if (getRows() != other.getRows() || getColumns() != other.getColumns()) {
            throw new IllegalArgumentException("Matrices are not compatible.");
        }

        Matrix result = getCopy();
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                result.matrix[i][j] -= other.matrix[i][j];
            }
        }

        return result;
    }



}
