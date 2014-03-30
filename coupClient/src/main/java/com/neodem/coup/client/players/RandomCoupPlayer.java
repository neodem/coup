package com.neodem.coup.client.players;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.neodem.common.utility.collections.Lists;
import com.neodem.coup.common.game.CoupCommunicationInterface;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.actions.CoupAction;
import com.neodem.coup.common.game.actions.CoupAction.ActionType;
import com.neodem.coup.common.game.actions.CoupActionFactory;
import com.neodem.coup.common.game.cards.CoupCard;
import com.neodem.coup.common.game.cards.CoupCardType;
import com.neodem.coup.common.game.player.BaseCoupPlayer;
import com.neodem.coup.common.game.player.CoupPlayerInfo;
import com.neodem.coup.common.util.DisplayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.neodem.coup.common.game.actions.CoupAction.ActionType.Assassinate;
import static com.neodem.coup.common.game.actions.CoupAction.ActionType.Coup;
import static com.neodem.coup.common.game.actions.CoupAction.ActionType.Steal;

/**
 * This player plays randomly
 * <p/>
 * Author: vfumo
 * Date: 2/28/14
 */
public class RandomCoupPlayer extends BaseCoupPlayer implements CoupCommunicationInterface {
    private static final Logger log = LogManager.getLogger(RandomCoupPlayer.class.getName());
    protected CoupGameContext currentGameContext;
    private CoupPlayerInfo myState = null;
    private final Random r = new Random(System.currentTimeMillis());

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
    public void messageFromGM(String message) {
        String msg = String.format("%s : GameMaster said : '%s'", playerName, message);
        log.debug(msg);
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

        String actionOn = null;
        ActionType actionType;

        do {
            actionType = chooseRandomAction();
        } while ((actionType == Assassinate && myState.coins < 3) || (actionType == Coup && myState.coins < 7));

        if (actionType == Assassinate || actionType == Steal || actionType == Coup) {
            List<String> players = currentGameContext.getActivePlayers();
            actionOn = Lists.getRandomElement(players, playerName);
        }

        CoupAction action = CoupActionFactory.newAction(actionType, actionOn);
        getLog().debug(playerName + " : I will try : " + action);

        return action;
    }

    private ActionType chooseRandomAction() {
        ActionType actionType;

        if (myState.coins >= 10) {
            actionType = ActionType.Coup;
        } else do {
            actionType = ActionType.values()[r.nextInt(ActionType.values().length)];
        } while (!actionType.isValidPlayableAction());
        return actionType;
    }

    @Override
    public void actionHappened(String actingPlayer, CoupAction hisAction, CoupGameContext gc) {
        updateContext(gc);
        String msg = String.format("%s : %s", playerName, DisplayUtils.formatAction(hisAction, actingPlayer));
        getLog().debug(msg);
    }

    @Override
    public void tryAgain(String reason) {
        getLog().debug(String.format("%s : I have to try again because : %s", playerName, reason));
    }
}
