package tech.ypsilon.matrix;

import util.Terminal;
import util.Message;

public class Main {

    public static int MOD;

    public static void main(String[] args) {
        // Let the user define the field
        System.out.println(Message.DEFINE_FIELD);
        String chosenField = Terminal.readLine();

        try {
            MOD = Integer.parseInt(chosenField);
            interact();
        } catch (NumberFormatException e) {
            Terminal.printError(Message.INVALID_FIELD);
        }
    }

    private static void interact() {
        Terminal.printLine(Message.INPUT_MATRIX);

        Matrix equationMatrix = new Matrix();
        Matrix solutionMatrix = new Matrix();
        int columns = 0;

        String matrixLine;
        while (!(matrixLine = Terminal.readLine().strip()).isBlank()) { // basically does stuff

            if (matrixLine.matches(Message.FULL_ROW_REGEX) || matrixLine.matches(Message.MATRIX_LINE_REGEX)) {
                // int[] numbers = parseNumbers(matrixLine.split(" "));
                int[] equationNumbers = parseNumbers(matrixLine.split("\\s\\|\\s")[0].split(" "));
                int[] solutionNumbers;
                if (matrixLine.split("\\s\\|\\s").length == 2) {
                    solutionNumbers = parseNumbers(matrixLine.split("\\s\\|\\s")[1].split(" "));
                } else {
                    solutionNumbers = new int[0];
                }

                if (equationNumbers.length == 0) {
                    Terminal.printError("no empty lines");
                } else {
                    if (columns == 0) {
                        columns = equationNumbers.length;
                    }

                    if (equationNumbers.length != columns) {
                        Terminal.printError("incorrect column size");
                    } else {
                        MatrixLine equationLine = new MatrixLine(equationNumbers);
                        equationMatrix.addLine(equationLine);
                        MatrixLine solutionLine = new MatrixLine(solutionNumbers);
                        solutionMatrix.addLine(solutionLine);
                    }
                }
            } else {
                Terminal.printError("no valid line");
            }
        }

        EquationSystem eqs;
        switch (Terminal.readLine()) {
            case "solve":
                eqs = new EquationSystem(equationMatrix, solutionMatrix);
                eqs.solve2();
                eqs.getSolutions();
                //Terminal.printLine(eqs.solve2());
                break;
            case "trace":
                Terminal.printLine(equationMatrix.getTrace());
                break;
            case "image":
                eqs = new EquationSystem(equationMatrix.transposeMatrix(), solutionMatrix);
                eqs.solve2();
                Terminal.printLine(eqs.equationMatrix.transposeMatrix().toString());
                break;
            case "transpose":
                Terminal.printLine(equationMatrix.transposeMatrix());
                break;
            case "rank":
                eqs = new EquationSystem(equationMatrix, solutionMatrix);
                eqs.solve2();
                // eqs.getSolutions();
                Terminal.printLine(eqs.equationMatrix);
                Terminal.printLine(String.format("Rank of matrix is %d", eqs.equationMatrix.countLinearIndependentLines()));
                break;
            case "kernel":
                eqs = new EquationSystem(equationMatrix, Matrix.zeroMatrix(equationMatrix.getLines().size(), 1));
                eqs.solve2();
                Terminal.printLine(eqs.equationMatrix);
                eqs.getSolutions();
                break;
            case "invert":
                eqs = new EquationSystem(equationMatrix, Matrix.identityMatrix(equationMatrix.getLines().size()));
                eqs.solve2();
                Terminal.printLine(eqs.toString());
                // eqs.getSolutions();
                break;
            default:
                break;
        }
    }

    private static int[] parseNumbers(String[] numbers) {
        int[] parsedNumbers = new int[numbers.length];

        try {
            for (int i = 0; i < numbers.length; i++) {
                parsedNumbers[i] = Integer.parseInt(numbers[i]);
            }
        } catch (NumberFormatException e) {
            Terminal.printError("invalid matrix line due to the given numbers");
        }

        return parsedNumbers;
    }
}
