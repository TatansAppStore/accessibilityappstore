package net.accessiblility.app.store.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2016/11/8.
 */
public class AppInstallReceive extends BroadcastReceiver {
    private static String TAG = "AppInstallReceive";
    private static AppInstallCallback appInstallCallback = null;

    public interface AppInstallCallback {
        void onReceive(Context context, Intent intent);

    }

    public static void setAppInstallCallback(AppInstallCallback appInstallCallback) {
        AppInstallReceive.appInstallCallback = appInstallCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (appInstallCallback != null) {
            appInstallCallback.onReceive(context, intent);
        }
//        PackageManager pm = context.getPackageManager();
//        if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_ADDED)) {
//            String packageName = intent.getData().getSchemeSpecificPart();
//            Log.d(TAG, "--------安装成功" + packageName);
//            if (appInstallCallback != null) {
//                appInstallCallback.onReceive(context, intent);
//            }
//
//        } else if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_REPLACED)) {
//            String packageName = intent.getData().getSchemeSpecificPart();
//            Log.d(TAG, "--------替换成功" + packageName);
//
//        } else if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_REMOVED)) {
//            String packageName = intent.getData().getSchemeSpecificPart();
//            Log.d(TAG, "--------卸载成功" + packageName);
//        }
    }
}

