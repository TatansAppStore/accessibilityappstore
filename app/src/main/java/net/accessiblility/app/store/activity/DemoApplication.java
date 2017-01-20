package net.accessiblility.app.store.activity;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.network.tools.CrashHandler;
import net.tatans.coeus.network.tools.TatansApplication;
import net.tatans.rhea.network.event.OnClick;
import net.tatans.rhea.network.view.ContentView;

import cn.smssdk.SMSSDK;

public class DemoApplication extends TatansApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        SMSSDK.initSDK(this, "1a00cc4c683f8", "64e27b5bd4f9e7c3e087b842eca5354a");
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init("AccessiblilityStore");
    }
}
