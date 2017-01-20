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
public class RegisterActivityBak extends BaseActivity implements View.OnClickListener {
    private EventHandler eh;
    private TextView btnCode, btnRegister;
    private EditText etCode, etPhoneNum, etPassword, etPasswordAgain;
    private SmsObserver smsObserver;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setMyTitle("账号注册");
        setTitle("账号注册");
        setContentView(R.layout.activity_login_register);
        etPhoneNum = (EditText) findViewById(R.id.et_phone_num);
        etPassword = (EditText) findViewById(R.id.et_password);
        etPasswordAgain = (EditText) findViewById(R.id.et_password_again);
        etCode = (EditText) findViewById(R.id.et_code);
        btnCode = (TextView) findViewById(R.id.btn_code);
        btnRegister = (TextView) findViewById(R.id.btn_register);
        btnCode.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        initEventHandler();
        smsObserver = new SmsObserver(this, smsHandler);
        getContentResolver().registerContentObserver(SMS_INBOX, true, smsObserver);
    }

    public Handler smsHandler = new Handler() {
        // 这里可以进行回调的操作
        // TODO
        public void handleMessage(Message msg) {
            etCode.setText((String) msg.obj);
        }
    };

    private Uri SMS_INBOX = Uri.parse("content://sms/");

    public void getSmsFromPhone() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{"body", "address", "person"};// "_id", "address",
        // "person",, "date",
        // "type
        String where = " date >  "
                + (System.currentTimeMillis() - 10 * 60 * 1000);
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        if (null == cur)
            return;
        if (cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));// 手机号
            String name = cur.getString(cur.getColumnIndex("person"));// 联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));

            // 这里我是要获取自己短信服务号码中的验证码~~
            Pattern pattern = Pattern.compile("[0-9]{4}");
            Matcher matcher = pattern.matcher(body);//String body="测试验证码2346ds";
            if (matcher.find() && body.contains("嗨市场的验证码：")) {
                String res = matcher.group().substring(0, 4);// 获取短信的内容
                Message message = Message.obtain();
                message.obj = res;
                smsHandler.sendMessage(message);
            }
        }
    }

    class SmsObserver extends ContentObserver {

        public SmsObserver(Context context, Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            // 每当有新短信到来时，使用我们获取短消息的方法
            getSmsFromPhone();
        }
    }


    private void initEventHandler() {
        eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        register(phoneNum, password, "86");
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {

                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    Looper.prepare();
                    TatansToast.showAndCancel(((Throwable) data).getMessage());
                    Looper.loop();
                }
            }

        };
        SMSSDK.registerEventHandler(eh);
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

                    Intent intent = new Intent(RegisterActivityBak.this, FragmentMine.class);
                    intent.putExtra("USER_NAME", o.toString());
                    TatansPreferences.put(ConstantValues.KEY_USER, o.toString());
                    TatansPreferences.put(ConstantValues.KEY_PHONE, phoneNum);
                    setResult(RESULT_OK, intent);
                    RegisterActivityBak.this.finish();
                }

            }
        });
    }

    String phoneNum;

    @Override
    public void onClick(View view) {
        phoneNum = etPhoneNum.getText().toString();
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
                String code = etCode.getText().toString();
                if (!phoneNum.equals("") && PhoneFormatCheckUtils.isChinaPhoneLegal(phoneNum)) {
                    if (password.length() >= 6 && password.length() < 20) {
                        if (passwordAgain.length() >= 6 && passwordAgain.length() < 20) {
                            if (password.equals(passwordAgain)) {
                                if (code.length() == 4) {
                                    SMSSDK.submitVerificationCode("86", phoneNum, code);

                                } else {
                                    TatansToast.showAndCancel("请输入验证码");
                                }
                            } else {
                                TatansToast.showAndCancel("两次输入密码不一致");
                            }
                        } else {
                            TatansToast.showAndCancel("请再次输入6-20位密码");
                        }

                    } else {
                        TatansToast.showAndCancel("请输入6-20位密码");
                    }
                } else {
                    TatansToast.showAndCancel("请输入正确的手机号码");
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }
}
