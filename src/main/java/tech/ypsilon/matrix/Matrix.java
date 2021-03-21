package tech.ypsilon.matrix;

import util.Terminal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Matrix {
    private final List<MatrixLine> lines = new ArrayList<>();

    public void addLine(MatrixLine line) {
        this.lines.add(line);
    }

    public String solve2() {
        for (int i = 1; i <= this.lines.size(); i++) {

            this.sort();

            // Add, if line i is already the inverse of line j
            for (int j = 1; j <= this.lines.size(); j++) {
                if (j != i) {
                    FieldNumber inverse = lines.get(j - 1).get(i).getAdditionInverse();
                    if (inverse.equals(lines.get(i - 1).firstNonZeroNumber())) {
                        if (!inverse.isZero() && !lines.get(i-1).get(i).isZero()) {
                            lines.set(j - 1, lines.get(i - 1).add(lines.get(j - 1)));
                            log(String.format("Adding line %d onto line %d", i, j));
                        }
                    }
                }
            }


            FieldNumber inv = lines.get(i - 1).pivotize();
            if (!inv.isOne()) {
                log(String.format("Pivotizing line %d (times %s)", i, inv.toString()));
            }


            // add the pivotized one.
            for (int j = 1; j <= this.lines.size(); j++) {
                if (j != i) {
                    FieldNumber inverse = lines.get(j - 1).get(i).getAdditionInverse();
                    if (!inverse.isZero() && !lines.get(i-1).get(i).isZero()) {
                        lines.set(j - 1, lines.get(i - 1).multiply(inverse).add(lines.get(j - 1)));
                        log(String.format("Adding line %d times %s onto line %d", i, inverse.toString(), j));
                    }
                }
            }
        }
        return "";
    }

    private void log(String s) {
        Terminal.printLine(s);
        Terminal.printLine(toString());
    }

    public void sort() {
        Collections.sort(this.lines);
    }

    public String toString() {
        String[] lineNames = new String[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            lineNames[i] = lines.get(i).toString();
        }

        StringBuilder builder = new StringBuilder();
        for (String lineName : lineNames) {
            if (builder.length() > 0) {
                builder.append("\n");
            }
            builder.append(lineName);
        }

        return builder.toString();
    }

}
