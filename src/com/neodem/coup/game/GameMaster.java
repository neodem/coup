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
    PlayerId registerPlayerName(String playerName);

    /**
     * must have a valid PlayerId (eg. call registerPlayerName)
     *
     * @param player
     * @return
     */
    GameContext registerPlayerForNextGame(Player player);

    /**
     * entry into the main game loop process. This will start the main game loop
     */
    void startGame();
}
