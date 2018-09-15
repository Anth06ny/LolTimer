package timer.anthony.com.loltimer.model.dao;

import java.util.ArrayList;

import timer.anthony.com.loltimer.kitDev.MyApplication;
import timer.anthony.com.loltimer.model.beans.dao.ChampionBean;
import timer.anthony.com.loltimer.model.beans.dao.ChampionBeanDao;

public class ChampionDao {

    public static void save(ArrayList<ChampionBean> list) {
        getDao().insertOrReplaceInTx(list);
    }

    public static ChampionBean load(long key) {
        return getDao().load(key);
    }

    public static ChampionBeanDao getDao() {
        return MyApplication.getDaoSession().getChampionBeanDao();
    }
}
