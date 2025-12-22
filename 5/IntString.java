import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeSet;

public class IntString implements Comparable<IntString> {
    public String value;

    public IntString(String input) {
        this.value = input;
    }

    public boolean isInRange(IntString rangeStarts, IntString rangeEnds) {
        return rangeStarts.compareTo(this) <= 0 && this.compareTo(rangeEnds) <= 0;
    }

    private static int compareStrings(String leftSide, String rightSide) {
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

    public BigInteger subtract(IntString other) {
        return new BigInteger(this.value).subtract(new BigInteger(other.value));
    }

    @Override
    public int compareTo(IntString other) {
        return compareStrings(this.value, other.value);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof IntString))
            throw new ClassCastException();
        String otherValue = ((IntString) other).value;
        return this.value.equals(otherValue);
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static void main(String[] args) {
        ArrayList<IntString[]> freshRanges = new ArrayList<>();
        ArrayList<IntString> availableIds = new ArrayList<>();
        try (Scanner inputReader = new Scanner(new File(args[0]))) {
            while (inputReader.hasNextLine()) {
                String line = inputReader.nextLine();
                if (line.equals(""))
                    break;
                String[] stringRange = line.split("[-]");
                IntString[] intStringRange = { new IntString(stringRange[0]), new IntString(stringRange[1]) };
                freshRanges.add(intStringRange);
            }
            while (inputReader.hasNextLine()) {
                availableIds.add(new IntString(inputReader.nextLine()));
            }
        } catch (IOException e) {
            System.err.println("Please provide a valid input file.");
            return;
        }

        System.out.print("Part One: ");
        partOne(freshRanges, availableIds);

        System.out.print("Part Two: ");
        partTwo(freshRanges);
    }

    private static void partOne(ArrayList<IntString[]> freshRanges, ArrayList<IntString> availableIds) {
        int result = 0;
        for (IntString id : availableIds)
            for (IntString[] range : freshRanges)
                if (id.isInRange(range[0], range[1])) {
                    result++;
                    break;
                }
        System.out.println(result);
    }

    private static void partTwo(ArrayList<IntString[]> freshRanges) {
        TreeSet<Range> sortedRanges = new TreeSet<>();
        for (IntString[] range : freshRanges)
            sortedRanges.add(new Range(range[0], range[1]));

        ArrayList<Range> mergedRanges = getMergedRanges(sortedRanges);

        BigInteger result = BigInteger.ZERO;
        for (Range range : mergedRanges) {
            result = result.add(range.size());
            System.out.println(range);
        }
        System.out.println(result);
    }

    private static ArrayList<Range> getMergedRanges(TreeSet<Range> sortedRanges) {
        ArrayList<Range> finalRanges = new ArrayList<>();
        for (Range range : sortedRanges) {
            if (finalRanges.isEmpty()) {
                finalRanges.add(range);
                continue;
            }

            boolean hasMerged = false;
            for (Range target : finalRanges) {
                if (target.unite(range) != null)
                    continue;

                hasMerged = true;
                break;
            }
            if (hasMerged)
                continue;

            finalRanges.add(range);
        }
        return finalRanges;
    }

}
