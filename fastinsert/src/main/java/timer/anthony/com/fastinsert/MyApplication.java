package timer.anthony.com.fastinsert;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.facebook.stetho.Stetho;

import org.greenrobot.greendao.database.Database;

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

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
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
}
