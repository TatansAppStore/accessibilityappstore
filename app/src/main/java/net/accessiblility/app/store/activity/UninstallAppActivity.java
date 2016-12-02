package net.accessiblility.app.store.activity;

import android.os.Bundle;
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
public class UninstallAppActivity extends BaseActivity  {

    private ListView listView;
    private String uninstall = "卸载";
    private UninstallAppAdapter uninstallAppAdapter;
    private List<LocalAppInfo> localAppList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_interface);
        listView = (ListView) findViewById(R.id.lv_test);
        TextView tv_loading_tips = (TextView) findViewById(R.id.tv_loading_tips);
        tv_loading_tips.setVisibility(View.GONE);
        setMyTitle(uninstall);
        setTitle(uninstall);
        localAppList = AppUtils.getLocalAppInfo(this);
        uninstallAppAdapter = new UninstallAppAdapter(this, localAppList);
        listView.setAdapter(uninstallAppAdapter);
    }


}
