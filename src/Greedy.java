import java.util.ArrayList;
import java.util.Arrays;

class Greedy {

    private final ArrayList<Node> nodes;
    private final Node startServer;
    private final Node endServer;

    public Greedy(Node[] nodes, Server[] servers, int startServer, int endServer) {
        this.nodes = new ArrayList<>(Arrays.asList(nodes));

        // Convert server1 to node
        Server server1 = getServerById(servers, startServer);
        NodeConnection[] startNodeConnections = new NodeConnection[server1.getReachable_from().length];

        for (int i = 0; i < startNodeConnections.length; ++i) {
            startNodeConnections[i] = new NodeConnection(server1.getReachable_from()[i], "", 0);
        }

        this.startServer = new Node(-1, 1, startNodeConnections);

        // Convert server2 to node
        Server server2 = getServerById(servers, endServer);
        NodeConnection[] endNodeConnections = new NodeConnection[server2.getReachable_from().length];

        for (int i = 0; i < endNodeConnections.length; ++i) {
            endNodeConnections[i] = new NodeConnection(server2.getReachable_from()[i], "", 0);
            for (int j = 0; j < this.nodes.size(); ++j) {
                Node n = this.nodes.get(j);
                if (server2.getReachable_from()[i] == n.getId()) {
                    NodeConnection[] tmp = n.getConnectsTo();
                    tmp = Arrays.copyOf(tmp, tmp.length + 1);
                    tmp[tmp.length - 1] = new NodeConnection(-2,"", 0);
                    n.setConnectsTo(tmp);
                    this.nodes.set(j, n);
                }
            }
        }

        this.endServer = new Node(-2, 1, endNodeConnections);

        this.nodes.add(this.endServer);
    }

    public Solution fiabilitat() {
        Solution s = new Solution(startServer, true);
        Node[] sortedNodes = nodes.toArray(new Node[nodes.size()]);

        Arrays.sort(sortedNodes, (o1, o2) -> Double.compare(o2.getReliability(), o1.getReliability()));

        ArrayList<Node> candidats = new ArrayList<>(Arrays.asList(sortedNodes));

        for (int i = 0; candidats.size() > 0 && i < candidats.size(); ++i) {
            Node c = candidats.get(i);

            if (esFactible(s, c)) {
                s.addNode(c);

                if (isSolution(s)) {
                    return s;
                }

                candidats.remove(i);
                i = -1;
            }
        }

        return null;
    }

    public Solution cost() {
        Solution s = new Solution(startServer, false);

        ArrayList<NodeConnection> candidats = null;
        Node lastNode = null;

        do {
            if (lastNode != s.getLast()) {
                NodeConnection[] sortedConnections = s.getLast().getConnectsTo();
                Arrays.sort(sortedConnections, (o1, o2) -> Integer.compare(o1.getCost(), o2.getCost()));
                candidats = new ArrayList<>(Arrays.asList(sortedConnections));
            }

            lastNode = s.getLast();

            Node c = getNodeById(candidats.get(0).getTo());

            if (s.isNotVisited(c)) {
                s.addNode(c, candidats.get(0).getCost());
                if (isSolution(s)) {
                    return s;
                }
            } else {
                candidats.remove(0);
            }
        } while (candidats.size() > 0);

        return null;
    }

    private boolean esFactible(Solution s, Node c) {
        Node lastNode = s.getLast();

        for (NodeConnection nc:lastNode.getConnectsTo()) {
            if (c.getId() == nc.getTo()) {
                return true;
            }
        }

        return false;
    }

    private boolean isSolution(Solution option) {
        return option.getLast() == endServer;
    }

    private Node getNodeById(int id) {
        for (Node n:nodes) {
            if (n.getId() == id) {
                return n;
            }
        }

        return null;
    }

    private Server getServerById(Server[] servers, int id) {
        for (Server s:servers) {
            if (s.getId() == id) {
                return s;
            }
        }

        return null;
    }

}
