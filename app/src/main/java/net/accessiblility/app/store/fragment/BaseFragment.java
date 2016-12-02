package net.accessiblility.app.store.fragment;


import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

import net.tatans.rhea.network.view.TatansIoc;

/**
 * Created by Administrator on 2016/10/25.
 */
public class BaseFragment extends LazyLoadFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        TatansIoc.inject(getActivity());
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }

    @Override
    protected void lazyLoad() {

    }
}
