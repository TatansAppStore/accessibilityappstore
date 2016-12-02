package net.accessiblility.app.store.activity;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.network.tools.CrashHandler;
import net.tatans.coeus.network.tools.TatansApplication;
import net.tatans.rhea.network.event.OnClick;
import net.tatans.rhea.network.view.ContentView;

public class DemoApplication extends TatansApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init("AccessiblilityStore");
    }
}
