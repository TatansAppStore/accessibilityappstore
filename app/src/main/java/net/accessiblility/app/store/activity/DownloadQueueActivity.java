package net.accessiblility.app.store.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.adapter.DownloadQueueAdapter;
import net.accessiblility.app.store.broadcast.AppInstallReceive;
import net.accessiblility.app.store.controller.DownloadController;
import net.accessiblility.app.store.model.DownloadInfo;
import net.accessiblility.app.store.model.LocalAppInfo;
import net.accessiblility.app.store.utils.AppUtils;
import net.tatans.coeus.network.tools.TatansDb;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 */
public class DownloadQueueActivity extends BaseActivity implements DownloadController.DownloadCallback, AppInstallReceive.AppInstallCallback {

    private ListView listView;
    private String classify = "下载";
    private static String TAG = "DownloadQueueActivity";
    private DownloadQueueAdapter downloadQueueAdapter;
    private List<DownloadInfo> list;
    private TextView tv_loading_tips;
    private ArrayList<LocalAppInfo> localAppList;
    /**
     * 通过调用handler实现列表的刷新
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            downloadQueueAdapter = new DownloadQueueAdapter(DownloadQueueActivity.this, list, localAppList);
            listView.setAdapter(downloadQueueAdapter);
            if(list.size()==0){
                tv_loading_tips.setText("当前下载队列为空");
            }else {
                tv_loading_tips.setVisibility(View.GONE);
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_interface);
        listView = (ListView) findViewById(R.id.lv_test);
        tv_loading_tips = (TextView) findViewById(R.id.tv_loading_tips);
        setMyTitle("下载队列");
        setTitle(classify);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final TatansDb db = TatansDb.create(DownloadController.downloadInfoTable);
                List<DownloadInfo> mlist = db.findAll(DownloadInfo.class, "Date");
                list = new ArrayList<>();
                for (int i = 0; i < mlist.size(); i++) {
                    DownloadInfo downloadInfo = mlist.get(i);
                    if (downloadInfo.getType() != -1) {
                        list.add(downloadInfo);
                    }
                }
                Collections.reverse(list);
                DownloadController.setDownloadCallback(DownloadQueueActivity.this);
                AppInstallReceive.setAppInstallCallback(DownloadQueueActivity.this);
                localAppList = AppUtils.getLocalAppInfo(DownloadQueueActivity.this);
                Message message = Message.obtain();
                handler.sendMessage(message);
            }
        }, 300);

        final TextView header_text = (TextView) findViewById(R.id.header_text);

        header_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (header_text.getText().toString().equals("全部暂停")) {
//                    header_text.setText("全部继续");
//                    for (int i = 0; i < list.size(); i++) {
//                        final DownloadInfo downloadInfo = list.get(i);
//                        if (downloadInfo.getDownload_state().equals("下载中") || downloadInfo.getDownload_state().equals("等待下载")) {
//                            HttpHandler<File> httpHandler = AppUtils.httpHashmap.get(downloadInfo.getApp_name());
//                            if (httpHandler != null) {
//                                if (!httpHandler.isStop()) {
//                                    httpHandler.stop();
//                                    downloadQueueAdapter.updataView(listView, downloadInfo.getDownload_progress(), downloadInfo.getApp_name(), "下载");
//                                }
//                            }
//
//                        }
//                    }
//                } else {
//                    header_text.setText("全部暂停");
//                    for (int i = 0; i < list.size(); i++) {
//                        final DownloadInfo downloadInfo = list.get(i);
//                        if (!downloadInfo.getDownload_state().equals("下载成功")) {
//                            AppUtils.httpHashmap.put(downloadInfo.getApp_name(), DownloadController.startDownload(DownloadQueueActivity.this, downloadInfo));
//                            Log.d("TTTTTTTTTT", downloadInfo.getDownload_progress() + "" + downloadInfo);
//                            downloadQueueAdapter.updataView(listView, downloadInfo.getDownload_progress(), downloadInfo.getApp_name(), "暂停");
//                        }
//                    }
//                }

            }
        });
    }


    @Override
    public void onLoading(long count, long current, String appName) {
        int progress = (int) (current * 100 / count);
        if(downloadQueueAdapter!=null){
            downloadQueueAdapter.updataView(listView, progress, appName);
        }

    }

    @Override
    public void onStartCallback() {
        Log.e(TAG, "开始下载");

    }

    @Override
    public void onFailure(Throwable t, String strMsg,String appName) {
        Log.e(TAG, "下载失败");
        if(downloadQueueAdapter!=null){
                downloadQueueAdapter.updataView(listView, -101, appName);
        }
    }

    @Override
    public void onSuccess(File file) {
        Log.e(TAG, "下载成功");

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            String name = AppUtils.getAppNameByPackageName(this, packageName);
            downloadQueueAdapter.updataView(listView, -1, name);
        }
    }


}
