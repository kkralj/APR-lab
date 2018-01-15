package hr.fer.zemris.apr.lab05;

import hr.fer.zemris.apr.lab05.matrix.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class Util {

    public static void writeResults(List<Matrix> result, String filePath) throws FileNotFoundException {
        try (PrintWriter pw = new PrintWriter(new File(filePath))) {
            for (Matrix m : result) {
                for (int i = 0; i < m.getRows(); i++) {
                    for (int j = 0; j < m.getColumns(); j++) {
                        pw.write(m.getValue(i, j) + " ");
                    }
                }
                pw.write("\n");
            }
        }
    }

    public static void print(List<Matrix> result, int iter) {
        System.out.println(result.get(0));
        for (int i = 1; i < result.size(); i++) {
            if (i % iter == 0) {
                System.out.println(result.get(i));
            }
        }
        System.out.println(result.get(result.size() - 1));
    }
}
