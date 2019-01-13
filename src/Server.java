import java.util.ArrayList;

class Server {
    private int id;
    private String country;
    private double[] location;
    private int[] reachable_from;
    private double activity;
    private ArrayList<User> users;

    public Server(int id, String country, double[] location, int[] reachable_from) {
        this.id = id;
        this.country = country;
        this.location = location;
        this.reachable_from = reachable_from;
    }

    public Server(Server s) {
        this.id = s.id;
        this.country = s.country;
        this.location = s.location.clone();
        this.reachable_from = s.reachable_from.clone();
        this.activity = s.activity;
        this.users = (ArrayList) s.users.clone();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double[] getLocation() {
        return location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public void setReachable_from(int[] reachable_from) {
        this.reachable_from = reachable_from;
    }

    public double getActivity() {
        return activity;
    }

    public void setActivity(double activity) {
        this.activity = activity;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public int[] getReachable_from() {
        return reachable_from;
    }

    public double getLoad() {
        return activity;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void prepare() {
        this.activity = 0;
        this.users = new ArrayList<>();
    }

    public void addUser(User u) {
        users.add(u);
        activity += u.getActivity();
    }

    public void removeUser(User u) {
        users.remove(u);
        activity -= u.getActivity();
    }
}
