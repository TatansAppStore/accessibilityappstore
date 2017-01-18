package net.accessiblility.app.store.activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.activity.BaseActivity;
import net.accessiblility.app.store.controller.Controller;
import net.accessiblility.app.store.utils.ConstantValues;
import net.tatans.coeus.network.callback.HttpRequestCallBack;
import net.tatans.coeus.network.callback.HttpRequestParams;
import net.tatans.coeus.network.tools.TatansHttp;
import net.tatans.coeus.network.tools.TatansPreferences;
import net.tatans.coeus.network.tools.TatansToast;

/**
 * Created by Administrator on 2016/10/26.
 */
public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private EditText mPassword, mPasswordAgain;
    private Button btnComplete;
    private String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle("重置密码");
        setTitle("重置密码");
        setContentView(R.layout.activity_login_reset);
        phoneNum = getIntent().getStringExtra("PHONE_NUM");
        mPassword = (EditText) findViewById(R.id.et_password);
        mPasswordAgain = (EditText) findViewById(R.id.et_password_again);
        btnComplete = (Button) findViewById(R.id.btn_complete);
        btnComplete.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_complete:
                String password = mPassword.getText().toString();
                String passwordAgain = mPasswordAgain.getText().toString();

                if (password.length() >= 6 && password.length() < 20) {
                    if (passwordAgain.length() >= 6 && passwordAgain.length() < 20) {
                        if (password.equals(passwordAgain)) {
                            resetPassword(phoneNum, password);
                        } else {
                            TatansToast.showAndCancel("两次输入密码不一致");
                        }
                    } else {
                        TatansToast.showAndCancel("请再次输入6-20位密码");
                    }
                } else {
                    TatansToast.showAndCancel("请输入6-20位密码");
                }
                break;
        }

    }


    /**
     * 登录
     */

    private void resetPassword(String phoneNum, String password) {
        String uri = "";
        HttpRequestParams params = new HttpRequestParams();
        uri = Controller.ModifyPassword;
        params.put("modifyPassword", password);
        params.put("phoneNumber", phoneNum);
        TatansHttp fh = new TatansHttp();
        fh.postAsync(uri, params, new HttpRequestCallBack() {
            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                TatansToast.showAndCancel(t.getMessage() + strMsg + "_______________________________");
            }

            @Override
            public void onSuccess(final Object o) {
                super.onSuccess(o);
                String result = o.toString();
                if (result.equals("3")) {
                    TatansToast.showAndCancel("该手机号不存在");
                } else if (result.equals("true")) {
                    TatansToast.showAndCancel("修改成功");
                    TatansPreferences.put(ConstantValues.KEY_USER, "");
                    finish();
                }

            }
        });
    }
}
