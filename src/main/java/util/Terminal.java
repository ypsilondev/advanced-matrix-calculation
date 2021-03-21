package util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class provides some simple methods for input/output from and to a
 * terminal as well as a method to read in files.
 *
 * <p>
 * <b>Never modify this class, never upload it to Praktomat.</b> This is only
 * for your local use. If an assignment tells you to use this class for input
 * and output never use System.out, System.err or System.in in the same
 * assignment.
 *
 * @author ITI, VeriAlg Group
 * @author IPD, SDQ Group
 * @version 5.04, 2020/09/12
 */
@SuppressWarnings("unused")
public final class Terminal {
    /**
     * A simple debugger
     */
    public static final Debugger DEBUGGER = new Debugger(System.out);

    private static final boolean TEST_CREATE = false;


    private static final Queue<String> COMMAND_QUEUE = new LinkedList<>();

    private static final Queue<String> IO_DATA = new LinkedList<>();

    private static String lastOutput = "";


    /**
     * Reads text from the "standard" input stream, buffering characters so as to
     * provide for the efficient reading of characters, arrays, and lines. This
     * stream is already open and ready to supply input data and corresponds to
     * keyboard input.
     */
    private static final BufferedReader IN = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Private constructor to avoid object generation.
     */
    private Terminal() {
        printLine(new char[]{'a'});
        loadCommandList("", false);
        printIOData();
        dumpIOData("");
        DEBUGGER.log(DEBUGGER);
        throw new IllegalStateException("Utility-class constructor.");
    }

    /**
     * Prints the given error-{@code message} with the prefix "{@code Error, }".
     *
     * <p>
     * More specific, this method behaves exactly as if the following code got
     * executed: <blockquote>
     *
     * <pre>
     * Terminal.printLine("Error, " + message);
     * </pre>
     *
     * </blockquote>
     *
     * @param message the error message to be printed
     * @see #printLine(Object)
     */
    public static void printError(final String message) {
        if (TEST_CREATE && false) {
            Terminal.printLine("<e");
        } else {
            Terminal.printLine("Error, " + message);
        }
    }

    /**
     * Prints the string representation of an {@code Object} and then terminate the
     * line.
     *
     * <p>
     * If the argument is {@code null}, then the string {@code "null"} is printed,
     * otherwise the object's string value {@code obj.toString()} is printed.
     *
     * @param object the {@code Object} to be printed
     * @see String#valueOf(Object)
     */
    public static void printLine(final Object object) {
        System.out.println(object);
        IO_DATA.add(object.toString());
        lastOutput = object.toString();
    }

    /**
     * Prints an array of characters and then terminate the line.
     *
     * <p>
     * If the argument is {@code null}, then a {@code NullPointerException} is
     * thrown, otherwise the value of {@code
     * new String(charArray)} is printed.
     *
     * @param charArray an array of chars to be printed
     * @see String#valueOf(char[])
     */
    public static void printLine(final char[] charArray) {
        /*
         * Note: This method's sole purpose is to ensure that the Terminal-class behaves
         * exactly as System.out regarding output. (System.out.println(char[]) calls
         * String.valueOf(char[]) which itself returns 'new String(char[])' and is
         * therefore the only method that behaves differently when passing the provided
         * parameter to the System.out.println(Object) method.)
         */
        StringBuilder s = new StringBuilder();
        for (char c : charArray) {
            s.append(c);
        }
        IO_DATA.add(s.toString());
        System.out.println(charArray);
        lastOutput = s.toString();
    }

    /**
     * Reads a line of text. A line is considered to be terminated by any one of a
     * line feed ('\n'), a carriage return ('\r'), or a carriage return followed
     * immediately by a linefeed.
     *
     * @return a {@code String} containing the contents of the line, not including
     *         any line-termination characters, or {@code null} if the end of the
     *         stream has been reached
     * @throws RuntimeException lol
     */
    public static String readLine() {
        try {
            String read;
            if (TEST_CREATE) {
                System.out.print("> ");
            }
            if (!COMMAND_QUEUE.isEmpty()) {
                System.out.print("> ");
                System.out.println(COMMAND_QUEUE.peek());
                read = COMMAND_QUEUE.poll();
            } else {
                read = IN.readLine();
            }
            IO_DATA.add("> " + read);
            return read;
        } catch (final IOException e) {
            /*
             * The IOException will not occur during tests executed by the praktomat,
             * therefore the following RuntimeException does not have to get handled.
             */
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads all commands from the file "path" into the queue; then executes one
     * after each other
     *
     * @param path      the path of the test-file
     * @param formatted true, if the file is formatted, false if not
     */
    public static void loadCommandList(String path, boolean formatted) {
        String[] commands = Terminal.readFile(path);
        for (String cmd : commands) {
            if (formatted) {
                if (!cmd.startsWith("> ")) {
                    continue;
                }
            }
            COMMAND_QUEUE.add(cmd.replaceFirst("> ", ""));
        }
    }

    public static void loadCommandList(String... commands) {
        for (String cmd : commands){
            COMMAND_QUEUE.add(cmd);
        }
    }



    /**
     * Prints all IO happened so far to the console
     */
    public static void printIOData() {
        for (String line : IO_DATA) {
            System.out.println(line);
        }
    }

    /**
     * Dumps all IO happened so far into the file "path".
     *
     * The file will be formatted as code-tester does.
     *
     * @param path the path of the file to write to
     * @throws RuntimeException lol
     */
    public static void dumpIOData(String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (String line : IO_DATA) {
                writer.write(line);
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLastOutput() {
        return lastOutput;
    }

    /**
     * Reads the file with the specified path and returns its content stored in a
     * {@code String} array, whereas the first array field contains the file's first
     * line, the second field contains the second line, and so on.
     *
     * @param path the path of the file to be read
     * @return the content of the file stored in a {@code String} array
     * @throws RuntimeException lol
     */
    public static String[] readFile(final String path) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(path))) {
            return reader.lines().toArray(String[]::new);
        } catch (final IOException e) {
            /*
             * You can expect that the praktomat exclusively provides valid file-paths.
             * Therefore there will no IOException occur while reading in files during the
             * tests, the following RuntimeException does not have to get handled.
             */
            throw new RuntimeException(e);
        }
    }

    /**
     * Clears the command-list
     */
    public static void clearCommandList() {
        COMMAND_QUEUE.clear();
        IO_DATA.clear();
    }

    /**
     *
     */
    @SuppressWarnings("unused")
    public static final class Debugger {
        private final PrintStream outputStream;

        private Debugger(PrintStream outputStream) {
            this.outputStream = outputStream;
        }

        /**
         * Prints the object to the outputStream
         *
         * @param obj the object to be printed
         */
        public void println(Object obj) {
            outputStream.println(obj);
        }

        /**
         * Logs in Debug-format
         *
         * @param obj object to be print
         */
        public void log(Object obj) {
            StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            StackTraceElement caller = trace[trace.length - 1];
            this.println(
                    String.format("%s:%s:  %s", caller.getClassName(), caller.getLineNumber(), obj.toString()));
        }

    }

}