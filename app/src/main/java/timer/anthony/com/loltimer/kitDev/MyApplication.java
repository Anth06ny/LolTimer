package timer.anthony.com.loltimer.kitDev;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.facebook.stetho.Stetho;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import org.greenrobot.greendao.database.Database;

import timer.anthony.com.loltimer.model.beans.dao.DaoMaster;
import timer.anthony.com.loltimer.model.beans.dao.DaoSession;
import timer.anthony.com.loltimer.sevice.StartIntentService;

/**
 * Created by Anthony on 05/04/2016.
 */
public class MyApplication extends Application {

    /**
     * Annalyser l'envoie de message
     */

    private static MyApplication instance;
    private static String versionAppli;
    private static DaoSession daoSession;
    private static Bus bus;

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
        bus = new Bus(ThreadEnforcer.ANY);
        setupDatabase();

        //Version de l'application
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionAppli = "" + pInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Stetho.initializeWithDefaults(this);

        //on lance le service
        startService(new Intent(this, StartIntentService.class));
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static String getVersionAppli() {
        return versionAppli;
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static Bus getBus() {
        return bus;
    }
}
