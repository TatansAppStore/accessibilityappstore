package net.accessiblility.app.store.model;

/**
 * Created by zhouzhaocai on 2017/1/20.
 */

public class CommentDto {

    private int id;
    private String content;
    private String contentTime;
    private int thumbsUp;
    private int score;
    private UserDto user;
    private VersionDto version;



    public CommentDto(int id, String content, String contentTime, int thumbsUp, int score, UserDto user, VersionDto version) {
        this.id = id;
        this.content = content;
        this.contentTime = contentTime;
        this.thumbsUp = thumbsUp;
        this.score = score;
        this.user = user;
        this.version = version;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentTime() {
        return contentTime;
    }

    public void setContentTime(String contentTime) {
        this.contentTime = contentTime;
    }

    public int getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(int thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public VersionDto getVersion() {
        return version;
    }

    public void setVersion(VersionDto version) {
        this.version = version;
    }
}
