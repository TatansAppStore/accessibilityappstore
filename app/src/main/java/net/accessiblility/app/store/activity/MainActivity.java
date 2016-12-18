package net.accessiblility.app.store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.adapter.MainViewPagerAdapter;
import net.accessiblility.app.store.fragment.FragmentClassify;
import net.accessiblility.app.store.fragment.FragmentCommend;
import net.accessiblility.app.store.fragment.FragmentMine;
import net.accessiblility.app.store.fragment.FragmentRank;
import net.accessiblility.app.store.fragment.FragmentShare;
import net.accessiblility.app.store.viewpage.ParentViewPager;
import net.tatans.coeus.network.tools.TatansApplication;
import net.tatans.coeus.network.tools.TatansToast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    // 声明碎片即ViewPager的适配器
    private MainViewPagerAdapter mAdapter;
    // 声明一些控件
    private ParentViewPager pvp;


    private FragmentCommend fragmentRecommend;
    private FragmentClassify fragmentClassify;
    private FragmentRank fragmentRank;
    private FragmentShare fragmentShare;
    private FragmentMine fragmentMine;

    private FragmentManager mFt;
    private List<Fragment> mList;
    private LinearLayout ll_recommend, ll_category, ll_rank, ll_share, ll_mine;
    private TextView tv_recommend, tv_category, tv_rank, tv_share, tv_mine, header_text;

    // 声明控件数组
    private LinearLayout[] lls = {ll_recommend, ll_category, ll_rank,
            ll_share, ll_mine};
    private TextView[] tvs = {tv_recommend, tv_category, tv_rank, tv_share, tv_mine};
    private int[] llIds = {R.id.ll_recommend, R.id.ll_category,
            R.id.ll_rank, R.id.ll_share, R.id.ll_mine};
    private int[] tvIds = {R.id.tv_recommend, R.id.tv_category, R.id.tv_rank,
            R.id.tv_share, R.id.tv_mine};

    private int currentPager;// 记录当前页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        initFragment();
        initView();
        header_text = (TextView) findViewById(R.id.header_text);
        header_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                    startActivity(intent);
            }
        });
//        createShortCut();
        boolean isAutoUpdate = PreferenceManager.getDefaultSharedPreferences(TatansApplication.getContext()).getBoolean("checkbox_preference_wifi", true);

    }


    public void createShortCut() {
        Intent addShortCut;
        if (getIntent().getAction().equals(Intent.ACTION_CREATE_SHORTCUT)) {//判断是否需要添加快捷方式
            addShortCut = new Intent();
            addShortCut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "快捷方式");//快捷方式的名称
            Parcelable icon = Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher);//显示的图片
            addShortCut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);//快捷方式激活的activity，需要执行的intent，自己定义
            addShortCut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent());
            setResult(RESULT_OK, addShortCut);//OK，生成
        } else {//取消
            setResult(RESULT_CANCELED);
        }

        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");    //快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        shortcut.putExtra("duplicate", false); //不允许重复创建
//指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
//注意: ComponentName的第二个参数必须加上点号(.)，否则快捷方式无法启动相应程序
//	ComponentName comp = new ComponentName(this.getPackageName(), "."+this.getLocalClassName());
//	shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(this, MainActivity.class));//快捷方式的图标
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        sendBroadcast(shortcut);
    }


    /**
     * 初始化碎片，并添加到list中
     */
    public void initFragment() {
        mList = new ArrayList<Fragment>();
        fragmentRecommend = new FragmentCommend();
        fragmentClassify = new FragmentClassify();
        fragmentRank = new FragmentRank();
        fragmentShare = new FragmentShare();
        fragmentMine = new FragmentMine();

        mList.add(fragmentRecommend);
        mList.add(fragmentClassify);
        mList.add(fragmentRank);
        mList.add(fragmentShare);
        mList.add(fragmentMine);
    }

    /**
     * 初始化控件及其状态
     */
    public void initView() {
        pvp = (ParentViewPager) findViewById(R.id.vp_main);
        header_text = (TextView) findViewById(R.id.header_text);
        header_text.setText("推荐");

        for (int i = 0; i < lls.length; i++) {
            lls[i] = (LinearLayout) findViewById(llIds[i]);
            lls[i].setOnClickListener(this);
        }
        for (int i = 0; i < tvs.length; i++) {
            tvs[i] = (TextView) findViewById(tvIds[i]);
        }
        mFt = getSupportFragmentManager();
        mAdapter = new MainViewPagerAdapter(mFt, mList);
        pvp.setAdapter(mAdapter);
        setViewPagerListener();
        currentPager = 0;
        pvp.setCurrentItem(currentPager);
        tvs[currentPager].setTextColor(0xff4169E1);
    }

    /**
     * 设置外层ViewPager的监听
     */
    private void setViewPagerListener() {
        // TODO Auto-generated method stub
        pvp.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                resetButtonStateToNormal();
                currentPager = position;// 记录当前页面
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
                tvs[currentPager].setTextColor(0xff4169E1);
                header_text.setText(tvs[currentPager].getText().toString());
                TatansToast.showAndCancel(tvs[currentPager].getText().toString());
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 重置按钮状态为未点击状态
     */
    private void resetButtonStateToNormal() {
        tvs[0].setTextColor(0xffffffff);
        tvs[1].setTextColor(0xffffffff);
        tvs[2].setTextColor(0xffffffff);
        tvs[3].setTextColor(0xffffffff);
        tvs[4].setTextColor(0xffffffff);
    }


    /**
     * 按钮的点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_recommend:
                currentPager = 0;
                break;
            case R.id.ll_category:
                currentPager = 1;
                break;
            case R.id.ll_rank:
                currentPager = 2;
                break;
            case R.id.ll_share:
                currentPager = 3;
                break;
            case R.id.ll_mine:
                currentPager = 4;
                break;
        }
        pvp.setCurrentItem(currentPager);// 切换ViewPager页面
    }

}
