package timer.anthony.com.loltimer.kitDev.req;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timer.anthony.com.loltimer.BuildConfig;
import timer.anthony.com.loltimer.kitDev.MyApplication;
import timer.anthony.com.loltimer.kitDev.exception.ExceptionA;
import timer.anthony.com.loltimer.kitDev.exception.TechnicalException;
import timer.anthony.com.loltimer.kitDev.log.LogUtils;
import timer.anthony.com.loltimer.kitDev.log.Logger;

public class ReqHelper<T> {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final Gson gson = new Gson();

    //Pour ll'envoie
    private String url;
    private boolean post;
    private String json;

    private HashMap<String, String> headers;

    //Pour la réponse
    Response response;

    public ReqHelper(String url) {
        this(url, false, null);
    }

    public ReqHelper(String url, boolean post, String json) {
        this.url = url;
        this.post = post;
        this.json = json;
        headers = new HashMap<>();
    }

    public void setHeader(String cle, String value) {
        headers.put(cle, value);
    }

    public void send() throws ExceptionA {
        LogUtils.w("TAG_URL_" + (post ? "POST" : "GET"), url);

        if (StringUtils.isNotBlank(json)) {
            LogUtils.w("TAG_REQ", "json envoyé : " + json);
        }

        //Création de la requete
        Request.Builder builder = new Request.Builder().url(url);

        //Ajout des headers
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.header(entry.getKey(), entry.getValue());
        }

        if (post) {
            RequestBody body = RequestBody.create(JSON, json);
            builder.post(body);
        }

        //Execution de la requête
        try {
            response = getOkHttpClient().newCall(builder.build()).execute();
        }
        catch (IOException e) {
            //On test si google répond pour différencier si c'est internet ou le serveur le probleme
            throw testInternetConnexionOnGoogle(e);
        }

        if (response.code() < HttpURLConnection.HTTP_OK || response.code() >= HttpURLConnection.HTTP_MULT_CHOICE) {
            //On regarde s'il y a un json dans le message/
            try {
                String message = response.body().string();
                throw new TechnicalException("Erreur serveur : " + response.code() + "\nErreur:" + response.message() + "\nbody=" + message);
            }
            catch (IOException e) {
                e.printStackTrace();
                throw new TechnicalException("Erreur serveur : " + response.code() + "\nErreur:" + response.message());
            }
        }
    }

    public T parseReponse(Class<T> classOfT) throws ExceptionA {

        if (response == null) {
            send();
        }

        //En cas d'erreur on regarde s'il y a un json avec la réponse

        //Résultat de la requete.
        try {
            if (BuildConfig.DEBUG) {
                String jsonRecu = response.body().string();
                Logger.logJson("TAG_JSON_RECU", jsonRecu);
                return gson.fromJson(jsonRecu, classOfT);
            }
            else {
                //JSON -> Java (Parser une ArrayList typée)
                return gson.fromJson(new InputStreamReader(response.body().byteStream()), classOfT);
            }
        }
        catch (Exception e) {
            throw new TechnicalException("Erreur lors du parsing Json", e);
        }
    }

    public T parseReponse(Type type) throws ExceptionA {
        if (response == null) {
            send();
        }

        //Résultat de la requete.
        try {
            if (BuildConfig.DEBUG) {
                String jsonRecu = response.body().string();
                Logger.logJson("TAG_JSON_RECU", jsonRecu);
                return gson.fromJson(jsonRecu, type);
            }
            else {
                //JSON -> Java (Parser une ArrayList typée)
                return gson.fromJson(new InputStreamReader(response.body().byteStream()), type);
            }
        }
        catch (Exception e) {
            throw new TechnicalException("Erreur lors du parsing Json", e);
        }
    }

    public String getDataInBody() throws ExceptionA {
        if (response == null) {
            send();
        }

        //Résultat de la requete.
        try {
            String jsonRecu = response.body().string();
            Logger.logJson("TAG_JSON_RECU", jsonRecu);
            return jsonRecu;
        }
        catch (Exception e) {
            throw new TechnicalException("Erreur lors de la lecture du body de la requête", e);
        }
    }

     /* ---------------------------------
    // private
    // -------------------------------- */

    static TechnicalException testInternetConnexionOnGoogle(IOException e) {
        //On test si google répond pour différencier si c'est internet ou le serveur le probleme
        Request request = request = new Request.Builder().url("https://www.google.fr").build();
        try {

            new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS).build().newCall(request).execute();
            //Ca marche -> C'est le serveur le probleme
            return new TechnicalException("L'url ne répond pas", e);
        }
        catch (Exception e1) {
            //Ca crash encore -> problème d'internet
            return new TechnicalException("Bande passante insufisante", e1);
        }
    }

    static OkHttpClient getOkHttpClient() throws TechnicalException {
        //On test la connexion à un réseau
        if (!isNetworkAvailable()) {
            throw new TechnicalException("Non connecté à un réseau.");
        }

        return new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Est ce que le téléphone est relié à un réseau ?
     *
     * @return
     */
    private static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

/* ---------------------------------
// Getter/Setter
// -------------------------------- */

    public Response getResponse() {
        return response;
    }
}

