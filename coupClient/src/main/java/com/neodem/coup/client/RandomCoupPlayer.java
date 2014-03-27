package com.neodem.coup.client;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.neodem.common.utility.collections.Lists;
import com.neodem.coup.common.CoupAction;
import com.neodem.coup.common.CoupAction.ActionType;
import com.neodem.coup.common.CoupCard;
import com.neodem.coup.common.CoupCardType;
import com.neodem.coup.common.CoupGameContext;
import com.neodem.coup.common.CoupPlayer;
import com.neodem.coup.common.CoupPlayerInfo;
import com.neodem.coup.common.DisplayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.neodem.coup.common.CoupAction.ActionType.Assassinate;
import static com.neodem.coup.common.CoupAction.ActionType.Coup;
import static com.neodem.coup.common.CoupAction.ActionType.Steal;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class RandomCoupPlayer implements CoupPlayer, Serializable {
    private static Log log = LogFactory.getLog(RandomCoupPlayer.class.getName());
    protected String myName;
    protected CoupGameContext currentGameContext;
    private CoupPlayerInfo myState = null;
    private Random r = new Random(System.currentTimeMillis());

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RandomCoupPlayer)) return false;

        RandomCoupPlayer that = (RandomCoupPlayer) o;

        if (myName != null ? !myName.equals(that.myName) : that.myName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return myName != null ? myName.hashCode() : 0;
    }

    public RandomCoupPlayer(String name) {
        myName = name;
    }

    @Override
    public String toString() {
        return myName;
    }

    @Override
    public void updateContext(CoupGameContext gc) {
        currentGameContext = gc;
        myState = gc.playerInfos.get(this);
    }

    @Override
    public void initializePlayer(CoupGameContext g) {
        currentGameContext = g;
        myState = g.playerInfos.get(this);
    }

    protected Log getLog() {
        return log;
    }

    @Override
    public boolean doYouWantToCounterThisAction(CoupAction hisAction, CoupPlayer actingPlayer, CoupGameContext gc) {
        updateContext(gc);
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
    public boolean doYouWantToChallengeThisAction(CoupAction hisAction, CoupPlayer actingPlayer, CoupGameContext gc) {
        updateContext(gc);
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
    public boolean doYouWantToProveYouHaveACardOfThisType(CoupCardType challengedCard) {
        return r.nextBoolean();
    }

    /**
     * player must return 2 cards out of the 4 given. (2 they already had)
     *
     * @param cards the given cards to select from
     * @return the return cards
     */
    public Multiset<CoupCard> exchangeCards(Multiset<CoupCard> cards) {

        Multiset<CoupCard> returnedCards = HashMultiset.create();

        Iterator<CoupCard> i = cards.iterator();
        returnedCards.add(i.next());
        returnedCards.add(i.next());

        return returnedCards;
    }

    @Override
    public CoupCard youMustLooseAnInfluence() {
        if (myState.cardOne.faceUp) {
            return myState.cardTwo;
        }

        return myState.cardOne;
    }

    @Override
    public CoupAction yourTurn(CoupGameContext gc) {
        updateContext(gc);

        getLog().debug(myName + " : my turn");
        //getLog().debug(gc);

        CoupPlayer actionOn = null;
        ActionType actionType = ActionType.values()[r.nextInt(ActionType.values().length)];

        if (actionType == Assassinate || actionType == Steal || actionType == Coup) {
            List<CoupPlayer> players = currentGameContext.getPlayers();
            actionOn = Lists.getRandomElement(players, this);
        }

        CoupAction action = new CoupAction(actionOn, actionType);
        getLog().debug(myName + " : I will try : " + action);

        return action;
    }

    @Override
    public void actionHappened(CoupPlayer player, CoupAction hisAction, CoupGameContext gc) {
        updateContext(gc);
        String msg = String.format("%s : %s", myName, DisplayUtils.formatAction(hisAction, player));
        getLog().debug(msg);
    }

    @Override
    public void tryAgain(String reason) {
        getLog().info(String.format("%s : I have to try again because : %s", myName, reason));
    }

    public String getMyName() {
        return myName;
    }
}
