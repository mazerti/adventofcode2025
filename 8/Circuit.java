import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Circuit {

    public static void main(String[] args) {

        double[][] junctionBoxes;
        try {
            junctionBoxes = Files.lines(Paths.get(args[0]))
                    .map(Circuit::parseInputRow)
                    .toArray(double[][]::new);
        } catch (IOException e) {
            System.err.println("Please provide a valid input file.");
            return;
        }

        System.out.print("Part One: ");
        partOne(junctionBoxes, Integer.parseInt(args[1]));

    }

    private static double[] parseInputRow(String inputRow) {
        return Arrays.stream(inputRow.split("[,]"))
                .mapToDouble(Double::parseDouble)
                .toArray();
    }

    private static void partOne(double[][] junctionBoxes, int nbConnexions) {
        int[][] connexions = createConnexions(junctionBoxes, nbConnexions);

        long res = Arrays.stream(circuitsSizes(connexions))
                .mapToObj(x -> x)
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .mapToLong(x -> x)
                .reduce(1, (a, b) -> a * b);

        System.out.println(res);
    }

    private static int[][] createConnexions(double[][] junctionBoxes, int nbConnexions) {
        HashMap<Integer, ArrayList<Integer>> existingConnexions = new HashMap<>();
        int[][] connexions = new int[nbConnexions][2];
        for (int k = 0; k < nbConnexions; k++) {
            double minDistance = Double.POSITIVE_INFINITY;
            int[] closestBoxes = { -1, -1 };
            for (int i = 0; i < junctionBoxes.length; i++) {
                double[] junctionBoxe1 = junctionBoxes[i];
                for (int j = i + 1; j < junctionBoxes.length; j++) {
                    if (existingConnexions.get(i) != null
                            && existingConnexions.get(i).contains(j))
                        continue;
                    double[] junctionBoxe2 = junctionBoxes[j];
                    double distance = euclideanDistance(junctionBoxe1, junctionBoxe2);
                    if (distance < minDistance) {
                        minDistance = distance;
                        closestBoxes[0] = i;
                        closestBoxes[1] = j;
                    }
                }
            }
            if (!existingConnexions.containsKey(closestBoxes[0]))
                existingConnexions.put(closestBoxes[0], new ArrayList<>());
            existingConnexions.get(closestBoxes[0])
                    .add(closestBoxes[1]);
            connexions[k] = closestBoxes.clone();
        }
        return connexions;
    }

    private static double euclideanDistance(double[] pointA, double[] pointB) {
        assert pointA.length == 3 && pointB.length == 3;
        return Math.pow(Math.abs(pointA[0] - pointB[0]), 2)
                + Math.pow(Math.abs(pointA[1] - pointB[1]), 2)
                + Math.pow(Math.abs(pointA[2] - pointB[2]), 2);
    }

    private static int[] circuitsSizes(int[][] connexions) {
        ArrayList<Integer> circuits = new ArrayList<>(); // List the size of each circuit.

        HashMap<Integer, Integer> nodeToCircuit = new HashMap<>(); // Maps each node to the circuit it belongs to.
        HashMap<Integer, ArrayList<Integer>> CircuitToNodes = new HashMap<>(); // Maps each circuits to the lists of
                                                                               // nodes it contains.
        for (int[] connexion : connexions) {
            int nodeA = connexion[0];
            int nodeB = connexion[1];

            if (nodeToCircuit.get(nodeA) == null) {
                if (nodeToCircuit.get(nodeB) == null) {
                    int newCircuit = addNewCircuit(circuits, CircuitToNodes);
                    addToCircuit(circuits, nodeToCircuit, CircuitToNodes, nodeA, newCircuit);
                    addToCircuit(circuits, nodeToCircuit, CircuitToNodes, nodeA, newCircuit);
                } else {
                    int circuit = nodeToCircuit.get(nodeB);
                    addToCircuit(circuits, nodeToCircuit, CircuitToNodes, nodeA, circuit);
                }
            } else {
                if (nodeToCircuit.get(nodeB) == null) {
                    int circuit = nodeToCircuit.get(nodeA);
                    addToCircuit(circuits, nodeToCircuit, CircuitToNodes, nodeB, circuit);
                } else {
                    int circuitA = nodeToCircuit.get(nodeA);
                    int circuitB = nodeToCircuit.get(nodeB);
                    if (circuitA == circuitB)
                        continue;
                    mergeCircuits(circuits, nodeToCircuit, CircuitToNodes, circuitA, circuitB);
                }
            }
        }
        return circuits.stream()
                .mapToInt(x -> x)
                .filter(x -> x > 0)
                .toArray();
    }

    private static int addNewCircuit(ArrayList<Integer> circuits,
            HashMap<Integer, ArrayList<Integer>> CircuitToNodes) {
        int newCircuit = circuits.size();
        circuits.add(0);
        CircuitToNodes.put(newCircuit, new ArrayList<Integer>());
        return newCircuit;
    }

    private static void addToCircuit(ArrayList<Integer> circuits,
            HashMap<Integer, Integer> nodeToCircuit,
            HashMap<Integer, ArrayList<Integer>> CircuitToNodes,
            int node, int circuit) {
        circuits.set(circuit, circuits.get(circuit) + 1);
        nodeToCircuit.put(node, circuit);
        CircuitToNodes.get(circuit).add(node);
    }

    private static void mergeCircuits(ArrayList<Integer> circuits,
            HashMap<Integer, Integer> nodeToCircuit,
            HashMap<Integer, ArrayList<Integer>> CircuitToNodes, int circuitA, int circuitB) {
        int mergeFrom = circuits.get(circuitA) < circuits.get(circuitB) ? circuitA : circuitB;
        int mergeInto = circuits.get(circuitA) < circuits.get(circuitB) ? circuitB : circuitA;
        circuits.set(mergeInto, circuits.get(mergeInto) + circuits.get(mergeFrom));
        circuits.set(mergeFrom, -1);
        for (int node : CircuitToNodes.get(mergeFrom)) {
            nodeToCircuit.put(node, mergeInto);
        }
        CircuitToNodes.get(mergeInto).addAll(CircuitToNodes.get(mergeFrom));
    }

}
