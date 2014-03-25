package com.neodem.coup.players;

import com.neodem.bandaid.game.BasePlayer;
import com.neodem.bandaid.game.GameContext;
import com.neodem.bandaid.game.Player;
import com.neodem.common.utility.collections.Lists;
import com.neodem.coup.CoupAction;
import com.neodem.coup.CoupAction.ActionType;
import com.neodem.coup.CoupPlayerInfo;
import com.neodem.coup.cards.CoupCard;
import com.neodem.coup.util.DisplayUtils;
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
    public boolean doYouWantToCounterThisAction(CoupAction hisAction, CoupPlayer actingPlayer, GameContext gc) {
        int rand = r.nextInt(100);

        if (rand < 40) {
            String msg = String.format("%s : %s, and I'm Countering Them!", myName, DisplayUtils.formatAction(hisAction, actingPlayer));
            log.debug(msg);
            return true;
        }

        String msg = String.format("%s : %s, and I'm not Countering her.", myName, DisplayUtils.formatAction(hisAction, actingPlayer));
        log.debug(msg);
        return false;
    }

    @Override
    public boolean doYouWantToChallengeThisAction(CoupAction hisAction, CoupPlayer actingPlayer, GameContext gc) {
        int rand = r.nextInt(100);

        if (rand < 20) {
            String msg = String.format("%s : %s, and I'm Challenging Them!", myName, DisplayUtils.formatAction(hisAction, actingPlayer));
            log.debug(msg);
            return true;
        }

        String msg = String.format("%s : %s, and I'm not Challenging him", myName, DisplayUtils.formatAction(hisAction, actingPlayer));
        log.debug(msg);
        return false;
    }

    @Override
    public boolean doYouWantToChallengeThisCounter(CoupPlayer playerCountering) {
        int rand = r.nextInt(100);

        if (rand < 70) {
            String msg = String.format("%s : %s is trying to counter me, and I'm Challenging Them!", myName, playerCountering.getMyName());
            log.debug(msg);
            return true;
        }

        String msg = String.format("%s : %s is trying to counter me, and I'm not going to challenge them.", myName, playerCountering.getMyName());
        log.debug(msg);
        return false;
    }

    @Override
    public boolean doYouWantToProveYouHaveThisCard(CoupCard challengedCard) {
        return r.nextBoolean();
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
        //getLog().debug(gc);

        CoupPlayer actionOn = null;
        ActionType actionType = ActionType.values()[r.nextInt(ActionType.values().length)];

        if (actionType == Assassinate || actionType == Steal || actionType == Coup) {
            List<CoupPlayer> players = currentGameContext.getPlayers();
            actionOn = Lists.getRandomElement(players, this);
        }

        CoupAction action = new CoupAction(actionOn, actionType);
        getLog().debug("I will try : " + action);

        return action;
    }

    @Override
    public void actionHappened(Player player, CoupAction hisAction, GameContext gc) {
        String msg = String.format("%s : %s", myName, DisplayUtils.formatAction(hisAction, player));
        getLog().debug(msg);
    }

    @Override
    public void tryAgain(String reason) {
        getLog().info("have to try again :" + reason);
    }
}
