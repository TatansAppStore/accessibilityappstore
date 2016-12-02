package net.accessiblility.app.store.model;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * 本地应用
 * Created by tatans on 2015/8/10.
 */
public class LocalAppInfo {
    public String appName = "";
    public String packageName = "";
    public String versionName = ""; //版本名
    public int versionCode = 0; //版本号
    public Drawable appIcon = null; //图标
    public String size ="";   //应用大小

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void print() {
        Log.v("app", "Name:" + appName + " Package:" + packageName);
        Log.v("app", "Name:" + appName + " versionName:" + versionName);
        Log.v("app", "Name:" + appName + " versionCode:" + versionCode);
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

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

}
