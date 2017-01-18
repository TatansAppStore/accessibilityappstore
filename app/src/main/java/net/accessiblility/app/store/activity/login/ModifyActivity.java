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

public class ModifyActivity extends BaseActivity implements View.OnClickListener {
    private String oldUser = (String) TatansPreferences.get(ConstantValues.KEY_USER, "");
    private String phoneNum = (String) TatansPreferences.get(ConstantValues.KEY_PHONE, "");
    private EditText mModifyUser;
    private Button mModifyComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_modify);
        setMyTitle("修改用户名");
        setTitle("修改用户名");
        mModifyUser = (EditText) findViewById(R.id.modify__user);
        mModifyComplete = (Button) findViewById(R.id.modify_btn_complete);
        mModifyComplete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.modify_btn_complete:
                String userName = mModifyUser.getText().toString();
                if (!userName.equals("")) {
                    resetUserName(oldUser, userName);
                } else {
                    TatansToast.showAndCancel("用户名不能为空");
                }
                break;

        }
    }

    /**
     * 登录
     */
    private void resetUserName(final String oldUserName, final String newUserName) {
        String uri = "";
        HttpRequestParams params = new HttpRequestParams();
        uri = Controller.ModifyUserName;
        params.put("updateUserName", newUserName);
        params.put("userName", oldUserName);
        TatansHttp fh = new TatansHttp();
        fh.postAsync(uri, params, new HttpRequestCallBack() {
            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                TatansToast.showAndCancel(t.getMessage() + strMsg + "");
            }

            @Override
            public void onSuccess(final Object o) {
                super.onSuccess(o);
                String result = o.toString();
                if (result.equals("2")) {
                    TatansToast.showAndCancel("该用户名已经存在");
                } else if (result.equals("3")) {
                    TatansToast.showAndCancel("该用户不存在，无法修改");
                } else if (result.equals("true")){
                    TatansToast.showAndCancel("用户名修改成功");
                    TatansPreferences.put(ConstantValues.KEY_USER, newUserName);
                    finish();
                }

            }
        });
    }
}
