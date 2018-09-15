package timer.anthony.com.loltimer.kitDev.log;

import android.util.Log;

public class LogUtils {

    public static void w(String tag, String text) {

        //Ajout à CrashLytics
        Log.w(tag, text);
    }
}
