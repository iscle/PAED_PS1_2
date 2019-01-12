import java.util.ArrayList;
import java.util.Arrays;

class Backtracking {

    private final ArrayList<Node> nodes;
    private final Node startServer;
    private final Node endServer;

    public Backtracking(Node[] nodes, Server[] servers, int startServer, int endServer) {
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

    public Solution cost(Solution s, Solution best) {
        if (s != null) {
            s = new Solution(s);
        } else {
            s = new Solution(startServer, false);
        }

        if (isSolution(s)) {
            best = min(s, best);
        } else {
            Node lastNode = s.getLast();

            for (NodeConnection opt:lastNode.getConnectsTo()) {
                Node node = getNodeById(opt.getTo());
                if (s.isNotVisited(node)) {
                    if (isPromising(s, opt, node, best)) {
                        s.addNode(node, opt.getCost());
                        best = cost(s, best);
                        s.removeNode(opt.getCost());
                    }
                }
            }
        }

        return best;
    }

    private boolean isPromising(Solution s, NodeConnection optionConnection, Node option, Solution best) {
        if (best == null) {
            return true;
        }

        if (s.isReliability()) {
            return s.getBound() * option.getReliability() > best.getBound();
        } else {
            return s.getBound() + optionConnection.getCost() < best.getBound();
        }
    }

    private boolean isSolution(Solution option) {
        return option.getLast() == endServer;
    }

    public Solution fiabilitat(Solution s, Solution best) {
        if (s != null) {
            s = new Solution(s);
        } else {
            s = new Solution(startServer, true);
        }

        if (isSolution(s)) {
            best = max(s, best);
        } else {
            for (NodeConnection opt:s.getLast().getConnectsTo()) {
                Node node = getNodeById(opt.getTo());
                if (s.isNotVisited(node)) {
                    if (isPromising(s, opt, node, best)) {
                        s.addNode(node);
                        best = fiabilitat(s, best);
                        s.removeNode();
                    }
                }
            }
        }

        return best;
    }

    private Solution max(Solution o1, Solution o2) {
        if (o1 == null) {
            return o2;
        }

        if (o2 == null) {
            return o1;
        }

        if (o1.getBound() > o2.getBound()) {
            return o1;
        } else {
            return o2;
        }
    }

    private Solution min(Solution o1, Solution o2) {
        if (o1 == null) {
            return o2;
        }

        if (o2 == null) {
            return o1;
        }

        if (o1.getBound() > o2.getBound()) {
            return o2;
        } else {
            return o1;
        }
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
