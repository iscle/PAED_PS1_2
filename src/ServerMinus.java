class ServerMinus {
    private int id;
    private String country;
    private double[] location;
    private int reachable_from;

    public ServerMinus(int id, String country, double[] location, int reachable_from) {
        this.id = id;
        this.country = country;
        this.location = location;
        this.reachable_from = reachable_from;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public double[] getLocation() {
        return location;
    }

    public int getReachable_from() {
        return reachable_from;
    }
}
