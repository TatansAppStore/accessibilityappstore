package net.accessiblility.app.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.model.AppItemInfo;

import java.util.List;

/**
 * ListViewDifferentType
 *
 * @author Yuanjunhua
 *         <p>
 *         2014-7-28下午6:23:31
 */
public class DetailAdapter extends BaseAdapter {

    private LayoutInflater li;
    private List<String> list;
    private AppItemInfo.AppInfo appInfo;
    private final int TYPE_ONE = 0, TYPE_TWO = 1, TYPE_THREE = 2, TYPE_FOUR = 3, TYPE_COUNT = 4;

    public DetailAdapter(Context context,AppItemInfo.AppInfo appInfo, List<String> list) {
        // TODO Auto-generated constructor stub
        this.appInfo = appInfo;
        this.list = list;
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
            return list.get(position);
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
                    vh1.appScore = (TextView) convertView.findViewById(R.id.detail_tv_score);
                    vh1.appDownloadCount = (TextView) convertView.findViewById(R.id.detail_tv_download_count);
                    vh1.appSize = (TextView) convertView.findViewById(R.id.detail_tv_size);
                    convertView.setTag(vh1);
                    break;
                case TYPE_TWO:
                    vh2 = new ViewHolder2();
                    convertView = li.inflate(R.layout.detail_introduce, null);
                    vh2.appIntroduce = (TextView) convertView.findViewById(R.id.detail_tv_introduce);
                    convertView.setTag(vh2);
                    break;

                case TYPE_THREE:
                    vh3 = new ViewHolder3();
                    convertView = li.inflate(R.layout.detail_assess_item, null);
                    vh3.appAssessNumber = (TextView) convertView.findViewById(R.id.detail_tv_assess_number);
                    vh3.appAassessBtn = (Button) convertView.findViewById(R.id.detail_btn_assess);
                    convertView.setTag(vh3);
                    break;

                case TYPE_FOUR:
                    vh4 = new ViewHolder4();
                    convertView = li.inflate(R.layout.detail_assess_list_item, null);
                    vh4.userName = (TextView) convertView.findViewById(R.id.detail_tv_user);
                    vh4.appScore = (TextView) convertView.findViewById(R.id.detail_tv_score);
                    vh4.appVersion = (TextView) convertView.findViewById(R.id.detail_tv_version);
                    vh4.appAssess = (TextView) convertView.findViewById(R.id.detail_tv_assess);
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
//                    vh2 = (ViewHolder2) convertView.getTag();
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
                vh1.appScore.setText("5分");
                vh1.appDownloadCount.setText("110下载");
                vh1.appSize.setText(appInfo.getSize()+"M");

            case TYPE_TWO:
//                vh2.appIntroduce.setText(appInfo.getDecription());
                break;

            case TYPE_THREE:
                vh3.appAssessNumber.setText("2024个人参与评价");
                break;

            case TYPE_FOUR:
                vh4.userName.setText("用户1");
                vh4.appScore.setText("5分");
                vh4.appVersion.setText("版本：1.1.1");
                vh4.appAssess.setText("评论：不错啊");
                vh4.appDate.setText("日期：2016.1.6" );
                break;
        }
        return convertView;
    }

    static class ViewHolder1 {
        TextView appName;
        TextView appScore;
        TextView appDownloadCount;
        TextView appSize;
    }

    static class ViewHolder2 {
        TextView appIntroduce;

    }

    static class ViewHolder3 {
        TextView appAssessNumber;
        Button appAassessBtn;
    }

    static class ViewHolder4 {
        TextView userName;
        TextView appScore;
        TextView appVersion;
        TextView appAssess;
        TextView appDate;
    }


}