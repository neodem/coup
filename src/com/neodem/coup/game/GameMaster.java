package com.neodem.coup.game;

/**
 *
 * Author: vfumo
 * Date: 2/28/14
 */
public interface GameMaster {
    /**
     * call this to get a PlayerId. If the name exists already you will get null back
     *
     * @param playerName
     * @return
     */
    PlayerId registerPlayer(String playerName);

    /**
     * must have a valid PlayerId (eg. call registerPlayer)
     *
     * @param player
     * @return
     */
    GameContext register(Player player);

    void startGame();
}
