package net.accessiblility.app.store.model.base;

import net.accessiblility.app.store.model.User;
import net.accessiblility.app.store.model.Version;

import java.io.Serializable;


public abstract class BaseComment implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private String content;
    private String contentTime;
    private User user;
    private int thumbsUp;
    private int score;
	private Version version;

    public BaseComment() {
        super();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + ((contentTime == null) ? 0 : contentTime.hashCode());
        result = prime * result + id;
        result = prime * result + score;
        result = prime * result + thumbsUp;
        result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseComment other = (BaseComment) obj;
        if (content == null) {
            if (other.content != null)
                return false;
        } else if (!content.equals(other.content))
            return false;
        if (contentTime == null) {
            if (other.contentTime != null)
                return false;
        } else if (!contentTime.equals(other.contentTime))
            return false;
        if (id != other.id)
            return false;
        if (score != other.score)
            return false;
        if (thumbsUp != other.thumbsUp)
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public void setContentTime(String content) {
        this.contentTime = contentTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(int thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

	public Version getVersion() {
		return version;
	}


	public void setVersion(Version version) {
		this.version = version;
	}

    public BaseComment(int id, String content, String contentTime, User user, int thumbsUp, int score) {
        super();
        this.id = id;
        this.content = content;
        this.contentTime = contentTime;
        this.user = user;
        this.thumbsUp = thumbsUp;
        this.score = score;
//		this.version = version;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
