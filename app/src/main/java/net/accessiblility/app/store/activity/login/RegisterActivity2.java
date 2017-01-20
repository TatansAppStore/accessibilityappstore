package net.accessiblility.app.store.activity.login;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.activity.BaseActivity;
import net.accessiblility.app.store.controller.Controller;
import net.accessiblility.app.store.fragment.FragmentMine;
import net.accessiblility.app.store.utils.ConstantValues;
import net.accessiblility.app.store.utils.CountDownTimerUtils;
import net.accessiblility.app.store.utils.PhoneFormatCheckUtils;
import net.tatans.coeus.network.callback.HttpRequestCallBack;
import net.tatans.coeus.network.callback.HttpRequestParams;
import net.tatans.coeus.network.tools.TatansHttp;
import net.tatans.coeus.network.tools.TatansPreferences;
import net.tatans.coeus.network.tools.TatansToast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2016/10/26.
 */
public class RegisterActivity2 extends BaseActivity implements View.OnClickListener {
    private EventHandler eh;
    private TextView btnCode, btnRegister;
    private EditText etPassword, etPasswordAgain;
    private String password;
    String phoneNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setMyTitle("输入密码");
        setTitle("输入密码");
        setContentView(R.layout.activity_login_register2);
        etPassword = (EditText) findViewById(R.id.et_password);
        etPasswordAgain = (EditText) findViewById(R.id.et_password_again);
        btnCode = (TextView) findViewById(R.id.btn_code);
        btnRegister = (TextView) findViewById(R.id.btn_register);
        btnCode.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        phoneNum=getIntent().getStringExtra("phoneNum");
    }


    /**
     * 注册
     */
    private void register(final String phoneNum, String password, String country) {
        String uri = "";
        HttpRequestParams params = new HttpRequestParams();
        uri = Controller.RegisterUser;
        params.put("phoneNumber", phoneNum);
        params.put("password", password);
        params.put("country", country);
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
                if (o.toString().equals("2")) {
                    TatansToast.showAndCancel("该手机号已经注册");
                } else {
                    TatansToast.showAndCancel(o.toString()+"注册成功");
                    Intent intent = new Intent(RegisterActivity2.this, FragmentMine.class);
                    intent.putExtra("USER_NAME", o.toString());
                    TatansPreferences.put(ConstantValues.KEY_USER, o.toString());
                    TatansPreferences.put(ConstantValues.KEY_PHONE, phoneNum);
                    setResult(RESULT_OK, intent);
                    RegisterActivity2.this.finish();
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_code:
                if (!phoneNum.equals("") && PhoneFormatCheckUtils.isChinaPhoneLegal(phoneNum)) {
                    CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(btnCode, 60000, 1000);
                    countDownTimerUtils.start();
                    SMSSDK.getVerificationCode("86", phoneNum);
                } else {
                    TatansToast.showAndCancel("请输入正确的手机号码");
                }
                break;

            case R.id.btn_register:
                password = etPassword.getText().toString();
                String passwordAgain = etPasswordAgain.getText().toString();
                    if (password.length() >= 6 && password.length() < 20) {
                        if (passwordAgain.length() >= 6 && passwordAgain.length() < 20) {
                            if (password.equals(passwordAgain)) {
                                    //提交验证码成功
                                    register(phoneNum, password, "86");
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

}
