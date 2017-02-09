package net.accessiblility.app.store.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.adapter.HistoryAdapter;
import net.accessiblility.app.store.broadcast.AppInstallReceive;
import net.accessiblility.app.store.controller.DownloadController;
import net.accessiblility.app.store.model.AppItemInfo;
import net.accessiblility.app.store.model.LocalAppInfo;
import net.accessiblility.app.store.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/3.
 */
public class AppHistoryListActivity extends BaseActivity implements DownloadController.DownloadCallback, AppInstallReceive.AppInstallCallback {

    private ListView listView;
    private TextView tv_loading_tips;
    private ArrayList<AppItemInfo.AppInfo> list_app_item = new ArrayList<AppItemInfo.AppInfo>();//存放后台请求获取的数据
    private String classify;
    private HistoryAdapter appAdapter;
    private ArrayList<LocalAppInfo> localAppList;
    private AppItemInfo.AppInfo appInfo;

    /**
     * 通过调用handler实现列表的刷新
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            appAdapter = new HistoryAdapter(getApplicationContext(), list_app_item, localAppList);
            listView.setAdapter(appAdapter);
            tv_loading_tips.setVisibility(View.GONE);
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_interface);
        listView = (ListView) findViewById(R.id.lv_test);
        tv_loading_tips = (TextView) findViewById(R.id.tv_loading_tips);
        Intent intent = getIntent();
        classify = intent.getStringExtra("classifyType");
        setMyTitle(classify);
        setTitle(classify);
        DownloadController.setDownloadCallback(this);
        AppInstallReceive.setAppInstallCallback(this);

        String versionJson = intent.getStringExtra("VERSION_JSON");
        appInfo = (AppItemInfo.AppInfo) intent.getSerializableExtra("AppInfo");
        try {
            JSONArray array = new JSONArray(versionJson);
            AppItemInfo.AppInfo info;
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);//得到的obj}
                int versionCode = obj.getInt("versionCode");
                String versionName = obj.getString("versionName");
                String sizes = obj.getString("sizes");
                String gradle = obj.getString("gradle");
                info = new AppItemInfo.AppInfo();
                info.setAppName(appInfo.getAppName());
                info.setPackageName(appInfo.getPackageName());
                info.setId(appInfo.getId());
                info.setVersionName(versionName);
                info.setGradle(gradle);
                info.setSize(sizes);
                info.setDecription(appInfo.getDecription());
                list_app_item.add(info);

            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    localAppList = AppUtils.getLocalAppInfo(getApplicationContext());
                    Message message = Message.obtain();
                    handler.sendMessage(message);
                }
            }).start();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            String name = AppUtils.getAppNameByPackageName(this, packageName);
            appAdapter.updataView(listView, -1, name);
        }
    }

    @Override
    public void onLoading(long count, long current, String appName) {
        int progress = (int) (current * 100 / count);
        if (appAdapter != null) {
            appAdapter.updataView(listView, progress, appName);
        }

    }

    @Override
    public void onStartCallback() {

    }

    @Override
    public void onFailure(Throwable t, String strMsg, String appName) {
        if (appAdapter != null) {
            appAdapter.updataView(listView, -101, appName);
        }
    }


    @Override
    public void onSuccess(File file) {

    }

}
