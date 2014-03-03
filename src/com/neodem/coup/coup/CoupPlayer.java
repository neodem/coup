package com.neodem.coup.coup;

import com.neodem.coup.game.*;

/**
 *
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupPlayer extends BasePlayer {

    public CoupPlayer(String playerName) {
         super(playerName);
    }

    @Override
    protected void initializePlayer(GameContext g) {


    }

    @Override
    public Action yourTurn(GameContext gc) {
        return null;
    }

    @Override
    public Action actionHappened(PlayerId playerId, Action hisAction, GameContext gc) {
        return null;
    }

    @Override
    public void alert(Alert a) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}
