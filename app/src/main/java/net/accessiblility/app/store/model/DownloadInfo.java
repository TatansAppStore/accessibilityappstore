package net.accessiblility.app.store.model;


import net.tatans.coeus.annotation.sqlite.Table;

import java.util.Date;

/**
 * 下载列表数据库存储（本地）
 * Created by tatans on 2015/8/21.
 */
@Table(name = "DownloadInfo_005")
public class DownloadInfo {

    private String icon_uri = null; //图标
    private String id;
    private String app_name;
    private String download_state;//当前下载状态
    private int download_progress;//下载进度
    private String uri; //下载地址
    private String versionName;//版本名
    private int versionCode;//版本名
    private String app_packageName;
    private int type ;   //进行排序用    2 为安装完成和安装失败   1 为等待下载   0 为下载中 已暂停 安装中
    private Date date;  //日期   进行排序用
    private String decription; //介绍
    private String app_size;//应用大小

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }


    public String getIcon_uri() {
        return icon_uri;
    }

    public void setIcon_uri(String icon_uri) {
        this.icon_uri = icon_uri;
    }

    public String getDownload_state() {
        return download_state;
    }

    public void setDownload_state(String download_state) {
        this.download_state = download_state;
    }

    public int getDownload_progress() {
        return download_progress;
    }

    public void setDownload_progress(int download_progress) {
        this.download_progress = download_progress;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getApp_packageName() {
        return app_packageName;
    }
    public void setApp_packageName(String app_packageName) {
        this.app_packageName = app_packageName;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String getDecription() {
        return decription;
    }
    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getApp_size() {
        return app_size;
    }
    public void setApp_size(String app_size) {
        this.app_size = app_size;
    }


}
