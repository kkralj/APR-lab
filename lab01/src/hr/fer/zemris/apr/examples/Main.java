package hr.fer.zemris.apr.examples;

import hr.fer.zemris.apr.LUPSolver;
import hr.fer.zemris.apr.Matrix;
import hr.fer.zemris.apr.LUSolver;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Select LU or LUP algorithm");
        }

        System.out.println("Matrix A:");
        Matrix A = Matrix.readMatrix(Paths.get("examples/lab/ex5.txt"));
        System.out.println(A);

        System.out.println("Matrix b:");
        Matrix b = Matrix.readMatrix(Paths.get("examples/lab/ex5b.txt"));
        System.out.println(b);

        if (args[0].toUpperCase().equals("LUP")) {
            solveLUP(A, b);

        } else {
            solveLU(A, b);
        }
    }

    private static void solveLU(Matrix A, Matrix b) {
        System.out.println("Decomposed LA matrix:");
        Matrix LU = LUSolver.decomposeLU(A);
        System.out.println(LU);

        System.out.println("Matrix y (Ly=b):");
        Matrix y = LUSolver.forwardSubstitute(LU, b);
        System.out.println(y);

        System.out.println("Solution -> Matrix x (Ux=y)");
        Matrix x = LUSolver.backwardSubstitute(LU, y);
        System.out.println(x);
    }


    private static void solveLUP(Matrix A, Matrix b) {
        LUPSolver solver = new LUPSolver(A);

        System.out.println("Decomposed LUP matrix:");
        Matrix matrixLUP = solver.decompose();
        System.out.println(matrixLUP);

        System.out.println("Matrix P");
        Matrix p = solver.getP();
        System.out.println(p);

        System.out.println("Pb");
        Matrix pb = p.multiply(b);
        System.out.println(pb);

        System.out.println("Matrix y (Ly=b):");
        Matrix y = LUSolver.forwardSubstitute(matrixLUP, pb);
        System.out.println(y);

        System.out.println("Solution -> Matrix x (Ux=y)");
        Matrix x = LUSolver.backwardSubstitute(matrixLUP, y);
        System.out.println(x);


    }
}
