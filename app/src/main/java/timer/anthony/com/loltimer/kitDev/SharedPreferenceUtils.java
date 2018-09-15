package timer.anthony.com.loltimer.kitDev;

import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Anthony on 29/06/2017.
 */

public class SharedPreferenceUtils {

    private static SharedPreferences getSharedPreference() {
        return MyApplication.getInstance().getSharedPreferences("Register", MODE_PRIVATE);
    }

    /* ---------------------------------
    //Sauvegarde Version DataDragon
    // -------------------------------- */
    private static final String VERSION_DATA_DRAGON = "VERSION_DATA_DRAGON";

    public static String getVersionDataDragon() {

        return getSharedPreference().getString(VERSION_DATA_DRAGON, "8.16.1");
    }

    public static void saveVersionDataDragon(String url) {
        getSharedPreference().edit().putString(VERSION_DATA_DRAGON, url).apply();
    }

    /* ---------------------------------
//Sauvegarde Last pseudo
// -------------------------------- */
    private static final String LAST_PSEUDO = "LAST_PSEUDO";

    public static String getLastPseudo() {

        return getSharedPreference().getString(LAST_PSEUDO, "Skev Anka");
    }

    public static void saveLastPseudo(String pseudo) {
        getSharedPreference().edit().putString(LAST_PSEUDO, pseudo).apply();
    }
}
