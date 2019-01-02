import java.util.ArrayList;
import java.util.PriorityQueue;

public class Main {

    public static void main(String[] args) {
	// write your code here

    }

    public class Node {
        public Node() {

        }
    }

    public class Solution {
        double cost;
        Node rootNode;

        public Solution() {

        }
    }

    public boolean isSolutionSalts(Solution s) {
        return false;
    }

    public boolean isPromisingSalts(Solution s) {
        return false;
    }

    public boolean isSolutionFiabilitat(Solution s) {
        return false;
    }

    public boolean isPromisingFiabilitat(Solution s) {
        return false;
    }

    public Solution[] branchAndBound() {
        Solution xSalts, xFiabilitat, bestSalts = null, bestFiabilitat = null;
        PriorityQueue<Solution> liveNodesSalts = new PriorityQueue<>();
        PriorityQueue<Solution> liveNodesFiabilitat = new PriorityQueue<>();
        ArrayList<Solution> options = new ArrayList<>();

        //encua(liveNodesSalts, xSalts);
        //encua(liveNodesFiabilitat, xFiabilitat);

        while (liveNodesSalts != null && liveNodesFiabilitat != null) {
            //xSalts = desencua(liveNodesSalts);
            //xFiabilitat = desencua(liveNodesFiabilitat);

            for (Solution option : options) {
                if (isSolutionSalts(option)) {
                    //bestSalts = min(option, bestSalts);
                } else if (isPromisingSalts(option)) {
                    //encua(liveNodesSalts, option);
                } else if (isSolutionFiabilitat(option)) {
                    //bestFiabilitat = max(option, bestFiabilitat);
                } else if (isPromisingFiabilitat(option)) {
                    //encua(liveNodesFiabilitat, option);
                }
            }
        }

        return new Solution[] {bestSalts, bestFiabilitat};
    }
}
