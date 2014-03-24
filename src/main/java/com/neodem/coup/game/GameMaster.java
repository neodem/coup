package com.neodem.coup.game;

/**
 *
 * Author: vfumo
 * Date: 2/28/14
 */
public interface GameMaster<P extends Player> {

    /**
     * must have a valid PlayerId (eg. call registerPlayerName)
     *
     * @param player
     * @return
     */
    GameContext registerPlayerForNextGame(P player);

    /**
     * entry into the main game loop process. This will start the main game loop
     */
    void startGame();
}
