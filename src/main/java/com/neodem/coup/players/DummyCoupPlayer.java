package com.neodem.coup.players;

import com.neodem.bandaid.game.BasePlayer;
import com.neodem.bandaid.game.GameContext;
import com.neodem.bandaid.game.Player;
import com.neodem.common.utility.collections.Lists;
import com.neodem.coup.CoupAction;
import com.neodem.coup.CoupAction.ActionType;
import com.neodem.coup.CoupPlayerInfo;
import com.neodem.coup.cards.CoupCard;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.neodem.coup.CoupAction.ActionType.Assassinate;
import static com.neodem.coup.CoupAction.ActionType.Coup;
import static com.neodem.coup.CoupAction.ActionType.Steal;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class DummyCoupPlayer extends BasePlayer<CoupAction> implements CoupPlayer {

    private CoupPlayerInfo myState = null;

    private Random r = new Random(System.currentTimeMillis());

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
        return r.nextBoolean();
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
        System.out.println(currentState);
        myState = currentState;
    }

    @Override
    public CoupAction yourTurn(GameContext gc) {
        System.out.println("my turn");
        System.out.println(gc);

        CoupPlayer actionOn = null;
        ActionType actionType = ActionType.values()[r.nextInt(ActionType.values().length)];

        if (actionType == Assassinate || actionType == Steal || actionType == Coup) {
            List<CoupPlayer> players = currentGameContext.getPlayers();
            actionOn = Lists.getRandomElement(players, this);
        }

        return new CoupAction(actionOn, actionType);
    }

    @Override
    public CoupAction actionHappened(Player player, CoupAction hisAction, GameContext gc) {
        String msg = String.format("%s : %s played %s, and I'm doing nothing ", playerName, player, hisAction);
        System.out.println(msg);

        int rand = r.nextInt(100);

        if (rand < 20) {
            return new CoupAction(null, CoupAction.ActionType.Challenge);
        }

        if (rand < 40) {
            return new CoupAction(null, CoupAction.ActionType.Counter);
        }

        return CoupAction.NoAction;
    }

    @Override
    public void tryAgain(String reason) {
        System.out.println("have to try again :" + reason);
    }
}
