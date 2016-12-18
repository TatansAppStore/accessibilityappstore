package net.accessiblility.app.store.model;

import java.io.Serializable;
import java.util.List;

/**
 *  应用信息
 * Created by tatans on 2015/8/26.
 */
public class AppItemInfo  implements Serializable {
    private boolean code; //返回值判断请求数据正确与否
    private int pageNo; //当前页数
    private int pageCount; //总页数
    private List<AppInfo> result; //请求结果

    public boolean getCode() {
        return code;
    }
    public void setCode(boolean code) {
        this.code = code;
    }

    public int getPageNo() {
        return pageNo;
    }
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageCount() {
        return pageCount;
    }
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<AppInfo> getResult()
    {
        return result;
    }
    public void setResult(List<AppInfo> result)
    {
        this.result = result;
    }

    public static class AppInfo implements Serializable{

        private String id;
        private String appName;
        private String decription; //介绍
        private String packageName;
        private int versionCode; //版本号
        private String versionName; //版本名
        private String iconUrl; //图标地址
        private String url; //下载地址
        private String size; //安装包大小
        private String down; //安装包大小

        public String getSize() {
            return size;
        }
        public void setSize(String size) {
            this.size = size;
        }

        public String getIconUrl() {
            return iconUrl;
        }
        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

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

        public String getDown() {
            return down;
        }

        public void setDown(String down) {
            this.down = down;
        }
    }
}
