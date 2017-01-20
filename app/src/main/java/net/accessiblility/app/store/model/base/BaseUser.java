package net.accessiblility.app.store.model.base;

import net.accessiblility.app.store.model.Comment;
import net.accessiblility.app.store.model.Version;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public abstract class BaseUser implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public BaseUser(Integer id, String userName, String password, String phoneNumber, String enabled,
                    Set<Version> version) {
        super();
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.enabled = enabled;
        this.version = version;
    }

    public BaseUser(Integer id, String userName, String phoneNumber, String enabled
    ) {
        super();
        this.id = id;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.enabled = enabled;
    }

    public BaseUser(Integer id, String userName, String country
    ) {
        super();
        this.id = id;
        this.userName = userName;
        this.country = country;
    }


    private Integer id;
    private String userName;
    private String country;
    private String password;
    private String phoneNumber;
    private String enabled;
    private Set<Version> version = new HashSet<Version>();
    private Set<Comment> comment = new HashSet<Comment>();

    public Set<Comment> getComment() {
        return comment;
    }

    public void setComment(Set<Comment> comment) {
        this.comment = comment;
    }

    public Set<Version> getVersion() {
        return version;
    }

    public void setVersion(Set<Version> version) {
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public static long getSerialversionuid() {
        return serialVersionUID;
    }


    public BaseUser() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
        BaseUser other = (BaseUser) obj;
        if (enabled == null) {
            if (other.enabled != null)
                return false;
        } else if (!enabled.equals(other.enabled))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (phoneNumber == null) {
            if (other.phoneNumber != null)
                return false;
        } else if (!phoneNumber.equals(other.phoneNumber))
            return false;
        if (userName == null) {
            if (other.userName != null)
                return false;
        } else if (!userName.equals(other.userName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "BaseUser [id=" + id + ", userName=" + userName + ", password=" + password + ", phoneNumber="
                + phoneNumber + ", enabled=" + enabled + "]";
    }

    public BaseUser(Integer id, String userName, String password, String phoneNumber, String enabled) {
        super();
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.enabled = enabled;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
