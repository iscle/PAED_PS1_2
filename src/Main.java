import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Main {

    private static Node[] nodes;
    private static Server[] servers;
    private static User[] users;

    public static void main(String[] args) {
        System.out.println("Reading JSON...");
        readJSON();
        System.out.println("\tNodes: " + nodes.length);
        System.out.println("\tServers: " + servers.length);
        System.out.println("\tUsers: " + users.length);
    }

    private static boolean readJSON() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("nodes.json"));
            nodes = new Gson().fromJson(br, Node[].class);
            br = new BufferedReader(new FileReader("servers.json"));
            servers = new Gson().fromJson(br, Server[].class);
            br = new BufferedReader(new FileReader("users.json"));
            users = new Gson().fromJson(br, User[].class);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    private boolean isSolutionSalts(Solution s) {
        return false;
    }

    private boolean isPromisingSalts(Solution s) {
        return false;
    }

    private boolean isSolutionFiabilitat(Solution s) {
        return false;
    }

    private boolean isPromisingFiabilitat(Solution s) {
        return false;
    }

    private Solution[] branchAndBound() {
        // TODO: change initial nodes from solutions
        Solution xSalts = new Solution(nodes[0]);
        Solution xFiabilitat = new Solution(nodes[0]);
        Solution bestSalts = new Solution(nodes[0]);
        Solution bestFiabilitat = new Solution(nodes[0]);
        PriorityQueue<Solution> liveNodesSalts = new PriorityQueue<>(11, new Comparator<Solution>() {
            @Override
            public int compare(Solution o1, Solution o2) {
                return 0;
            }
        });
        PriorityQueue<Solution> liveNodesFiabilitat = new PriorityQueue<>(11, new Comparator<Solution>() {
            @Override
            public int compare(Solution o1, Solution o2) {
                return 0;
            }
        });
        ArrayList<Solution> options = new ArrayList<>();

        liveNodesSalts.add(xSalts);
        liveNodesFiabilitat.add(xFiabilitat);

        while (liveNodesSalts.size() != 0 && liveNodesFiabilitat.size() != 0) {
            xSalts = liveNodesSalts.poll();
            xFiabilitat = liveNodesFiabilitat.poll();

            for (Solution option : options) {
                if (isSolutionSalts(option)) {
                    //bestSalts = min(option, bestSalts);
                } else if (isPromisingSalts(option)) {
                    liveNodesSalts.add(option);
                } else if (isSolutionFiabilitat(option)) {
                    //bestFiabilitat = max(option, bestFiabilitat);
                } else if (isPromisingFiabilitat(option)) {
                    liveNodesFiabilitat.add(option);
                }
            }
        }

        return new Solution[] {bestSalts, bestFiabilitat};
    }
}
