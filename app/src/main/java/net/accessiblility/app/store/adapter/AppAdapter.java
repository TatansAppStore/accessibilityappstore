package net.accessiblility.app.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.activity.DetailActivity;
import net.accessiblility.app.store.controller.DownloadController;
import net.accessiblility.app.store.model.AppItemInfo;
import net.accessiblility.app.store.model.DownloadInfo;
import net.accessiblility.app.store.model.LocalAppInfo;
import net.accessiblility.app.store.utils.AppUtils;
import net.accessiblility.app.store.utils.CommonUtils;
import net.tatans.coeus.network.callback.HttpHandler;
import net.tatans.coeus.network.tools.TatansDb;
import net.tatans.coeus.network.utils.DirPath;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tatans on 2015/12/4.
 */
public class AppAdapter extends BaseAdapter {

    private List<AppItemInfo.AppInfo> itemList;
    private ArrayList<LocalAppInfo> localAppList;
    private Context context;
    private TatansDb db;
    private static String TAG = "AppAdapter";

    public AppAdapter(Context context, List<AppItemInfo.AppInfo> list, ArrayList<LocalAppInfo> localAppList) {
        this.itemList = list;
        this.context = context;
        this.localAppList = localAppList;
        if (db == null) {
            db = TatansDb.create(DownloadController.downloadInfoTable);
        }
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final AppItemInfo.AppInfo appInfo = itemList.get(position);
        DownloadInfo downloadDbInfo = db.findById(appInfo.getId(), DownloadInfo.class);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_test_app_item, null);
            holder = new ViewHolder();
            holder.appDetails = (LinearLayout) convertView.findViewById(R.id.ll_app_details);
            holder.appName = (TextView) convertView.findViewById(R.id.tv_app_name);
            holder.appRank = (TextView) convertView.findViewById(R.id.tv_app_rank);
            holder.appVersion = (TextView) convertView.findViewById(R.id.tv_app_version);
            holder.appSize = (TextView) convertView.findViewById(R.id.tv_app_size);
            holder.appState = (TextView) convertView.findViewById(R.id.tv_app_state);
            holder.appInfo = (TextView) convertView.findViewById(R.id.tv_app_info);
            holder.appDownload = (TextView) convertView.findViewById(R.id.tv_app_download);
            holder.appDownloadCount = (TextView) convertView.findViewById(R.id.tv_app_download_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String downloadCount = itemList.get(position).getDown();
        int count = position + 1;
        if (downloadCount != null) {
            String downloadStr = "下载：" + downloadCount + "次 ";
            holder.appRank.setVisibility(View.VISIBLE);
            holder.appRank.setText(count + "");
            holder.appDownloadCount.setVisibility(View.VISIBLE);
            holder.appDownloadCount.setText(downloadStr);
        }

        holder.appName.setText(itemList.get(position).getAppName());
        holder.appVersion.setText("版本名：" + itemList.get(position).getVersionName());
        holder.appSize.setText(itemList.get(position).getSize() + "M");
        holder.appInfo.setText((itemList.get(position).getDecription()));

        holder.appDownload.setText("下载");
        holder.appState.setText(context.getString(R.string.downloadState_uninstalled));
        final File mFile = new File(DirPath.getMyCacheDir("stores/download/", appInfo.getAppName() + ".apk"));
        if (localAppList != null) {
            for (int i = 0; i < localAppList.size(); i++) {
                LocalAppInfo localAppInfo = localAppList.get(i);
                if (localAppInfo.packageName.equals(appInfo.getPackageName())) {
                    if (localAppInfo.versionCode >= appInfo.getVersionCode()) {
                        holder.appState.setText(context.getString(R.string.downloadState_installed));
                        holder.appDownload.setText("打开");
                        break;
                    } else if (localAppInfo.versionCode < appInfo.getVersionCode()) {
                        holder.appState.setText(context.getString(R.string.downloadState_installed));
                        holder.appDownload.setText("更新");
                        if (mFile.exists()) {
                            holder.appDownload.setText("安装");
                        }
                    }
                }
            }
        }

        if (holder.appState.getText().equals("未安装")) {
            if (mFile.exists()) {
                holder.appDownload.setText("安装");
            }
        }

        if (downloadDbInfo != null) {
            String state = downloadDbInfo.getDownload_state();
            Log.d(TAG, state);
            if (state.equals("暂停下载") || state.equals("下载中")) {
                holder.appState.setText(downloadDbInfo.getDownload_progress() + "%");
                holder.appDownload.setText("下载");
            }
        }

        final ViewHolder finalHolder = holder;
        final ViewHolder finalHolder1 = holder;
        holder.appDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.isFastDoubleClick()) {
                    return;
                }
                HttpHandler<File> httpHandler = AppUtils.httpHashmap.get(appInfo.getAppName());
                String stateStr = finalHolder.appDownload.getText().toString();
                if (stateStr.equals("打开")) {
                    AppUtils.startAppByPackageName(context, appInfo.getPackageName());
                } else if (stateStr.equals("安装")) {
                    File mFile = new File(DirPath.getMyCacheDir("stores/download/", appInfo.getAppName() + ".apk"));
                    if (mFile.exists()) {
                        Intent install = new Intent();
                        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        install.setAction(android.content.Intent.ACTION_VIEW);
                        install.setDataAndType(Uri.fromFile(mFile), "application/vnd.android.package-archive");
                        context.startActivity(install);
                    }
                } else if (stateStr.equals("下载") || stateStr.equals("更新")) {
                    finalHolder1.appDownload.setText("暂停");
                    AppUtils.httpHashmap.put(appInfo.getAppName(), DownloadController.startDownload(context, DownloadController.getDownloadInfo(appInfo)));
                } else if (stateStr.equals("暂停")) {
                    if (httpHandler != null) {
                        if (!httpHandler.isStop()) {
                            finalHolder1.appDownload.setText("下载");
                            httpHandler.stop();
                        }
                    }

                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("AppInfo", appInfo);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView appName;
        TextView appVersion;
        TextView appSize;
        TextView appState;
        TextView appInfo;
        TextView appDownload;
        TextView appRank;
        TextView appDownloadCount;
        LinearLayout appDetails;


    }

    /**
     * 更新单个item
     *
     * @param name     应用名
     * @param listView ListView控件
     */
    public void updataView(ListView listView, int download_progress, String name) {
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();
        int posi = -1;
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getAppName().equals(name)) {
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
                holder.appDownload.setText("安装");
                holder.appState.setText("下载成功");
            } else if (download_progress == -1) {
                holder.appDownload.setText("打开");
                holder.appState.setText("已安装");
            } else if (download_progress == -2) {
                holder.appDownload.setText("暂停");
                holder.appState.setText("暂停");
            } else {
                holder.appState.setText(download_progress + "%");
            }

        }
    }
}
