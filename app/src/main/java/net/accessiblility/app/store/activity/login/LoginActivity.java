package net.accessiblility.app.store.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.activity.BaseActivity;

/**
 * Created by Administrator on 2016/10/26.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle("账号登录");
        setTitle("账号登录");
        setContentView(R.layout.activity_login_login);
        TextView textView = (TextView) findViewById(R.id.tv_fast_register);
        textView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_fast_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);

                break;


        }

    }
}
