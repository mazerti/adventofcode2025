import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class Repeated {

    public static void main(String[] args) {
        System.out.print("Part One: ");
        partOne(args[0]);

    }

    private static void partOne(String inputFileName) {
        BigInteger result = new BigInteger("0");
        try {
            String[][] ranges = getInputRanges(inputFileName);
            for (String[] range : ranges) {
                result = result.add(retrieveRepeats(range));
            }
            System.err.println(result);
        } catch (FileNotFoundException e) {
            System.out.println("Please provide a valid input file.");
        }
    }

    private static BigInteger retrieveRepeats(String[] range) {
        int minNbDigits = range[0].length();
        int maxNbDigits = range[1].length();
        if (minNbDigits == 0 || maxNbDigits == 0 || minNbDigits >= maxNbDigits && minNbDigits % 2 == 1)
            return new BigInteger("0"); // odd number of digits: no possible repetition
        if (minNbDigits % 2 == 1) {
            range[0] = "1" + "0".repeat(minNbDigits++);
        }
        if (maxNbDigits % 2 == 1) {
            range[1] = "9".repeat(--maxNbDigits);
        }

        int minLeftHand = Integer.parseInt(range[0].substring(0, minNbDigits / 2 + (minNbDigits % 2 == 0 ? 0 : 1)));
        int maxLeftHand = Integer.parseInt(range[1].substring(0, maxNbDigits / 2 + (minNbDigits % 2 == 0 ? 0 : 1)));
        int minRightHand = Integer.parseInt(range[0].substring(minNbDigits / 2 + (minNbDigits % 2 == 0 ? 0 : 1)));
        int maxRightHand = Integer.parseInt(range[1].substring(maxNbDigits / 2 + (minNbDigits % 2 == 0 ? 0 : 1)));
        BigInteger result = new BigInteger("0");
        if (minLeftHand >= minRightHand)
            result = result.add(repeatedNumber(minLeftHand));
        for (int i = minLeftHand + 1; i < maxLeftHand; i++)
            result = result.add(repeatedNumber(i));
        if (maxLeftHand <= maxRightHand && maxLeftHand != minLeftHand)
            result = result.add(repeatedNumber(maxLeftHand));
        return result;
    }

    private static BigInteger repeatedNumber(int leftHand) {
        return new BigInteger(String.format("%d%d", leftHand, leftHand));
    }

    private static String[][] getInputRanges(String inputFileName) throws FileNotFoundException {
        try (Scanner inputReader = new Scanner(new File(inputFileName))) {
            String inputLine = inputReader.nextLine();
            String[] inputRangeStrings = inputLine.split("[,]");

            return Arrays.stream(inputRangeStrings).map(range -> range.split("[-]")).toArray(String[][]::new);
        } catch (Exception e) {
            throw e;
        }
    }
}
