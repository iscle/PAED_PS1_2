import java.util.ArrayList;

public class Solution {
    private final ArrayList<Node> path;
    private int last;
    private double bound;
    private final boolean isReliability;

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

    public void addNode(Node node, int cost) {
        path.add(node);
        bound += cost;
        ++last;
    }

    public void removeNode() {
        bound /= path.get(last).getReliability();
        path.remove(last);
        --last;
    }

    public void removeNode(double cost) {
        bound -= cost;
        path.remove(last);
        --last;
    }

    public Node getLast() {
        if (last == -1) {
            return null;
        }

        return path.get(last);
    }

    public boolean isReliability() {
        return isReliability;
    }

    public boolean isNotVisited(Node n) {
        return !path.contains(n);
    }
}