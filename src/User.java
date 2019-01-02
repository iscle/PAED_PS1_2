public class User {
    private String username;
    private int followers;
    private int follows;
    private double activity;
    private UserConnection[] connections;
    private Post[] posts;

    public User(String username, int followers, int follows, double activity, UserConnection[] connections, Post[] posts) {
        this.username = username;
        this.followers = followers;
        this.follows = follows;
        this.activity = activity;
        this.connections = connections;
        this.posts = posts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public double getActivity() {
        return activity;
    }

    public void setActivity(double activity) {
        this.activity = activity;
    }

    public UserConnection[] getConnections() {
        return connections;
    }

    public void setConnections(UserConnection[] connections) {
        this.connections = connections;
    }

    public Post[] getPosts() {
        return posts;
    }

    public void setPosts(Post[] posts) {
        this.posts = posts;
    }
}
