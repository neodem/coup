package com.neodem.coup.coup;

import com.neodem.coup.coup.cards.CoupCard;
import com.neodem.coup.game.*;

import java.util.Collection;

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

    /**
     * player must return 2 cards out of the 4 given. (2 they already had)
     * @param cards
     * @return
     */
    public Collection<CoupCard> exchangeCards(Collection<CoupCard> cards) {
        return null;  //To change body of created methods use File | Settings | File Templates.
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
    public void tryAgain(String reason) {
        //To change body of implemented methods use File | Settings | File Templates.
    }



}
