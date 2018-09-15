package timer.anthony.com.loltimer.sevice;

import android.app.IntentService;
import android.content.Intent;

import org.apache.commons.lang3.StringUtils;

import timer.anthony.com.loltimer.kitDev.MyApplication;
import timer.anthony.com.loltimer.kitDev.SharedPreferenceUtils;
import timer.anthony.com.loltimer.kitDev.Utils.OttoEvent;
import timer.anthony.com.loltimer.kitDev.exception.ExceptionA;
import timer.anthony.com.loltimer.kitDev.log.LogUtils;
import timer.anthony.com.loltimer.model.DataDragonWSUtils;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class StartIntentService extends IntentService {

    public StartIntentService() {
        super("StartIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            String newVersion = DataDragonWSUtils.getLastDragonVersion();
            String oldVersion = SharedPreferenceUtils.getVersionDataDragon();
            //On compare les version
            if (StringUtils.compare(newVersion, oldVersion) > 0) {
                DataDragonWSUtils.loadSummoners(newVersion);
                DataDragonWSUtils.loadChampions(newVersion);
                LogUtils.w("TAG_", "Version " + newVersion + " bien sauvegardée");
                //Si cela a fonctionné on sauvegarde la derniere version
                SharedPreferenceUtils.saveVersionDataDragon(newVersion);
                MyApplication.getBus().post(OttoEvent.DATA_LOAD);
            }
            else {
                MyApplication.getBus().post(OttoEvent.NO_NEW_VERSION);
            }
        }
        catch (ExceptionA exceptionA) {
            exceptionA.printStackTrace();
            MyApplication.getBus().post(exceptionA);
        }
    }
}
