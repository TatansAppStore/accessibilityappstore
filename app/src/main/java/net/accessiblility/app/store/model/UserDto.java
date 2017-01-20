package net.accessiblility.app.store.model;

/**
 * Created by zhouzhaocai on 2017/1/20.
 */

public class UserDto {
    private Integer id;
    private String userName;
    private String country;

    public UserDto(Integer id, String userName, String country) {
        this.id = id;
        this.userName = userName;
        this.country = country;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


}
