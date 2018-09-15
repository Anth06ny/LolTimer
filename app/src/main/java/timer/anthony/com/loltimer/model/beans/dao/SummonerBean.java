package timer.anthony.com.loltimer.model.beans.dao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import timer.anthony.com.loltimer.model.beans.ws.ImageSummonerBean;

@Entity(

        active = true,
        generateConstructors = true,
        generateGettersSetters = true
)
public class SummonerBean {

    private String id; //Nom du spell "SummonerBarrier"
    private String name;  //nom du spell "Barrier"
    private long cooldownBurn;
    @Id
    private long key; //correspond à l'id qu'il y aura dans les spell des participants
    @Transient
    private ImageSummonerBean image;

    //hors WS
    private String urlImage;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1081119972)
    private transient SummonerBeanDao myDao;

    @Generated(hash = 792224001)
    public SummonerBean(String id, String name, long cooldownBurn, long key,
                        String urlImage) {
        this.id = id;
        this.name = name;
        this.cooldownBurn = cooldownBurn;
        this.key = key;
        this.urlImage = urlImage;
    }

    @Generated(hash = 1268019433)
    public SummonerBean() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCooldownBurn() {
        return this.cooldownBurn;
    }

    public void setCooldownBurn(long cooldownBurn) {
        this.cooldownBurn = cooldownBurn;
    }

    public long getKey() {
        return this.key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getUrlImage() {
        return this.urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
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

    public ImageSummonerBean getImage() {
        return image;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1471559802)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSummonerBeanDao() : null;
    }
}
