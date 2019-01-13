import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

class BranchAndBound {

    private final ArrayList<Node> nodes;
    private final Node startServer;
    private final Node endServer;

    public BranchAndBound(Node[] nodes, Server[] servers, int startServer, int endServer) {
        Node[] nclone = new Node[nodes.length];
        for (int i = 0; i < nodes.length; ++i) {
            nclone[i] = new Node(nodes[i]);
        }
        this.nodes = new ArrayList<>(Arrays.asList(nclone));

        // Convert server1 to node
        Server server1 = new Server(getServerById(servers, startServer));
        NodeConnection[] startNodeConnections = new NodeConnection[server1.getReachable_from().length];

        for (int i = 0; i < startNodeConnections.length; ++i) {
            startNodeConnections[i] = new NodeConnection(server1.getReachable_from()[i], "", 0);
        }

        this.startServer = new Node(-1, 1, startNodeConnections);

        // Convert server2 to node
        Server server2 = new Server(getServerById(servers, endServer));
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

    public Solution cost(Solution best) {
        PriorityQueue<Solution> liveNodes = new PriorityQueue<>(11, (o1, o2) -> Double.compare(o1.getBound(), o2.getBound()));
        ArrayList<Solution> options;

        Solution x = new Solution(startServer, false);

        liveNodes.add(x);

        while (liveNodes.size() > 0) {
            x = liveNodes.poll();
            options = expand(x);

            for (Solution option:options) {
                if (isSolution(option)) {
                    best = min(option, best);
                } else if (isPromising(option, best)) {
                    liveNodes.add(option);
                }
            }
        }

        return best;
    }

    private boolean isSolution(Solution option) {
        return option.getLast() == endServer;
    }

    public Solution fiabilitat(Solution best) {
        PriorityQueue<Solution> liveNodes = new PriorityQueue<>(11, (o1, o2) -> Double.compare(o2.getBound(), o1.getBound()));
        ArrayList<Solution> options;

        Solution x = new Solution(startServer, true);

        liveNodes.add(x);

        while (liveNodes.size() > 0) {
            x = liveNodes.poll();
            options = expand(x);

            for (Solution option:options) {
                if (isSolution(option)) {
                    best = max(option, best);
                } else if (isPromising(option, best)) {
                    liveNodes.add(option);
                }
            }
        }

        return best;
    }

    private ArrayList<Solution> expand(Solution x) {
        ArrayList<Solution> solutions = new ArrayList<>();
        Solution s;

        for (NodeConnection nc:x.getLast().getConnectsTo()) {
            Node node = getNodeById(nc.getTo());
            if (x.isNotVisited(node)) {
                s = new Solution(x);
                if (s.isReliability()) {
                    s.addNode(node);
                } else {
                    s.addNode(node, nc.getCost());
                }
                solutions.add(s);
            }
        }

        return solutions;
    }

    private boolean isPromising(Solution option, Solution best) {
        if (best == null) {
            return true;
        }

        if (option.isReliability()) {
            return option.getBound() > best.getBound();
        } else {
            return option.getBound() < best.getBound();
        }


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
