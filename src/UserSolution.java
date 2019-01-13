import java.util.HashMap;

public class UserSolution {
    HashMap<User, Server> combination;
    double equity;
    int distTotal;
    User nextUser;

    public UserSolution(HashMap<User, Server> combination, int equity, int distTotal, User nextUser) {
        this.combination = combination;
        this.equity = equity;
        this.distTotal = distTotal;
        this.nextUser = nextUser;
    }

    public UserSolution(UserSolution u) {
        this.combination = (HashMap) u.combination.clone();
        this.equity = u.equity;
        this.distTotal = u.distTotal;
        this.nextUser = u.nextUser;
    }

    public UserSolution(User user) {
        this.combination = new HashMap<>();
        this.equity = 0;
        this.distTotal = 0;
        this.nextUser = user;
    }

    public UserSolution() {
        this.combination = new HashMap<>();
        this.equity = 0;
        this.distTotal = 0;
        this.nextUser = null;
    }

    public void add(User user, Server server) {
        combination.put(user, server);
        equity = maxServer() - minServer();
        distTotal += getDistance(server.getLocation()[0], server.getLocation()[1], user.getPosts()[0].getLocation()[0], user.getPosts()[0].getLocation()[1]);
    }

    public void remove(User user, Server server) {
        combination.remove(user, server);
        equity -= user.getActivity();
        distTotal -= getDistance(server.getLocation()[0], server.getLocation()[1], user.getPosts()[0].getLocation()[0], user.getPosts()[0].getLocation()[1]);
    }

    public double minServer() {
        double min = -1;

        for (Server sv:combination.values()) {
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

        for (Server sv:combination.values()) {
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

    public HashMap<User, Server> getCombination() {
        return combination;
    }

    public void setCombination(HashMap<User, Server> combination) {
        this.combination = combination;
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
