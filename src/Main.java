import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Main {

    private static Node[] nodes;
    private static Server[] servers;
    private static ServerPlus[] serversPlus;
    private static User[] users;

    public static void main(String[] args) {
        System.out.println("Reading JSON...");

        if (!readJSON()) {
            System.out.println("No s'han pogut llegir tots els fitxers!");
            return;
        }

        System.out.println("\tNodes: " + nodes.length);
        System.out.println("\tServers: " + servers.length);
        System.out.println("\tUsers: " + users.length);

        //System.out.println(branchAndBoundFiabilitat(1, 10).getBound());
        System.out.println(branchAndBoundSalts(1, 10).getBound());
    }

    private static Solution branchAndBoundSalts(int start, int end) {
        Solution x;
        Solution best = null;
        PriorityQueue<Solution> liveNodes = new PriorityQueue<>(11, new Comparator<Solution>() {
            @Override
            public int compare(Solution o1, Solution o2) {
                return Double.compare(o1.getBound(), o2.getBound());
            }
        });
        ArrayList<Solution> options;

        x = new Solution(getNodeById(start), false);
        liveNodes.add(x);

        while (liveNodes.size() > 0) {
            x = liveNodes.poll();
            options = expand(x);

            for (Solution s:options) {
                System.out.println("\nNew solution: ");
                System.out.println("\tCost: " + s.getBound());
                for (Node n:s.getPath()) {
                    System.out.println("\t" + n.getId());
                }
            }

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

    private static Solution branchAndBoundFiabilitat(int start, int end) {
        Solution x;
        Solution best = null;
        PriorityQueue<Solution> liveNodes = new PriorityQueue<>(11, new Comparator<Solution>() {
            @Override
            public int compare(Solution o1, Solution o2) {
                return Double.compare(o2.getBound(), o1.getBound());
            }
        });
        ArrayList<Solution> options;

        x = new Solution(getNodeById(start), true);
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

    private static boolean readJSON() {
        try {
            //nodes = new Gson().fromJson(new BufferedReader(new FileReader("nodes.json")), Node[].class);
            nodes = new Gson().fromJson(new BufferedReader(new FileReader("nodes_plus.json")), Node[].class);
            servers = new Gson().fromJson(new BufferedReader(new FileReader("servers.json")), Server[].class);
            serversPlus = new Gson().fromJson(new BufferedReader(new FileReader("servers_plus.json")), ServerPlus[].class);
            users = new Gson().fromJson(new BufferedReader(new FileReader("users.json")), User[].class);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Node getNodeById(int id) {
        for (Node n:nodes) {
            if (n.getId() == id) {
                return n;
            }
        }

        return null;
    }

    private static ArrayList<Solution> expand(Solution x) {
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

    private static boolean isSolution(int end, Solution option) {
        return option.getLast().getId() == end;
    }

    private static boolean isPromising(Solution option, Solution best) {
        if (best == null) {
            return true;
        }

        if (option.isReliability()) {
            return option.getBound() > best.getBound();
        } else {
            return option.getBound() < best.getBound();
        }


    }

    private static Solution max(Solution o1, Solution o2) {
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

    private static Solution min(Solution o1, Solution o2) {
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
}
