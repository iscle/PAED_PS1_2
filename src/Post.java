public class Post {
    private int id;
    private long published;
    private double[] location;
    private String category;
    private String[] liked_by;
    private String[] commented_by;

    public Post(int id, long published, double[] location, String category, String[] liked_by, String[] commented_by) {
        this.id = id;
        this.published = published;
        this.location = location;
        this.category = category;
        this.liked_by = liked_by;
        this.commented_by = commented_by;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getPublished() {
        return published;
    }

    public void setPublished(long published) {
        this.published = published;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String[] getLiked_by() {
        return liked_by;
    }

    public void setLiked_by(String[] liked_by) {
        this.liked_by = liked_by;
    }

    public String[] getCommented_by() {
        return commented_by;
    }

    public void setCommented_by(String[] commented_by) {
        this.commented_by = commented_by;
    }
}
