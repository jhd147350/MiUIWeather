package studio.jhd.miuiweather;


/**
 * Created by jiahaodong on 2016/11/5-17:43.
 * 935410469@qq.com
 * https://github.com/jhd147350
 */

public class Log {
    //开启就打印，不开启不打印
    public static boolean isDebug = true;

    public static void d(String TAG, String message) {
        if (!isDebug) {
            return;
        }
        android.util.Log.d(TAG, message);
    }

    public static void e(String TAG, String message, Exception e) {
        if (!isDebug) {
            return;
        }
        android.util.Log.e(TAG, message, e);
    }

}