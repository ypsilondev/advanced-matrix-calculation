package tech.ypsilon.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MatrixLine implements Comparable<MatrixLine> {
    private List<FieldNumber> numberList = new ArrayList<>();

    public MatrixLine(int[] numbers) {
        for (int number : numbers) {
            numberList.add(new FieldNumber(number));
        }
    }

    private MatrixLine() {
    }

    public MatrixLine add(MatrixLine other) {
        MatrixLine output = new MatrixLine();
        output.numberList = new ArrayList<>();
        for (int i = 0; i < this.numberList.size(); i++) {
            output.numberList.add(this.numberList.get(i).add(other.numberList.get(i)));
        }
        return output;
    }

    public FieldNumber get(int column) {
        return numberList.get(column - 1);
    }

    public FieldNumber pivotize() {
        for (int i = 1; i <= this.numberList.size(); i++) {
            if (!get(i).isZero()) {
                FieldNumber inverse = get(i).getMultiplicalInverse();
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
        return output;
    }

    public int getSize() {
        return numberList.size();
    }

    public int firstNonZeroIndex() {
        for (int i = 1; i <= numberList.size() - 1; i++) {
            if (!get(i).isZero()) {
                return i;
            }
        }
        return numberList.size();
    }

    public FieldNumber firstNonZeroNumber(){
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

        return new StringBuilder(builder.reverse().toString().replaceFirst(" ", " | ")).reverse().toString();
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

            return isThisFirst ? -1 : 1;
        } else {
            return firstNonZeroIndex() - o.firstNonZeroIndex();
        }
    }

}
