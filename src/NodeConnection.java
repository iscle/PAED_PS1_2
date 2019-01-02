public class NodeConnection {
    private int to;
    private String name;
    private int cost;

    public NodeConnection(int to, String name, int cost) {
        this.to = to;
        this.name = name;
        this.cost = cost;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
