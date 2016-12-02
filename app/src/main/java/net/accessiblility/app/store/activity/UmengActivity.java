package net.accessiblility.app.store.activity;

import android.app.Activity;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

import net.tatans.rhea.network.view.TatansIoc;

/**
 * Created by Yuriy on 2016/10/25.
 */

public class UmengActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TatansIoc.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
