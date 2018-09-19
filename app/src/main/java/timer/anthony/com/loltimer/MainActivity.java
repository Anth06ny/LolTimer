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

import com.bumptech.glide.Glide;
import com.squareup.otto.Subscribe;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;

import timer.anthony.com.loltimer.kitDev.MyApplication;
import timer.anthony.com.loltimer.kitDev.SharedPreferenceUtils;
import timer.anthony.com.loltimer.kitDev.Utils.DateUtils;
import timer.anthony.com.loltimer.kitDev.Utils.OttoEvent;
import timer.anthony.com.loltimer.kitDev.exception.ExceptionA;
import timer.anthony.com.loltimer.model.WSUtils;
import timer.anthony.com.loltimer.model.beans.GameBean;
import timer.anthony.com.loltimer.model.beans.PlayerBean;

public class MainActivity extends AppCompatActivity {
    public static final long TIME_24H = 1000 * 60 * 60 * 24;
    public static final long TIME_3MIN = 1000 * 60 * 3;

    private TextView tvResultat;
    private EditText etPseudo;
    private ProgressDialog pd;
    private CardView cvPseudo;
    private Chronometer chronometer;
    private LinearLayout ll_top;

    private ArrayList<GestionRowPlayer> gestionRowPlayers;

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

        gestionRowPlayers = new ArrayList<>();
        gestionRowPlayers.add(new GestionRowPlayer(findViewById(R.id.rowP1)));
        gestionRowPlayers.add(new GestionRowPlayer(findViewById(R.id.rowP2)));
        gestionRowPlayers.add(new GestionRowPlayer(findViewById(R.id.rowP3)));
        gestionRowPlayers.add(new GestionRowPlayer(findViewById(R.id.rowP4)));
        gestionRowPlayers.add(new GestionRowPlayer(findViewById(R.id.rowP5)));

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
            long now = new Date().getTime();
            long difference = (now - gameBean.getGameStartTime());
            if (difference < TIME_24H) {
                chronometer.setBase(SystemClock.elapsedRealtime() - TIME_3MIN);
            }
            else {
                chronometer.setBase(SystemClock.elapsedRealtime() - difference);
            }

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

            for (int i = 0; i < gestionRowPlayers.size() && i < gameBean.getPlayers().size(); i++) {
                final PlayerBean playerBean = gameBean.getPlayers().get(i);
                GestionRowPlayer gestionRowPlayer = gestionRowPlayers.get(i);

                gestionRowPlayer.ivNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = gameBean.getPlayers().indexOf(playerBean);
                        if (index < gameBean.getPlayers().size() - 1) {
                            gameBean.getPlayers().remove(index);
                            gameBean.getPlayers().add(index + 1, playerBean);
                            refreshScreen();
                        }
                    }
                });
                gestionRowPlayer.ivPrevious.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = gameBean.getPlayers().indexOf(playerBean);
                        if (index > 0) {
                            gameBean.getPlayers().remove(index);
                            gameBean.getPlayers().add(index - 1, playerBean);
                            refreshScreen();
                        }
                    }
                });

                Glide.with(this).load(playerBean.getUrlImageChamp()).into(gestionRowPlayer.ivChamp);
                Glide.with(this).load(playerBean.getSpell1().getUrlImage()).into(gestionRowPlayer.ivSpell1);
                Glide.with(this).load(playerBean.getSpell2().getUrlImage()).into(gestionRowPlayer.ivSpell2);
            }
        }
        else {
            cvPseudo.setVisibility(View.VISIBLE);
            ll_top.setVisibility(View.GONE);
            chronometer.setVisibility(View.GONE);
        }

        //On remplit les cellules

        //        for (GestionRowPlayer player : gestionRowPlayers) {
        //            Glide.with(this).load().into(imageView);
        //            player.ivChamp
        //        }
    }

    public void updateGAme(GameBean gameBean) {
        this.gameBean = gameBean;
        refreshData();
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

                while (true) {
                    resultat = WSUtils.getSpectatorInfo(pseudo);
                    if (!DateUtils.isToday(resultat.getGameStartTime())) {
                        publishProgress(resultat);
                        SystemClock.sleep(5000);
                    }
                    else {
                        break;
                    }
                }
            }
            catch (ExceptionA exceptionA) {
                exceptionA.printStackTrace();
                this.exceptionA = exceptionA;
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            updateGAme((GameBean) values[0]);
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
                refreshData();
            }
        }
    }
}
