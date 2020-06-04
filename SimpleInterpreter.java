import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Program which acts as a basic interpreter for a Made-up Programming Language.
 *
 * @author Simon Hyslop
 * @version 2019-12-10
 */
public class SimpleInterpreter {

    private static ArrayList<String> inputs = new ArrayList<>();
    private static ArrayList<ArrayList<String>> parsed = new ArrayList<>();
    private static ArrayList<Integer> values = new ArrayList<>();
    private static ArrayList<Character> symbols = new ArrayList<>();
    private static boolean verboseOutput;


    /**
     * Method for reading the instruction file, parsing the values, and storing them in an ArrayList.
     * @param filename Name of the file containing instructions for SimpleInterpreter to process.
     */
    public static void parse(String filename) throws FileNotFoundException {
        Scanner inputFile = new Scanner(new File(filename));

        while (inputFile.hasNext()) {
            inputs.add(inputFile.nextLine());
        }

        // Now we need to process the raw input into a usable format.
        String[][] splitInputs = new String[inputs.size()][2];

        for (int i = 0; i < splitInputs.length; i++) {
            splitInputs[i] = inputs.get(i).split("=", 2);
        }

        // First we need to determine the argument in the expression
        for (int i = 0; i < splitInputs.length; i++) {
            ArrayList<String> argument = new ArrayList<>();
            if (splitInputs[i].length == 1) {
                // Mark as 'return'
                argument.add("2");
            } else if (splitInputs[i][1].indexOf("+") != -1) {
                // Mark as 'addition'
                argument.add("1");
            } else {
                // Mark as 'assignment'
                argument.add("0");
            }

            // Add first part of instruction (variable name), stripping whitespace
            argument.add(splitInputs[i][0].replaceAll("\\s", ""));

            // Add remaining instruction (if any)
            for (int j = 1; j < splitInputs[i].length; j++) {

                // For assignment, strip whitespace
                if (argument.get(0).equals("0")) {
                    argument.add(splitInputs[i][j].replaceAll("\\s", ""));

                    // For addition need to do something else
                } else if (argument.get(0).equals("1")) {
                    // Split using '+' symbol
                    String[] addValues = splitInputs[i][j].split("\\u002B");

                    for (String s : addValues) {
                        argument.add(s.replaceAll("\\s", ""));
                    }
                }
            }
            parsed.add(argument);
        }

        if (verboseOutput) {
            // Print all parsed inputs to screen
            System.out.println("Parsed inputs:");
            for (ArrayList<String> s : parsed) {
                System.out.println(s);
            }
        }
    }

    /**
     * Method for interpreting the ArrayList 'parsed' of parsed values.
     */
    public static void interpret() {
        if (verboseOutput) {
            System.out.println("\nInterpreter output:");
        }

        for (ArrayList<String> p : parsed) {
            // Addition
            if (p.get(0).equals("1")) {
                int sum = 0;
                // Add all values from ArrayList position 2 onwards
                for (int j= 2; j < p.size(); j++) {
                    String elemString = p.get(j);
                    sum += stringToInt(p.get(j));
                }
                // Store them to the values ArrayList
                store(p.get(1).charAt(0), sum);

                // Assignment
            } else if (p.get(0).equals("0")) {
                store(p.get(1).charAt(0), stringToInt(p.get(2)));

                // Return
            } else {
                System.out.println(load(p.get(1).charAt(0)));
            }
        }
    }

    /**
     * Method to find the ArrayList location of a symbol/variable
     * @param symbol character of symbol to be looked up
     * @return int of ArrayList location where it can be found
     * @throws IllegalArgumentException if symbol is not in list
     */
    private static int findSymbol(char symbol) throws IllegalArgumentException {
        // If symbol in 'symbols', return ArrayList location
        for (int i = 0; i < symbols.size(); i++) {
            if (symbols.get(i) == symbol) {
                return i;
            }
        }
        // Else if symbol not in ArrayList, return error
        throw new IllegalArgumentException("Symbol not found.");
    }

    /**
     * Method to load the value of a symbol/variable.
     * @param symbol character of symbol for which value is to be loaded
     * @return value assigned to the given symbol
     */
    private static int load(char symbol) {
        return values.get(findSymbol(symbol));
    }

    /**
     * Method to store a value to a given symbol/variable.
     * @param symbol symbol to be used to store the value
     * @param value value which is to be stored
     */
    private static void store(char symbol, int value) {
        try {
            values.set(findSymbol(symbol), value);
        } catch (IllegalArgumentException e) {
            symbols.add(symbol);
            values.add(value);
        }
    }

    /**
     * Method for converting a String containing a symbol or number to an int.
     * @param elemString String to be converted
     * @return int which String has been converted into
     */
    private static int stringToInt(String elemString) {
        // If element is a variable, need to load value
        if (elemString.matches(".*[a-zA-Z].*")) {
            // Variable: Check if negative
            if (elemString.charAt(0) == '-') {
                // If negative, load value of variable and make negative
                return load(elemString.charAt(1)) * -1;
                // Else if positive, load value of variable
            } else {
                return load(elemString.charAt(0));
            }
            // Else if value is already a number, convert it to an int
        } else {
            return Integer.parseInt(elemString);
        }
    }

    public static void main(String[] args) {
        // Set to true for parsed values to also be printed to screen
        verboseOutput = true;

        try {
            // Parse the input file into ArrayLists
            parse("input.txt");
            // Interpret parsed instructions
            interpret();
        } catch (FileNotFoundException e1) {
            System.out.println("Error: Instruction file could not be found. Please try again.");
        } catch (IllegalArgumentException e2) {
            System.out.println("Error: You have referenced a symbol/variable which does not exist. Please check your input file and try again.");
        }
    }

}
