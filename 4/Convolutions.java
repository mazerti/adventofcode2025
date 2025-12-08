import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

public class Convolutions {

    public static void main(String[] args) {
        try {
            int[][] inputMatrix = getInputMatrix(args[0]);
            System.out.print("Part One :");
            partOne(inputMatrix);

            System.out.print("Part Two :");
            partTwo(inputMatrix);
        } catch (IOException e) {
            System.err.println("Please provide a valid input file.");
            return;
        }
    }

    private static void partOne(int[][] inputMatrix) {
        int sumConvolutedMatrix = computeConvolutionSum(inputMatrix);

        System.out.println(sumConvolutedMatrix);
    }

    private static int[][] getInputMatrix(String inputFileName) throws IOException {
        String[] inputLines = Files.lines(FileSystems.getDefault().getPath(inputFileName)).toArray(String[]::new);
        int[][] inputMatrix = new int[inputLines.length][inputLines[0].length()];
        for (int i = 0; i < inputLines.length; i++) {
            String line = inputLines[i];
            for (int j = 0; j < line.length(); j++)
                inputMatrix[i][j] = line.charAt(j) == '@' ? 1 : 0;
        }
        return inputMatrix;
    }

    private static int computeConvolutionSum(int[][] inputMatrix) {
        int sumConvolutedMatrix = 0;
        for (int i = 0; i < inputMatrix.length; i++)
            for (int j = 0; j < inputMatrix[i].length; j++)
                sumConvolutedMatrix += kernelSumAt(inputMatrix, i, j) < 4 ? inputMatrix[i][j] : 0;
        return sumConvolutedMatrix;
    }

    private static int kernelSumAt(int[][] inputMatrix, int i, int j) {
        boolean topBorder = i <= 0;
        boolean bottomBorder = i >= inputMatrix.length - 1;
        boolean leftBorder = j <= 0;
        boolean rightBorder = j >= inputMatrix[i].length - 1;

        int sum = 0;
        sum += !topBorder && !leftBorder ? inputMatrix[i - 1][j - 1] : 0;
        sum += !topBorder ? inputMatrix[i - 1][j] : 0;
        sum += !topBorder && !rightBorder ? inputMatrix[i - 1][j + 1] : 0;
        sum += !leftBorder ? inputMatrix[i][j - 1] : 0;
        sum += !rightBorder ? inputMatrix[i][j + 1] : 0;
        sum += !bottomBorder && !leftBorder ? inputMatrix[i + 1][j - 1] : 0;
        sum += !bottomBorder ? inputMatrix[i + 1][j] : 0;
        sum += !bottomBorder && !rightBorder ? inputMatrix[i + 1][j + 1] : 0;
        return sum;
    }

    private static void partTwo(int[][] inputMatrix) {
        int totalSum = 0;
        int lastRemoved = -1;

        int[][] runningShelf = inputMatrix;

        while (lastRemoved != 0) {
            int[][] toRemoveRolls = computeConvolution(runningShelf);
            int nbRemoved = matrixSum(toRemoveRolls);
            removeRolls(runningShelf, toRemoveRolls);
            totalSum += nbRemoved;
            lastRemoved = nbRemoved;
            System.out.println(String.format("Removed %d rolls.", nbRemoved));
        }
        System.out.println(totalSum);
    }

    private static int[][] computeConvolution(int[][] inputMatrix) {
        int height = inputMatrix.length;
        int width = inputMatrix[0].length;
        int[][] convolutedMatrix = new int[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                convolutedMatrix[i][j] = kernelSumAt(inputMatrix, i, j) < 4 ? inputMatrix[i][j] : 0;
        return convolutedMatrix;
    }

    private static int matrixSum(int[][] matrix) {
        int sum = 0;
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                sum += matrix[i][j];
        return sum;
    }

    private static void removeRolls(int[][] from, int[][] to) {
        for (int i = 0; i < from.length; i++)
            for (int j = 0; j < from[i].length; j++)
                from[i][j] -= to[i][j];
    }

}
