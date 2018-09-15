package timer.anthony.com.loltimer.model.beans.responseBean;

import java.util.List;

import timer.anthony.com.loltimer.model.beans.ws.ParticipantBean;

public class GetSpectatorResponseBean {

    //The ID of the game
    private long gameId;
    //The game start time represented in epoch milliseconds
    private long gameStartTime;

    //Constante classic
    private String gameMode;

    private List<ParticipantBean> participants = null;

    //The amount of time in seconds that has passed since the game started
    private long gameLength;

    private StatusBeanResponse status;

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getGameStartTime() {
        return gameStartTime;
    }

    public void setGameStartTime(long gameStartTime) {
        this.gameStartTime = gameStartTime;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public List<ParticipantBean> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantBean> participants) {
        this.participants = participants;
    }

    public long getGameLength() {
        return gameLength;
    }

    public void setGameLength(long gameLength) {
        this.gameLength = gameLength;
    }

    public StatusBeanResponse getStatus() {
        return status;
    }

    public void setStatus(StatusBeanResponse status) {
        this.status = status;
    }
}
