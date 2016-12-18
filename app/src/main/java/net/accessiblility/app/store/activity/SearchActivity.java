package net.accessiblility.app.store.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.adapter.AppAdapter;
import net.accessiblility.app.store.controller.Controller;
import net.accessiblility.app.store.model.AppItemInfo;
import net.accessiblility.app.store.model.LocalAppInfo;
import net.accessiblility.app.store.utils.AppUtils;
import net.tatans.coeus.network.callback.HttpRequestCallBack;
import net.tatans.coeus.network.callback.HttpRequestParams;
import net.tatans.coeus.network.tools.TatansHttp;
import net.tatans.coeus.network.tools.TatansToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2016/12/18.
 */

public class SearchActivity extends BaseActivity {
    private EditText mEdittext;
    private ImageButton mSearchButton;
    private ListView listView;
    private TextView tv_loading_tips;
    private ArrayList<AppItemInfo.AppInfo> list_app_item = new ArrayList<AppItemInfo.AppInfo>();//存放后台请求获取的数据
    private AppAdapter appAdapter;
    private ArrayList<LocalAppInfo> localAppList;

    /**
     * 通过调用handler实现列表的刷新
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            appAdapter = new AppAdapter(SearchActivity.this, list_app_item, localAppList);
            listView.setAdapter(appAdapter);
            tv_loading_tips.setVisibility(View.GONE);
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setMyTitle("搜索");
        setTitle("搜索");
        mEdittext = (EditText) findViewById(R.id.et_input);
        listView = (ListView) findViewById(R.id.lv_test);
        tv_loading_tips = (TextView) findViewById(R.id.tv_loading_tips);
        mSearchButton = (ImageButton) findViewById(R.id.ib_search);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        askForDate(mEdittext.getText().toString());
                    }
                }, 300);

            }
        });
    }

    /**
     * 请求应用分类数据
     */
    private void askForDate(String appName) {
        String uri = "";
        HttpRequestParams params = new HttpRequestParams();
        uri = Controller.SearchApp;
        params.put("appName", appName);
        params.put("pageNo", 1 + "");
        params.put("mobileModel", "");
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
                        localAppList = AppUtils.getLocalAppInfo(SearchActivity.this);
                        Message message = Message.obtain();
                        handler.sendMessage(message);
                        TatansToast.showAndCancel("已搜索到"+list_app_item.size()+"条相关记录");
                    } else {
                        TatansToast.showAndCancel("抱歉，后台还未上传该类应用");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
