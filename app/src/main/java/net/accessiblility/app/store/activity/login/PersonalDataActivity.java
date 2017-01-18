package net.accessiblility.app.store.activity.login;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.accessiblility.app.store.R;
import net.accessiblility.app.store.activity.BaseActivity;
import net.accessiblility.app.store.utils.ConstantValues;
import net.tatans.coeus.network.tools.TatansPreferences;

import static net.tatans.coeus.core.AsyncTask.init;

public class PersonalDataActivity extends BaseActivity implements View.OnClickListener {
    private String user = (String) TatansPreferences.get(ConstantValues.KEY_USER, "");
    private String phoneNum = (String) TatansPreferences.get(ConstantValues.KEY_PHONE, "");
    private TextView mUser, mPhoneNum, mResetPassword;
    private Button mExits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_personal);
        setMyTitle("个人资料");
        setTitle("个人资料");
        mUser = (TextView) findViewById(R.id.personal_user);
        mPhoneNum = (TextView) findViewById(R.id.personal_phone);
        mPhoneNum.setText(phoneNum);
        mResetPassword = (TextView) findViewById(R.id.personal_reset);
        mExits = (Button) findViewById(R.id.personal_btn_exits);
        mUser.setOnClickListener(this);
        mPhoneNum.setOnClickListener(this);
        mResetPassword.setOnClickListener(this);
        mExits.setOnClickListener(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = (String) TatansPreferences.get(ConstantValues.KEY_USER, "");
        mUser.setText(user);
    }

    //    protected void init() {
//        final List<String> list = new ArrayList<>();
//        list.add(user);
//        list.add(phoneNum);
//        list.add("修改密码");
//        list.add("退出登录");
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_app_classify_item, R.id.tv_classify, list);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                switch (i) {
//                    case 0:
//
//                        break;
//                    case 1:
//
//                        break;
//                    case 2:
//                        Intent intentCode = new Intent(PersonalDataActivity.this, VerificationCodeActivity.class);
//                        intentCode.putExtra("TITLE", "修改密码");
//                        intentCode.putExtra("PHONE_NUM", phoneNum);
//                        startActivity(intentCode);
//                        break;
//                    case 3:
//                        TatansPreferences.put(ConstantValues.KEY_USER, "");
//                        TatansPreferences.put(ConstantValues.KEY_PHONE, "");
//                        finish();
//
//                        break;
//                }
//
//            }
//        });
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_user:
                Intent intentUser = new Intent(PersonalDataActivity.this, ModifyActivity.class);
                startActivity(intentUser);
                break;

            case R.id.personal_phone:

                break;

            case R.id.personal_reset:
                Intent intentCode = new Intent(PersonalDataActivity.this, VerificationCodeActivity.class);
                intentCode.putExtra("TITLE", "修改密码");
                intentCode.putExtra("PHONE_NUM", phoneNum);
                startActivity(intentCode);

                break;

            case R.id.personal_btn_exits:
                TatansPreferences.put(ConstantValues.KEY_USER, "");
                TatansPreferences.put(ConstantValues.KEY_PHONE, "");
                finish();

                break;

        }


    }
}
