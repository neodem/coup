package com.neodem.coup.game;

/**
 *
 * Author: vfumo
 * Date: 2/28/14
 */
public class PlayerId {
    private String playerName;
    private int playerId;

    public PlayerId(String playerName, int playerId) {
        this.playerName = playerName;
        this.playerId = playerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerId)) return false;

        PlayerId playerId1 = (PlayerId) o;

        if (playerId != playerId1.playerId) return false;
        if (!playerName.equals(playerId1.playerName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = playerName.hashCode();
        result = 31 * result + playerId;
        return result;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
