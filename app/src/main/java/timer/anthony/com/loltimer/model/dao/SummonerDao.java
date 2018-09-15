package timer.anthony.com.loltimer.model.dao;

import timer.anthony.com.loltimer.kitDev.MyApplication;
import timer.anthony.com.loltimer.model.beans.dao.SummonerBean;
import timer.anthony.com.loltimer.model.beans.dao.SummonerBeanDao;

public class SummonerDao {

    public static SummonerBean load(long key) {
        return getDao().load(key);
    }

    public static void save(SummonerBean... summonerBean) {
        getDao().insertOrReplaceInTx(summonerBean);
    }

    public static SummonerBeanDao getDao() {
        return MyApplication.getDaoSession().getSummonerBeanDao();
    }
}
