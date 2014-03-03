package com.neodem.coup.game;

/**
 *
 * Author: vfumo
 * Date: 2/28/14
 */
public abstract class BasePlayer implements Player {
    private String playerName;

    protected PlayerId id;
    protected GameMaster s;

    public BasePlayer(String playerName) {
        this.playerName = playerName;
        startPlayer();
    }

    public void startPlayer() {

        id = s.registerPlayerName(playerName);
        int i = 1;
        while(id == null) {
            playerName = String.format("%s%s", playerName, i++);
        }

        GameContext currentGameContext = s.registerPlayerForNextGame(this);

        initializePlayer(currentGameContext);

        while(true) {
             // wait for turn
             // process turn
        }
    }

    protected abstract void initializePlayer(GameContext g);

    @Override
    public PlayerId getPlayerId() {
        return id;
    }

}
