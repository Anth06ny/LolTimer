package timer.anthony.com.loltimer.model;

import java.util.ArrayList;

import timer.anthony.com.loltimer.kitDev.exception.ExceptionA;
import timer.anthony.com.loltimer.kitDev.exception.LogicException;
import timer.anthony.com.loltimer.kitDev.exception.TechnicalException;
import timer.anthony.com.loltimer.kitDev.log.LogUtils;
import timer.anthony.com.loltimer.kitDev.req.ReqHelper;
import timer.anthony.com.loltimer.model.beans.GameBean;
import timer.anthony.com.loltimer.model.beans.PlayerBean;
import timer.anthony.com.loltimer.model.beans.dao.ChampionBean;
import timer.anthony.com.loltimer.model.beans.dao.SummonerBean;
import timer.anthony.com.loltimer.model.beans.responseBean.GetPlayerInfoResponseBean;
import timer.anthony.com.loltimer.model.beans.responseBean.GetSpectatorResponseBean;
import timer.anthony.com.loltimer.model.beans.responseBean.StatusBeanResponse;
import timer.anthony.com.loltimer.model.beans.ws.ParticipantBean;
import timer.anthony.com.loltimer.model.dao.ChampionDao;
import timer.anthony.com.loltimer.model.dao.SummonerDao;

public class WSUtils {

    private static final String HEADER_KEY = "X-Riot-Token";

    private static final String API_KEY = "RGAPI-f0e08f6c-1ada-4e91-8af7-269d6f148b15";
    private static final String GET_PLAYER_INFO = "https://euw1.api.riotgames.com/lol/summoner/v3/summoners/by-name/";

    private static final String GET_SPECTATOR_INFO = "https://euw1.api.riotgames.com/lol/spectator/v3/active-games/by-summoner/";

    public static GetPlayerInfoResponseBean getPlayerBean(String pseudo) throws ExceptionA {
        pseudo = pseudo.trim().replace(" ", "%20");
        ReqHelper<GetPlayerInfoResponseBean> reqHelper = new ReqHelper<>(GET_PLAYER_INFO + pseudo);
        reqHelper.setHeader(HEADER_KEY, API_KEY);
        GetPlayerInfoResponseBean res = reqHelper.parseReponse(GetPlayerInfoResponseBean.class);

        checkStatus(res.getStatus());

        return res;
    }

    /**
     * A partir du pseudo on récupére les info du joueur puis sa partie en cours
     *
     * @param pseudo
     * @return
     * @throws ExceptionA
     */
    public static GameBean getSpectatorInfo(String pseudo) throws ExceptionA {
        GetPlayerInfoResponseBean getPlayerInfoResponseBean = getPlayerBean(pseudo);

        ReqHelper<GetSpectatorResponseBean> reqHelper = new ReqHelper<>(GET_SPECTATOR_INFO + getPlayerInfoResponseBean.getId());
        reqHelper.setHeader(HEADER_KEY, API_KEY);

        GetSpectatorResponseBean res;
        try {
            res = reqHelper.parseReponse(GetSpectatorResponseBean.class);
        }
        catch (ExceptionA exceptionA) {
            //Si on a une erreur on regarde s'il n'y a pas un JSon qui va avec
            try {
                res = reqHelper.parseReponse(GetSpectatorResponseBean.class);
            }
            catch (ExceptionA exceptionA1) {
                //En cas d'erreur on transmet la 1ere
                throw exceptionA;
            }
        }

        try {
            checkStatus(res.getStatus());
        }
        catch (ExceptionA exceptionA) {
            if (exceptionA.getStatusCode() == 404) {
                throw new LogicException("Aucune partie en cours", exceptionA);
            }
            else {
                throw exceptionA;
            }
        }
        //On verifie que tout est rempli
        if (res.getParticipants().isEmpty()) {
            throw new TechnicalException("Aucun participant");
        }
        else if (res.getParticipants().size() != 10) {
            throw new LogicException("Mode de jeu non pris en compte. 10 participants attendus : " + res.getParticipants().size());
        }

        ArrayList<PlayerBean> list = new ArrayList<>();
        long teamIDOfPseudo = 0;

        //Maintenant qu'on les info on crée les players
        for (ParticipantBean participantBean : res.getParticipants()) {
            if (participantBean.getSummonerId() == getPlayerInfoResponseBean.getId()) {
                teamIDOfPseudo = participantBean.getTeamId();
                //inutile de l'ajouter si c'est pour le retirer
            }
            else {
                SummonerBean speel1 = SummonerDao.load(participantBean.getSpell1Id());
                SummonerBean speel2 = SummonerDao.load(participantBean.getSpell2Id());
                ChampionBean championBean = ChampionDao.load(participantBean.getChampionId());
                PlayerBean playerBean = new PlayerBean(championBean, speel1, speel2);
                list.add(playerBean);
            }
        }
        LogUtils.w("TAG_", "teamiID :" + teamIDOfPseudo);

        //on retire les joueurs de l'equipe du pseudo
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).getTeamId() == teamIDOfPseudo) {
                list.remove(i);
            }
        }

        GameBean gameBean = new GameBean();
        gameBean.setGameStartTime(res.getGameStartTime());
        gameBean.setPlayers(list);

        return gameBean;
    }



    /* ---------------------------------
    // Private
    // -------------------------------- */

    private static void checkStatus(StatusBeanResponse statusBeanResponse) throws ExceptionA {

        if (statusBeanResponse != null) {
            ExceptionA exceptionA;

            if (statusBeanResponse.getStatus_code() != 401) {
                exceptionA = new TechnicalException("Clé manquante : " + statusBeanResponse.getMessage());
            }
            else if (statusBeanResponse.getStatus_code() != 403) {
                exceptionA = new TechnicalException("Nombre de requête maximal atteinte : " + statusBeanResponse.getMessage());
            }
            else if (statusBeanResponse.getStatus_code() != 404) {
                exceptionA = new LogicException("Donnée indisponible : " + statusBeanResponse.getMessage());
            }
            else if (statusBeanResponse.getStatus_code() != 429) {
                exceptionA = new TechnicalException("Clé expirée : " + statusBeanResponse.getMessage());
            }
            else {
                exceptionA = new LogicException(statusBeanResponse.getMessage() + "\ncode : " + statusBeanResponse.getStatus_code());
            }
            exceptionA.setStatusCode(statusBeanResponse.getStatus_code());

            throw exceptionA;
        }
    }
}
