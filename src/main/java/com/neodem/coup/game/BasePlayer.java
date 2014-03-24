package com.neodem.coup.game;

import org.springframework.beans.factory.annotation.Required;

/**
 *
 * Author: vfumo
 * Date: 2/28/14
 */
public abstract class BasePlayer<A extends Action> implements Player<A> {
    protected String playerName;
    protected GameMaster gameMaster;

    @Override
    public String toString() {
        return playerName;
    }

    public void startPlayer() {

        GameContext currentGameContext = gameMaster.registerPlayerForNextGame(this);

        initializePlayer(currentGameContext);
    }

    protected abstract void initializePlayer(GameContext g);

    public String getPlayerName() {
        return playerName;
    }

    @Required
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Required
    public void setGameMaster(GameMaster gameMaster) {
        this.gameMaster = gameMaster;
    }
}
