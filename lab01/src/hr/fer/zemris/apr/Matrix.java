package hr.fer.zemris.apr;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Matrix {

    private double[][] matrix;

    private int rows, cols;

    public Matrix(double[][] matrix) {
        this.matrix = Objects.requireNonNull(matrix);
        this.rows = matrix.length;
        this.cols = matrix[0].length;
    }

    private double[][] getCopy() {
        double[][] m = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(matrix[i], 0, m[i], 0, cols);
        }
        return m;
    }

    public Matrix add(Matrix m) {
        if (rows != m.getRows() && cols != m.getCols()) throw new IllegalArgumentException();
        double[][] result = getCopy();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] += m.getValue(i, j);
            }
        }

        return new Matrix(result);
    }

    public Matrix multiply(Matrix m) {
        if (cols != m.rows) throw new IllegalArgumentException();
        double[][] result = new double[rows][m.cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                for (int k = 0; k < cols; k++) {
                    result[i][j] += matrix[i][k] * m.getValue(k, j);
                }
            }
        }
        return new Matrix(result);
    }

    public Matrix transpose() {
        double[][] result = new double[cols][rows];

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                result[i][j] = matrix[j][i];
            }
        }

        return new Matrix(result);
    }

    public Matrix multiply(double scalar) {
        double[][] result = getCopy();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] *= scalar;
            }
        }
        return new Matrix(result);
    }

    public Matrix subtract(Matrix m) {
        if (rows != m.getRows() && cols != m.getCols()) throw new IllegalArgumentException();
        double[][] result = getCopy();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] -= m.getValue(i, j);
            }
        }

        return new Matrix(result);
    }

    public void writeMatrix(String dataPath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(dataPath));
        for (double[] row : matrix) {
            for (double val : row) {
                bw.write(val + " ");
            }
            bw.write("\n");
        }
        bw.close();
    }

    public static Matrix readMatrix(Path dataPath) throws IOException {
        List<String> rows = Files.readAllLines(dataPath);

        double[][] matrix = null;
        int rowCount = rows.size(), colCount;

        for (int i = 0; i < rowCount; i++) {
            String[] values = rows.get(i).split("\\s+");

            if (matrix == null) {
                colCount = values.length;
                matrix = new double[rowCount][colCount];
            }

            for (int j = 0; j < values.length; j++) {
                matrix[i][j] = Double.parseDouble(values[j]);
            }
        }

        return new Matrix(matrix);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public void setValue(int x, int y, double val) {
        matrix[x][y] = val;
    }

    public double getValue(int x, int y) {
        return matrix[x][y];
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (double[] row : matrix) {
            for (double val : row) {
                s.append(" ").append(val);
            }
            s.append("\n");
        }
        return s.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Matrix matrix1 = (Matrix) o;

        if (rows != matrix1.rows) return false;
        if (cols != matrix1.cols) return false;
        return Arrays.deepEquals(matrix, matrix1.matrix);
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(matrix);
        result = 31 * result + rows;
        result = 31 * result + cols;
        return result;
    }

    public Matrix clone() {
        return new Matrix(this.matrix);
    }
}
