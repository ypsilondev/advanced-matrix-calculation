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

    public String solve() {
        for (int x = 0; x < 10; x++) {
            Terminal.printLine("Pivotize...");
            for (MatrixLine line : lines) {
                for (int i = 1; i <= line.getSize(); i++) {
                    if (!line.get(i).isZero()) {
                        line.pivotize();
                        break;
                    }
                }
            }

            Terminal.printLine(toString());

            Terminal.printLine("Sort...");
            Collections.sort(this.lines);
            Terminal.printLine(toString());

            Terminal.printLine("Add...");

            // todo:
            for (int j = lines.size() - 1; j >= 1; j--) {
                MatrixLine line = lines.get(j);
                MatrixLine previousLine = lines.get(j - 1);
                int i = line.firstNonZeroIndex();
                lines.set(j - 1, line.multiply(previousLine.get(i).getAdditionInverse()).add(previousLine));
                break;
            }

            Terminal.printLine(toString());
        }
        return "";
    }

    public String solve2() {
        for (int i = 1; i <= this.lines.size(); i++) {
            this.sort();
            lines.get(i - 1).pivotize();
            log(String.format("Pivotizing line %d", i));
            for (int j = 1; j <= this.lines.size(); j++) {
                if (j != i) {
                    FieldNumber inverse = lines.get(j - 1).get(i).getAdditionInverse();
                    if(!inverse.isZero()){
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

    public FieldNumber get(int row, int column) {
        return lines.get(row - 1).get(column);
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
