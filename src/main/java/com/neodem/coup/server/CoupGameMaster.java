package com.neodem.coup.server;

import com.neodem.common.utility.collections.Lists;
import com.neodem.coup.common.CoupAction;
import com.neodem.coup.common.CoupGameContext;
import com.neodem.coup.common.CoupPlayer;
import com.neodem.coup.common.DisplayUtils;
import com.neodem.coup.common.PlayerError;
import com.neodem.coup.server.actionProcessors.AssasinationProcessor;
import com.neodem.coup.server.actionProcessors.ChallengeResolver;
import com.neodem.coup.server.actionProcessors.CounterResolver;
import com.neodem.coup.server.actionProcessors.CoupActionProcessor;
import com.neodem.coup.server.actionProcessors.ExchangeActionProcessor;
import com.neodem.coup.server.actionProcessors.StealActionProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.List;

import static com.neodem.coup.common.CoupAction.ActionType.Assassinate;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupGameMaster {

    private static Log log = LogFactory.getLog(CoupGameMaster.class.getName());
    private ServerSideGameContext context;
    private ExchangeActionProcessor exchangeActionProcessor;
    private StealActionProcessor stealActionProcessor;
    private CoupActionProcessor coupActionProcessor;
    private ChallengeResolver challengeResolver;
    private CounterResolver counterResolver;
    private AssasinationProcessor assasianationProcessor;

    public void initGame(List<CoupPlayer> registeredPlayers) {

        context = new ServerSideGameContext();

        // add players to context (this will set them up)
        for (CoupPlayer p : registeredPlayers) {
            context.addPlayer(p);
        }

        // setup processors
        exchangeActionProcessor = new ExchangeActionProcessor(context);
        stealActionProcessor = new StealActionProcessor(context);
        coupActionProcessor = new CoupActionProcessor(context);
        challengeResolver = new ChallengeResolver(context);
        counterResolver = new CounterResolver(context, challengeResolver);
        assasianationProcessor = new AssasinationProcessor(context);

        initPlayers();
    }

    private void initPlayers() {
        for (CoupPlayer p : context.getPlayerList()) {
            p.initializePlayer(getCurrentGameContext(p));
        }
    }

    public CoupPlayer runGameLoop() {
        CoupPlayer winningPlayer = null;
        while (winningPlayer == null) {
            for (CoupPlayer currentPlayer : context.getPlayerList()) {
                PlayerInfoState currentPlayerInfo = context.getPlayerInfo(currentPlayer);

                if (currentPlayerInfo.isActive()) {

                    log.info(context.generateCurrentPublicGameContext());
                    log.info("It is " + currentPlayer.getMyName() + "'s turn");

                    CoupAction currentAction = getValidCoupAction(currentPlayer, currentPlayerInfo);

                    // alert other players in sequence (and let them counter or challenge)
                    boolean actionSucceeds = alertOtherPlayers(currentPlayer, currentAction);

                    // process the action
                    if (actionSucceeds) processAction(currentPlayer, currentAction);

                    updatePlayers();

                    // evaluate end game (is there a winner?)
                    winningPlayer = evaluateGame();
                } else {
                    log.info(currentPlayer.getMyName() + " is not active.");
                }
            }
        }

        return winningPlayer;
    }

    public CoupGameContext getCurrentGameContext(CoupPlayer p) {
        return context.generatePrivateGameContext(p);
    }

    /**
     * alert players of the current game context and their specific context
     */
    private void updatePlayers() {
        for (CoupPlayer p : context.getPlayerList()) {
            p.updateContext(getCurrentGameContext(p));
        }
    }

    /**
     * Alert the other players in clockwise order and allow each the opportunity to challenge or counter
     * the current action.
     *
     * @param currentPlayer the current player (the player whos turn it is)
     * @param currentAction the action being played
     * @return true if the action succeeds or false if the action is over (challenged or countered)
     */
    private boolean alertOtherPlayers(CoupPlayer currentPlayer, CoupAction currentAction) {
        log.debug("alertOtherPlayers()");

        // get a list of the players starting with the person taking the turn
        List<CoupPlayer> orderedPlayers = Lists.reorder(context.getPlayerList(), currentPlayer);

        // remove the currentPlayer
        orderedPlayers.remove(currentPlayer);

        // let all players know of the action
        log.debug("alerting other players of the action...");
        for (CoupPlayer op : orderedPlayers) {
            op.actionHappened(currentPlayer, currentAction, getCurrentGameContext(op));
        }

        // remove inactive players from our list
        for (Iterator<CoupPlayer> i = orderedPlayers.iterator(); i.hasNext(); ) {
            CoupPlayer p = i.next();
            if (!context.getPlayerInfo(p).isActive()) {
                i.remove();
            }
        }

        // go to each one in turn and see if they want to challenge it
        if (currentAction.isChallengeable()) {
            log.debug("determining if players want to challenge this action...");
            for (CoupPlayer op : orderedPlayers) {
                if (op.doYouWantToChallengeThisAction(currentAction, currentPlayer, getCurrentGameContext(op))) {
                    if (challengeResolver.resolveChallenge(op, currentPlayer, currentAction.getActionCard())) {
                        // if we are here, the challenge succeeded, thus the action failed
                        log.info("Challenge Succeeded thus the Action Failed.");
                        return false;
                    } else {
                        // no more challenges (?? is this a rule?)
                        break;
                    }
                }
            }
        } else {
            log.debug(currentAction + " is not Challengeable");
        }

        // go to each one in turn and see if they want to counter it
        if (currentAction.isCounterable()) {

            if (currentAction.isCounterableByGroup()) {
                log.debug("determining if players want to counter this action...");
                for (CoupPlayer op : orderedPlayers) {
                    if (op.doYouWantToCounterThisAction(currentAction, currentPlayer, getCurrentGameContext(op))) {
                        if (counterResolver.resolveCounter(currentPlayer, op, currentAction)) {
                            // if we are here, the counter succeeded, thus the action was blocked/failed
                            log.info("Counter Succeeded thus the Action Failed.");
                            return false;
                        }
                    }
                }
            } else {
                CoupPlayer actionOn = currentAction.getActionOn();
                log.debug("seeing if " + actionOn.getMyName() + " wants to counter this action...");
                if (actionOn.doYouWantToCounterThisAction(currentAction, currentPlayer, getCurrentGameContext(actionOn))) {
                    if (counterResolver.resolveCounter(currentPlayer, actionOn, currentAction)) {
                        // if we are here, the counter succeeded, thus the action was blocked/failed
                        log.info("Counter Succeeded thus the Action Failed.");

                        if (currentAction.getActionType() == Assassinate) {
                            log.info(currentPlayer.getMyName() + " still has to pay for his/her assasination attempt!");
                            assasianationProcessor.handlePayment(currentPlayer);
                        }

                        return false;
                    }
                }
            }
        } else {
            log.debug(currentAction + " is not Counterable");
        }

        log.info("Action succeeds.");
        return true;
    }

    private CoupAction getValidCoupAction(CoupPlayer p, PlayerInfoState info) {

        CoupAction a = p.yourTurn(getCurrentGameContext(p));

        log.info(DisplayUtils.formatAction(a, p));

        try {
            validateAction(info, a);
        } catch (PlayerError e) {
            return tryAgain(e, p, info);
        }

        return a;
    }

    private CoupAction tryAgain(PlayerError pe, CoupPlayer p, PlayerInfoState info) {
        CoupAction a;

        p.tryAgain(pe.getMessage());
        a = p.yourTurn(getCurrentGameContext(p));

        try {
            validateAction(info, a);
        } catch (PlayerError e) {
            return tryAgain(e, p, info);
        }

        return a;
    }

    /**
     * determine who may still have a turn
     *
     * @return null if no winner, the player that won if there was
     */
    private CoupPlayer evaluateGame() {
        int activeCount = 0;
        CoupPlayer possibleWinner = null;
        for (CoupPlayer p : context.getPlayerList()) {
            PlayerInfoState info = context.getPlayerInfo(p);
            if (info.isActive()) {
                activeCount++;
                possibleWinner = p;
            }
        }

        if (activeCount == 1) return possibleWinner;

        return null;
    }

    private void validateAction(PlayerInfoState info, CoupAction a) throws PlayerError {
        if ((info.getCoinCount() >= 10) && a.getActionType() != CoupAction.ActionType.Coup) {
            String msg = "Player has more than 10 coins and they need to Coup someone. Yet they didn't";
            log.error(msg);
            throw new PlayerError(msg);
        }

        if (info.getCoinCount() < 7 && a.getActionType() == CoupAction.ActionType.Coup) {
            String msg = "Player has attempted a Coup but they only have " + info.getCoinCount() + " coins (they need 7).";
            log.error(msg);
            throw new PlayerError(msg);
        }

        if (info.getCoinCount() < 3 && a.getActionType() == CoupAction.ActionType.Assassinate) {
            String msg = "Player has attempted an Assasination but they only have " + info.getCoinCount() + " coins (they need 3).";
            log.error(msg);
            throw new PlayerError(msg);
        }

        if (!CoupAction.isValidPlayableAction(a.getActionType())) {
            String msg = "Player has attempted an non playable action : " + a.getActionType();
            log.error(msg);
            throw new PlayerError(msg);
        }

        if (a.getActionOn() != null && !context.isPlayerActive(a.getActionOn())) {
            String msg = "Player has attempted to act upon a player that is already inactive : " + a.getActionOn();
            log.error(msg);
            throw new PlayerError(msg);
        }
    }

    /**
     * @param actingPlayer  the player doing the action
     * @param currentAction the current action
     */
    private void processAction(CoupPlayer actingPlayer, CoupAction currentAction) {
        switch (currentAction.getActionType()) {
            case Income:
                log.info(actingPlayer + " gets one coin");
                context.addCoinsToPlayer(1, actingPlayer);
                break;
            case ForeignAid:
                log.info(actingPlayer + " gets two coins");
                context.addCoinsToPlayer(2, actingPlayer);
                break;
            case Coup:
                coupActionProcessor.handleCoup(actingPlayer, currentAction.getActionOn());
                break;
            case Tax:
                log.info(actingPlayer + " gets two coins");
                context.addCoinsToPlayer(2, actingPlayer);
                break;
            case Assassinate:
                assasianationProcessor.handleAssasinate(actingPlayer, currentAction.getActionOn());
                break;
            case Steal:
                stealActionProcessor.handleSteal(actingPlayer, currentAction);
                break;
            case Exchange:
                exchangeActionProcessor.handleExchange(actingPlayer);
                break;
        }

        updatePlayers();
    }
}
