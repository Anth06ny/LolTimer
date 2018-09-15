package timer.anthony.com.loltimer.model.beans.ws;

import java.util.List;

public class ParticipantBean {

    //100 ou 200 en fonction de la team
    private long teamId;
    private long spell1Id;
    private long spell2Id;
    private long championId;
    private long profileIconId;
    private String summonerName;
    private boolean bot;
    private long summonerId;
    private List<Object> gameCustomizationObjects = null;
    private PerksBean perks;

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public long getSpell1Id() {
        return spell1Id;
    }

    public void setSpell1Id(long spell1Id) {
        this.spell1Id = spell1Id;
    }

    public long getSpell2Id() {
        return spell2Id;
    }

    public void setSpell2Id(long spell2Id) {
        this.spell2Id = spell2Id;
    }

    public long getChampionId() {
        return championId;
    }

    public void setChampionId(long championId) {
        this.championId = championId;
    }

    public long getProfileIconId() {
        return profileIconId;
    }

    public void setProfileIconId(long profileIconId) {
        this.profileIconId = profileIconId;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    public long getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(long summonerId) {
        this.summonerId = summonerId;
    }

    public List<Object> getGameCustomizationObjects() {
        return gameCustomizationObjects;
    }

    public void setGameCustomizationObjects(List<Object> gameCustomizationObjects) {
        this.gameCustomizationObjects = gameCustomizationObjects;
    }

    public PerksBean getPerks() {
        return perks;
    }

    public void setPerks(PerksBean perks) {
        this.perks = perks;
    }
}
