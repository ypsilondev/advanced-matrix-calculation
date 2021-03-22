package tech.ypsilon.matrix;

import util.Terminal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MatrixLine implements Comparable<MatrixLine> {
    private List<FieldNumber> numberList = new ArrayList<>();
    private Matrix matrix;


    public MatrixLine(int[] numbers) {
        for (int number : numbers) {
            numberList.add(new FieldNumber(number));
        }
    }

    public MatrixLine(FieldNumber[] numbers) {
        for (FieldNumber number : numbers) {
            numberList.add(number);
        }
    }

    public MatrixLine() {
    }

    public MatrixLine setMatrix(Matrix matrix) {
        this.matrix = matrix;
        return this;
    }

    public MatrixLine add(MatrixLine other) {
        assert this.numberList.size() == other.numberList.size();
        for (int i = 0; i < this.numberList.size(); i++) {
            this.numberList.set(i, this.numberList.get(i).add(other.numberList.get(i)));
        }
        return this;
    }

    public FieldNumber get(int column) {
        return numberList.get(column - 1);
    }

    public FieldNumber getLast() {
        return numberList.get(numberList.size() - 1);
    }

    public FieldNumber pivotize() {
        for (int i = 1; i <= this.numberList.size(); i++) {
            if (!get(i).isZero()) {
                FieldNumber inverse = get(i).getMultiplicativeInverse();
                numberList = numberList.stream().map(n -> n.multiply(inverse)).collect(Collectors.toList());
                return inverse;
            }
        }
        return new FieldNumber(1);
    }

    public MatrixLine multiply(FieldNumber number) {
        MatrixLine output = new MatrixLine();
        output.numberList = new ArrayList<>();
        for (int i = 0; i < this.numberList.size(); i++) {
            output.numberList.add(this.numberList.get(i).multiply(number));
        }
        return output.setMatrix(matrix);
    }

    public int getSize() {
        return numberList.size();
    }

    public int firstNonZeroIndex() {
        for (int i = 1; i <= numberList.size(); i++) {
            if (!get(i).isZero()) {
                return i;
            }
        }
        return numberList.size();
    }

    public int getPivotPosition(){
        for (int i = 1; i <= numberList.size(); i++) {
            if (!get(i).isZero()) {
                return i;
            }
        }
        return -1;
    }

    public FieldNumber firstNonZeroNumber() {
        return this.get(firstNonZeroIndex());
    }

    public boolean isFinalized() {
        int entryCounter = 0;
        for (FieldNumber fieldNumber : numberList) {
            if (!fieldNumber.isZero() && numberList.indexOf(fieldNumber) != numberList.size() - 1) {
                entryCounter++;
            }
        }
        return entryCounter <= 1;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (FieldNumber fieldNumber : numberList) {
            if (builder.length() > 0) {
                builder.append(" ");
            }
            builder.append(fieldNumber.toString());
        }
        /*if(matrix.withSolutions){
            return new StringBuilder(builder.reverse().toString().replaceFirst(" ", " | ")).reverse().toString();
        }*/
        return builder.toString();
    }

    @Override
    public int compareTo(MatrixLine o) {
        if (firstNonZeroIndex() == o.firstNonZeroIndex()) {
            boolean isThisFirst = false;
            for (int i = firstNonZeroIndex() + 1; i < numberList.size(); i++) {
                if (get(i).compareTo(o.get(i)) < 0) {
                    break;
                } else if (get(i).compareTo(o.get(i)) > 0) {
                    isThisFirst = true;
                    break;
                }
            }

            if(firstNonZeroIndex() == numberList.size()){
                return firstNonZeroNumber().compareTo(o.firstNonZeroNumber());
            }

            return isThisFirst ? -1 : 1;
        } else {
            return firstNonZeroIndex() - o.firstNonZeroIndex();
        }
    }

    public void set(int j, FieldNumber fieldNumber) {
        this.numberList.set(j, fieldNumber);
    }

    public boolean isZeroLine(){
        return this.numberList.stream().allMatch(FieldNumber::isZero);
    }

    public FieldNumber getFirst() {
        try{
            return this.get(1);
        }catch (IndexOutOfBoundsException e){
            return null;
        }
    }


    public MatrixLine copy() {
        FieldNumber[] numbers = new FieldNumber[this.numberList.size()];
        for(int i = 0; i<this.numberList.size(); i++){
            numbers[i] = numberList.get(i).copy();
        }
        return new MatrixLine(numbers);
    }
}
