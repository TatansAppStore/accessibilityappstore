package net.accessiblility.app.store.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.adapter.AppAdapter;
import net.accessiblility.app.store.broadcast.AppInstallReceive;
import net.accessiblility.app.store.controller.Controller;
import net.accessiblility.app.store.controller.DownloadController;
import net.accessiblility.app.store.model.AppItemInfo;
import net.accessiblility.app.store.model.LocalAppInfo;
import net.accessiblility.app.store.utils.AppUtils;
import net.tatans.coeus.network.callback.HttpRequestCallBack;
import net.tatans.coeus.network.callback.HttpRequestParams;
import net.tatans.coeus.network.tools.TatansHttp;
import net.tatans.coeus.network.tools.TatansToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class FragmentRank extends BaseFragment implements DownloadController.DownloadCallback, AppInstallReceive.AppInstallCallback {

    private ListView listView;
    private TextView tv_loading_tips;
    private ArrayList<AppItemInfo.AppInfo> list_app_item = new ArrayList<AppItemInfo.AppInfo>();//存放后台请求获取的数据
    private AppAdapter appAdapter;
    private ArrayList<LocalAppInfo> localAppList;
    private boolean isPrepared;
    /**
     * 通过调用handler实现列表的刷新
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            appAdapter = new AppAdapter(getActivity(), list_app_item, localAppList);
            listView.setAdapter(appAdapter);
            tv_loading_tips.setVisibility(View.GONE);
            super.handleMessage(msg);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.list_interface, null);
        listView = (ListView) view.findViewById(R.id.lv_test);
        tv_loading_tips = (TextView) view.findViewById(R.id.tv_loading_tips);
        DownloadController.setDownloadCallback(this);
        AppInstallReceive.setAppInstallCallback(this);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible) {
            isPrepared = false;
            new AskforDateTesk().execute();
        }
    }

    class AskforDateTesk extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    askForDate();
                }
            }, 300);
            return null;
        }

    }

    /**
     * 请求应用分类数据
     */
    private void askForDate() {
        String uri = "";
        HttpRequestParams params = new HttpRequestParams();
        uri = Controller.TopChartsApp;
//        params.put("tag", "热门APP");
//        params.put("pageNo", 1 + "");
//        params.put("mobileModel", "");
        TatansHttp fh = new TatansHttp();
        fh.postAsync(uri, params, new HttpRequestCallBack() {
            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);

                try {
                    JSONObject jsonObject = new JSONObject(o + "");
                    String code = jsonObject.get("code") + "";
                    if (code.equals("true")) {
                        Gson gson = new Gson();
                        final AppItemInfo info = gson.fromJson(o + "", AppItemInfo.class);
                        list_app_item = (ArrayList<AppItemInfo.AppInfo>) info.getResult();
                        localAppList = AppUtils.getLocalAppInfo(getActivity());
                        Message message = Message.obtain();
                        handler.sendMessage(message);
                    } else {
                        TatansToast.showAndCancel("抱歉，后台还未上传该类应用");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            String name = AppUtils.getAppNameByPackageName(getActivity(), packageName);
            appAdapter.updataView(listView, -1, name);
        }
    }

    @Override
    public void onLoading(long count, long current, String appName) {
        int progress = (int) (current * 100 / count);
        if(appAdapter!=null) {
            appAdapter.updataView(listView, progress, appName);
        }

    }

    @Override
    public void onStartCallback() {

    }

    @Override
    public void onFailure(Throwable t, String strMsg, String appName) {
        if(appAdapter!=null){
            appAdapter.updataView(listView, -101, appName);
        }
    }



    @Override
    public void onSuccess(File file) {

    }
}
