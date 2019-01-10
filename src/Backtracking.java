class Backtracking {

    private final Node[] nodes;
    private final Server[] servers;

    public Backtracking(Node[] nodes, Server[] servers) {
        this.nodes = nodes;
        this.servers = servers;
    }

    public Solution cost(int startServer, int endServer, Solution s, Solution best, int[] end) {
        if (s != null) {
            s = new Solution(s);
        } else {
            Server server = getServerById(startServer);
            NodeConnection[] startNodeConnections = new NodeConnection[server.getReachable_from().length];

            for (int i = 0; i < startNodeConnections.length; ++i) {
                startNodeConnections[i] = new NodeConnection(server.getReachable_from()[i], "", 0);
            }

            s = new Solution(new Node(-1, 1, startNodeConnections), false);
        }

        end = getEndNodes(endServer, end);

        if (isSolution(end, s)) {
            best = min(s, best);
        } else {
            Node lastNode = s.getLast();

            for (NodeConnection opt:lastNode.getConnectsTo()) {
                Node node = getNodeById(opt.getTo());
                if (s.isNotVisited(node)) {
                    if (isPromising(s, opt, node, best)) {
                        s.addNode(node, opt.getCost());
                        best = cost(startServer, endServer, s, best, end);
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

    private boolean isSolution(int end[], Solution option) {
        for (int i:end) {
            if (i == option.getLast().getId()) {
                return true;
            }
        }

        return false;
    }

    public Solution fiabilitat(int startServer, int endServer, Solution s, Solution best, int[] end) {
        if (s != null) {
            s = new Solution(s);
        } else {
            Server server = getServerById(startServer);
            NodeConnection[] startNodeConnections = new NodeConnection[server.getReachable_from().length];

            for (int i = 0; i < startNodeConnections.length; ++i) {
                startNodeConnections[i] = new NodeConnection(server.getReachable_from()[i], "", 0);
            }

            s = new Solution(new Node(-1, 1, startNodeConnections), true);
        }

        end = getEndNodes(endServer, end);

        if (isSolution(end, s)) {
            best = max(s, best);
        } else {
            for (NodeConnection opt:s.getLast().getConnectsTo()) {
                Node node = getNodeById(opt.getTo());
                if (s.isNotVisited(node)) {
                    if (isPromising(s, opt, node, best)) {
                        s.addNode(node);
                        best = fiabilitat(startServer, endServer, s, best, end);
                        s.removeNode();
                    }
                }
            }
        }

        return best;
    }

    private int[] getEndNodes(int endServer, int[] end) {
        if (end == null) {
            Server server = getServerById(endServer);
            end = new int[server.getReachable_from().length];

            for (int i = 0; i < server.getReachable_from().length; ++i) {
                end[i] = server.getReachable_from()[i];
            }
        }
        return end;
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
