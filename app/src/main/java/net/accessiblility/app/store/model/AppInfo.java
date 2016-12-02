package net.accessiblility.app.store.model;

import java.io.Serializable;
import java.util.List;

/**
 * 应用商店应用信息
 * Created by tatans on 2015/8/7.
 */
public class AppInfo implements Serializable {

    public List<AI> results;
    public static class AI implements Serializable {
        public String id;
        public String appName;
        public String decription; //介绍
        public String packageName;
        public int versionCode; //版本号
        public String versionName; //版本名
        public String iconUrl; //图标地址
        public String url; //下载地址
        public String size; //安装包大小

        public String getId()
        {
            return id;
        }
        public void setId(String id)
        {
            this.id = id;
        }

        public String getAppName()
        {
            return appName;
        }
        public void setAppName(String appName)
        {
            this.appName = appName;
        }

        public String getDecription()
        {
            return decription;
        }

        public void setDecription(String decription)
        {
            this.decription = decription;
        }

        public String getPackageName()
        {
            return packageName;
        }
        public void setPackageName(String packageName)
        {
            this.packageName = packageName;
        }

        public int  getVersionCode()
        {
            return versionCode;
        }
        public void setVersionCode(int versionCode)
        {
            this.versionCode = versionCode;
        }

        public String getVersionName()
        {
            return versionName;
        }
        public void setVersionName(String versionName)
        {
            this.versionName = versionName;
        }

        public String getUrl()
        {
            return url;
        }
        public void setUrl(String url)
        {
            this.url = url;
        }

        public String getIconUrl() {
            return iconUrl;
        }
        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getSize() {
            return size;
        }
        public void setSize(String size) {
            this.size = size;
        }
    }
    public List<AI> getResults()
    {
        return results;
    }
    public void setResults(List<AI> results)
    {
        this.results = results;
    }
}
