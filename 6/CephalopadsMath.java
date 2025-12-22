import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CephalopadsMath {
    public static void main(String[] args) {
        ArrayList<char[]> inputMat = new ArrayList<>();
        try (Scanner inputScanner = new Scanner(new File(args[0]))) {
            while (inputScanner.hasNextLine()) {
                String line = inputScanner.nextLine();
                inputMat.add(line.toCharArray());
            }
        } catch (IOException e) {
            System.err.println("Please provide a valid input file.");
            return;
        }

        System.out.print("Part Two: ");
        partTwo(inputMat);
    }

    private static void partTwo(ArrayList<char[]> inputMat) {
        String[] humanReadable = transpose(inputMat)
                .stream()
                .map(line -> String.join("", line))
                .toArray(String[]::new);

        long result = splitOperations(humanReadable)
                .stream()
                .map(CephalopadsMath::compute)
                .mapToLong(a -> a)
                .reduce((long) 0, Long::sum);

        System.out.println(result);
    }

    private static ArrayList<String[]> splitOperations(String[] inputLines) {
        ArrayList<String[]> operations = new ArrayList<>();
        int startOp = 0;
        for (int i = 0; i < inputLines.length; i++) {
            String line = inputLines[i];
            if (line.isBlank()) {
                if (i == startOp) {
                    startOp++;
                    continue;
                }
                operations.add(Arrays.copyOfRange(inputLines, startOp, i));
                startOp = i + 1;
            }
        }
        if (startOp < inputLines.length)
            operations.add(Arrays.copyOfRange(inputLines, startOp, inputLines.length));
        return operations;
    }

    private static long compute(String[] operation) {
        // System.err.print(String.join(", ", operation));
        String operationType = "";
        long[] values = new long[operation.length];
        for (int i = 0; i < operation.length; i++) {
            String val = operation[i];
            if (val.contains("+")) {
                operationType += "+";
                values[i] = Long.parseLong(val.replace("+", "").strip());
                continue;
            }
            if (val.contains("*")) {
                operationType += "*";
                values[i] = Long.parseLong(val.replace("*", "").strip());
                continue;
            }
            values[i] = Long.parseLong(val.strip());
        }
        // System.err.println(" -> " + String.join(" "+ operationType + " ", Arrays.stream(values).mapToObj(a->String.format("%d", a)).toArray(String[]::new)));
        switch (operationType) {
            case "+":
                return Arrays.stream(values).sum();

            case "*":
                return Arrays.stream(values).reduce(1, (a, b) -> a * b);

            default:
                return -1;
        }
    }

    private static ArrayList<ArrayList<String>> transpose(ArrayList<char[]> inputMat) {
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        for (int i = 0; i < inputMat.size(); i++) {
            for (int j = 0; j < inputMat.get(i).length; j++) {
                while (result.size() <= j)
                    result.add(new ArrayList<String>());

                result.get(j).add(Character.toString(inputMat.get(i)[j]));

            }
        }
        return result;
    }
}
