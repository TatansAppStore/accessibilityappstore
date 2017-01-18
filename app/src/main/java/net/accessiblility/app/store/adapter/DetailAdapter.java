package net.accessiblility.app.store.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.controller.Controller;
import net.accessiblility.app.store.model.AppItemInfo;
import net.accessiblility.app.store.model.Comment;
import net.accessiblility.app.store.view.RatingBarView;
import net.tatans.coeus.network.callback.HttpRequestCallBack;
import net.tatans.coeus.network.callback.HttpRequestParams;
import net.tatans.coeus.network.tools.TatansHttp;
import net.tatans.coeus.network.tools.TatansToast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static net.accessiblility.app.store.R.id.et_comment;

/**
 * ListViewDifferentType
 *
 * @author Yuanjunhua
 *         <p>
 *         2014-7-28下午6:23:31
 */
public class DetailAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater li;
    private List<Comment> list;
    private AppItemInfo.AppInfo appInfo;
    private final int TYPE_ONE = 0, TYPE_TWO = 1, TYPE_THREE = 2, TYPE_FOUR = 3, TYPE_COUNT = 4;

    public DetailAdapter(Context context, AppItemInfo.AppInfo appInfo, List<Comment> list) {
        // TODO Auto-generated constructor stub
        this.appInfo = appInfo;
        this.list = list;
        this.context = context;
        li = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size() + 3;
    }

    /**
     * 该方法返回多少个不同的布局
     */
    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return TYPE_COUNT;
    }

    /**
     * 根据position返回相应的Item
     */
    @Override
    public int getItemViewType(int position) {
//        int po = position % 2;
//        if (po == TYPE_ONE)
//            return TYPE_ONE;
//        else
//            return TYPE_TWO;
        if (position == 0) {
            return TYPE_ONE;
        } else if (position == 1) {
            return TYPE_TWO;
        } else if (position == 2) {
            return TYPE_THREE;
        } else {
            return TYPE_FOUR;
        }
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (position == 0) {
            return appInfo;
        } else if (position == 1) {
            return appInfo;
        } else if (position == 2) {
            return appInfo;
        } else {
            return list.get(position - 3);
        }

    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder1 vh1 = null;
        ViewHolder2 vh2 = null;
        ViewHolder3 vh3 = null;
        ViewHolder4 vh4 = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_ONE:
                    vh1 = new ViewHolder1();
                    convertView = li.inflate(R.layout.detail_top_item, null);
                    vh1.appName = (TextView) convertView.findViewById(R.id.detail_tv_name);
                    vh1.appHistoryVersion = (Button) convertView.findViewById(R.id.detail_btn_history_version);
                    vh1.appScore = (TextView) convertView.findViewById(R.id.detail_tv_score);

                    vh1.appDownloadCount = (TextView) convertView.findViewById(R.id.detail_tv_download_count);
                    vh1.appSize = (TextView) convertView.findViewById(R.id.detail_tv_size);
                    convertView.setTag(vh1);
                    break;
                case TYPE_TWO:
                    vh2 = new ViewHolder2();
                    convertView = li.inflate(R.layout.detail_introduce, null);
                    vh2.appIntroduce = (TextView) convertView.findViewById(R.id.detail_tv_introduce);
                    vh2.appIntroduce.setText(appInfo.getDecription());
                    convertView.setTag(vh2);
                    break;

                case TYPE_THREE:
                    vh3 = new ViewHolder3();
                    convertView = li.inflate(R.layout.detail_comment_item, null);
                    vh3.appCommentNumber = (TextView) convertView.findViewById(R.id.detail_tv_comment_number);
                    vh3.appCommentBtn = (Button) convertView.findViewById(R.id.detail_btn_comment);
                    convertView.setTag(vh3);
                    break;

                case TYPE_FOUR:
                    vh4 = new ViewHolder4();
                    convertView = li.inflate(R.layout.detail_comment_list_item, null);
                    vh4.userName = (TextView) convertView.findViewById(R.id.detail_tv_user);
                    vh4.appScore = (TextView) convertView.findViewById(R.id.detail_tv_score);
                    vh4.appVersion = (TextView) convertView.findViewById(R.id.detail_tv_version);
                    vh4.appComment = (TextView) convertView.findViewById(R.id.detail_tv_comment);
                    vh4.appDate = (TextView) convertView.findViewById(R.id.detail_tv_date);
                    convertView.setTag(vh4);
                    break;
            }
        } else {
            switch (type) {
                case TYPE_ONE:
                    vh1 = (ViewHolder1) convertView.getTag();
                    break;
                case TYPE_TWO:
                    vh2 = (ViewHolder2) convertView.getTag();
                    break;
                case TYPE_THREE:
                    vh3 = (ViewHolder3) convertView.getTag();
                    break;
                case TYPE_FOUR:
                    vh4 = (ViewHolder4) convertView.getTag();
                    break;
            }
        }

        switch (type) {
            case TYPE_ONE:
                vh1.appName.setText(appInfo.getAppName());
                vh1.appScore.setText("");
                vh1.appDownloadCount.setText("110下载");
                vh1.appSize.setText(appInfo.getSize() + "M");
                vh1.appHistoryVersion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            case TYPE_TWO:

                break;

            case TYPE_THREE:
                vh3.appCommentNumber.setText("共有" + list.size() + "个人参与评价");
                vh3.appCommentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPasswordSetDailog((Activity) context);
                    }
                });
                break;

            case TYPE_FOUR:
                if (list.size() != 0) {
                    Comment commentInfo = list.get(position - 3);
                    vh4.userName.setText("用户：" + commentInfo.getUser().getUserName());
                    String score = "评分：" + commentInfo.getScore() + "分     ";
                    vh4.appScore.setText(score);
                    vh4.appVersion.setText("版本：" + commentInfo.getVersion().getVersionName());
                    vh4.appComment.setText("评论：" + commentInfo.getContent());
                    vh4.appDate.setText("日期：" + commentInfo.getContentTime());
                }

                break;
        }
        return convertView;
    }

    static class ViewHolder1 {
        TextView appName;
        TextView appScore;
        TextView appDownloadCount;
        TextView appSize;
        Button appHistoryVersion;
    }

    static class ViewHolder2 {
        TextView appIntroduce;
    }

    static class ViewHolder3 {
        TextView appCommentNumber;
        Button appCommentBtn;
    }

    static class ViewHolder4 {
        TextView userName;
        TextView appScore;
        TextView appVersion;
        TextView appComment;
        TextView appDate;
    }

    private int ratingScore = 5;
    private AlertDialog dialog;

    private void showPasswordSetDailog(final Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        View window = View.inflate(context, R.layout.layout_popup, null);
        // dialog.setView(view);// 将自定义的布局文件设置给dialog
        dialog.setView(window, 0, 0, 0, 0);// 设置边距为0,保证在2.x的版本上运行没问题
        dialog.show();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
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
                Toast.makeText(context, commentScore, Toast.LENGTH_SHORT).show();
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
                if (comment.equals("")||comment==null) {
                    Toast.makeText(context, "你还没有评论", Toast.LENGTH_SHORT).show();
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


}