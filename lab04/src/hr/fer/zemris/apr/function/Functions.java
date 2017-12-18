package hr.fer.zemris.apr.function;

public class Functions {

    public static final Function F1 = point -> {
        if (point.size() != 2) throw new IllegalArgumentException();
        return 100 * Math.pow(point.get(1) - point.get(0) * point.get(0), 2) + Math.pow(1 - point.get(0), 2);
    };

    public static final Function F3 = point -> {
        double res = 0;
        for (int i = 1; i <= point.size(); i++) {
            res += Math.pow(point.get(i - 1) - i, 2);
        }
        return res;
    };

    public static final Function F6 = point -> {
        double sum = 0;
        for (Double x : point) {
            sum += x * x;
        }
        return 0.5 + (Math.pow(Math.sin(Math.sqrt(sum)), 2) - 0.5) / Math.pow(1 + 0.001 * sum, 2);
    };

    public static final Function F7 = point -> {
        double sum = 0;
        for (Double x : point) {
            sum += x * x;
        }
        return Math.pow(sum, 0.25) * (1 + Math.pow(Math.sin(50 * Math.pow(sum, 0.1)), 2));
    };
}
