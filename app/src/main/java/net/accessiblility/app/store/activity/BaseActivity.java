package net.accessiblility.app.store.activity;

import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import net.accessiblility.app.store.R;

/**
 * Created by Yuriy on 2016/10/25.
 */

public class BaseActivity extends UmengActivity {

    private TextView header_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        header_text = (TextView) findViewById(R.id.header_text);

    }

    protected void setMyTitle(String title) {
        header_text.setText(title);
    }



}
