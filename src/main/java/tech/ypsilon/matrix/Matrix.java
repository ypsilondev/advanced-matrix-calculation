package tech.ypsilon.matrix;

import util.Terminal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Matrix {
    private final List<MatrixLine> lines;

    public Matrix(List<MatrixLine> newLines) {
        this.lines = newLines;
        this.lines.forEach(line -> line.setMatrix(this));
    }

    public Matrix() {
        this.lines = new ArrayList<>();
    }

    public static Matrix zeroMatrix(int height, int width) {
        List<MatrixLine> lines = new ArrayList<>();
        FieldNumber[] lineContent = new FieldNumber[width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < lineContent.length; j++) {
                lineContent[j] = new FieldNumber(0);
            }
            lines.add(new MatrixLine(lineContent));
        }
        return new Matrix(lines);
    }

    public static Matrix identityMatrix(int size) {
        List<MatrixLine> lines = new ArrayList<>();
        FieldNumber[] lineContent = new FieldNumber[size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                lineContent[j] = new FieldNumber(i == j ? 1 : 0);
            }
            lines.add(new MatrixLine(lineContent));
        }
        return new Matrix(lines);
    }

    public void addLine(MatrixLine line) {
        line.setMatrix(this);
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
                        if (!inverse.isZero() && !lines.get(i - 1).get(i).isZero()) {
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
                    if (!inverse.isZero() && !lines.get(i - 1).get(i).isZero()) {
                        lines.set(j - 1, lines.get(i - 1).multiply(inverse).add(lines.get(j - 1)));
                        log(String.format("Adding line %d times %s onto line %d", i, inverse.toString(), j));
                    }
                }
            }
        }
        return "";
    }

    public Matrix transposeMatrix() {
        List<MatrixLine> newLines = new ArrayList<>();

        for (int i = 0; i < lines.get(0).getSize(); i++) {
            FieldNumber[] lineContent = new FieldNumber[lines.size()];
            for (int j = 0; j < lines.size(); j++) {
                lineContent[j] = lines.get(j).get(i + 1);
            }
            newLines.add(new MatrixLine(lineContent));
        }

        return new Matrix(newLines);
    }


    public void getSolutions() {
        Collection<Integer> freeVariablePositions = freeVariablePostions();

        List<FieldNumber> offsetVectorValues = new ArrayList<>();
        this.lines.stream().forEachOrdered(line -> offsetVectorValues.add(line.getLast()));
        Terminal.printLine(formatVector("Offset vector", offsetVectorValues));

        for (int i : freeVariablePositions) {
            List<FieldNumber> vectorValues = new ArrayList<>();
            String test = "";
            for (int j = 0; j < this.lines.size(); j++) {
                if (i != j + 1) {
                    vectorValues.add(lines.get(j).get(i).multiply(-1));
                } else {
                    vectorValues.add(new FieldNumber(1));
                }
            }
            Terminal.printLine(formatVector(String.format("Span vector [derived by x_%d]", i), vectorValues));
        }
    }

    private String formatVector(String name, List<FieldNumber> values) {
        String out = name + " = (";
        out += String.join(", ", numberListToStringArr(values));
        out += ")^T";
        return out;
    }

    private String[] numberListToStringArr(List<FieldNumber> list) {
        String[] tmp = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            tmp[i] = list.get(i).toString();
        }
        return tmp;
    }

    private Collection<Integer> freeVariablePostions() {
        Collection<Integer> pivotPositions = this.pivotPositions();

        Collection<Integer> freeVariablePositions = new TreeSet<>();

        for (int i = 1; i < this.lines.get(0).getSize(); i++) {
            if (!pivotPositions.contains(i)) {
                freeVariablePositions.add(i);
            }
        }
        return freeVariablePositions;
    }

    private Collection<Integer> pivotPositions() {
        Set<Integer> out = new TreeSet<>();
        for (MatrixLine line : this.lines) {
            out.add(line.firstNonZeroIndex());
        }
        return out;
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

    public List<MatrixLine> getLines() {
        return this.lines;
    }

    public FieldNumber getTrace() {
        FieldNumber trace = new FieldNumber(0);
        for (int i = 0; i < this.lines.size(); i++) {
            trace.add(this.lines.get(i).get(i + 1));
        }
        return trace;
    }

    public long countLinearIndependentLines() {
        return this.lines.stream().filter(line -> !line.isZeroLine()).count();
    }
}
