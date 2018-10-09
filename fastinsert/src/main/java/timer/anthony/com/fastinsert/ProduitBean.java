package timer.anthony.com.fastinsert;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Arrays;

@Entity(

        active = true,
        generateConstructors = true,
        generateGettersSetters = true
)
public class ProduitBean {

    private static final int NB_COLONE_CSV = 8;

    @Id(autoincrement = true)
    private Long id;
    private String gencod;
    private String designation;
    private String cip7, cleInventaire, dateNfp, quatiteStock, prixpublic, nfp;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1211099929)
    private transient ProduitBeanDao myDao;

    public ProduitBean(String[] cut) throws Exception {

        if (cut.length < NB_COLONE_CSV - 1) {
            throw new Exception("Une ligne ne contient pas " + NB_COLONE_CSV + " colone\n" + Arrays.toString(cut));
        }

        gencod = cut[0];
        designation = cut[1];
        cip7 = cut[2];
        cleInventaire = cut[3];
        dateNfp = cut[4];
        quatiteStock = cut[5];
        prixpublic = cut[6];
        if (cut.length > 7) {
            nfp = cut[7];
        }
    }

    @Generated(hash = 526048056)
    public ProduitBean(Long id, String gencod, String designation, String cip7, String cleInventaire, String dateNfp,
                       String quatiteStock, String prixpublic, String nfp) {
        this.id = id;
        this.gencod = gencod;
        this.designation = designation;
        this.cip7 = cip7;
        this.cleInventaire = cleInventaire;
        this.dateNfp = dateNfp;
        this.quatiteStock = quatiteStock;
        this.prixpublic = prixpublic;
        this.nfp = nfp;
    }

    @Generated(hash = 1178789244)
    public ProduitBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGencod() {
        return this.gencod;
    }

    public void setGencod(String gencod) {
        this.gencod = gencod;
    }

    public String getDesignation() {
        return this.designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCip7() {
        return this.cip7;
    }

    public void setCip7(String cip7) {
        this.cip7 = cip7;
    }

    public String getCleInventaire() {
        return this.cleInventaire;
    }

    public void setCleInventaire(String cleInventaire) {
        this.cleInventaire = cleInventaire;
    }

    public String getDateNfp() {
        return this.dateNfp;
    }

    public void setDateNfp(String dateNfp) {
        this.dateNfp = dateNfp;
    }

    public String getQuatiteStock() {
        return this.quatiteStock;
    }

    public void setQuatiteStock(String quatiteStock) {
        this.quatiteStock = quatiteStock;
    }

    public String getPrixpublic() {
        return this.prixpublic;
    }

    public void setPrixpublic(String prixpublic) {
        this.prixpublic = prixpublic;
    }

    public String getNfp() {
        return this.nfp;
    }

    public void setNfp(String nfp) {
        this.nfp = nfp;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 444795465)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getProduitBeanDao() : null;
    }


    /* ---------------------------------
    // Generated
    // -------------------------------- */
}
