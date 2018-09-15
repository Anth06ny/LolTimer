package timer.anthony.com.loltimer;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import timer.anthony.com.loltimer.kitDev.MyApplication;
import timer.anthony.com.loltimer.kitDev.SharedPreferenceUtils;
import timer.anthony.com.loltimer.kitDev.Utils.OttoEvent;
import timer.anthony.com.loltimer.kitDev.exception.ExceptionA;
import timer.anthony.com.loltimer.kitDev.log.LogUtils;
import timer.anthony.com.loltimer.model.WSUtils;
import timer.anthony.com.loltimer.model.beans.GameBean;

public class MainActivity extends AppCompatActivity {

    private TextView tvResultat;
    private EditText etPseudo;
    private ProgressDialog pd;
    private CardView cvPseudo;
    private Chronometer chronometer;
    private LinearLayout ll_top;

    //outils
    private MonAT monAt;

    //Data
    private String message;
    GameBean gameBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResultat = findViewById(R.id.tvResultat);
        etPseudo = findViewById(R.id.etPseudo);
        cvPseudo = findViewById(R.id.cvPseudo);
        chronometer = findViewById(R.id.chronometer);
        ll_top = findViewById(R.id.ll_top);

        MyApplication.getBus().register(this);

        gameBean = null;

        etPseudo.setText(SharedPreferenceUtils.getLastPseudo());

        refreshScreen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MyApplication.getBus().unregister(this);
        if (pd != null) {
            pd.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Version").setIcon(R.mipmap.ic_info).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            Toast.makeText(this, "Version  : " + SharedPreferenceUtils.getVersionDataDragon(), Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /* ---------------------------------
    // Click
    // -------------------------------- */

    public void onClick(View view) {
        if (monAt == null || monAt.getStatus() == AsyncTask.Status.FINISHED) {
            message = null;
            refreshScreen();
            SharedPreferenceUtils.saveLastPseudo(etPseudo.getText().toString());
            monAt = new MonAT(etPseudo.getText().toString());
            monAt.execute();
        }
    }

    /* ---------------------------------
    // Otto
    // -------------------------------- */
    @Subscribe
    public void onOttoEvent(final OttoEvent ottoEvent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (ottoEvent) {
                    case DATA_LOAD:
                        Toast.makeText(MainActivity.this, "Nouvelle version téléchargée : " + SharedPreferenceUtils.getVersionDataDragon(), Toast.LENGTH_SHORT).show();
                        break;

                    case NO_NEW_VERSION:
                        Toast.makeText(MainActivity.this, "Pas de nouvelle version", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Subscribe
    public void onException(final ExceptionA exceptionA) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, exceptionA.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* ---------------------------------
    // Private
    // -------------------------------- */
    private void refreshData() {

        if (gameBean != null) {
            long difference = (new Date().getTime() - gameBean.getGameStartTime());
            chronometer.setBase(SystemClock.elapsedRealtime() - difference);
            chronometer.start();
        }

        refreshScreen();
    }

    private void refreshScreen() {

        if (StringUtils.isNotBlank(message)) {
            tvResultat.setText(message);
            tvResultat.setVisibility(View.VISIBLE);
        }
        else {
            tvResultat.setVisibility(View.GONE);
        }

        if (gameBean != null) {
            //On masque la demande de nom
            cvPseudo.setVisibility(View.GONE);
            ll_top.setVisibility(View.VISIBLE);
            chronometer.setVisibility(View.VISIBLE);
        }
        else {
            cvPseudo.setVisibility(View.VISIBLE);
            ll_top.setVisibility(View.GONE);
            chronometer.setVisibility(View.GONE);
        }

        //On remplit les cellules

    }

    /* ---------------------------------
    // AsyncTask
    // -------------------------------- */

    public class MonAT extends AsyncTask {

        String pseudo;
        ExceptionA exceptionA;
        GameBean resultat;

        public MonAT(String pseudo) {
            this.pseudo = pseudo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(MainActivity.this, "", "Chargement...");
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                resultat = WSUtils.getSpectatorInfo(pseudo);
            }
            catch (ExceptionA exceptionA) {
                exceptionA.printStackTrace();
                this.exceptionA = exceptionA;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (pd != null) {
                pd.dismiss();
            }

            if (exceptionA != null) {
                message = exceptionA.getMessage() + "\n";
                refreshScreen();
            }
            else if (resultat != null) {
                gameBean = resultat;
                LogUtils.w("tag_", "game : " + gameBean.getGameStartTime());
                LogUtils.w("tag_", "now : " + new Date().getTime());
                LogUtils.w("tag_", "Difference de temps : " + (new Date().getTime() - gameBean.getGameStartTime()) + "\n");
                refreshData();
            }
        }
    }
}
