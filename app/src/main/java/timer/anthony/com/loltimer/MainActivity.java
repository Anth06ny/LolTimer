package timer.anthony.com.loltimer;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
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
import java.util.Locale;
import java.util.Set;

import timer.anthony.com.loltimer.kitDev.MyApplication;
import timer.anthony.com.loltimer.kitDev.SharedPreferenceUtils;
import timer.anthony.com.loltimer.kitDev.Utils.AlertDialogUtils;
import timer.anthony.com.loltimer.kitDev.Utils.DateUtils;
import timer.anthony.com.loltimer.kitDev.Utils.OttoEvent;
import timer.anthony.com.loltimer.kitDev.exception.ExceptionA;
import timer.anthony.com.loltimer.kitDev.log.LogUtils;
import timer.anthony.com.loltimer.model.WSUtils;
import timer.anthony.com.loltimer.model.beans.EventBean;
import timer.anthony.com.loltimer.model.beans.GameBean;
import timer.anthony.com.loltimer.model.beans.PlayerBean;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, AlertDialogUtils.SelectVoiceListener, Chronometer.OnChronometerTickListener {
    public static final long TIME_24H = 1000 * 60 * 60 * 24;
    public static final long TIME_1MIN = 1000 * 60;

    private static final String UTERENCE_ID = "UTERENCE_ID";// Truc demander pour le speech

    //Composants graphiques
    private TextView tvResultat;
    private EditText etPseudo;
    private ProgressDialog pd;
    private CardView cvPseudo;
    private Chronometer chronometer;
    private LinearLayout ll_top;
    private ArrayList<GestionRowPlayer> gestionRowPlayers;

    //outils
    private MonAT monAt;
    private TextToSpeech t1;
    private Handler handler;

    //Data
    private String message;
    GameBean gameBean;
    private ArrayList<EventBean> events;

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

        events = EventBean.getBasicEvent();

        t1 = new TextToSpeech(this, this);
        t1.stop();

        chronometer.setOnChronometerTickListener(this);

        refreshScreen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        t1.stop();

        MyApplication.getBus().unregister(this);
        if (pd != null) {
            pd.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Version").setIcon(R.mipmap.ic_info).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, 2, 0, "Changer de voix").setIcon(R.mipmap.ic_voice).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            Toast.makeText(this, "Version  : " + SharedPreferenceUtils.getVersionDataDragon(), Toast.LENGTH_LONG).show();
        }
        else if (item.getItemId() == 2) {
            Set<Voice> voices = t1.getVoices();
            if (voices == null) {
                Toast.makeText(this, "Syntetiseur vocale non fonctionnel", Toast.LENGTH_LONG).show();
            }
            else {
                AlertDialogUtils.showSelectOneDialog(this, voices, this);
            }
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
   // Syntetiseur vocal
   // -------------------------------- */
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            t1.setLanguage(Locale.FRANCE);
            t1.setSpeechRate(0.8f);
            //La voie sauvegarder
            String saveVoicename = SharedPreferenceUtils.getSaveVoice();
            if (saveVoicename == null) {
                saveVoicename = t1.getVoice().getName();
            }
            for (Voice voice : t1.getVoices()) {
                if (voice.getLocale().getCountry().equals("FR")) {
                    //Voix séléctionnée
                    if (voice.getName().equalsIgnoreCase(saveVoicename)) {
                        t1.setVoice(voice);
                    }
                }
            }
        }
        else {
            Toast.makeText(this, "Erreur d'initialisation de TextToSpeech", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSelectVoiceListener(Voice voice) {
        if (voice != null) {
            t1.setVoice(voice);
            SharedPreferenceUtils.saveVoice(voice.getName());
        }
    }

    /* ---------------------------------
    // Chrono
    // -------------------------------- */
    @Override
    public void onChronometerTick(Chronometer chronometer) {

        LogUtils.w("TAG_TICK", "tick");

        long time = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000;
        //on parcours les events et on indique
        for (EventBean eventBean : events) {
            if (eventBean.getTimeInSec() == time) {
                LogUtils.w("TAG_TICK", eventBean.getTexte());
                t1.speak(eventBean.getTexte(), TextToSpeech.QUEUE_ADD, null, UTERENCE_ID);
                return;
            }
        }
    }

    /* ---------------------------------
    // Private
    // -------------------------------- */
    private void refreshData() {

        if (gameBean != null) {
            long now = new Date().getTime();
            long difference = (now - gameBean.getGameStartTime());
            if (difference < 0 || difference > TIME_24H) {
                chronometer.setBase(SystemClock.elapsedRealtime() - TIME_1MIN);
            }
            else {
                chronometer.setBase(SystemClock.elapsedRealtime() - difference);
                chronometer.start();
            }
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

                gestionRowPlayer.ivSpell1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long timeEvent = (new Date().getTime() + (playerBean.getSpell1().getCooldownBurn() - Constants.TIME_BEFORE_FLASH) * 1000);
                        EventBean eventBean = new EventBean(playerBean.getName() + " " + playerBean.getSpell1().getName() + " dans " + Constants.TIME_BEFORE_FLASH
                                + "secondes", timeEvent);
                        events.add(eventBean);
                        EventBean.sortByTime(events);
                    }
                });

                gestionRowPlayer.ivSpell1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long timeEvent = (new Date().getTime() + (playerBean.getSpell2().getCooldownBurn() - Constants.TIME_BEFORE_FLASH) * 1000);
                        EventBean eventBean = new EventBean(playerBean.getName() + " " + playerBean.getSpell2().getName() + " dans " + Constants.TIME_BEFORE_FLASH
                                + "secondes", timeEvent);
                        events.add(eventBean);
                        EventBean.sortByTime(events);
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
                    Log.w("TAG_TIME", "time:" + resultat.getGameStartTime());
                    Log.w("TAG_TIME", DateUtils.dateToString(new Date(resultat.getGameStartTime()), DateUtils.DATE_FORMAT.ddMMyyyy_HHmm));

                    if (!DateUtils.isToday(resultat.getGameStartTime())) {
                        publishProgress(resultat);
                        SystemClock.sleep(3000);
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
