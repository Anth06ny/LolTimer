package timer.anthony.com.loltimer.model.beans;

import java.util.ArrayList;

public class GameBean {

    public ArrayList<PlayerBean> players;
    private long gameStartTime;

    public ArrayList<PlayerBean> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<PlayerBean> players) {
        this.players = players;
    }

    public long getGameStartTime() {
        return gameStartTime;
    }

    public void setGameStartTime(long gameStartTime) {
        this.gameStartTime = gameStartTime;
    }
}
