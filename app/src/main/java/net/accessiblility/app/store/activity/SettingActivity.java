package net.accessiblility.app.store.activity;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;

import net.accessiblility.app.store.R;
import net.tatans.coeus.network.tools.TatansToast;

/**
 * Created by Administrator on 2016/10/26.
 */
public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();
        setMyTitle("设置");
        setTitle("设置");
    }

    public static class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.mine_setting_preference);
            CheckBoxPreference mapply_internetPreference = (CheckBoxPreference) findPreference("checkbox_preference_wifi");
            TatansToast.showAndCancel(mapply_internetPreference.isChecked()+"");
        }
    }
}
