package timer.anthony.com.fastinsert;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.greendao.database.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private MonAT monAT;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.bt) {
            if (monAT == null || monAT.getStatus() == AsyncTask.Status.FINISHED) {
                monAT = new MonAT();
                monAT.execute();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public class MonAT extends AsyncTask<Void, Integer, Void> {

        private Exception exception;
        private long temps;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this, "", "Chargement en cours");
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //on vide la base   pour les test
            ProduitDao.clear();

            temps = SystemClock.currentThreadTimeMillis();
            Database db = MyApplication.getDaoSession().getDatabase();
            BufferedReader reader = null;
            try {
                //on lit le fichier qui se trouve dans l'asset
                reader = new BufferedReader(new InputStreamReader(getAssets().open("produitsdg.csv")));

                String mLine;
                ProduitBean produitBean;
                db.beginTransaction();     //J'ouvre une transaction
                for (int i = 0; (mLine = reader.readLine()) != null; i++) {
                    if (i % 10 == 0) {
                        publishProgress(i);
                    }
                    String[] cut = mLine.split(";");
                    ProduitDao.insert(new ProduitBean(cut));
                }
                db.setTransactionSuccessful(); //Succes on confirme que c'est bon
            }
            catch (Exception e) {
                exception = e;
                e.printStackTrace();
            }
            finally {
                db.endTransaction();   //Echec on efface ce qui a été fait

                if (reader != null) {
                    try {
                        reader.close();
                    }
                    catch (IOException e) {
                        //log the exception
                    }
                }
            }

            temps = SystemClock.currentThreadTimeMillis() - temps;

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (dialog != null) {
                dialog.setMessage("Chargement en cours : " + values[0]);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (dialog != null) {
                dialog.dismiss();
            }

            if (exception != null) {
                Toast.makeText(MainActivity.this, "erreur : " + exception.getMessage(), Toast.LENGTH_LONG).show();
                Log.w("TAG_", "Echec " + exception.getMessage());
            }
            else {
                //On verifie que cela a bien fonctionné
                long count = ProduitDao.count();

                String text = "Sucess " + (temps / 1000) + "\nNombre en base : " + count;

                Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
                Log.w("TAG_", text);
            }
        }
    }
}
