package net.accessiblility.app.store.adapter;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.activity.ListViewIndexActivity;
import net.accessiblility.app.store.broadcast.AppInstallReceive;
import net.accessiblility.app.store.utils.AppUtils;
import net.accessiblility.app.store.utils.ListContantsUtil;
import net.accessiblility.app.store.view.MySideBar;
import net.tatans.coeus.network.tools.TatansLog;


public class ListViewAdapter extends BaseAdapter implements OnScrollListener, SectionIndexer, AppInstallReceive.AppInstallCallback {

    Context context;
    List<String> infos;
    LayoutInflater inflater = null;
    int listViewscrollState = SCROLL_STATE_IDLE;
    MySideBar mySideBar = null;

    TextView headView = null;
    int headHeight = 0;
    private int currentPosition;

    public ListViewAdapter(Context context, MySideBar mySideBar, List<String> infos, TextView head) {
        this.context = context;
        this.infos = infos;
        this.mySideBar = mySideBar;
        inflater = LayoutInflater.from(context);
        this.headView = head;
    }

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_list_index_layout, null);
            holder.head = (TextView) convertView.findViewById(R.id.adapter_list_index_tv_head);
            holder.body = (TextView) convertView.findViewById(R.id.adapter_list_index_tv_body);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.body.setText(infos.get(position));
        int section = getSectionForPosition(position);
        String navigation = ListContantsUtil.AbcList.get(section);
        if(position==0){
            headView.setText(navigation);
        }
        if (getPositionForSection(section) == position) {
            Log.d("WWWWWWW", navigation);
            holder.head.setVisibility(View.VISIBLE);
            holder.head.setText(navigation);
        } else {
            holder.head.setVisibility(View.GONE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = position;
                String appNme = infos.get(position);
                AppUtils.uninstall(AppUtils.packageMapList.get(0).get(appNme), context);
            }
        });
        return convertView;
    }


    static class ViewHolder {
        TextView head;
        TextView body;
    }

    @Override
    public int getPositionForSection(int section) {

        if (section < 0 || section >= ListContantsUtil.indexPositionList.size()) {
            return -1;
        }
        return ListContantsUtil.indexPositionList.get(section);
    }

    @Override
    public int getSectionForPosition(int position) {

        if (position < 0 || position >= infos.size()) {
            return -1;
        }
        int index = Collections.binarySearch(ListContantsUtil.indexPositionList, position);
        return index >= 0 ? index : -index - 2;
    }

    @Override
    public Object[] getSections() {

        return ListContantsUtil.AbcList.toArray();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (getCount() > 0 && (listViewscrollState == SCROLL_STATE_TOUCH_SCROLL || listViewscrollState == SCROLL_STATE_FLING)) {
            mySideBar.setColorWhenListViewScrolling(getSectionForPosition(firstVisibleItem));
            ((ListViewIndexActivity) context).showSelectionToast(infos.get(firstVisibleItem).substring(0, 1));

            if (headHeight == 0) {
                headHeight = headView.getHeight() + 2;
            }
            int section = getSectionForPosition(firstVisibleItem + 1);
            if (ListContantsUtil.indexPositionList.get(section) == firstVisibleItem + 1) {
                View topView = view.getChildAt(1);
                if (topView.getTop() <= headHeight) {
                    ((ListViewIndexActivity) context).configHeadView(topView.getTop(), headHeight, section);
                }
            } else {
                ((ListViewIndexActivity) context).setHeadView(0, ListContantsUtil.AbcList.get(section));
                ((ListViewIndexActivity) context).setFirstVisiablePosition(firstVisibleItem);
            }
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        listViewscrollState = scrollState;
        if (scrollState == SCROLL_STATE_IDLE && getCount() > 0) {
            ((ListViewIndexActivity) context).setHeadViewVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            TatansLog.d("--------卸载成功" + packageName);
            infos.remove(currentPosition);
            notifyDataSetChanged();
        }
    }

}
