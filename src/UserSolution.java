import java.util.ArrayList;

public class UserSolution {
    Server[] servers;
    double equity;
    int distTotal;
    User nextUser;

    public UserSolution(Server[] servers, int equity, int distTotal, User nextUser) {
        this.servers = servers;
        this.equity = equity;
        this.distTotal = distTotal;
        this.nextUser = nextUser;
    }

    public UserSolution(Server[] servers) {
        this.servers = servers;
        this.equity = 0;
        this.distTotal = 0;
        this.nextUser = null;
    }

    public UserSolution(UserSolution u) {
        this.servers = u.servers.clone();
        this.equity = u.equity;
        this.distTotal = u.distTotal;
        this.nextUser = u.nextUser;
    }

    public void add(User user, Server server) {
        int indexOfServer = indexOfServer(server);
        servers[indexOfServer].addUser(user);
        equity = maxServer() - minServer();
        distTotal += getDistance(servers[indexOfServer].getLocation()[0], servers[indexOfServer].getLocation()[1], user.getPosts()[0].getLocation()[0], user.getPosts()[0].getLocation()[1]);
    }

    public void remove(User user, Server server) {
        int indexOfServer = indexOfServer(server);
        distTotal -= getDistance(servers[indexOfServer].getLocation()[0], servers[indexOfServer].getLocation()[1], user.getPosts()[0].getLocation()[0], user.getPosts()[0].getLocation()[1]);
        equity -= user.getActivity();
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
            if (servers[i] == s) {
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

    public void setEquity(int equity) {
        this.equity = equity;
    }

    public int getDistTotal() {
        return distTotal;
    }

    public void setDistTotal(int distTotal) {
        this.distTotal = distTotal;
    }

    public User getNextUser() {
        return nextUser;
    }

    public void setNextUser(User nextUser) {
        this.nextUser = nextUser;
    }
}
