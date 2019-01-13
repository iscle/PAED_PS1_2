public class UserSolution {
    private Server[] servers;
    private double equity;
    private double distTotal;
    private int totalUsers;

    public UserSolution(Server[] servers) {
        this.servers = cloneServerArray(servers);
        this.equity = 0;
        this.distTotal = 0;
        this.totalUsers = 0;
    }

    public UserSolution(UserSolution u) {
        this.servers = cloneServerArray(u.servers);
        this.equity = u.equity;
        this.distTotal = u.distTotal;
        this.totalUsers = u.totalUsers;
    }

    public Server[] cloneServerArray(Server[] servers) {
        Server[] clone = new Server[servers.length];

        for (int i = 0; i < servers.length; ++i) {
            clone[i] = new Server(servers[i]);
        }

        return clone;
    }

    public void add(User user, Server server) {
        int indexOfServer = indexOfServer(server);
        servers[indexOfServer].addUser(user);
        equity = maxServer() - minServer();
        distTotal += getDistance(servers[indexOfServer].getLocation()[0], servers[indexOfServer].getLocation()[1], user.getPosts()[0].getLocation()[0], user.getPosts()[0].getLocation()[1]);
        ++totalUsers;
    }

    public void remove(User user, Server server) {
        int indexOfServer = indexOfServer(server);
        --totalUsers;
        distTotal -= getDistance(servers[indexOfServer].getLocation()[0], servers[indexOfServer].getLocation()[1], user.getPosts()[0].getLocation()[0], user.getPosts()[0].getLocation()[1]);
        equity = maxServer() - minServer();
        servers[indexOfServer].removeUser(user);
    }

    public double minServer() {
        double min = -1;

        for (Server sv:servers) {
            if (min == -1) {
                min = sv.getLoad();
            } else {
                if (sv.getLoad() < min) {
                    min = sv.getLoad();
                }
            }
        }

        return min;
    }

    public double maxServer() {
        double max = -1;

        for (Server sv:servers) {
            if (max == -1) {
                max = sv.getLoad();
            } else {
                if (sv.getLoad() > max) {
                    max = sv.getLoad();
                }
            }
        }

        return max;
    }

    public int indexOfServer(Server s) {
        for (int i = 0; i < servers.length; ++i) {
            if (servers[i].getId() == s.getId()) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Gets the distance between two points using the haversine formula
     * @param lat1 Latitude 1
     * @param lon1 Longitude 1
     * @param lat2 Latitude 2
     * @param lon2 Longitude 2
     * @return distance in KM
     */
    public static double getDistance(double lat1, final double lon1, double lat2, final double lon2) { // In KM
        final double dLat = Math.toRadians(lat2 - lat1); // Get the difference between the two latitudes
        final double dLon = Math.toRadians(lon2 - lon1); // Get the difference between the two longitudes
        final double rad = 6371; // Earth radius in KM
        double a, c;

        lat1 = Math.toRadians(lat1); // Convert lat1 to radians
        lat2 = Math.toRadians(lat2); // Convert lat2 to radians

        // Apply the haversine formula
        a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        c = 2 * Math.asin(Math.sqrt(a));

        return rad * c;
    }

    public Server[] getServers() {
        return servers;
    }

    public void setServers(Server[] servers) {
        this.servers = servers;
    }

    public void setEquity(double equity) {
        this.equity = equity;
    }

    public double getEquity() {
        return equity;
    }

    public double getDistTotal() {
        return distTotal;
    }

    public int getTotalUsers() {
        return totalUsers;
    }
}
