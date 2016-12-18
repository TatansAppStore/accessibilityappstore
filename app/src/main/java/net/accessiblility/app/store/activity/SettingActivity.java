package net.accessiblility.app.store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import net.accessiblility.app.store.R;

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
//            TatansToast.showAndCancel(mapply_internetPreference.isChecked()+"");

            PreferenceScreen mIntelligenceCclick = (PreferenceScreen) findPreference("preference_screen_intelligence_click");
            mIntelligenceCclick.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent =  new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                    return false;
                }
            });

        }
    }
}
