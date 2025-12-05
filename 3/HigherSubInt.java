import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.stream.Stream;

public class HigherSubInt {

    public static void main(String[] args) {
        System.out.print("Part One: ");
        partOne(args[0]);
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

}
