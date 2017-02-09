package net.accessiblility.app.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.controller.DownloadController;
import net.accessiblility.app.store.model.DownloadInfo;
import net.accessiblility.app.store.model.LocalAppInfo;
import net.accessiblility.app.store.utils.AppUtils;
import net.tatans.coeus.network.callback.HttpHandler;
import net.tatans.coeus.network.tools.TatansBitmap;
import net.tatans.coeus.network.tools.TatansDb;
import net.tatans.coeus.network.tools.TatansLog;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.network.utils.DirPath;

import java.io.File;
import java.util.List;

/**
 * Created by tatans on 2015/12/4.
 */
public class DownloadQueueAdapter extends BaseAdapter {

    private List<DownloadInfo> itemList;
    private Context context;
    private List<LocalAppInfo> localAppList;
    private int progress = 0;

    public DownloadQueueAdapter(Context context, List<DownloadInfo> list, List<LocalAppInfo> localAppList) {
        this.itemList = list;
        this.context = context;
        this.localAppList = localAppList;

    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final DownloadInfo downloadInfo = itemList.get(position);
        final String appName = downloadInfo.getApp_name();
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_download_app_item, null);
            holder = new ViewHolder();
            holder.appDetails = (LinearLayout) convertView.findViewById(R.id.ll_app_details);
            holder.appName = (TextView) convertView.findViewById(R.id.tv_app_name);
            holder.appIcon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
            holder.appVersion = (TextView) convertView.findViewById(R.id.tv_app_version);
            holder.appSize = (TextView) convertView.findViewById(R.id.tv_app_size);
            holder.appState = (TextView) convertView.findViewById(R.id.tv_app_state);
            holder.appInfo = (TextView) convertView.findViewById(R.id.tv_app_info);
            holder.appInstall = (TextView) convertView.findViewById(R.id.tv_app_install);
            holder.appDelete = (TextView) convertView.findViewById(R.id.tv_app_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.appName.setText(appName);
        TatansBitmap tb = TatansBitmap.create();
        tb.display(holder.appIcon, downloadInfo.getIcon_uri());
        holder.appVersion.setText("版本：" + downloadInfo.getVersionName());
        TatansLog.d(downloadInfo.getApp_size() + "M");
        holder.appSize.setText(downloadInfo.getApp_size() + "M");
        holder.appInfo.setText(downloadInfo.getDecription());
        String appStateStr = downloadInfo.getDownload_state();
        holder.appState.setText(appStateStr);

        if (downloadInfo.getType() == -1) {
            holder.appDelete.setVisibility(View.GONE);
        }
        boolean isdownload = false;

        if (appStateStr.equals("下载成功")) {
            holder.appInstall.setText("安装");
            for (int i = 0; i < localAppList.size(); i++) {
                LocalAppInfo localAppInfo = localAppList.get(i);
                if (localAppInfo.packageName.equals(downloadInfo.getApp_packageName())) {
                    if (localAppInfo.versionCode >= downloadInfo.getVersionCode()) {
                        holder.appInstall.setText("打开");
                        break;
                    }
                }
            }
        } else if (appStateStr.equals("未更新")) {
            final File mFile = new File(DirPath.getMyCacheDir("stores/download/", appName + ".apk"));
            if (mFile.exists()) {
                isdownload = true;
                holder.appInstall.setText("安装");
                holder.appState.setText("下载成功");
            } else {
                holder.appInstall.setText("更新");
            }

        } else if (appStateStr.equals("暂停下载")) {
            holder.appInstall.setText(downloadInfo.getDownload_progress() + "%");
        } else if (appStateStr.equals("下载失败")) {
            holder.appInstall.setText("重新下载");
        } else if (appStateStr.equals("下载中")) {
            holder.appInstall.setText(downloadInfo.getDownload_progress() + "%");
        } else if (appStateStr.equals("等待下载")) {
            holder.appInstall.setText("等待中");
        }

        if (downloadInfo != null&&!isdownload) {
            progress = downloadInfo.getDownload_progress();
            if(progress<100&&progress>=0){
                holder.appInstall.setText( "继续");
            }

        }

//        final HttpHandler<File> httpHandler = AppUtils.httpHashmap.get(downloadInfo.getApp_name());
        final ViewHolder finalHolder = holder;
        holder.appInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stateStr = finalHolder.appInstall.getText().toString();
                if (stateStr.equals("下载") || stateStr.equals("重新下载") || (stateStr.equals("更新") || stateStr.equals("继续"))) {
                    finalHolder.appInstall.setText("0%");
                    AppUtils.httpHashmap.put(downloadInfo.getApp_name(), DownloadController.startDownload(context, downloadInfo));
                } else if (stateStr.contains("%")) {
                    finalHolder.appInstall.setText("继续");
                    HttpHandler<File> httpHandler = AppUtils.httpHashmap.get(downloadInfo.getApp_name());
                    if (httpHandler != null) {
                        httpHandler.stop();
                    }
                } else {
                    File mFile = new File(DirPath.getMyCacheDir("stores/download/", appName + ".apk"));
                    if (mFile.getName().endsWith(".apk")) {
                        String appInstall = finalHolder.appInstall.getText().toString();
                        if (appInstall.equals("打开")) {
                            AppUtils.startAppByPackageName(context, downloadInfo.getApp_packageName());
                        } else if (appInstall.equals("安装")) {
                            Intent install = new Intent();
                            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            install.setAction(android.content.Intent.ACTION_VIEW);
                            install.setDataAndType(Uri.fromFile(mFile), "application/vnd.android.package-archive");
                            context.startActivity(install);
                        }
                    }
                }
            }
        });

        holder.appDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final File mFile = new File(DirPath.getMyCacheDir("stores/download/", appName + ".apk"));
                if (mFile.getName().endsWith(".apk")) {
                    HttpHandler<File> httpHandler = AppUtils.httpHashmap.get(downloadInfo.getApp_name());
                    if (httpHandler != null) {
                        if (!httpHandler.isStop()) {
                            httpHandler.stop();
                        }
                    }
                    TatansDb tatansDb = TatansDb.create(DownloadController.downloadInfoTable);
                    tatansDb.deleteById(DownloadInfo.class, downloadInfo.getId());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            mFile.delete();
                        }
                    }).start();
                    itemList.remove(position);
                    notifyDataSetChanged();
                    TatansToast.showAndCancel("删除任务同时删除本地文件成功");
                }
            }
        });


        return convertView;
    }

    class ViewHolder {
        ImageView appIcon;
        TextView appName;
        TextView appVersion;
        TextView appSize;
        TextView appState;
        TextView appInfo;
        TextView appInstall;
        TextView appDelete;
        LinearLayout appDetails;


    }

    private Handler handler = null;

    /**
     * 更新单个item
     *
     * @param name     应用名
     * @param listView ListView控件
     */
    public void updataView(final ListView listView, final int download_progress, final String name) {
        if (handler == null) {
            handler = new Handler();
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                int visibleFirstPosi = listView.getFirstVisiblePosition();
                int visibleLastPosi = listView.getLastVisiblePosition();
                int posi = -1;
                for (int i = 0; i < itemList.size(); i++) {
                    if (itemList.get(i).getApp_name().equals(name)) {
                        posi = i;
                        break;
                    }
                }
                if (posi == -1) {
                    return;
                }
                if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {
                    View view = listView.getChildAt(posi - visibleFirstPosi);
                    ViewHolder holder = (ViewHolder) view.getTag();
                    if (download_progress == 100) {
                        holder.appInstall.setText("安装");
                        holder.appState.setText("下载成功");

                    } else if (download_progress == -1) {
                        holder.appInstall.setText("打开");
                        holder.appState.setText("下载成功");
                    } else if (download_progress == -101) {
                        holder.appInstall.setText("继续");
                    } else {
                        holder.appInstall.setText(download_progress + "%");
                    }
                }
            }
        });

    }

    /**
     * 更新单个item
     *
     * @param name     应用名
     * @param listView ListView控件
     */
    public void updataView(final ListView listView, final int download_progress, final String name, final String downloadState) {
        if (handler == null) {
            handler = new Handler();
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                int visibleFirstPosi = listView.getFirstVisiblePosition();
                int visibleLastPosi = listView.getLastVisiblePosition();
                int posi = -1;
                for (int i = 0; i < itemList.size(); i++) {
                    if (itemList.get(i).getApp_name().equals(name)) {
                        posi = i;
                        break;
                    }
                }
                if (posi == -1) {
                    return;
                }
                if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {
                    View view = listView.getChildAt(posi - visibleFirstPosi);
                    ViewHolder holder = (ViewHolder) view.getTag();
                    holder.appInstall.setText(downloadState);
                    holder.appState.setText(download_progress + "%");

                }
            }
        });
    }
}