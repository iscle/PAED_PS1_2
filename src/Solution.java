public class Solution {
    private double cost;
    private Node rootNode;

    public Solution(Node rootNode) {
        this.cost = -1;
        this.rootNode = rootNode;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }
}