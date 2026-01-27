import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.BiFunction;

public class Patterns {

    public static void main(String[] args) {
        int[][] redTiles;
        try {
            redTiles = Files.lines(Paths.get(args[0]))
                    .map(x -> Arrays.stream(x.split("[,]"))
                            .mapToInt(Integer::parseInt)
                            .toArray())
                    .toArray(int[][]::new);
        } catch (IOException e) {
            System.err.println("Please provide a valid input file.");
            return;
        }

        System.out.print("Part One: ");
        partOne(redTiles);
    }

    private static void partOne(int[][] redTiles) {
        long largestArea = 0;
        for (int i = 0; i < redTiles.length; i++) {
            for (int j = i + 1; j < redTiles.length; j++) {
                long area = area(redTiles[i], redTiles[j]);
                if (area > largestArea)
                    largestArea = area;
            }
        }
        System.out.println(largestArea);
    }

    private static long area(int[] tile1, int[] tile2) {
        return ((long) Math.abs(tile1[0] - tile2[0]) + 1) * ((long) Math.abs(tile1[1] - tile2[1]) + 1);
    }
}
