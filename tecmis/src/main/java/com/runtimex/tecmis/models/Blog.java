public class Blog {

    private String blogId;
    private String userId;
    private String title;
    private Date date;

    public Blog() {}

    public Blog(String blogId, String userId, String title, Date date) {
        this.blogId = blogId;
        this.userId = userId;
        this.title = title;
        this.date = date;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
