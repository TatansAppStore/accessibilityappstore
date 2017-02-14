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

import com.google.gson.Gson;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.adapter.DownloadQueueAdapter;
import net.accessiblility.app.store.broadcast.AppInstallReceive;
import net.accessiblility.app.store.controller.Controller;
import net.accessiblility.app.store.controller.DownloadController;
import net.accessiblility.app.store.model.AppInfo;
import net.accessiblility.app.store.model.DownloadInfo;
import net.accessiblility.app.store.model.LocalAppInfo;
import net.accessiblility.app.store.utils.AppUtils;
import net.accessiblility.app.store.utils.PackageUtils;
import net.tatans.coeus.network.callback.HttpRequestCallBack;
import net.tatans.coeus.network.callback.HttpRequestParams;
import net.tatans.coeus.network.tools.TatansHttp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 */
public class UpdateQueueActivity extends BaseActivity implements DownloadController.DownloadCallback, AppInstallReceive.AppInstallCallback {

    private ListView listView;
    private String title = "应用升级";
    private static String TAG = "UpdateQueueActivity";
    private DownloadQueueAdapter downloadQueueAdapter;
    private TextView tv_loading_tips;
    private ArrayList<LocalAppInfo> localAppList;
    private List<DownloadInfo> updateList;
    /**
     * 通过调用handler实现列表的刷新
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            downloadQueueAdapter = new DownloadQueueAdapter(UpdateQueueActivity.this, updateList, localAppList);
            listView.setAdapter(downloadQueueAdapter);
            if(updateList.size()==0){
                tv_loading_tips.setText("当前没有需要升级的应用");
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
        setMyTitle(title);
        setTitle(title);
        DownloadController.setDownloadCallback(this);
        AppInstallReceive.setAppInstallCallback(this);
        askForUpdate();
    }


    /**
     * 请求应用更新数据
     */
    private void askForUpdate() {
        Log.e("AjaxCallBack", "");
        HttpRequestParams params = new HttpRequestParams();
        params.put("mobileModel", "");
        params.put("packagename", PackageUtils.plintPkgAndCls(PackageUtils.getResolveInfos(this)));
        TatansHttp fh = new TatansHttp();

        fh.postAsync(Controller.GetAllNewApp, params, new HttpRequestCallBack() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
                Log.e("AjaxCallBack", "onLoading");
            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                Log.e("AjaxCallBack", "onSuccess");
                String StrJson = "{\"results\":" + o + "}";
                Gson gson = new Gson();
                AppInfo appinfo = gson.fromJson(StrJson, AppInfo.class);
                List<AppInfo.AI> serverList = appinfo.getResults();
                localAppList = AppUtils.getLocalAppInfo(UpdateQueueActivity.this);
                updateList = AppUtils.CompareAppUpdate(serverList, localAppList);
                Message message = Message.obtain();
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                tv_loading_tips.setText("请求数据失败，请检查网络");
            }
        });
    }

    @Override
    public void onLoading(long count, long current, String appName) {
        int progress = (int) (current * 100 / count);
        Log.e(TAG, "百分之" + progress);
        if (downloadQueueAdapter != null) {
            downloadQueueAdapter.updataView(listView, progress, appName);
        }
    }

    @Override
    public void onStartCallback() {
        Log.e(TAG, "开始下载");

    }

    @Override
    public void onFailure(Throwable t, String strMsg, String appName) {
        if (downloadQueueAdapter != null) {
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
