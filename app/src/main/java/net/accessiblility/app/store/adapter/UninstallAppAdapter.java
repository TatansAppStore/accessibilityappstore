package net.accessiblility.app.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.broadcast.AppInstallReceive;
import net.accessiblility.app.store.model.LocalAppInfo;
import net.accessiblility.app.store.utils.AppUtils;
import net.tatans.coeus.network.tools.TatansLog;

import java.util.List;

/**
 * Created by tatans on 2015/12/4.
 */
public class UninstallAppAdapter extends BaseAdapter implements AppInstallReceive.AppInstallCallback {

    private List<LocalAppInfo> localAppList;
    private Context context;
    private int currentPosition;

    public UninstallAppAdapter(Context context, List<LocalAppInfo> localAppList) {
        this.context = context;
        this.localAppList = localAppList;
        AppInstallReceive.setAppInstallCallback(this);
    }

    @Override
    public int getCount() {
        return localAppList.size();
    }

    @Override
    public Object getItem(int position) {
        return localAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final LocalAppInfo appInfo = localAppList.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_uninstall_app_item, null);
            holder = new ViewHolder();
            holder.appDetails = (LinearLayout) convertView.findViewById(R.id.ll_app_details);
            holder.appName = (TextView) convertView.findViewById(R.id.tv_app_name);
            holder.appIcon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
            holder.appVersion = (TextView) convertView.findViewById(R.id.tv_app_version);
            holder.appSize = (TextView) convertView.findViewById(R.id.tv_app_size);
            holder.appState = (TextView) convertView.findViewById(R.id.tv_app_state);
            holder.appInfo = (TextView) convertView.findViewById(R.id.tv_app_info);
            holder.appUninstall = (TextView) convertView.findViewById(R.id.tv_app_uninstall);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.appName.setText(appInfo.getAppName());
        holder.appIcon.setImageDrawable(appInfo.getAppIcon());
        holder.appState.setText("版本号：" + appInfo.getVersionName());
//        holder.appSize.setText(appInfo.getSize() + "M");
        holder.appUninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition = position;
                AppUtils.uninstall(appInfo.packageName, context);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView appIcon;
        TextView appName;
        TextView appVersion;
        TextView appSize;
        TextView appState;
        TextView appInfo;
        TextView appUninstall;
        LinearLayout appDetails;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            TatansLog.d("--------卸载成功" + packageName);
            localAppList.remove(currentPosition);
            notifyDataSetChanged();
        }
    }

}
