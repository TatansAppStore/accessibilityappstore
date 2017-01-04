package net.accessiblility.app.store.model;

import java.io.File;

/**
 * Created by zhouzhaocai on 2017/1/4.
 */

public class ShareInfo {
    private String userName;
    private String size;
    private String appName;
    private String packageName;
    private String versionName;
    private int versionCode;
    private String sign;
    private Integer cid;
    private File file;

    public ShareInfo() {

    }

    public ShareInfo(String userName, String size, String appName, String packageName, String versionName, int versionCode, String sign, Integer cid, File file) {
        this.userName = userName;
        this.size = size;
        this.appName = appName;
        this.packageName = packageName;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.sign = sign;
        this.cid = cid;
        this.file = file;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
