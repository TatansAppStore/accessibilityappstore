package net.accessiblility.app.store.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.activity.BaseActivity;
import net.accessiblility.app.store.controller.Controller;
import net.accessiblility.app.store.fragment.FragmentMine;
import net.accessiblility.app.store.utils.ConstantValues;
import net.accessiblility.app.store.utils.PhoneFormatCheckUtils;
import net.tatans.coeus.network.callback.HttpRequestCallBack;
import net.tatans.coeus.network.callback.HttpRequestParams;
import net.tatans.coeus.network.tools.TatansHttp;
import net.tatans.coeus.network.tools.TatansPreferences;
import net.tatans.coeus.network.tools.TatansToast;

/**
 * Created by Administrator on 2016/10/26.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText mPhoneNum, mPassword;
    private Button mBtnlogin;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle("账号登录");
        setTitle("账号登录");
        setContentView(R.layout.activity_login_login);
        mPhoneNum = (EditText) findViewById(R.id.login_et_phone);
        mPassword = (EditText) findViewById(R.id.login_et_password);
        mBtnlogin = (Button) findViewById(R.id.login_btn_login);
        TextView register = (TextView) findViewById(R.id.login_tv_fast_register);
        TextView forgotPassword = (TextView) findViewById(R.id.login_tv_forgot_password);
        register.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        mBtnlogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_tv_fast_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.login_tv_forgot_password:
                Intent intentCode = new Intent(this, VerificationCodeActivity.class);
                intentCode.putExtra("TITLE", "忘记密码");
                intentCode.putExtra("PHONE_NUM", mPhoneNum.getText().toString());
                startActivity(intentCode);
                finish();
                break;

            case R.id.login_btn_login:
                String phoneNum = mPhoneNum.getText().toString();
                String password = mPassword.getText().toString();
                int length = password.length();
                if (!phoneNum.equals("") && PhoneFormatCheckUtils.isChinaPhoneLegal(phoneNum)) {
                    if (length >= 6 && length < 20) {
                        login(phoneNum, password);
                    } else {
                        TatansToast.showAndCancel("请输入6-20位密码");
                    }
                } else {
                    TatansToast.showAndCancel("请输入正确的手机号码");
                }
                break;
        }

    }


    /**
     * 登录
     */
    private void login(final String phoneNum, String password) {
        String uri = "";
        HttpRequestParams params = new HttpRequestParams();
        uri = Controller.LoginUser;
        params.put("phoneNumber", phoneNum);
        params.put("password", password);
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
                String userName = o.toString();
                if (userName.equals("false")) {
                    TatansToast.showAndCancel("用户名或密码错误");
                } else {
                    TatansToast.showAndCancel(userName + "登陆成功");
                    Intent intent = new Intent(LoginActivity.this, FragmentMine.class);
                    intent.putExtra("USER_NAME", userName);
                    TatansPreferences.put(ConstantValues.KEY_USER, userName);
                    TatansPreferences.put(ConstantValues.KEY_PHONE, phoneNum);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });
    }
}
