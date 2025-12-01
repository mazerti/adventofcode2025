import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Rotation {

    public static void main(String[] args) {
        System.out.print("Part One: ");
        partOne(args);
        System.out.print("Part Two: ");
        partTwo(args);
    }

    private static void partOne(String[] args) {
        int i = 50;
        int result = 0;
        try (Scanner inputReader = new Scanner(new File(args[0]))) {
            while (inputReader.hasNextLine()) {
                String rotation = inputReader.nextLine();
                switch (rotation.charAt(0)) {
                    case 'L':
                        i = (i - Integer.parseInt(rotation.substring(1))) % 100;
                        break;
                    case 'R':
                        i = (i + Integer.parseInt(rotation.substring(1))) % 100;
                        break;
                    default:
                        throw new IOException("Invalid rotation provided");
                }
                if (i == 0) {
                    result += 1;
                }
            }
            System.out.println(String.format("The arrow pointed %d times at 0.", result));
        } catch (FileNotFoundException | java.lang.ArrayIndexOutOfBoundsException e) {
            System.out.println("Please provide a valid input file.");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static void partTwo(String[] args) {
        int i = 50;
        int result = 0;
        try (Scanner inputReader = new Scanner(new File(args[0]))) {
            while (inputReader.hasNextLine()) {
                String rotation = inputReader.nextLine();
                assert Integer.parseInt(rotation.substring(1)) != 0;
                switch (rotation.charAt(0)) {
                    case 'L':
                        if (i == 0)
                            i += 100;
                        i -= Integer.parseInt(rotation.substring(1));
                        if (i <= 0)
                            result += (-i + 1) / 100 + (((-i + 1) % 100 == 0) ? 0 : 1);
                        break;
                    case 'R':
                        i += Integer.parseInt(rotation.substring(1));
                        if (i >= 100)
                            result += i / 100;
                        break;
                    default:
                        throw new IOException("Invalid rotation provided");
                }
                i %= 100;
                if (i<0)
                    i+=100;
            }
            System.out.println(String.format("The arrow pointed %d times at 0.", result));
        } catch (FileNotFoundException | java.lang.ArrayIndexOutOfBoundsException e) {
            System.out.println("Please provide a valid input file.");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
