package net.accessiblility.app.store.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.support.v4.preference.PreferenceFragment;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.activity.MainActivity;
import net.accessiblility.app.store.activity.UpdateQueueActivity;
import net.accessiblility.app.store.activity.login.LoginActivity;
import net.accessiblility.app.store.activity.login.PersonalDataActivity;
import net.accessiblility.app.store.adapter.DownloadQueueAdapter;
import net.accessiblility.app.store.controller.Controller;
import net.accessiblility.app.store.controller.DownloadController;
import net.accessiblility.app.store.model.AppInfo;
import net.accessiblility.app.store.model.DownloadInfo;
import net.accessiblility.app.store.model.LocalAppInfo;
import net.accessiblility.app.store.utils.AppUtils;
import net.accessiblility.app.store.utils.ConstantValues;
import net.accessiblility.app.store.utils.PackageUtils;
import net.tatans.coeus.network.callback.HttpRequestCallBack;
import net.tatans.coeus.network.callback.HttpRequestParams;
import net.tatans.coeus.network.tools.TatansHttp;
import net.tatans.coeus.network.tools.TatansPreferences;
import net.tatans.coeus.network.tools.TatansToast;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import static android.app.Activity.RESULT_OK;

//import cn.sharesdk.framework.Platform;
//import cn.sharesdk.framework.PlatformActionListener;
//import cn.sharesdk.framework.ShareSDK;
//import cn.sharesdk.onekeyshare.OnekeyShare;
//import cn.sharesdk.sina.weibo.SinaWeibo;
//import cn.sharesdk.tencent.qq.QQ;
//import cn.sharesdk.tencent.qzone.QZone;
//import cn.sharesdk.wechat.friends.Wechat;
//import cn.sharesdk.wechat.moments.WechatMoments;
//import cn.smssdk.EventHandler;
//import cn.smssdk.SMSSDK;
//import cn.smssdk.gui.RegisterPage;


public class FragmentMine extends PreferenceFragment {
    private Preference loginPreference;
    private Preference updatePreference;
    private ArrayList<LocalAppInfo> localAppList;
    private List<DownloadInfo> updateList;

    Handler shareHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    TatansToast.showAndCancel(getResources().getString(R.string.share_to_sina_weibo_succeed));
                    break;
                case 2:
                    TatansToast.showAndCancel(getResources().getString(R.string.share_to_wechat_succeed));
                    break;
                case 3:
                    TatansToast.showAndCancel(getResources().getString(R.string.share_to_wechat_friends_succeed));
                    break;
                case 4:
                    TatansToast.showAndCancel(getResources().getString(R.string.share_to_qq_succeed));
                    break;
                case 5:
                    TatansToast.showAndCancel(getResources().getString(R.string.share_to_qqzone_succeed));
                    break;
                case 6:
                    TatansToast.showAndCancel(getResources().getString(R.string.share_cancel));
                    break;
                case 7:
                    if (msg.obj == null) {
                        TatansToast.showAndCancel(getResources().getString(R.string.share_fail_maybe_not_install_the_latest_qq));
                    } else if (msg.obj.toString().contains("400")) {
                        TatansToast.showAndCancel(getResources().getString(R.string.share_not_repeat));
                    } else {
                        TatansToast.showAndCancel(getResources().getString(R.string.share_fail));
                    }
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 通过调用handler实现列表的刷新
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (updateList.size() != 0) {
                updatePreference.setSummary("有新版本更新");
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        ShareSDK.initSDK(getActivity());
        addPreferencesFromResource(R.xml.preference);
        findPreference("share_app").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                showShare(getActivity());
                return true;
            }
        });

        loginPreference = findPreference("login_logout");
        loginPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (loginPreference.getTitle().equals("未登录")) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    Intent intent = new Intent(getActivity(), PersonalDataActivity.class);
                    startActivityForResult(intent, 2);
                }

                return true;
            }
        });

        askForUpdate();

        updatePreference = findPreference("update_app");
        updatePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (preference.getSummary().equals("有新版本更新")) {
                    AppUtils.httpHashmap.put(updateList.get(0).getApp_name(), DownloadController.startDownload(getActivity(), updateList.get(0)));
                } else {
                    TatansToast.showAndCancel("当前已经是最新版本");
                }

                return true;
            }

        });
    }

    /**
     * 请求应用更新数据
     */
    private void askForUpdate() {
        Log.e("AjaxCallBack", "");
        HttpRequestParams params = new HttpRequestParams();
        params.put("mobileModel", "");
        params.put("packagename", PackageUtils.plintPkgAndCls(PackageUtils.getResolveInfos(getActivity())));
        TatansHttp fh = new TatansHttp();

        fh.postAsync(Controller.GetAllNewApp, params, new HttpRequestCallBack() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
                Log.e("AjaxCallBack", "onLoading");
            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                Log.e("AjaxCallBack", "onSuccess");
                String StrJson = "{\"results\":" + o + "}";
                if (o.toString().startsWith("<html>")){
                 //   FragmentCommend.tv_loading_tips.setText("当前网络异常，请检查网络情况！");
                    return;
                }
                Gson gson = new Gson();
                AppInfo appinfo = gson.fromJson(StrJson, AppInfo.class);
                List<AppInfo.AI> serverList = appinfo.getResults();
                localAppList = AppUtils.getAppStoreInfo(getActivity());
                updateList = AppUtils.CompareAppUpdate(serverList, localAppList);
                Message message = Message.obtain();
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String user = (String) TatansPreferences.get(ConstantValues.KEY_USER, "");
        if (user.equals("")) {
            loginPreference.setTitle("未登录");
        } else {
            loginPreference.setTitle(user);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String userName = data.getStringExtra("USER_NAME");
                loginPreference.setTitle(userName);
            }
        }
    }

    private void showShare(Context context) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("嗨市场");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(Controller.GetShareApp);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("全球第一款无障碍应用市场");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(Controller.GetShareApp);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(Controller.GetShareApp);

        // 启动分享GUI
        oks.show(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(getActivity());
    }
//
//    //    public static FragmentMine newInstance(Bundle bundle) {
////        FragmentMine frag = new FragmentMine();
////        frag.setArguments(bundle);
////        return frag;
////    }
//    //    private View view;
////
////    @Override
////    public void onActivityCreated(Bundle savedInstanceState) {
////        super.onActivityCreated(savedInstanceState);
////    }
////
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////        view = inflater.inflate(R.layout.fragment_mine, null);
////        getFragmentManager().beginTransaction().replace(android.R.id.content,
////                new PrefsFragment()).commit();
////        return view;
////    }
////
////
////    public static class PrefsFragmentt extends PreferenceFragment {
////        @Override
////        public void onCreate(Bundle savedInstanceState) {
////            super.onCreate(savedInstanceState);
////            addPreferencesFromResource(R.xml.preference);
////        }
////    }
//    /**
//     * 显示分享
//     */
//    ShareDialog shareDialog;
//
//    private void showShareDialog(final Context context, final String shareTxt) {
//        shareDialog = new ShareDialog(context);
//        shareDialog.setCancelButtonOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                shareDialog.dismiss();
//            }
//        });
//        shareDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @SuppressWarnings("unchecked")
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                //noinspection unchecked,unchecked,unchecked,unchecked,unchecked,unchecked,unchecked,unchecked,unchecked,unchecked
//                @SuppressWarnings("unchecked") HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
//                if (item.get("ItemText").equals(context.getResources().getString(R.string.share_to_sina_weibo))) {
//                    //设置分享内容
//                    Platform.ShareParams sp = new Platform.ShareParams();
//                    sp.setText(shareTxt); //分享文本
//                    //非常重要：获取平台对象
//                    Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
//                    sinaWeibo.setPlatformActionListener(FragmentMine.this); // 设置分享事件回调
//                    //执行分享
//                    sinaWeibo.share(sp);
//                } else if (item.get("ItemText").equals(context.getResources().getString(R.string.share_to_wechat))) {
//                    //设置分享内容
//                    Platform.ShareParams sp = new Platform.ShareParams();
//                    sp.setShareType(Platform.SHARE_TEXT);//非常重要：一定要设置分享属性
////                    sp.setTitle("分享标题");  //分享标题
//                    sp.setText(shareTxt);   //分享文本
//                    //非常重要：获取平台对象
//                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
//                    wechat.setPlatformActionListener(FragmentMine.this); // 设置分享事件回调
//                    // 执行分享
//                    wechat.share(sp);
//                } else if (item.get("ItemText").equals(context.getResources().getString(R.string.share_to_wechat_friends))) {
//                    //设置分享内容
//                    Platform.ShareParams sp = new Platform.ShareParams();
//                    sp.setShareType(Platform.SHARE_TEXT); //非常重要：一定要设置分享属性
////                    sp.setTitle("分享标题");   //分享文本
//                    sp.setText(shareTxt);   //分享文本
//                    //非常重要：获取平台对象
//                    Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
//                    wechatMoments.setPlatformActionListener(FragmentMine.this); // 设置分享事件回调
//                    // 执行分享
//                    wechatMoments.share(sp);
//                } else if (item.get("ItemText").equals(context.getResources().getString(R.string.share_to_qq))) {
//                    //设置分享内容
//                    Platform.ShareParams sp = new Platform.ShareParams();
////                    sp.setTitle("分享标题");
//                    sp.setText(shareTxt);   //分享文本
//                    sp.setUrl("www.baidu.com");
//                    //非常重要：获取平台对象
//                    Platform qq = ShareSDK.getPlatform(QQ.NAME);
//                    qq.setPlatformActionListener(FragmentMine.this); // 设置分享事件回调
//                    // 执行分享
//                    qq.share(sp);
//                } else if (item.get("ItemText").equals(context.getResources().getString(R.string.share_to_qqzone))) {
//                    //设置分享内容
//                    Platform.ShareParams sp = new Platform.ShareParams();
////                    sp.setTitle("分享标题");
//                    sp.setText(shareTxt);
//                    Platform qzone = ShareSDK.getPlatform(QZone.NAME);
//                    qzone.setPlatformActionListener(FragmentMine.this); // 设置分享事件回调
//                    // 执行分享
//                    qzone.share(sp);
//                }
//                shareDialog.dismiss();
//            }
//        });
//    }
//
//
//    /**
//     * 回调的地方是子线程，进行UI操作要用handle处理
//     */
//    @Override
//    public void onCancel(Platform arg0, int arg1) {
//        shareHandler.sendEmptyMessage(6);
//    }
//
//    /**
//     * 回调的地方是子线程，进行UI操作要用handle处理
//     */
//    @Override
//    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
//        /**判断成功的平台是不是新浪微博*/
//        if (arg0.getName().equals(SinaWeibo.NAME)) {
//            shareHandler.sendEmptyMessage(1);
//        } else if (arg0.getName().equals(Wechat.NAME)) {
//            shareHandler.sendEmptyMessage(2);
//        } else if (arg0.getName().equals(WechatMoments.NAME)) {
//            shareHandler.sendEmptyMessage(3);
//        } else if (arg0.getName().equals(QQ.NAME)) {
//            shareHandler.sendEmptyMessage(4);
//        } else if (arg0.getName().equals(QZone.NAME)) {
//            shareHandler.sendEmptyMessage(5);
//        }
//    }
//
//    /**
//     * 回调的地方是子线程，进行UI操作要用handle处理
//     */
//    @Override
//    public void onError(Platform arg0, int arg1, Throwable arg2) {
//        arg2.printStackTrace();
//        Message msg = new Message();
//        msg.what = 7;
//        msg.obj = arg2.getMessage();
//        Log.d("wwwwwwwww", arg2.getMessage());
//        shareHandler.sendMessage(msg);
//    }

}
