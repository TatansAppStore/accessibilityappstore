package net.accessiblility.app.store.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.fragment.FragmentShare;
import net.tatans.coeus.network.tools.TatansLog;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public class ScanActivity extends BaseActivity {
    private TextView tv_loading_tips;
    private int fileNumber;
    private ListView listView;
    private int apkFileNumber;
    private List<File> list = new ArrayList<>();
    private List<String> apkNamelist = new ArrayList<>();

    /**
     * 通过调用handler实现列表的刷新
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what==0){
                tv_loading_tips.setText("已扫描" + fileNumber + "个文件夹，" + "找到"
                        + apkFileNumber + "个APK文件");
            }else if(msg.what==1){
                tv_loading_tips.setVisibility(View.GONE);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ScanActivity.this, R.layout.list_app_classify_item, R.id.tv_classify, apkNamelist);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        File file = list.get(i);
                        Intent intent = new Intent(ScanActivity.this, FragmentShare.class);
                        intent.putExtra("APK_PATH",file.getAbsolutePath());
                        setResult(RESULT_OK, intent);
                        finish();

                    }
                });

            }


            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle("智能扫描");
        setTitle("智能扫描");
        setContentView(R.layout.list_interface);
        tv_loading_tips = (TextView) findViewById(R.id.tv_loading_tips);
        listView = (ListView) findViewById(R.id.lv_test);
        new AskforDateTesk().execute();

    }



    class AskforDateTesk extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
                    getTxtFile();
                    handler.sendEmptyMessage(1);

            return null;
        }

    }


    protected void getTxtFile() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File path = Environment.getExternalStorageDirectory();// 获得SD卡路径
            File[] files = path.listFiles();// 读取
            getFileName(files);
        }}


    private void getFileName(File[] files) {
        if (files != null) {
            for (File file : files) {
                if(file!=null){
                    fileNumber++;

                    if (file.isDirectory()) {
                        TatansLog.i("zeng", "若是文件目录。继续读1" + file.getName().toString()
                                + file.getPath().toString());
                        getFileName(file.listFiles());

                    } else {
                        String fileName = file.getName();
                        if (fileName.endsWith(".apk")) {
                            try {
                                apkFileNumber++;
                                list.add(file);
                                apkNamelist.add(file.getName().replace(".apk",""));
                            } catch (Exception e) {

                            }

                        }

                    }
                }
                if (fileNumber % 100 == 0) {
                    handler.sendEmptyMessage(0);
                }
            }

        }
    }

}
