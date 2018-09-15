package timer.anthony.com.loltimer.model;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import timer.anthony.com.loltimer.kitDev.exception.ExceptionA;
import timer.anthony.com.loltimer.kitDev.exception.TechnicalException;
import timer.anthony.com.loltimer.kitDev.log.LogUtils;
import timer.anthony.com.loltimer.kitDev.req.ReqHelper;
import timer.anthony.com.loltimer.model.beans.dao.ChampionBean;
import timer.anthony.com.loltimer.model.beans.dao.SummonerBean;
import timer.anthony.com.loltimer.model.dao.ChampionDao;
import timer.anthony.com.loltimer.model.dao.SummonerDao;
import timer.anthony.com.loltimer.model.beans.responseBean.GetSummonersResponseBean;

public class DataDragonWSUtils {

    private static final String URL_SERVER_DRAGON = "https://ddragon.leagueoflegends.com/";

    private static final String URL_VERSION = URL_SERVER_DRAGON + "api/versions.json";
    private static final String URL_IMAGE_SPELL = URL_SERVER_DRAGON + "cdn/###/img/spell/";
    private static final String URL_IMAGE_CHAMPION = URL_SERVER_DRAGON + "cdn/###/img/champion/";
    private static final String GET_SUMMONNER_INFO = URL_SERVER_DRAGON + "cdn/###/data/en_US/summoner.json";
    private static final String GET_CHAMPION_INFO = URL_SERVER_DRAGON + "cdn/###/data/en_US/champion.json";

    /**
     * @return true si cela a changé
     * @throws ExceptionA
     */
    public static String getLastDragonVersion() throws ExceptionA {

        ReqHelper<ArrayList<String>> reqHelper = new ReqHelper<>(URL_VERSION);
        ArrayList<String> res = reqHelper.parseReponse(new TypeToken<ArrayList<String>>() {
        }.getType());

        if (res == null || res.isEmpty()) {
            throw new TechnicalException("Tableau de version de DataDragon reçu vide");
        }
        else {

            return res.get(0);
        }
    }

    public static void loadSummoners(String newVersion) throws ExceptionA {
        ReqHelper<GetSummonersResponseBean> reqHelper = new ReqHelper<>(GET_SUMMONNER_INFO.replace("###", newVersion));

        GetSummonersResponseBean res = reqHelper.parseReponse(GetSummonersResponseBean.class);

        if (res.getData() == null) {
            throw new TechnicalException("loadSummoners : Data manquantes");
        }

        SummonerBean[] list = {res.getData().getSummonerBarrier(), res.getData().getSummonerBoost(), res.getData().getSummonerDot(), res.getData().getSummonerExhaust(), res.getData().getSummonerFlash(),
                res.getData().getSummonerHaste(), res.getData().getSummonerHeal(), res.getData().getSummonerMana(), res.getData().getSummonerSmite(), res.getData().getSummonerTeleport()};

        for (SummonerBean summonerBean : list) {

            if (summonerBean == null || summonerBean.getId() == null) {
                LogUtils.w("TAG_JSON", "List summoners : " + Arrays.toString(list));
                throw new TechnicalException("Chargement des Summoners incomplet, élément null");
            }

            //On applique l'url de l'image en entier
            if (summonerBean != null && summonerBean.getImage() != null && StringUtils.isNotBlank(summonerBean.getImage().getFull())) {
                summonerBean.setUrlImage(URL_IMAGE_SPELL.replace("###", newVersion) + summonerBean.getImage().getFull());
            }
        }

        //on actualise la base
        SummonerDao.save(list);
    }

    public static void loadChampions(String newVersion) throws ExceptionA {
        ReqHelper<JSONObject> reqHelper = new ReqHelper<>(GET_CHAMPION_INFO.replace("###", newVersion));

        ArrayList<ChampionBean> championsList = new ArrayList<>();
        String json = reqHelper.getDataInBody();
        try {
            JSONObject data = new JSONObject(json).getJSONObject("data");
            JSONArray name = data.names();
            Log.w("TAG_", "");
            for (int i = 0; i < name.length(); i++) {
                ChampionBean championBean = new ChampionBean();
                JSONObject champion = data.getJSONObject(name.getString(i));
                championBean.setId(champion.getString("id"));
                championBean.setKey(champion.getLong("key"));
                championBean.setName(champion.getString("name"));
                championBean.setUrlImage(URL_IMAGE_CHAMPION.replace("###", newVersion) + champion.getJSONObject("image").getString("full"));

                championsList.add(championBean);
            }

            //on sauvegarde
            ChampionDao.save(championsList);
        }
        catch (JSONException e) {
            e.printStackTrace();
            throw new TechnicalException("Erreur de parsing du json.\njson=" + json);
        }
    }
}
