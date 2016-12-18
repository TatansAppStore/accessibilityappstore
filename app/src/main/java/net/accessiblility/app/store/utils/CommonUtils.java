package net.accessiblility.app.store.utils;

/**
 * Created by Lenovo on 2016/12/18.
 */

public class CommonUtils {
    private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 1500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}