package net.accessiblility.app.store.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.adapter.UninstallAppAdapter;
import net.accessiblility.app.store.model.LocalAppInfo;
import net.accessiblility.app.store.utils.AppUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 */
public class UninstallAppActivity extends BaseActivity {

    private ListView listView;
    private String uninstall = "卸载";
    TextView tv_loading_tips;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            uninstallAppAdapter = new UninstallAppAdapter(UninstallAppActivity.this, localAppList);
//            listView.setAdapter(uninstallAppAdapter);
            List<LocalAppInfo> localAppList = (List<LocalAppInfo>) msg.obj;
            UninstallAppAdapter uninstallAppAdapter = new UninstallAppAdapter(UninstallAppActivity.this, localAppList);
            listView.setAdapter(uninstallAppAdapter);
            tv_loading_tips.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_interface);
        listView = (ListView) findViewById(R.id.lv_test);
        tv_loading_tips = (TextView) findViewById(R.id.tv_loading_tips);
        setMyTitle(uninstall);
        setTitle(uninstall);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                List<LocalAppInfo> localAppList = AppUtils.getLocalAppInfo(UninstallAppActivity.this);//耗时工作
                Message msg = handler.obtainMessage(0, localAppList);//通过handler得到消息，该消息的标识为0，消息内容是data
                handler.sendMessage(msg);//发送

            }
        }, 100);


    }


}
