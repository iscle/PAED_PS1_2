public class Backtracking {

    private Node[] nodes;
    private Server[] servers;
    private User[] users;

    public Backtracking(Node[] nodes, Server[] servers, User[] users) {
        this.nodes = nodes;
        this.servers = servers;
        this.users = users;
    }

    public Solution salts(int end, Solution s, Solution best) {
        if (s != null) {
            s = new Solution(s);
        }
        if (isSolution(end, s)) {
            best = min(s, best);
        } else {
            for (NodeConnection opt:s.getLast().getConnectsTo()) {
                Node node = getNodeById(opt.getTo());
                if (!s.isVisited(node)) {
                    if (isPromising(s, opt, node, best)) {
                        if (s.isReliability()) {
                            s.addNode(node);
                            best = salts(end, s, best);
                            s.removeNode();
                        } else {
                            s.addNode(node, opt.getCost());
                            best = salts(end, s, best);
                            s.removeNode(opt.getCost());
                        }
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

    private boolean isSolution(int end, Solution option) {
        return option.getLast().getId() == end;
    }

    public Solution fiabilitat(int start, int end) {
        Solution best = null;

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

    public Node getNodeById(int id) {
        for (Node n:nodes) {
            if (n.getId() == id) {
                return n;
            }
        }

        return null;
    }
}
