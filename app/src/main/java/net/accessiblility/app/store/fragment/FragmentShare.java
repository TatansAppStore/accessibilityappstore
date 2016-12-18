package net.accessiblility.app.store.fragment;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.accessiblility.app.store.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentShare extends BaseFragment {
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
//        Log.d("EEEEEEEEEE", "-----------------FragmentShare");
        lazyLoad();

        return view;
    }


    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible) {
            isPrepared = false;
            final List<String> list = new ArrayList<String>();
            list.add("选择文件");
//            list.add("工具");
//            list.add("生活");
//            list.add("娱乐");
//            list.add("新闻");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_app_classify_item, R.id.tv_classify, list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    Intent intent = new Intent(getActivity(), AppListActivity.class);
//                    intent.putExtra("classifyType", list.get(i));
//                    startActivity(intent);
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent,1);

                }
            });
            Log.d("EEEEEEEEEE", "FragmentShare");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            Toast.makeText(getActivity(), uri.toString(), Toast.LENGTH_SHORT).show();
        }

    }
}
