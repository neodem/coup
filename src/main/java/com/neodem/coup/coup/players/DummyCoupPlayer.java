package com.neodem.coup.coup.players;

import com.neodem.bandaid.game.BasePlayer;
import com.neodem.bandaid.game.GameContext;
import com.neodem.bandaid.game.Player;
import com.neodem.coup.coup.CoupAction;
import com.neodem.coup.coup.CoupPlayerInfo;
import com.neodem.coup.coup.cards.CoupCard;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class DummyCoupPlayer extends BasePlayer<CoupAction> implements CoupPlayer {

    private CoupPlayerInfo myState = null;

    @Override
    protected void initializePlayer(GameContext g) {
        System.out.println(g);
    }

    /**
     * player must return 2 cards out of the 4 given. (2 they already had)
     *
     * @param cards the given cards to select from
     * @return the return cards
     */
    public Collection<CoupCard> exchangeCards(Collection<CoupCard> cards) {
        Collection<CoupCard> returnedCards = new HashSet<>(2);

        Iterator<CoupCard> i = cards.iterator();
        returnedCards.add(i.next());
        returnedCards.add(i.next());

        return returnedCards;
    }

    @Override
    public boolean proveYouHaveCorrectCard(CoupAction challengedAction) {
        return false;
    }

    @Override
    public CoupCard looseAnInfluence() {
        if (myState.cardOne.faceUp) {
            return myState.cardTwo;
        }

        return myState.cardOne;
    }

    @Override
    public void updateInfo(CoupPlayerInfo currentState) {
        myState = currentState;
    }

    @Override
    public CoupAction yourTurn(GameContext gc) {
        System.out.println("my turn");
        System.out.println(gc);

        return new CoupAction(this, null, CoupAction.ActionType.Income);
    }

    @Override
    public CoupAction actionHappened(Player player, CoupAction hisAction, GameContext gc) {
        String msg = String.format("%s : %s played %s, and I'm doing nothing ", playerName, player, hisAction);
        System.out.println(msg);

        return CoupAction.NoAction;
    }

    @Override
    public void tryAgain(String reason) {
        System.out.println("have to try again :" + reason);
    }
}
