package tech.ypsilon.matrix;

import util.Terminal;
import util.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static int MOD;

    public static void main(String[] args) {
        // Let the user define the field
        System.out.println(Message.DEFINE_FIELD);
        String chosenField = Terminal.readLine();

        try {
            MOD = Integer.parseInt(chosenField);
            Solver activeSolver = new Solver(MOD);
            interact(activeSolver);
        } catch(NumberFormatException e) {
            Terminal.printError(Message.INVALID_FIELD);
        }
    }

    private static void interact(Solver solver) {
        Terminal.printLine(Message.INPUT_MATRIX);


        Matrix matrix = new Matrix();
        int columns = 0;

        String matrixLine;
        while(!(matrixLine = Terminal.readLine().strip()).isBlank()) {
            Pattern matrixLinePattern = Pattern.compile(Message.MATRIX_LINE_REGEX);
            Matcher matrixLineMatcher = matrixLinePattern.matcher(matrixLine);

            if (matrixLineMatcher.find()) {
                int[] numbers = parseNumbers(matrixLine.split(" "));

                if (numbers.length == 0) {
                    Terminal.printError("no empty lines");
                } else {
                    if (columns == 0) {
                        columns = numbers.length;
                    }

                    if (numbers.length != columns) {
                        Terminal.printError("incorrect column size");
                    } else {
                        MatrixLine line = new MatrixLine(numbers);
                        matrix.addLine(line);
                    }
                }
            } else {
                Terminal.printError("no valid line");
            }
        }

        while (!Terminal.readLine().equals("solve"));
        
        Terminal.printLine(matrix.solve());
    }

    private static int[] parseNumbers(String[] numbers) {
        int[] parsedNumbers = new int[numbers.length];

        try {
            for (int i = 0; i < numbers.length; i++) {
                parsedNumbers[i] = Integer.parseInt(numbers[i]);
            }
        } catch(NumberFormatException e) {
            Terminal.printError("invalid matrix line due to the given numbers");
        }

        return parsedNumbers;
    }
}
