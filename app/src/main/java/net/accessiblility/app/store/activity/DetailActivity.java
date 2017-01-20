package net.accessiblility.app.store.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.adapter.DetailAdapter;
import net.accessiblility.app.store.controller.Controller;
import net.accessiblility.app.store.controller.DownloadController;
import net.accessiblility.app.store.model.AppItemInfo;
import net.accessiblility.app.store.model.CommentDto;
import net.accessiblility.app.store.model.UserDto;
import net.accessiblility.app.store.model.Version;
import net.accessiblility.app.store.model.VersionDto;
import net.accessiblility.app.store.utils.AppUtils;
import net.tatans.coeus.network.callback.HttpHandler;
import net.tatans.coeus.network.callback.HttpRequestCallBack;
import net.tatans.coeus.network.callback.HttpRequestParams;
import net.tatans.coeus.network.tools.TatansHttp;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.network.utils.DirPath;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2016/12/18.
 */

public class DetailActivity extends BaseActivity implements DownloadController.DownloadCallback, View.OnClickListener {
    private ListView listView;
    private Button btnDownload;
    private AppItemInfo.AppInfo appInfo;
    private List<CommentDto> commentList = new ArrayList<>();
    private Version version = new Version();
    private String down;
    public static String state;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            DetailAdapter detailAdapter = new DetailAdapter(DetailActivity.this, appInfo, commentList, down);
            listView.setAdapter(detailAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        DownloadController.setDownloadCallback(this);
        listView = (ListView) findViewById(R.id.lv_detail);
        btnDownload = (Button) findViewById(R.id.btn_download);
        btnDownload.setOnClickListener(this);
        Intent intent = this.getIntent();
        appInfo = (AppItemInfo.AppInfo) intent.getSerializableExtra("AppInfo");
        state = intent.getStringExtra("STATE");
        btnDownload.setText(state);
        version.setVersionCode(appInfo.getVersionCode());
        version.setVersionName(appInfo.getVersionName());
        setMyTitle(appInfo.getAppName());
        setTitle(appInfo.getAppName());
        DetailAdapter detailAdapter = new DetailAdapter(DetailActivity.this, appInfo, commentList, "0");
        listView.setAdapter(detailAdapter);
        requestDown(appInfo);
    }

    private void requestDown(final AppItemInfo.AppInfo appInfo) {
        String uri = "";
        HttpRequestParams params = new HttpRequestParams();
        uri = Controller.GetDownByPackageId;
        params.put("packageId", appInfo.getId());
        TatansHttp fh = new TatansHttp();
        fh.postAsync(uri, params, new HttpRequestCallBack() {
            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                TatansToast.showAndCancel("数据获取失败，请检查网络");
            }

            @Override
            public void onSuccess(final Object o) {
                super.onSuccess(o);
                down = o.toString();
                requestComments(appInfo);
            }
        });
    }

    /**
     * 请求应用分类数据
     */
    private void requestComments(AppItemInfo.AppInfo appInfo) {
        String uri = "";
        HttpRequestParams params = new HttpRequestParams();
        uri = Controller.getUserCommentApp;
        params.put("packageId", appInfo.getId());
        params.put("versionName", appInfo.getVersionName());
        TatansHttp fh = new TatansHttp();
        fh.postAsync(uri, params, new HttpRequestCallBack() {
            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                TatansToast.showAndCancel("数据获取失败，请检查网络");
            }

            @Override
            public void onSuccess(final Object o) {
                super.onSuccess(o);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //首先:   将数据源转化为JSONArray对象
                        try {
                            JSONArray array = new JSONArray(o.toString());
                            commentList.clear();
                            CommentDto comment = null;
                            UserDto user = null;
                            VersionDto version = null;
                            for (int i = 0; i < array.length(); i++) {         //遍历数组中的json
                                JSONObject obj = array.optJSONObject(i);//得到的obj
//                                Gson gson = new Gson();                     //使用gson去解析.
//                                Comment bean = gson.fromJson(obj.toString(), Comment.class);        //bean为json数据对应的一个bean类
//                                bean.setVersion(version);
//                                commentList.add(bean);
                                int id = obj.getInt("id");
                                String content = obj.getString("content");
                                String contentTime = obj.getString("contentTime");
                                int thumbsUp = obj.getInt("thumbsUp");
                                int score = obj.getInt("score");
                                JSONObject userObj = obj.getJSONObject("user");
                                int userId = userObj.getInt("id");
                                String userName = userObj.getString("userName");
                                String country = userObj.getString("country");
                                user = new UserDto(userId, userName, country);
                                JSONObject userVersion = obj.getJSONObject("version");
                                int versionId = userVersion.getInt("id");
                                int versionCode = userVersion.getInt("versionCode");
                                String androidAppSecId = userVersion.getString("androidAppSecId");
                                String versionName = userVersion.getString("versionName");
                                String sizes = userVersion.getString("sizes");
                                String gradle = userVersion.getString("gradle");
                                version = new VersionDto(versionId, versionCode, androidAppSecId, versionName, sizes, gradle);
                                comment = new CommentDto(id, content, contentTime, thumbsUp, score, user, version);
                                commentList.add(comment);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(0);
                    }
                }, 300);

            }
        });
    }


    @Override
    public void onLoading(long count, long current, String appName) {
        int progress = (int) (current * 100 / count);
        if (progress == 100) {
            btnDownload.setText("安装");
            state = "安装";
        } else if (progress == -1) {
            btnDownload.setText("打开");
            state = "打开";
        } else if (progress == -2) {
            btnDownload.setText("暂停");
            state = "暂停";
        } else if (progress == -101) {
            btnDownload.setText("继续");
            state = "继续";
        } else {
            btnDownload.setText(progress + "%");
        }
    }

    @Override
    public void onStartCallback() {

    }

    @Override
    public void onFailure(Throwable t, String strMsg, String appName) {
        btnDownload.setText("继续");
    }

    @Override
    public void onSuccess(File file) {

    }

    @Override
    public void onClick(View view) {
        HttpHandler<File> httpHandler = AppUtils.httpHashmap.get(appInfo.getAppName());
        String stateStr = btnDownload.getText().toString();
        if (stateStr.equals("打开")) {
            AppUtils.startAppByPackageName(DetailActivity.this, appInfo.getPackageName());
        } else if (stateStr.equals("安装")) {
            File mFile = new File(DirPath.getMyCacheDir("stores/download/", appInfo.getAppName() + ".apk"));
            if (mFile.exists()) {
                Intent install = new Intent();
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                install.setAction(android.content.Intent.ACTION_VIEW);
                install.setDataAndType(Uri.fromFile(mFile), "application/vnd.android.package-archive");
                DetailActivity.this.startActivity(install);
            }
        } else if (stateStr.equals("下载") || stateStr.equals("更新") || stateStr.equals("继续")) {
            AppUtils.httpHashmap.put(appInfo.getAppName(), DownloadController.startDownload(DetailActivity.this, DownloadController.getDownloadInfo(appInfo)));
        } else if (stateStr.contains("%")) {
            if (httpHandler != null) {
                if (!httpHandler.isStop()) {
                    btnDownload.setText("继续");
                    httpHandler.stop();
                }
            }
        }
    }
}
