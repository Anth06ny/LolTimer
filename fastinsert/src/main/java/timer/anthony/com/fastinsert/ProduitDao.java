package timer.anthony.com.fastinsert;

public class ProduitDao {

    public static void clear() {
        MyApplication.getDaoSession().getProduitBeanDao().deleteAll();
    }

    public static void insert(ProduitBean produitBean) {
        MyApplication.getDaoSession().getProduitBeanDao().insertOrReplace(produitBean);
    }

    public static long count() {
        return MyApplication.getDaoSession().getProduitBeanDao().count();
    }
}
