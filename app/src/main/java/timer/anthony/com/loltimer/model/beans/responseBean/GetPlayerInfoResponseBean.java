package timer.anthony.com.loltimer.model.beans.responseBean;

public class GetPlayerInfoResponseBean {

    private long id;
    private long accountId;
    private String name;
    private long profileIconId;
    private long revisionDate;
    private long summonerLevel;

    private StatusBeanResponse status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getProfileIconId() {
        return profileIconId;
    }

    public void setProfileIconId(long profileIconId) {
        this.profileIconId = profileIconId;
    }

    public long getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(long revisionDate) {
        this.revisionDate = revisionDate;
    }

    public long getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(long summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    public StatusBeanResponse getStatus() {
        return status;
    }

    public void setStatus(StatusBeanResponse status) {
        this.status = status;
    }
}
