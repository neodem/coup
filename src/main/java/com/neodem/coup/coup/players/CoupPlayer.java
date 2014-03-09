package com.neodem.coup.coup.players;

import com.neodem.coup.coup.CoupAction;
import com.neodem.coup.coup.cards.CoupCard;
import com.neodem.coup.game.Action;
import com.neodem.coup.game.BasePlayer;
import com.neodem.coup.game.GameContext;
import com.neodem.coup.game.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupPlayer extends BasePlayer {


    @Override
    protected void initializePlayer(GameContext g) {
        System.out.println(g);
    }

    /**
     * player must return 2 cards out of the 4 given. (2 they already had)
     *
     * @param cards
     * @return
     */
    public Collection<CoupCard> exchangeCards(Collection<CoupCard> cards) {
        Collection<CoupCard> returnedCards = new HashSet<CoupCard>(2);

        Iterator<CoupCard> i = cards.iterator();
        returnedCards.add(i.next());
        returnedCards.add(i.next());

        return returnedCards;
    }

    @Override
    public Action yourTurn(GameContext gc) {
        System.out.println("my turn");
        System.out.println(gc);

        return new CoupAction(this, null, CoupAction.ActionType.Income);
    }

    @Override
    public Action actionHappened(Player player, Action hisAction, GameContext gc) {
        String msg = String.format("%s : %s played %s, and I'm doing nothing ", playerName, player, hisAction);
        System.out.println(msg);

        return CoupAction.NoAction;
    }

    @Override
    public void tryAgain(String reason) {
        System.out.println("have to try again :" + reason);
    }


}
