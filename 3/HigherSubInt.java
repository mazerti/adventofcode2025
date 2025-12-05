import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.stream.Stream;

public class HigherSubInt {

    public static void main(String[] args) {
        System.out.print("Part One: ");
        partOne(args[0]);

        System.out.print("Part Two: ");
        partTwo(args[0]);
    }

    private static void partOne(String inputFileName) {
        try (Stream<String> input = Files.lines(FileSystems.getDefault().getPath(inputFileName))) {
            Integer result = input
                    .map(bank -> keep2Highest(bank))
                    .reduce(0, (a, b) -> a + b);
            System.out.println(result);
        } catch (FileNotFoundException e) {
            System.err.println("Please provide a valid input file.");
        } catch (IOException e) {
            System.err.println("Please provide a valid input file.");
        }
    }

    private static int keep2Highest(String bank) {
        int firstByte = 0;
        int secondByte = 0;
        for (int i = 0; i < bank.length() - 1; i++) {
            if (bank.charAt(i) - '0' > firstByte) {
                secondByte = bank.charAt(i + 1) - '0';
                firstByte = bank.charAt(i) - '0';
                continue;
            }
            if (bank.charAt(i) - '0' > secondByte)
                secondByte = bank.charAt(i) - '0';
        }
        if (bank.charAt(bank.length() - 1) - '0' > secondByte)
            secondByte = bank.charAt(bank.length() - 1) - '0';
        return 10 * firstByte + secondByte;
    }

    private static void partTwo(String inputFileName) {
        try (Stream<String> input = Files.lines(FileSystems.getDefault().getPath(inputFileName))) {
            BigInteger result = input
                    .map(bank -> keepKHighest(12, bank))
                    .map(num -> new BigInteger(num))
                    .reduce(BigInteger.ZERO, BigInteger::add);
            System.out.println(result);
        } catch (FileNotFoundException e) {
            System.err.println("Please provide a valid input file.");
        } catch (IOException e) {
            System.err.println("Please provide a valid input file.");
        }
    }

    private static String keepKHighest(int k, String bank) {

        class MaxInBank {
            public int value = 0;
            public int index = -1;

            public MaxInBank(String bank) {
                for (int i = 0; i < bank.length(); i++) {
                    if (bank.charAt(i) - '0' > value) {
                        value = bank.charAt(i) - '0';
                        index = i;
                    }
                }
            }
        }

        if (k == 1)
            return String.valueOf(new MaxInBank(bank).value);

        MaxInBank firstByte = new MaxInBank(bank.substring(0, bank.length() - k + 1));
        return String.valueOf(firstByte.value) + keepKHighest(k - 1, bank.substring(firstByte.index + 1));
    }

}
