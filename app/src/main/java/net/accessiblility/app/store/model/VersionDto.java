package net.accessiblility.app.store.model;

/**
 * Created by zhouzhaocai on 2017/1/20.
 */

public class VersionDto {

    private int id;
    private int versionCode;
    private String androidAppSecId;
    private String versionName;
    private String size;
    private String gradle;

    public VersionDto(int id, int versionCode, String androidAppSecId, String versionName, String size, String gradle) {
        this.id = id;
        this.versionCode = versionCode;
        this.androidAppSecId = androidAppSecId;
        this.versionName = versionName;
        this.size = size;
        this.gradle = gradle;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getAndroidAppSecId() {
        return androidAppSecId;
    }

    public void setAndroidAppSecId(String androidAppSecId) {
        this.androidAppSecId = androidAppSecId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getGradle() {
        return gradle;
    }

    public void setGradle(String gradle) {
        this.gradle = gradle;
    }
}
