package hr.fer.zemris.apr.lab05;

public class RungeKutta {

    public static Matrix calculate(Matrix A, Matrix x0, double T, double interval) {
        Matrix xp = x0;

        for (double total = 0; total < interval; total += T) {
            Matrix m1 = A.multiply(xp);
            Matrix m2 = A.multiply(xp.add(m1.multiply(T / 2)));
            Matrix m3 = A.multiply(xp.add(m2.multiply(T / 2)));
            Matrix m4 = A.multiply(xp.add(m3.multiply(T)));

            Matrix msums = m1.add(m2.multiply(2)).add(m3.multiply(2)).add(m4);

            xp = xp.add(msums.multiply(T / 6));

            System.out.println(xp);
        }

        return xp;
    }

}
