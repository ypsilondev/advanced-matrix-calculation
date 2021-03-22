package tech.ypsilon.matrix;

import util.Terminal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import static util.Message.ERROR_UNEQUAL_MATRICES;

public class EquationSystem {

    private final Map<MatrixLine, MatrixLine> rowMap;
    int spanVectorCount = 0;
    Matrix equationMatrix;
    Matrix solutionMatrix;

    public EquationSystem(Matrix equationMatrix, Matrix solutionMatrix) {
        this.rowMap = new HashMap<>();
        this.equationMatrix = equationMatrix.copy();
        this.solutionMatrix = solutionMatrix.copy();

        if (this.equationMatrix.getLines().size() != this.solutionMatrix.getLines().size()) {
            throw new IllegalArgumentException(ERROR_UNEQUAL_MATRICES);
        } else if (this.equationMatrix.getLines().size() == 0) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < this.equationMatrix.getLines().size(); i++) {
            rowMap.put(this.equationMatrix.getLines().get(i), this.solutionMatrix.getLines().get(i));
        }
    }

    public String solve2() {
        List<MatrixLine> lines = equationMatrix.getLines();

        for (int i = 1; i <= lines.size(); i++) {

            this.sort();

            // Add, if line i is already the inverse of line j
            for (int j = 1; j <= lines.size(); j++) {
                if (j != i) {
                    FieldNumber inverse = lines.get(j - 1).get(i).getAdditionInverse();
                    if (inverse.equals(lines.get(i - 1).firstNonZeroNumber())) {
                        if (!inverse.isZero() && !lines.get(i - 1).get(i).isZero()) {
                            lines.set(j - 1, lines.get(j - 1).add(lines.get(i - 1)));
                            this.getCorrespondingSolutionLine(lines.get(j - 1)).add(this.getCorrespondingSolutionLine(lines.get(i - 1)));
                            //this.solutionMatrix.getLines().set(j - 1, this.solutionMatrix.getLines().get(j - 1).add(this.solutionMatrix.getLines().get(i - 1)));
                            this.log(String.format("Adding line %d onto line %d", i, j));
                        }
                    }
                }
            }


            FieldNumber inv = lines.get(i - 1).pivotize();
            this.rowMap.put(lines.get(i - 1), this.getCorrespondingSolutionLine(lines.get(i - 1)).multiply(inv));
            if (!inv.isOne()) {
                this.log(String.format("Pivotizing line %d (times %s)", i, inv.toString()));
            }


            // add the pivotized one.
            for (int j = 1; j <= lines.size(); j++) {
                if (j != i) {
                    FieldNumber inverse = lines.get(j - 1).get(i).getAdditionInverse();
                    if (!inverse.isZero() && !lines.get(i - 1).get(i).isZero()) {
                        lines.set(j - 1, lines.get(j - 1).add(lines.get(i - 1).multiply(inverse)));
                        this.getCorrespondingSolutionLine(lines.get(j - 1)).add(this.getCorrespondingSolutionLine(lines.get(i - 1)).multiply(inverse));
                        // this.solutionMatrix.getLines().set(j - 1, this.solutionMatrix.getLines().get(j - 1).add(this.solutionMatrix.getLines().get(i - 1).multiply(inverse)));
                        this.log(String.format("Adding line %d times %s onto line %d", i, inverse.toString(), j));
                    }
                }
            }
        }
        sort();
        return "";
    }

    private void log(String s) {
        Terminal.printLine(s);
        Terminal.printLine(this.toString());
    }

    public void sort() {
        Collections.sort(this.equationMatrix.getLines());
    }

    public MatrixLine getCorrespondingSolutionLine(MatrixLine line) {
        return rowMap.get(line);
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < this.equationMatrix.getLines().size(); i++) {
            assert this.getCorrespondingSolutionLine(this.equationMatrix.getLines().get(i)) != null;
            output += this.equationMatrix.getLines().get(i).toString() + " | " + this.getCorrespondingSolutionLine(this.equationMatrix.getLines().get(i)).toString() + "\n";
        }
        return output;
    }

    public void getSolutions() {
        Collection<Integer> freeVariablePositions = freeVariablePostions();

        List<FieldNumber> offsetVectorValues = new ArrayList<>();
        this.equationMatrix.getLines().stream().forEachOrdered(line -> offsetVectorValues.add(this.getCorrespondingSolutionLine(line).getFirst()));
        Terminal.printLine(formatVector("Offset vector", offsetVectorValues));
        for (int i : freeVariablePositions) {
            List<FieldNumber> vectorValues = new ArrayList<>();
            String test = "";
            for (int j = 0; j < this.equationMatrix.getLines().size(); j++) {
                if (i != j + 1) {
                    vectorValues.add(equationMatrix.getLines().get(j).get(i).multiply(-1));
                } else {
                    vectorValues.add(new FieldNumber(1));
                }
            }
            Terminal.printLine(formatVector(String.format("Span vector [derived by x_%d]", i), vectorValues));
            spanVectorCount++;
        }
    }

    public static String formatVector(String name, List<FieldNumber> values) {
        String out = name + " = (";
        out += String.join(", ", numberListToStringArr(values));
        out += ")^T";
        return out;
    }

    private static String[] numberListToStringArr(List<FieldNumber> list) {
        String[] tmp = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i) != null){
                tmp[i] = list.get(i).toString();
            }
        }
        return tmp;
    }

    private Collection<Integer> freeVariablePostions() {
        Collection<Integer> pivotPositions = this.pivotPositions();

        Collection<Integer> freeVariablePositions = new TreeSet<>();

        for (int i = 1; i <= this.equationMatrix.getLines().get(0).getSize(); i++) {
            if (!pivotPositions.contains(i)) {
                freeVariablePositions.add(i);
            }
        }
        return freeVariablePositions;
    }

    private Collection<Integer> pivotPositions() {
        Set<Integer> out = new TreeSet<>();
        for (MatrixLine line : this.equationMatrix.getLines()) {
            int index = line.getPivotPosition();
            if(index >= 0){
                out.add(index);
            }
        }
        return out;
    }

    public int countSpanVectors() {
        return spanVectorCount;
    }
}
