package net.accessiblility.app.store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.model.AppItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2016/12/18.
 */

public class DetailActivity extends BaseActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_interface);
        setTitle("搜索");
        listView = (ListView) findViewById(R.id.lv_test);
        TextView tv_loading_tips = (TextView) findViewById(R.id.tv_loading_tips);
        tv_loading_tips.setVisibility(View.GONE);
        Intent intent = this.getIntent();
        AppItemInfo.AppInfo appInfo = (AppItemInfo.AppInfo) intent.getSerializableExtra("AppInfo");
        setMyTitle(appInfo.getAppName());
        final List<String> list = new ArrayList<>();
        list.add("详情");
        list.add("历史版本");
        list.add("评论/评分");
        list.add("用户评论");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_app_classify_item, R.id.tv_classify, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

}
