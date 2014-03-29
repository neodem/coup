package com.neodem.coup.client.game;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.neodem.common.utility.collections.Lists;
import com.neodem.coup.common.DisplayUtils;
import com.neodem.coup.common.game.BaseCoupPlayer;
import com.neodem.coup.common.game.CoupAction;
import com.neodem.coup.common.game.CoupAction.ActionType;
import com.neodem.coup.common.game.CoupCard;
import com.neodem.coup.common.game.CoupCardType;
import com.neodem.coup.common.game.CoupCommunicationInterface;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.CoupPlayerInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.neodem.coup.common.game.CoupAction.ActionType.Assassinate;
import static com.neodem.coup.common.game.CoupAction.ActionType.Coup;
import static com.neodem.coup.common.game.CoupAction.ActionType.Steal;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class RandomCoupPlayer extends BaseCoupPlayer implements CoupCommunicationInterface {
    private static Logger log = LogManager.getLogger(RandomCoupPlayer.class.getName());
    protected CoupGameContext currentGameContext;
    private CoupPlayerInfo myState = null;
    private Random r = new Random(System.currentTimeMillis());

    public RandomCoupPlayer(String name) {
        super(name);
    }

    @Override
    public void updateContext(CoupGameContext gc) {
        currentGameContext = gc;
        myState = gc.getCoupPlayerInfos().get(playerName);
    }

    @Override
    public void initializePlayer(CoupGameContext g) {
        updateContext(g);
    }

    @Override
    protected Logger getLog() {
        return log;
    }

    @Override
    public boolean doYouWantToCounterThisAction(CoupAction hisAction, String actingPlayer, CoupGameContext gc) {
        updateContext(gc);
        int rand = r.nextInt(100);

        if (rand < 40) {
            String msg = String.format("%s : %s, and I'm Countering Them!", playerName, DisplayUtils.formatAction(hisAction, actingPlayer));
            log.debug(msg);
            return true;
        }

        String msg = String.format("%s : %s, and I'm not Countering her.", playerName, DisplayUtils.formatAction(hisAction, actingPlayer));
        log.debug(msg);
        return false;
    }

    @Override
    public boolean doYouWantToChallengeThisAction(CoupAction hisAction, String actingPlayer, CoupGameContext gc) {
        updateContext(gc);
        int rand = r.nextInt(100);

        if (rand < 20) {
            String msg = String.format("%s : %s, and I'm Challenging Them!", playerName, DisplayUtils.formatAction(hisAction, actingPlayer));
            log.debug(msg);
            return true;
        }

        String msg = String.format("%s : %s, and I'm not Challenging him", playerName, DisplayUtils.formatAction(hisAction, actingPlayer));
        log.debug(msg);
        return false;
    }

    @Override
    public boolean doYouWantToChallengeThisCounter(String counteringPlayer) {
        int rand = r.nextInt(100);

        if (rand < 70) {
            String msg = String.format("%s : %s is trying to counter me, and I'm Challenging Them!", playerName, counteringPlayer);
            log.debug(msg);
            return true;
        }

        String msg = String.format("%s : %s is trying to counter me, and I'm not going to challenge them.", playerName, counteringPlayer);
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

        getLog().debug(playerName + " : my turn");
        //getLog().debug(gc);

        String actionOn = null;
        ActionType actionType = ActionType.values()[r.nextInt(ActionType.values().length)];

        if (actionType == Assassinate || actionType == Steal || actionType == Coup) {
            List<String> players = currentGameContext.getPlayers();
            actionOn = Lists.getRandomElement(players, playerName);
        }

        CoupAction action = new CoupAction(actionOn, actionType);
        getLog().debug(playerName + " : I will try : " + action);

        return action;
    }

    @Override
    public void actionHappened(String actingPlayer, CoupAction hisAction, CoupGameContext gc) {
        updateContext(gc);
        String msg = String.format("%s : %s", playerName, DisplayUtils.formatAction(hisAction, actingPlayer));
        getLog().debug(msg);
    }

    @Override
    public void tryAgain(String reason) {
        getLog().info(String.format("%s : I have to try again because : %s", playerName, reason));
    }
}
