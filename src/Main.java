import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {

    private static Node[] nodes;
    private static Server[] servers;
    private static User[] users;

    public static void main(String[] args) {
        System.out.println("Reading JSON...");

        if (!readJSON("nodes_plus.json", "servers_plus.json", "users.json")) {
            System.out.println("No s'han pogut llegir tots els fitxers!");
            return;
        }

        System.out.println("\tNodes: " + nodes.length);
        System.out.println("\tServers: " + servers.length);
        System.out.println("\tUsers: " + users.length);

        while (true) {
            switch (getOption()) {
                case 1: // Backtracking
                    Backtracking bt = new Backtracking(nodes, servers, users);

                    System.out.println(bt.fiabilitat(2, new Solution(nodes[0], true), null).getBound());
                    System.out.println(bt.salts(2, new Solution(nodes[0], false), null).getBound());
                    break;
                case 2: // Branch & Bound
                    BranchAndBound bnb = new BranchAndBound(nodes, servers, users);

                    System.out.println(bnb.fiabilitat(1, 2).getBound());
                    System.out.println(bnb.salts(1, 2).getBound());
                    break;
                case 3: // Greedy
                    break;
                case 4: // Greedy + Backtracking
                    break;
                case 5: // Greedy + Branch & Bound
                    break;
                case 6:
                    return;
            }
        }
    }

    private static int getOption() {
        int opcio = 0;
        Scanner s = new Scanner(System.in);

        while (opcio < 1 || opcio > 6) {
            System.out.println("\n1. Backtracking");
            System.out.println("2. Branch & Bound");
            System.out.println("3. Greedy");
            System.out.println("4. Greedy + Backtracking");
            System.out.println("5. Greedy + Branch & Bound");
            System.out.println("6. Sortir");
            System.out.println("\nEscull una opció: ");
            opcio = s.nextInt();
        }

        return opcio;
    }

    private static boolean readJSON(String nodesFile, String serversFile, String usersFile) {
        try {
            nodes = new Gson().fromJson(new BufferedReader(new FileReader(nodesFile)), Node[].class);
            users = new Gson().fromJson(new BufferedReader(new FileReader(usersFile)), User[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        try {
            servers = new Gson().fromJson(new BufferedReader(new FileReader(serversFile)), Server[].class);
        } catch (JsonSyntaxException e) {
            try {
                ServerMinus[] sm = new Gson().fromJson(new BufferedReader(new FileReader(serversFile)), ServerMinus[].class);
                if (sm == null) {
                    System.out.println("caca");
                }
                servers = new Server[sm.length];

                for (int i = 0; i < sm.length; ++i) {
                    ServerMinus s = sm[i];
                    servers[i] = new Server(s.getId(), s.getCountry(), s.getLocation(), new int[]{s.getReachable_from()});
                }
            } catch (FileNotFoundException e2) {
                e.printStackTrace();
                return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
