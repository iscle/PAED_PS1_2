import java.util.ArrayList;

public class Solution {
    private ArrayList<Node> path;
    private int last;
    private double bound;
    private boolean isReliability;

    public Solution(Node node, boolean isReliability) {
        this.path = new ArrayList<>();
        this.path.add(node);
        this.last = 0;
        if (isReliability) {
            this.bound = node.getReliability();
        } else {
            this.bound = 0;
        }
        this.isReliability = isReliability;
    }

    public Solution(Solution s) {
        this.path = (ArrayList) s.path.clone();
        this.last = s.last;
        this.bound = s.bound;
        this.isReliability = s.isReliability;
    }

    public ArrayList<Node> getPath() {
        return path;
    }

    public double getBound() {
        return bound;
    }

    public void addNode(Node node) {
        path.add(node);
        bound *= node.getReliability();
        ++last;
    }

    public void addNode(Node node, double cost) {
        path.add(node);
        bound += cost;
        ++last;
    }

    public Node getLast() {
        return path.get(last);
    }

    public boolean isReliability() {
        return isReliability;
    }

    public boolean isVisited(Node n) {
        for (Node node:path) {
            if (n == node) {
                return true;
            }
        }

        return false;
    }
}