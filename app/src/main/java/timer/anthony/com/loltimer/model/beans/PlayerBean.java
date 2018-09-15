package timer.anthony.com.loltimer.model.beans;

import timer.anthony.com.loltimer.model.beans.dao.ChampionBean;
import timer.anthony.com.loltimer.model.beans.dao.SummonerBean;

public class PlayerBean {

    //Spell1
    private SummonerBean spell1;
    private SummonerBean spell2;
    private String name;
    private String urlImageChamp;
    private int teamId;

    public PlayerBean(ChampionBean championBean, SummonerBean spell1, SummonerBean spell2) {
        name = championBean.getName();
        urlImageChamp = championBean.getUrlImage();
        this.spell1 = spell1;
        this.spell2 = spell2;
    }

    public PlayerBean() {
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public SummonerBean getSpell1() {
        return spell1;
    }

    public void setSpell1(SummonerBean spell1) {
        this.spell1 = spell1;
    }

    public SummonerBean getSpell2() {
        return spell2;
    }

    public void setSpell2(SummonerBean spell2) {
        this.spell2 = spell2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImageChamp() {
        return urlImageChamp;
    }

    public void setUrlImageChamp(String urlImageChamp) {
        this.urlImageChamp = urlImageChamp;
    }
}
