package net.accessiblility.app.store.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import net.accessiblility.app.store.model.AppItemInfo;
import net.accessiblility.app.store.model.DownloadInfo;
import net.tatans.coeus.network.callback.HttpHandler;
import net.tatans.coeus.network.callback.HttpRequestCallBack;
import net.tatans.coeus.network.callback.HttpRequestParams;
import net.tatans.coeus.network.tools.TatansDb;
import net.tatans.coeus.network.tools.TatansHttp;
import net.tatans.coeus.network.utils.DirPath;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 下载管理
 * Created by tatans on 2015/9/21.
 */
public class DownloadController {
    private static DownloadCallback mDownloadCallback = null;
    private static String TAG = "DownloadController";
    public static String downloadInfoTable = "DownloadInfo_005";

    public interface DownloadCallback {
        void onLoading(long count, long current, String appName);

        void onStartCallback();

        void onFailure(Throwable t, String strMsg);

        void onSuccess(final File file);

    }

    public static void setDownloadCallback(DownloadCallback mDownloadCallback) {
        DownloadController.mDownloadCallback = mDownloadCallback;
    }

    /**
     * 下载时调用的方法
     *
     * @param context
     * @param mDownloadInfo app信息
     * @return HttpHandler<File> 下载线程
     */
    public static HttpHandler<File> startDownload(final Context context, final DownloadInfo mDownloadInfo) {
        final TatansDb db = TatansDb.create(downloadInfoTable);
        DownloadInfo downloadDbInfo = db.findById(mDownloadInfo.getId(), DownloadInfo.class);
        mDownloadInfo.setDownload_state("等待下载");
        mDownloadInfo.setDownload_progress(0);
        if (downloadDbInfo == null) {
            mDownloadInfo.setDate(new Date());
            db.save(mDownloadInfo);
        } else {
            db.update(mDownloadInfo);
        }

        TatansHttp fh = new TatansHttp();
        HttpRequestParams params = new HttpRequestParams();
        final String  uri = Controller.DownLoadApp;
        params.put("packageName", mDownloadInfo.getApp_packageName());
        params.put("versionName", mDownloadInfo.getVersionName());
        final String name = mDownloadInfo.getApp_name();
        //创建下载目录
        File myFile = new File(Environment.getExternalStorageDirectory().getPath(), "/tatans/stores/download");
        if (!myFile.exists()) {
            try {
                myFile.mkdirs();
                myFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        HttpHandler httpHandler = fh.download(uri,params, DirPath.getMyCacheDir("stores/download/", name + ".apk"), true, new HttpRequestCallBack<File>() {
            @Override
            public void onLoading(long count, long current) {
                int progress = (int) (current * 100 / count);
                Log.e(TAG, "百分之" + progress);
                super.onLoading(count, current);
                if (mDownloadCallback != null) {
                    mDownloadCallback.onLoading(count, current, name);
                }
                mDownloadInfo.setDownload_state("下载中");
                mDownloadInfo.setDownload_progress(progress);
                db.update(mDownloadInfo);
            }

            @Override
            public void onStart() {
                super.onStart();
                Log.e(TAG, "onStart" + name + "____" + uri);
                if (mDownloadCallback != null) {
                    mDownloadCallback.onStartCallback();
                }
                mDownloadInfo.setDownload_state("开始下载");
                db.update(mDownloadInfo);
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                Log.e(TAG, "onFailure" + strMsg);
                super.onFailure(t, strMsg);
                if (mDownloadCallback != null) {
                    mDownloadCallback.onFailure(t, strMsg);
                }
                if (strMsg.equals("user stop download thread")) {
                    Log.e(TAG, "onFailure" + "暂停下载");
                    mDownloadInfo.setDownload_state("暂停下载");
                    db.update(mDownloadInfo);
                } else {
                    mDownloadInfo.setDownload_state("下载失败");
                    db.update(mDownloadInfo);
                }

            }

            @Override
            public void onSuccess(final File file) {
                Log.e(TAG, "onSuccess");
                super.onSuccess(file);
                if (mDownloadCallback != null) {
                    mDownloadCallback.onSuccess(file);
                }
                mDownloadInfo.setDownload_state("下载成功");
                mDownloadInfo.setDownload_progress(100);
                db.update(mDownloadInfo);
                File mFile = new File(DirPath.getMyCacheDir("stores/download/", name + ".apk"));
                if (mFile.getName().endsWith(".apk")) {
                    Intent install = new Intent();
                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    install.setAction(android.content.Intent.ACTION_VIEW);
                    install.setDataAndType(Uri.fromFile(mFile), "application/vnd.android.package-archive");
                    context.startActivity(install);
                }
            }
        });

        return httpHandler;
    }



    public static DownloadInfo getDownloadInfo(AppItemInfo.AppInfo appInfo) {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setId(appInfo.getId());
        downloadInfo.setApp_name(appInfo.getAppName());
        downloadInfo.setApp_packageName(appInfo.getPackageName());
        downloadInfo.setApp_size(appInfo.getSize());
        downloadInfo.setDecription(appInfo.getDecription());
        downloadInfo.setIcon_uri(appInfo.getIconUrl());
        downloadInfo.setType(0);
        downloadInfo.setUri(appInfo.getUrl());
        downloadInfo.setVersionCode(appInfo.getVersionCode());
        downloadInfo.setVersionName(appInfo.getVersionName());
        downloadInfo.setApp_size(appInfo.getSize());
        return downloadInfo;
    }


}
