package tech.ypsilon.matrix;

import util.Terminal;

import java.util.ArrayList;
import java.util.List;

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


    public boolean isIdentityMatrix(){
        if(this.lines.size() != this.lines.get(0).getSize()){
            return false;
        }
        for(int i = 0; i < lines.size(); i++){
            for(int j = 1; j <= lines.get(i).getSize(); j++){
                if(i == j-1){
                    if(!lines.get(i).get(j).isOne()){
                        return false;
                    }
                }else{
                    if(!lines.get(i).get(j).isZero()){
                        return false;
                    }
                }
            }
        }
        return true;
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
            trace = trace.add(this.lines.get(i).get(i + 1));
        }
        return trace;
    }

    public long countLinearIndependentLines() {
        return this.lines.stream().filter(line -> !line.isZeroLine()).count();
    }

    public Matrix copy() {
        List<MatrixLine> newLines = new ArrayList<>();
        this.lines.stream().forEachOrdered(line -> newLines.add(line.copy()));
        return new Matrix(newLines);
    }

    public void printColumnVectors() {
        for(int j = 1; j <= this.lines.get(0).getSize(); j++){
            List<FieldNumber> values = new ArrayList<>();
            for(int i = 0; i < this.lines.size(); i++){
                values.add(this.lines.get(i).get(j));
            }
            if(!new MatrixLine(values).isZeroLine()){
                Terminal.printLine(EquationSystem.formatVector(String.format("span-vector %d", j), values));
            }
        }
    }
}
