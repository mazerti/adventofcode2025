import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class TachyonBeam {
    public static void main(String[] args) {
        int[][] splitterMatrix;
        try {
            splitterMatrix = Files.lines(Paths.get(args[0]))
                    .map(TachyonBeam::parseInputRow)
                    .toArray(int[][]::new);
        } catch (IOException e) {
            System.err.println("Please provide a valid input file.");
            return;
        }

        System.out.print("Part One:");
        partOne(splitterMatrix);

        System.out.print("Part Two:");
        partTwo(splitterMatrix);
    }

    private static int[] parseInputRow(String line) {
        return line.chars()
                .map(c -> c == '.' ? 0 : 1)
                .toArray();

    }

    private static void partOne(int[][] splitterMatrix) {
        long res = countSplits(splitterMatrix, false);

        System.out.println(res);
    }

    private static long countSplits(int[][] splitterMatrix, boolean isQuantum) {
        long res = 0;

        long[] beams = Arrays.stream(splitterMatrix[0])
                .mapToLong(x -> x)
                .toArray();
        for (int i = 1; i < splitterMatrix.length; i++) {
            long[] nextBeams = beams.clone();
            for (int k = 0; k < beams.length; k++) {
                if (beams[k] >= 1 && splitterMatrix[i][k] == 1) {
                    res += isQuantum ? beams[k] : 1;
                    nextBeams[k] = 0;
                    if (k > 0)
                        nextBeams[k - 1] += isQuantum ? beams[k] : 1;
                    if (k < beams.length - 1)
                        nextBeams[k + 1] += isQuantum ? beams[k] : 1;
                }
            }
            if (!isQuantum)
                for (int k = 0; k < beams.length; k++)
                    if (nextBeams[k] >= 1)
                        nextBeams[k] = 1;
            beams = nextBeams;
        }
        return res;
    }

    private static void partTwo(int[][] splitterMatrix) {
        long res = countSplits(splitterMatrix, true) + 1;
        System.out.println(res);
    }
}
