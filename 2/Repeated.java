import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Repeated {

    public static void main(String[] args) {
        System.out.print("Part One: ");
        partOne(args[0]);

        System.out.print("Part Two: ");
        partTwo(args[0]);
    }

    private static void partOne(String inputFileName) {
        BigInteger result = new BigInteger("0");
        try {
            String[][] ranges = getInputRanges(inputFileName);
            for (String[] range : ranges) {
                result = result.add(retrieveRepeats(range));
            }
            System.out.println(result);
        } catch (FileNotFoundException e) {
            System.err.println("Please provide a valid input file.");
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

    private static void partTwo(String inputFileName) {
        try (Scanner inputReader = new Scanner(new File(inputFileName))) {
            String inputLine = inputReader.nextLine();
            BigInteger result = Arrays.stream(inputLine.split("[,]"))
                    .map(range -> range.split("[-]"))
                    .map(range -> sumRepeatsInRange(range[0], range[1]))
                    .reduce(BigInteger.ZERO, BigInteger::add);

            System.out.println(result);
        } catch (FileNotFoundException e) {
            System.err.println("Please provide a valid input file.");
        }
    }

    private static BigInteger sumRepeatsInRange(String rangeStarts, String rangeEnds) {
        int minNbDigits = rangeStarts.length();
        int maxNbDigits = rangeEnds.length();

        Function<Integer, String[]> trimRangeToNbDigits = nbDigits -> {
            return new String[] { minNbDigits < nbDigits ? "1" + "0".repeat(nbDigits - 1) : rangeStarts,
                    maxNbDigits > nbDigits ? "9".repeat(nbDigits) : rangeEnds };
        };

        return IntStream.rangeClosed(minNbDigits, maxNbDigits)
                .mapToObj(Integer::valueOf)
                .map(trimRangeToNbDigits)
                .map(range -> sumRepeatsInRangeFixedDigits(range[0], range[1]))
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    private static BigInteger sumRepeatsInRangeFixedDigits(String rangeStarts, String rangeEnds) {
        int nbDigits = rangeStarts.length();
        HashSet<String> matchStore = new HashSet<>();
        for (int i = 2; i <= nbDigits; i++) {
            matchStore = listsRepeatsKInRange(i, rangeStarts, rangeEnds, matchStore);
        }
        return matchStore.stream()
                .map(repeats -> new BigInteger(repeats))
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    private static HashSet<String> listsRepeatsKInRange(int k, String rangeStarts, String rangeEnds,
            HashSet<String> matchStore) {
        if (rangeStarts.length() % k != 0)
            return matchStore;

        int kernelSize = rangeStarts.length() / k;

        String minKernel = rangeStarts.substring(0, kernelSize);
        String maxKernel = rangeEnds.substring(0, kernelSize);

        if (compareStrings(rangeStarts, minKernel.repeat(k)) <= 0
                && compareStrings(minKernel.repeat(k), rangeEnds) <= 0) {
            matchStore.add(minKernel.repeat(k));
        }

        for (int kernel = Integer.parseInt(minKernel) + 1; kernel < Integer.parseInt(maxKernel); kernel++) {
            matchStore.add(String.valueOf(kernel).repeat(k));
        }

        if (!(minKernel.equals(maxKernel)) && compareStrings(maxKernel.repeat(k), rangeEnds) <= 0) {
            matchStore.add(maxKernel.repeat(k));
        }

        return matchStore;
    }

    private static int compareStrings(String leftSide, String rightSide) {
        /*
         * Returns a positive integer if the left side is a bigger integer than the
         * right side, a negative integer if the right side is bigger and 0 if both
         * input are equals.
         * Expects all input to be positive integer values.
         * The returned value isn't representative of the difference between the input
         * values. Only the sign is meaningful.
         */
        if (leftSide.equals(rightSide))
            return 0;
        if (leftSide.length() - rightSide.length() != 0)
            return leftSide.length() - rightSide.length();
        if (leftSide.length() == 1)
            return Integer.parseInt(leftSide) - Integer.parseInt(rightSide);
        int headBitDifference = Integer.parseInt(leftSide.substring(0, 1))
                - Integer.parseInt(rightSide.substring(0, 1));
        if (headBitDifference != 0)
            return headBitDifference;
        return compareStrings(leftSide.substring(1), rightSide.substring(1));
    }
}