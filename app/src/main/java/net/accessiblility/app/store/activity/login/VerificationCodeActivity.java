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
import android.widget.Button;
import android.widget.EditText;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.activity.BaseActivity;
import net.accessiblility.app.store.utils.CountDownTimerUtils;
import net.accessiblility.app.store.utils.PhoneFormatCheckUtils;
import net.tatans.coeus.network.tools.TatansToast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2016/10/26.
 */
public class VerificationCodeActivity extends BaseActivity implements View.OnClickListener {
    private EditText mPhoneNum, mCode;
    private Button mBtnCode, mBtnNext;
    private VerificationCodeActivity.SmsObserver smsObserver;
    private EventHandler eh;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_verification_code);
        Intent intent = getIntent();
        String title = intent.getStringExtra("TITLE");
        String phoneNum = intent.getStringExtra("PHONE_NUM");
        setMyTitle(title);
        setTitle(title);
        mPhoneNum = (EditText) findViewById(R.id.login_et_phone);
        if(title.equals("修改密码")){
            mPhoneNum.setFocusable(false);
        }
        mPhoneNum.setText(phoneNum);
        mCode = (EditText) findViewById(R.id.et_code);
        mBtnCode = (Button) findViewById(R.id.btn_code);
        mBtnCode.setOnClickListener(this);
        mBtnNext = (Button) findViewById(R.id.login_btn_next);
        mBtnNext.setOnClickListener(this);
        Button pre = (Button) findViewById(R.id.login_tv_pre);
        pre.setOnClickListener(this);
        initEventHandler();
        smsObserver = new VerificationCodeActivity.SmsObserver(this, smsHandler);
        getContentResolver().registerContentObserver(SMS_INBOX, true, smsObserver);
    }


    public Handler smsHandler = new Handler() {
        // 这里可以进行回调的操作
        // TODO
        public void handleMessage(android.os.Message msg) {
            mCode.setText((String) msg.obj);
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
                        String phoneNum = mPhoneNum.getText().toString();
                        Intent intent = new Intent(VerificationCodeActivity.this,ResetPasswordActivity.class);
                        intent.putExtra("PHONE_NUM",phoneNum);
                        startActivity(intent);
                        finish();

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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_tv_pre:
                finish();
                break;

            case R.id.btn_code:
                String phoneNum = mPhoneNum.getText().toString();
                if (!phoneNum.equals("") && PhoneFormatCheckUtils.isChinaPhoneLegal(phoneNum)) {
                    CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(mBtnCode, 60000, 1000);
                    countDownTimerUtils.start();
                    SMSSDK.getVerificationCode("86", phoneNum);
                } else {
                    TatansToast.showAndCancel("请输入正确的手机号码");
                }
                break;

            case R.id.login_btn_next:
                String phoneNumStr = mPhoneNum.getText().toString();
                String code = mCode.getText().toString();
                if (!phoneNumStr.equals("") && PhoneFormatCheckUtils.isChinaPhoneLegal(phoneNumStr)) {
                    if (code.length() == 4) {
                        SMSSDK.submitVerificationCode("86", phoneNumStr, code);
                    } else {
                        TatansToast.showAndCancel("请输入验证码");
                    }
                } else {
                    TatansToast.showAndCancel("请输入正确的手机号码");
                }
                break;
        }

    }

}
