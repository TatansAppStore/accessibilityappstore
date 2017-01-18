package net.accessiblility.app.store.controller;

/**
 * Created by tatans on 2015/8/19.
 * 获取后台数据的地址
 */
public class Controller {
    //    public static String uri = "http://192.168.1.9:8080/android/rest/v1.0/"; //本地
    public static String uri = "http://115.29.11.17:8094/android/rest/v1.0/";  //服务器

//    public static String uri = "http://192.168.31.17:8094/android/rest/v1.0/";  //服务器
//    public static String uri01 = "http://192.168.1.9:8080/android/rest/v1.0/"; //本地

//    public static String GetUpdateInfo = uri+"android/validaVersion.do";
//    public static String ClassifyItem = uri+"findapp/appclassifyitem.do";
//    //专用于热门APP
//    public static String FindTagApp = uri+"findapp/appclassifytag.do";
//    public static String SearchApp = uri+"findapp/searchapp.do";
//    public static String GetAllNewApp = uri+"android/validaVersion.do";
//    //搜索跳转到指定应用
//    public static String FindSpecifyApp = uri+"findapp/findspecifyapp.do";

    public static String getUserCommentApp = uri + "findappsec/getUserCommentApp.do";
    public static String setUserCommentApp = uri + "findappsec/setUserCommentApp.do";
    public static String GetUpdateInfo = uri + "findappsec/validaVersion.do";
    public static String ClassifyItem = uri + "findappsec/appclassifyitem.do";
    //专用于热门APP
    public static String FindTagApp = uri + "findappsec/appclassifytag.do";
    public static String SearchApp = uri + "findappsec/searchapp.do";
    public static String GetAllNewApp = uri + "findappsec/validaVersion.do";
    //排行榜
    public static String TopChartsApp = uri + "findappsec/topChartsApp.do";
    //下载
    public static String DownLoadApp = uri + "findappsec/downLoadApp.do";
    //搜索跳转到指定应用
    public static String FindSpecifyApp = uri + "findappsec/findspecifyapp.do";
    //上传
    public static String UpLoad = uri + "findappsec/upload.do";
    //注册
    public static String RegisterUser = uri + "findappsec/registerUser.do";
    //登录
    public static String LoginUser = uri + "findappsec/loginUser.do";
    //修改密码
    public static String ModifyPassword = uri + "findappsec/modifyPassword.do";

    //修改用户名
    public static String ModifyUserName = uri + "findappsec/modifyUserName.do";

    //请求所有可更新标签
    public static String GetAppLabelUpdate = uri + "appMark/queryMarkForUpdate.do";
    //下载更新标签
    public static String DownloadLabel = uri + "appMark/queryAppMark.do";

    public static String MyPackage = "net.tatans.rhea.app";

    public static String launcher = "com.miui.home";
    public static String TBIS = "com.google.android.marvin.talkbackrhea";
}
