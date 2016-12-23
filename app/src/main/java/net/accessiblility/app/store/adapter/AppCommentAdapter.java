package net.accessiblility.app.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.model.Comment;

import java.util.List;

/**
 * Created by tatans on 2015/12/4.
 */
public class AppCommentAdapter extends BaseAdapter {

    private List<Comment> userCommentList;
    private Context context;
    private int currentPosition;

    public AppCommentAdapter(Context context, List<Comment> userCommentList) {
        this.context = context;
        this.userCommentList = userCommentList;
    }

    @Override
    public int getCount() {
        return userCommentList.size();
    }

    @Override
    public Object getItem(int position) {
        return userCommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final Comment commentInfo = userCommentList.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.comment_item, null);
            holder = new ViewHolder();
            holder.userName = (TextView) convertView.findViewById(R.id.tv_user);
            holder.appScore = (TextView) convertView.findViewById(R.id.tv_score);
            holder.appVersion = (TextView) convertView.findViewById(R.id.tv_version);
            holder.appComment = (TextView) convertView.findViewById(R.id.tv_comment);
            holder.appDate = (TextView) convertView.findViewById(R.id.tv_date);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String score = "评分：" + commentInfo.getScore() + "分     ";
        holder.userName.setText("用户：" + commentInfo.getUser().getUserName());
        holder.appScore.setText(score);
        holder.appVersion.setText("版本：" + commentInfo.getVersion().getVersionName());
        holder.appComment.setText("评论：" + commentInfo.getContent());
        holder.appDate.setText("日期：" + commentInfo.getContentTime());
        return convertView;
    }

    class ViewHolder {
        TextView userName;
        TextView appScore;
        TextView appVersion;
        TextView appComment;
        TextView appDate;
    }

}
