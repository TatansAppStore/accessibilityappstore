package net.accessiblility.app.store.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
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

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.model.ShareInfo;
import net.tatans.coeus.network.callback.HttpRequestCallBack;
import net.tatans.coeus.network.callback.HttpRequestParams;
import net.tatans.coeus.network.tools.TatansHttp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragmentShare extends BaseFragment implements View.OnClickListener {
    private String TAG = "FragmentShare";
    private ListView listView;
    private boolean isPrepared;
    private final List<String> list = new ArrayList<>();
    private int[] cidArray = {4, 6, 7, 8, 10, 14, 15};
    private int cid;
    private TextView tv_upload;
    private ShareInfo shareInfo;

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
        tv_upload = (TextView) view.findViewById(R.id.tv_upload);
        tv_loading_tips.setVisibility(View.GONE);
        tv_upload.setVisibility(View.VISIBLE);
        tv_upload.setOnClickListener(this);
        isPrepared = true;
        lazyLoad();

        return view;
    }


    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible) {
            isPrepared = false;
            list.clear();
            list.add("社交");
            list.add("工具");
            list.add("生活");
            list.add("娱乐");
            list.add("热门APP");
            list.add("测试");
            list.add("新闻");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_app_classify_item, R.id.tv_classify, list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    cid = cidArray[i];
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.putExtra("cid", cid);
                    intent.setType("application/vnd.android.package-archive");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, 1);

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
            PackageManager pm = this.getActivity().getPackageManager();
            String path = uri.getPath();
            File file = new File(path);
            long size = file.length();
            String appName = file.getName();

            PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES | PackageManager.GET_SIGNATURES);
            ApplicationInfo appInfo = null;
            if (info != null) {
                appInfo = info.applicationInfo;
                String packageName = appInfo.packageName;
                String versionName = info.versionName;
                int versionCode = info.versionCode;
                Signature[] signatures = info.signatures;
//                Toast.makeText(getActivity(), cid + "\n" + path + "\n" + packageName + "\n" + versionName + "\n" + versionCode + "\n" + signatures[0].toCharsString(), Toast.LENGTH_SHORT).show();
                shareInfo = new ShareInfo("admin", size + "", appName, packageName, versionName, versionCode, signatures[0].toCharsString(), cid, file);
                list.clear();
                list.add("用户名：admin");
                list.add("文件大小:" + size + "b");
                list.add("APP名字：" + appName);
                list.add("APP包名：" + packageName);
                list.add("版本：" + versionName);
                list.add("版本号：" + versionCode + "");
                list.add("签名：" + signatures[0].toCharsString());
                list.add("CID：" + cid + "");
                list.add("路径：" + path);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_app_classify_item, R.id.tv_classify, list);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(null);

            }

        }
    }

    @Override
    public void onClick(View view) {
        TatansHttp tatansHttp = new TatansHttp();
        HttpRequestParams httpRequestParams = new HttpRequestParams();
        try {
            httpRequestParams.put("userName", shareInfo.getUserName());
            httpRequestParams.put("size", shareInfo.getSize());
            httpRequestParams.put("appName", shareInfo.getAppName());
            httpRequestParams.put("packageName", shareInfo.getPackageName());
            httpRequestParams.put("versionName", shareInfo.getVersionName());
            httpRequestParams.put("versionCode", shareInfo.getVersionCode() + "");
            httpRequestParams.put("sign", shareInfo.getSign());
            httpRequestParams.put("cid", shareInfo.getCid() + "");
            httpRequestParams.put("file", shareInfo.getFile());
        } catch (IOException e) {
            Log.e(TAG, "onCreate: ", e);
        }
        tatansHttp.post("http://192.168.1.112:8080/android/rest/v1.0/findappsec/upload.do", httpRequestParams, new HttpRequestCallBack<String>() {
            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Log.d(TAG, "onFailure: " + strMsg);
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.d(TAG, "onSuccess: " + s);
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }
        });
    }
}
