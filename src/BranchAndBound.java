import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class BranchAndBound {

    private Node[] nodes;
    private Server[] servers;
    private User[] users;

    public BranchAndBound(Node[] nodes, Server[] servers, User[] users) {
        this.nodes = nodes;
        this.servers = servers;
        this.users = users;
    }

    public Solution salts(int startServer, int endServer, Solution best) {
        PriorityQueue<Solution> liveNodes = new PriorityQueue<>(11, new Comparator<Solution>() {
            @Override
            public int compare(Solution o1, Solution o2) {
                return Double.compare(o1.getBound(), o2.getBound());
            }
        });
        ArrayList<Solution> options;

        Server server = getServerById(startServer);
        NodeConnection[] startNodeConnections = new NodeConnection[server.getReachable_from().length];

        for (int i = 0; i < startNodeConnections.length; ++i) {
            startNodeConnections[i] = new NodeConnection(server.getReachable_from()[i], "", 0);
        }

        Solution x = new Solution(new Node(-1, 1, startNodeConnections), false);
        int[] end = getEndNodes(endServer);

        liveNodes.add(x);

        while (liveNodes.size() > 0) {
            x = liveNodes.poll();
            options = expand(x);

            for (Solution option:options) {
                if (isSolution(end, option)) {
                    best = min(option, best);
                } else if (isPromising(option, best)) {
                    liveNodes.add(option);
                }
            }
        }

        return best;
    }

    private int[] getEndNodes(int endServer) {
        Server server = getServerById(endServer);
        int[] end = new int[server.getReachable_from().length];

        for (int i = 0; i < server.getReachable_from().length; ++i) {
            end[i] = server.getReachable_from()[i];
        }

        return end;
    }

    private boolean isSolution(int end[], Solution option) {
        for (int i:end) {
            if (i == option.getLast().getId()) {
                return true;
            }
        }

        return false;
    }

    public Solution fiabilitat(int startServer, int endServer, Solution best) {
        PriorityQueue<Solution> liveNodes = new PriorityQueue<>(11, new Comparator<Solution>() {
            @Override
            public int compare(Solution o1, Solution o2) {
                return Double.compare(o2.getBound(), o1.getBound());
            }
        });
        ArrayList<Solution> options;

        Server server = getServerById(startServer);
        NodeConnection[] startNodeConnections = new NodeConnection[server.getReachable_from().length];

        for (int i = 0; i < startNodeConnections.length; ++i) {
            startNodeConnections[i] = new NodeConnection(server.getReachable_from()[i], "", 0);
        }

        Solution x = new Solution(new Node(-1, 1, startNodeConnections), true);
        int[] end = getEndNodes(endServer);

        liveNodes.add(x);

        while (liveNodes.size() > 0) {
            x = liveNodes.poll();
            options = expand(x);

            for (Solution option:options) {
                if (isSolution(end, option)) {
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
            if (!x.isVisited(node)) {
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

    private boolean isSolution(int end, Solution option) {
        return option.getLast().getId() == end;
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

    private Server getServerById(int id) {
        for (Server s:servers) {
            if (s.getId() == id) {
                return s;
            }
        }

        return null;
    }

}
