public class Node {

    private int id;
    private double reliability;
    private NodeConnection[] connectsTo;

    public Node(int id, double reliability, NodeConnection[] connectsTo) {
        this.id = id;
        this.reliability = reliability;
        this.connectsTo = connectsTo;
    }

    public Node(Node n) {
        this.id = n.id;
        this.reliability = n.reliability;
        this.connectsTo = new NodeConnection[n.connectsTo.length];
        for (int i = 0; i < n.connectsTo.length; ++i) {
            this.connectsTo[i] = new NodeConnection(n.connectsTo[i]);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getReliability() {
        return reliability;
    }

    public void setReliability(double reliability) {
        this.reliability = reliability;
    }

    public NodeConnection[] getConnectsTo() {
        return connectsTo;
    }

    public void setConnectsTo(NodeConnection[] connectsTo) {
        this.connectsTo = connectsTo;
    }
}
