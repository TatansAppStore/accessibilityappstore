package net.accessiblility.app.store.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.activity.login.LoginActivity;
import net.accessiblility.app.store.controller.Controller;
import net.accessiblility.app.store.model.ShareInfo;
import net.accessiblility.app.store.utils.ConstantValues;
import net.tatans.coeus.network.tools.HttpUtils;
import net.tatans.coeus.network.tools.TatansPreferences;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.rhea.network.http.RequestParams;
import net.tatans.rhea.network.http.ResponseInfo;
import net.tatans.rhea.network.http.callback.RequestCallBack;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FragmentShare extends BaseFragment {
    private String TAG = "FragmentShare";
    private LinearLayout ll_main;
    private boolean isPrepared;
    private final List<String> list = new ArrayList<>();
    private int[] cidArray = {4, 6, 7, 8, 15};
    private String[] sortArr = new String[]{"社交", "工具", "生活", "娱乐", "新闻"};
    private int cid;
    private TextView app_size, app_package, app_version, app_sort, app_upload;
    private EditText app_name, app_introduce;
    private ShareInfo shareInfo;
    private View view;

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.share_detail, null);
        isPrepared = true;
        lazyLoad();

        return view;
    }


    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible) {
            isPrepared = false;
            cid = 0;
            ll_main = (LinearLayout) view.findViewById(R.id.ll_main);
            TextView appChoose = (TextView) view.findViewById(R.id.app_choose);
            app_name = (EditText) view.findViewById(R.id.app_name);
            app_size = (TextView) view.findViewById(R.id.app_size);
            app_package = (TextView) view.findViewById(R.id.app_package);
            app_version = (TextView) view.findViewById(R.id.app_version);
            app_sort = (TextView) view.findViewById(R.id.app_sort);
            app_introduce = (EditText) view.findViewById(R.id.app_introduce);
            app_upload = (TextView) view.findViewById(R.id.app_upload);

            appChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String user = (String) TatansPreferences.get(ConstantValues.KEY_USER, "");
                    if (user.equals("")) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("application/vnd.android.package-archive");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(intent, 1);
                    }


                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            ll_main.setVisibility(View.GONE);
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            PackageManager pm = this.getActivity().getPackageManager();
            String path = uri.getPath();
            File file = new File(path);
            long size = file.length();
            int mSize = Integer.valueOf((int) size);
            BigDecimal apkSize = parseApkSize(mSize);
            PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES | PackageManager.GET_SIGNATURES);
            ApplicationInfo appInfo = null;
            if (info != null) {
                appInfo = info.applicationInfo;
                String packageName = appInfo.packageName;
                String appName = "";
                try {
                    appName = pm.getApplicationLabel(
                            pm.getApplicationInfo(packageName,
                                    PackageManager.GET_META_DATA)).toString();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (!appName.equals("")) {
                    app_name.setFocusable(false);
                }

                String versionName = info.versionName;
                int versionCode = info.versionCode;
                Signature[] signatures = info.signatures;
                String userName = (String) TatansPreferences.get(ConstantValues.KEY_USER,"admin");
                shareInfo = new ShareInfo(userName, apkSize + "", appName, packageName, versionName, versionCode, signatures[0].toCharsString(), cid, file);
                app_name.setText(appName.toString());
                app_size.setText("文件大小:" + apkSize + "M");
                app_package.setText("应用包名：" + packageName);
                app_version.setText("版本：" + versionName);
                app_sort.setText("分类");
                app_sort.setContentDescription("分类,点按可选择分类");
                app_introduce.setText("");
                app_sort.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("请选择")
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setSingleChoiceItems(sortArr, 0,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                app_sort.setText("分类：" + sortArr[which]);
                                                cid = cidArray[which];
                                                shareInfo.setCid(cid);
                                            }
                                        }
                                )
                                .setNegativeButton("取消", null)
                                .show();
                    }
                });

                app_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mAppName = app_name.getText().toString();
                        if (mAppName.equals("")) {
                            TatansToast.showAndCancel("请输入应用名");
                        } else {
                            shareInfo.setAppName(mAppName);
                            if (cid == 0) {
                                TatansToast.showAndCancel("请选择分类");
                            } else {
                                if (app_upload.getText().toString().equals("上传")) {
                                    upload(shareInfo);
                                    app_upload.setText("正在上传");
                                }
                            }
                        }

                    }
                });
            }
        }
    }

    private void upload(ShareInfo shareInfo) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("userName", shareInfo.getUserName());
        requestParams.addBodyParameter("sizes", shareInfo.getSize());
        requestParams.addBodyParameter("appName", shareInfo.getAppName());
        requestParams.addBodyParameter("packageName", shareInfo.getPackageName());
        requestParams.addBodyParameter("versionName", shareInfo.getVersionName());
        requestParams.addBodyParameter("versionCode", shareInfo.getVersionCode() + "");
        requestParams.addBodyParameter("sign", shareInfo.getSign());
        requestParams.addBodyParameter("cid", shareInfo.getCid() + "");
        requestParams.addBodyParameter("file", shareInfo.getFile());
        requestParams.addBodyParameter("decription",app_introduce.getText().toString());

        httpUtils.post(Controller.UpLoad, requestParams, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                app_upload.setText("上传");
                if (result.equals("2")) {
                    TatansToast.showAndCancel("此版本已经存在");
                    ll_main.setVisibility(View.VISIBLE);
                } else if (result.equals("3")) {
                    TatansToast.showAndCancel("非官方版本签名，请去官网下载");
                    ll_main.setVisibility(View.VISIBLE);
                } else if (result.equals("true")) {
                    TatansToast.showAndCancel("上传成功");
                    ll_main.setVisibility(View.VISIBLE);
                } else {
                    TatansToast.showAndCancel("上传失败");
                    ll_main.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(net.tatans.coeus.exception.HttpException e, String s) {
                app_upload.setText("上传");
                TatansToast.showAndCancel(e + s);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                int progress = (int) (current * 100 / total);
                app_upload.setText(progress + "%");

            }
        });
    }

    private BigDecimal parseApkSize(int size) {
        BigDecimal bd = new BigDecimal((double) size / (1024 * 1024));
        BigDecimal setScale = bd.setScale(2, BigDecimal.ROUND_DOWN);
        return setScale;
    }

}
