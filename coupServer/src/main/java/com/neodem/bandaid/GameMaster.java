package com.neodem.bandaid;

import com.neodem.coup.common.game.CoupCommunicationInterface;

import java.util.List;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/31/14
 */
public interface GameMaster {

    /**
     * let the game master get set up
     *
     * @param players the players who are going to be part of the game
     */
    void initGame(List<CoupCommunicationInterface> players);

    /**
     * this does not block. It will fire off a thread to run the game
     */
    void startGame();
}
