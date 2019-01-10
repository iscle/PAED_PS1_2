import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

class Greedy {

    private final Node[] nodes;
    private final Server[] servers;

    public Greedy(Node[] nodes, Server[] servers) {
        this.nodes = nodes;
        this.servers = servers;
    }

    public Solution fiabilitat(int start, int end) {
        Solution s = new Solution(getNodeById(start), true);
        Node[] sortedNodes = nodes.clone();

        // mirem tots els nodes amb els que connecta lultim node, si es el final, anem directament, sino fem greedy

        Arrays.sort(sortedNodes, (o1, o2) -> Double.compare(o2.getReliability(), o1.getReliability()));

        ArrayList<Node> candidats = new ArrayList<>(Arrays.asList(sortedNodes));

        for (int i = 0; candidats.size() > 0 && i < candidats.size(); ++i) {
            Node c = candidats.get(i);

            if (esFactible(s, c)) {
                s.addNode(c);

                if (isSolution(end, s)) {
                    return s;
                }

                candidats.remove(i);
                i = -1;
            }
        }

        return null;
    }

    public Solution salts(int end, Solution s, Solution best) {


        return best;
    }

    private boolean esFactible(Solution s, Node c) {
        Node lastNode = s.getLast();

        for (NodeConnection nc:c.getConnectsTo()) {
            if (lastNode.getId() == nc.getTo()) {
                return true;
            }
        }

        return false;
    }

    private boolean isSolution(int end, Solution option) {
        return option.getLast().getId() == end;
    }

    private Node getNodeById(int id) {
        for (Node n:nodes) {
            if (n.getId() == id) {
                return n;
            }
        }

        return null;
    }
}
