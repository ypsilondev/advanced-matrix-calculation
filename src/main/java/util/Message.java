package util;

public class Message {
    public static final String DEFINE_FIELD = "Define the field";
    public static final String INVALID_FIELD = "Invalid field size";
    public static final String INPUT_MATRIX = "Input matrix";
    public static final String ERROR_UNEQUAL_MATRICES = "Matrices must have equal row length";

    private static final String NUMBER_REGEX = "\\d(/\\d)?";

    public static final String MATRIX_LINE_REGEX = "^" + NUMBER_REGEX + "(\\s" + NUMBER_REGEX + ")*$";
    public static final String FULL_ROW_REGEX = "^(" + NUMBER_REGEX + "\\s)+\\|(\\s" + NUMBER_REGEX + ")+$";
}
