package com.neodem.coup.common.game.player;

import com.neodem.coup.common.game.CoupPlayerCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public abstract class BaseCoupPlayer implements CoupPlayerCallback {

    private static final Logger log = LogManager.getLogger(BaseCoupPlayer.class.getName());
    protected final String playerName;

    public BaseCoupPlayer(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String toString() {
        return playerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseCoupPlayer)) return false;

        BaseCoupPlayer that = (BaseCoupPlayer) o;

        return !(playerName != null ? !playerName.equals(that.playerName) : that.playerName != null);
    }

    @Override
    public int hashCode() {
        return playerName != null ? playerName.hashCode() : 0;
    }

    protected Logger getLog() {
        return log;
    }
}
