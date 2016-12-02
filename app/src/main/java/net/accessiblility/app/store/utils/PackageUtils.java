package net.accessiblility.app.store.utils;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.List;


/**
 * Created by LCM on 2016/8/29. 9:04
 * 获取本地应用的包名
 */

public class PackageUtils {

    public static String plintPkgAndCls(List<ApplicationInfo> resolveInfos) {
        String pkg = "";
        for (ApplicationInfo app : resolveInfos) {
            //非系统程序
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                pkg += app.processName + ",";
            } else if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                //本来是系统程序，被用户手动更新后，该系统程序也成为第三方应用程序了
                pkg += app.packageName + ",";
            }
        }
        return pkg;
    }

    public static List<ApplicationInfo> getResolveInfos(Context context) {
        List<ApplicationInfo> appList = null;
        PackageManager pm = context.getPackageManager();
        appList = pm.getInstalledApplications(0);
        return appList;
    }
}
