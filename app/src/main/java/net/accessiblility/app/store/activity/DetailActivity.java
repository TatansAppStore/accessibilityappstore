package net.accessiblility.app.store.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.adapter.AppCommentAdapter;
import net.accessiblility.app.store.adapter.DetailAdapter;
import net.accessiblility.app.store.controller.Controller;
import net.accessiblility.app.store.controller.DownloadController;
import net.accessiblility.app.store.model.AppItemInfo;
import net.accessiblility.app.store.model.Comment;
import net.accessiblility.app.store.model.Version;
import net.accessiblility.app.store.view.RatingBarView;
import net.tatans.coeus.network.callback.HttpRequestCallBack;
import net.tatans.coeus.network.callback.HttpRequestParams;
import net.tatans.coeus.network.tools.TatansHttp;
import net.tatans.coeus.network.tools.TatansToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static net.accessiblility.app.store.R.id.et_comment;

/**
 * Created by Lenovo on 2016/12/18.
 */

public class DetailActivity extends BaseActivity  implements DownloadController.DownloadCallback{
    private ListView listView, mCommentListView;
    private AppItemInfo.AppInfo appInfo;
    List<Comment> commentList = new ArrayList<>();
    Version version = new Version();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AppCommentAdapter appCommentAdapter = new AppCommentAdapter(DetailActivity.this, commentList);
            mCommentListView.setAdapter(appCommentAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        DownloadController.setDownloadCallback(this);
        listView = (ListView) findViewById(R.id.lv_detail);
        Intent intent = this.getIntent();
        appInfo = (AppItemInfo.AppInfo) intent.getSerializableExtra("AppInfo");
        version.setVersionCode(appInfo.getVersionCode());
        version.setVersionName(appInfo.getVersionName());
        setMyTitle(appInfo.getAppName());
        setTitle(appInfo.getAppName());
        final List<String> list = new ArrayList<>();
        list.add(appInfo.getDecription());
        list.add("下载");
        list.add("历史版本");
        list.add("评论/评分");
        list.add("用户评论");
        list.add("用户评论");
        list.add("用户评论");
        list.add("用户评论");
        list.add("用户评论");
        list.add("用户评论");
        list.add("用户评论");
        list.add("用户评论");
        DetailAdapter detailAdapter = new DetailAdapter(this,appInfo,list);

        listView.setAdapter(detailAdapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                switch (i) {
//                    case 0:
//                        break;
//                    case 1:
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                AppUtils.httpHashmap.put(appInfo.getAppName(), DownloadController.startDownload(DetailActivity.this, DownloadController.getDownloadInfo(appInfo)));
//                            }
//                        }).start();
//
//                        break;
//                    case 2:
//
//                        break;
//                    case 3:
//                        showPasswordSetDailog();
//                        break;
//
//                }
//
//
//            }
//        });
//        requestComments(appInfo);
    }

    private int ratingScore = 5;
    private AlertDialog dialog;

    private void showPasswordSetDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.create();
        View window = View.inflate(this, R.layout.layout_popup, null);
        // dialog.setView(view);// 将自定义的布局文件设置给dialog
        dialog.setView(window, 0, 0, 0, 0);// 设置边距为0,保证在2.x的版本上运行没问题
        dialog.show();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        lp.width = width;
        dialog.getWindow().setAttributes(lp);

        final TextView tvDescribe = (TextView) window
                .findViewById(R.id.tv_describe);
        final RatingBarView originRatingbar = (RatingBarView) window.findViewById(R.id.custom_ratingbar);
        originRatingbar.setStar(5, true);
        originRatingbar.setOnRatingListener(new RatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int RatingScore) {
                ratingScore = RatingScore;
                String commentScore = "";
                switch (RatingScore) {
                    case 1:
                        commentScore = "一星差评，真不能忍";
                        break;
                    case 2:
                        commentScore = "一星差评，真不能忍";
                        break;
                    case 3:
                        commentScore = "三星中评，再接再厉";
                        break;
                    case 4:
                        commentScore = "四星好评，心满意足";
                        break;
                    case 5:
                        commentScore = "五星好评，极力推荐";
                        break;
                }
                tvDescribe.setText(commentScore);
                Toast.makeText(DetailActivity.this, commentScore, Toast.LENGTH_SHORT).show();
            }
        });
        final EditText etComment = (EditText) window
                .findViewById(et_comment);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) etComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(etComment, 0);
                           }
                       },
                100);
        InputMethodManager inputManager =
                (InputMethodManager) etComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(etComment, 0);
        Button ok = (Button) window.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String comment = etComment.getText().toString();
                if (comment.equals("")) {
                    Toast.makeText(DetailActivity.this, "你还没有评论" + ratingScore, Toast.LENGTH_SHORT).show();
                } else {
                    submitComments(appInfo, comment, ratingScore);
                }

            }
        });

        // 关闭alert对话框架
        Button cancel = (Button) window.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    /**
     * 请求应用分类数据
     */
    private void submitComments(AppItemInfo.AppInfo appInfo, String comment, int score) {
        String uri = "";
        HttpRequestParams params = new HttpRequestParams();
        uri = Controller.setUserCommentApp;
        params.put("userName", "admin");
        params.put("packageId", appInfo.getId());
        params.put("versionName", appInfo.getVersionName());
        params.put("comment", comment);
        params.put("score", score + "");
        TatansHttp fh = new TatansHttp();
        fh.postAsync(uri, params, new HttpRequestCallBack() {
            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                TatansToast.showAndCancel("评论提交失败" + strMsg.toString());
            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String isSucceed = o.toString();
                if (isSucceed.equals("true")) {
                    TatansToast.showAndCancel("评论提交成功");
                    dialog.cancel();
                }


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
                TatansToast.showAndCancel("评论获取失败" + strMsg.toString());
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
                            for (int i = 0; i < array.length(); i++) {         //遍历数组中的json
                                JSONObject obj = array.optJSONObject(i);//得到的obj
                                Gson gson = new Gson();                     //使用gson去解析.
                                Comment bean = gson.fromJson(obj.toString(), Comment.class);        //bean为json数据对应的一个bean类
                                bean.setVersion(version);
                                commentList.add(bean);
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

    }

    @Override
    public void onStartCallback() {

    }

    @Override
    public void onFailure(Throwable t, String strMsg, String appName) {

    }

    @Override
    public void onSuccess(File file) {

    }
}
