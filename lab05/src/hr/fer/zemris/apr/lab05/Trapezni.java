package hr.fer.zemris.apr.lab05;

public class Trapezni {

    public static Matrix calculate(Matrix A, Matrix x0, double T, double interval) {
        Matrix xp = x0;

        Matrix U = Matrix.getIdentity(A.getRows());
        Matrix R = U.subtract(A.multiply(T / 2)).getInverse().multiply(U.add(A.multiply(T / 2)));

        for (double total = 0; total < interval; total += T) {
            xp = R.multiply(xp);
            System.out.println(xp);
        }

        return xp;
    }

}
