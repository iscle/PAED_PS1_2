import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.util.*;

class Main {

    private static Node[] nodes;
    private static Server[] servers;
    private static User[] users;
    private static RepartirUsuaris repartirUsuaris;
    private static int[] serverIds;

    public static void main(String[] args) {
        System.out.println("Reading JSON...");

        if (!readJSON("nodes_plus.json", "servers_plus.json", "users.json")) {
            System.out.println("No s'han pogut llegir tots els fitxers!");
            return;
        }

        System.out.println("\tNodes: " + nodes.length);
        System.out.println("\tServers: " + servers.length);
        System.out.println("\tUsers: " + users.length);

        repartirUsuaris = new RepartirUsuaris(servers, users);
        serverIds = new int[servers.length];
        for (int i = 0; i < servers.length; i++) {
            serverIds[i] = servers[i].getId();
        }

        while (true) {
            switch (getOption()) {
                case 1: // Backtracking
                    bt();
                    break;
                case 2: // Branch & Bound
                    bnb();
                    break;
                case 3: // Greedy
                    g();
                    break;
                case 4: // Greedy + Backtracking
                    gbt();
                    break;
                case 5: // Greedy + Branch & Bound
                    gbnb();
                    break;
                case 6:
                    return;
            }
        }
    }

    private static void bt() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("usuaris.txt"));

            System.out.println("\nRepartint usuaris...");

            long startTime = System.currentTimeMillis();
            UserSolution us = repartirUsuaris.backtracking(null, null, null, true);

            bw.write("Equity: " + us.getEquity() + ", Distance: " + us.getDistTotal() + "\n");
            for (Server s:us.getServers()) {
                bw.write("Server " + s.getId() + ":\n");
                for (User u:s.getUsers()) {
                    bw.write("\t" + u.getUsername() + "\n");
                }
            }

            System.out.println("S'han repartit els usuaris en " + (System.currentTimeMillis() - startTime) + "ms");
            bw.close();

            bw = new BufferedWriter(new FileWriter("camins.txt"));

            System.out.println("\nCalculant camins...");

            startTime = System.currentTimeMillis();

            for (int i = 0; i < servers.length; ++i) {
                for (int j = i + 1; j < servers.length; ++j) {
                    Backtracking bt = new Backtracking(nodes, servers, serverIds[i], serverIds[j]);
                    bw.write("Cami " + serverIds[i] + " <-> " + serverIds[j] + ":\n");

                    Solution s = bt.fiabilitat(null, null);
                    bw.write("\tFiabilitat: " + s.getBound() + "\n");
                    bw.write("\tPath: ");
                    for (int k = 1; k < s.getPath().size() - 1; ++k) {
                        if (k == s.getPath().size() - 2) {
                            bw.write(s.getPath().get(k).getId() + "\n");
                        } else {
                            bw.write(s.getPath().get(k).getId() + " -> ");
                        }
                    }

                    Solution s2 = bt.cost(null, null);
                    bw.write("\tCost: " + s2.getBound() + "\n");
                    bw.write("\tPath: ");
                    for (int k = 1; k < s2.getPath().size() - 1; ++k) {
                        if (k == s2.getPath().size() - 2) {
                            bw.write(s2.getPath().get(k).getId() + "\n");
                        } else {
                            bw.write(s2.getPath().get(k).getId() + " -> ");
                        }
                    }
                }
            }

            System.out.println("S'han calculat els camins en " + (System.currentTimeMillis() - startTime) + "ms");
            bw.close();

        } catch (IOException e) {
            System.out.println("No s'ha pogut crear un dels fitxers!");
        }
    }

    private static void bnb() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("usuaris.txt"));

            System.out.println("\nRepartint usuaris...");

            long startTime = System.currentTimeMillis();
            UserSolution us = repartirUsuaris.branchAndBound(-1);

            bw.write("Equity: " + us.getEquity() + ", Distance: " + us.getDistTotal() + "\n");
            for (Server s:us.getServers()) {
                bw.write("Server " + s.getId() + ":\n");
                for (User u:s.getUsers()) {
                    bw.write("\t" + u.getUsername() + "\n");
                }
            }

            System.out.println("S'han repartit els usuaris en " + (System.currentTimeMillis() - startTime) + "ms");
            bw.close();

            bw = new BufferedWriter(new FileWriter("camins.txt"));

            System.out.println("\nCalculant camins...");

            startTime = System.currentTimeMillis();
            for (int i = 0; i < servers.length; ++i) {
                for (int j = i + 1; j < servers.length; ++j) {
                    BranchAndBound bnb = new BranchAndBound(nodes, servers, serverIds[i], serverIds[j]);

                    Solution s = bnb.fiabilitat(null);
                    Solution s2 = bnb.cost(null);

                    bw.write("Cami " + serverIds[i] + " <-> " + serverIds[j] + ":\n");
                    bw.write("\tFiabilitat: " + s.getBound() + "\n");
                    bw.write("\tPath: ");
                    for (int k = 1; k < s.getPath().size() - 1; ++k) {
                        if (k == s.getPath().size() - 2) {
                            bw.write(s.getPath().get(k).getId() + "\n");
                        } else {
                            bw.write(s.getPath().get(k).getId() + " -> ");
                        }
                    }
                    bw.write("\tCost: " + s2.getBound() + "\n");
                    bw.write("\tPath: ");
                    for (int k = 1; k < s2.getPath().size() - 1; ++k) {
                        if (k == s2.getPath().size() - 2) {
                            bw.write(s2.getPath().get(k).getId() + "\n");
                        } else {
                            bw.write(s2.getPath().get(k).getId() + " -> ");
                        }
                    }
                }
            }

            System.out.println("S'han calculat els camins en " + (System.currentTimeMillis() - startTime) + "ms");
            bw.close();

        } catch (IOException e) {
            System.out.println("No s'ha pogut crear un dels fitxers!");
        }
    }

    private static void g() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("usuaris.txt"));

            System.out.println("\nRepartint usuaris...");

            long startTime = System.currentTimeMillis();
            UserSolution us = repartirUsuaris.greedy();

            bw.write("Equity: " + us.getEquity() + ", Distance: " + us.getDistTotal() + "\n");
            for (Server s:us.getServers()) {
                bw.write("Server " + s.getId() + ":\n");
                for (User u:s.getUsers()) {
                    bw.write("\t" + u.getUsername() + "\n");
                }
            }

            System.out.println("S'han repartit els usuaris en " + (System.currentTimeMillis() - startTime) + "ms");
            bw.close();

            bw = new BufferedWriter(new FileWriter("camins.txt"));

            System.out.println("\nCalculant camins...");

            startTime = System.currentTimeMillis();
            for (int i = 0; i < servers.length; ++i) {
                for (int j = i + 1; j < servers.length; ++j) {
                    Greedy g = new Greedy(nodes, servers, serverIds[i], serverIds[j]);

                    Solution s = g.fiabilitat();
                    Solution s2 = g.cost();

                    bw.write("Cami " + serverIds[i] + " <-> " + serverIds[j] + ":\n");

                    if (s != null) {
                        bw.write("\tFiabilitat: " + s.getBound() + "\n");
                        bw.write("\tPath: ");
                        for (int k = 1; k < s.getPath().size() - 1; ++k) {
                            if (k == s.getPath().size() - 2) {
                                bw.write(s.getPath().get(k).getId() + "\n");
                            } else {
                                bw.write(s.getPath().get(k).getId() + " -> ");
                            }
                        }
                    } else {
                        bw.write("\tFiabilitat: Not found\n");
                    }

                    if (s2 != null) {
                        bw.write("\tCost: " + s2.getBound() + "\n");
                        bw.write("\tPath: ");
                        for (int k = 1; k < s2.getPath().size() - 1; ++k) {
                            if (k == s2.getPath().size() - 2) {
                                bw.write(s2.getPath().get(k).getId() + "\n");
                            } else {
                                bw.write(s2.getPath().get(k).getId() + " -> ");
                            }
                        }
                    } else {
                        bw.write("\tCost: Not found\n");
                    }
                }
            }

            System.out.println("S'han calculat els camins en " + (System.currentTimeMillis() - startTime) + "ms");
            bw.close();

        } catch (IOException e) {
            System.out.println("No s'ha pogut crear un dels fitxers!");
        }
    }

    private static void gbt() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("usuaris.txt"));

            System.out.println("\nRepartint usuaris...");

            long startTime = System.currentTimeMillis();
            UserSolution us = repartirUsuaris.greedy();
            us = repartirUsuaris.backtracking(null, null, new double[] {us.getEquity()}, true);

            bw.write("Equity: " + us.getEquity() + ", Distance: " + us.getDistTotal() + "\n");
            for (Server s:us.getServers()) {
                bw.write("Server " + s.getId() + ":\n");
                for (User u:s.getUsers()) {
                    bw.write("\t" + u.getUsername() + "\n");
                }
            }

            System.out.println("S'han repartit els usuaris en " + (System.currentTimeMillis() - startTime) + "ms");
            bw.close();

            bw = new BufferedWriter(new FileWriter("camins.txt"));

            System.out.println("\nCalculant camins...");

            startTime = System.currentTimeMillis();
            for (int i = 0; i < servers.length; ++i) {
                for (int j = i + 1; j < servers.length; ++j) {
                    Greedy g = new Greedy(nodes, servers, serverIds[i], serverIds[j]);

                    Solution s = g.fiabilitat();
                    Solution s2 = g.cost();

                    Backtracking bt = new Backtracking(nodes, servers, serverIds[i], serverIds[j]);
                    s = bt.fiabilitat(null, s);
                    s2 = bt.cost(null, s2);

                    bw.write("Cami " + serverIds[i] + " <-> " + serverIds[j] + ":\n");
                    bw.write("\tFiabilitat: " + s.getBound() + "\n");
                    bw.write("\tPath: ");
                    for (int k = 1; k < s.getPath().size() - 1; ++k) {
                        if (k == s.getPath().size() - 2) {
                            bw.write(s.getPath().get(k).getId() + "\n");
                        } else {
                            bw.write(s.getPath().get(k).getId() + " -> ");
                        }
                    }
                    bw.write("\tCost: " + s2.getBound() + "\n");
                    bw.write("\tPath: ");
                    for (int k = 1; k < s2.getPath().size() - 1; ++k) {
                        if (k == s2.getPath().size() - 2) {
                            bw.write(s2.getPath().get(k).getId() + "\n");
                        } else {
                            bw.write(s2.getPath().get(k).getId() + " -> ");
                        }
                    }
                }
            }

            System.out.println("S'han calculat els camins en " + (System.currentTimeMillis() - startTime) + "ms");
            bw.close();

        } catch (IOException e) {
            System.out.println("No s'ha pogut crear un dels fitxers!");
        }
    }

    private static void gbnb() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("usuaris.txt"));

            System.out.println("\nRepartint usuaris...");

            long startTime = System.currentTimeMillis();
            UserSolution us = repartirUsuaris.greedy();

            bw.write("Equity: " + us.getEquity() + ", Distance: " + us.getDistTotal() + "\n");
            for (Server s:us.getServers()) {
                bw.write("Server " + s.getId() + ":\n");
                for (User u:s.getUsers()) {
                    bw.write("\t" + u.getUsername() + "\n");
                }
            }

            System.out.println("S'han repartit els usuaris en " + (System.currentTimeMillis() - startTime) + "ms");
            bw.close();

            bw = new BufferedWriter(new FileWriter("camins.txt"));

            System.out.println("\nCalculant camins...");

            startTime = System.currentTimeMillis();
            for (int i = 0; i < servers.length; ++i) {
                for (int j = i + 1; j < servers.length; ++j) {
                    Greedy g = new Greedy(nodes, servers, serverIds[i], serverIds[j]);

                    Solution s = g.fiabilitat();
                    Solution s2 = g.cost();

                    BranchAndBound bnb = new BranchAndBound(nodes, servers, serverIds[i], serverIds[j]);
                    s = bnb.fiabilitat(s);
                    s2 = bnb.cost(s2);

                    bw.write("Cami " + serverIds[i] + " <-> " + serverIds[j] + ":\n");
                    bw.write("\tFiabilitat: " + s.getBound() + "\n");
                    bw.write("\tPath: ");
                    for (int k = 1; k < s.getPath().size() - 1; ++k) {
                        if (k == s.getPath().size() - 2) {
                            bw.write(s.getPath().get(k).getId() + "\n");
                        } else {
                            bw.write(s.getPath().get(k).getId() + " -> ");
                        }
                    }
                    bw.write("\tCost: " + s2.getBound() + "\n");
                    bw.write("\tPath: ");
                    for (int k = 1; k < s2.getPath().size() - 1; ++k) {
                        if (k == s2.getPath().size() - 2) {
                            bw.write(s2.getPath().get(k).getId() + "\n");
                        } else {
                            bw.write(s2.getPath().get(k).getId() + " -> ");
                        }
                    }
                }
            }

            System.out.println("S'han calculat els camins en " + (System.currentTimeMillis() - startTime) + "ms");
            bw.close();

        } catch (IOException e) {
            System.out.println("No s'ha pogut crear un dels fitxers!");
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

        for (Server s:servers) {
            s.prepare();
        }

        return true;
    }
}
