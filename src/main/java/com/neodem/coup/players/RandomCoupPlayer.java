package com.neodem.coup.players;

import com.neodem.bandaid.game.BasePlayer;
import com.neodem.bandaid.game.GameContext;
import com.neodem.bandaid.game.Player;
import com.neodem.common.utility.collections.Lists;
import com.neodem.coup.CoupAction;
import com.neodem.coup.CoupAction.ActionType;
import com.neodem.coup.CoupPlayerInfo;
import com.neodem.coup.cards.CoupCard;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
public class RandomCoupPlayer extends BasePlayer<CoupAction> implements CoupPlayer {
    private static Log log = LogFactory.getLog(RandomCoupPlayer.class.getName());
    private CoupPlayerInfo myState = null;
    private Random r = new Random(System.currentTimeMillis());

    @Override
    protected Log getLog() {
        return log;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void initializePlayer(GameContext g) {
    }

    @Override
    public boolean counterAction(CoupPlayer actingPlayer, CoupAction hisAction, GameContext gc) {
        int rand = r.nextInt(100);

        if (rand < 40) {
            String msg = String.format("%s : %s played %s on %s, and I'm Countering Them!", myName, actingPlayer, hisAction, hisAction.getActionOn());
            log.debug(msg);
            return true;
        }

        String msg = String.format("%s : %s played %s on %s, and I'm not Countering her.", myName, actingPlayer, hisAction, hisAction.getActionOn());
        log.debug(msg);
        return false;
    }

    @Override
    public boolean challengeAction(CoupPlayer actingPlayer, CoupAction hisAction, GameContext gc) {
        int rand = r.nextInt(100);

        if (rand < 20) {
            String msg = String.format("%s : %s played %s on %s, and I'm Challenging Them!", myName, actingPlayer, hisAction, hisAction.getActionOn());
            log.debug(msg);
            return true;
        }

        String msg = String.format("%s : %s played %s on %s, and I'm not Challenging him", myName, actingPlayer, hisAction, hisAction.getActionOn());
        log.debug(msg);
        return false;
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
        myState = currentState;
        getLog().debug(myName + "\nprivate state::\n" + currentState);
    }

    @Override
    public CoupAction yourTurn(GameContext gc) {
        getLog().debug(myName + " : my turn");
        getLog().debug(gc);

        CoupPlayer actionOn = null;
        ActionType actionType = ActionType.values()[r.nextInt(ActionType.values().length)];

        if (actionType == Assassinate || actionType == Steal || actionType == Coup) {
            List<CoupPlayer> players = currentGameContext.getPlayers();
            actionOn = Lists.getRandomElement(players, this);
        }

        return new CoupAction(actionOn, actionType);
    }

    @Override
    public void actionHappened(Player player, CoupAction hisAction, GameContext gc) {
        String msg = String.format("%s : %s played %s on %s", myName, player, hisAction, hisAction.getActionOn());
        getLog().debug(msg);
    }

    @Override
    public void tryAgain(String reason) {
        getLog().info("have to try again :" + reason);
    }
}
