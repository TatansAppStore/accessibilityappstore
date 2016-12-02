package net.accessiblility.app.store.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.activity.AppListActivity;

import java.util.ArrayList;
import java.util.List;

public class FragmentRank extends BaseFragment {
    ListView listView;
    private boolean isPrepared;
    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_interface, null);
        listView = (ListView) view.findViewById(R.id.lv_test);
        TextView tv_loading_tips = (TextView) view.findViewById(R.id.tv_loading_tips);
        tv_loading_tips.setVisibility(View.GONE);
        isPrepared = true;
        lazyLoad();
//        Log.d("EEEEEEEEEE", "-----------------FragmentRank");
        return view;
    }



    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible) {
            isPrepared = false;
            final List<String> list = new ArrayList<String>();
            list.add("社交");
            list.add("工具");
            list.add("生活");
            list.add("娱乐");
            list.add("新闻");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_app_classify_item, R.id.tv_classify, list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), AppListActivity.class);
                    intent.putExtra("classifyType", list.get(i));
                    startActivity(intent);
                }
            });
            Log.d("EEEEEEEEEE", "FragmentRank");
        }
    }
}
