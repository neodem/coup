package com.neodem.coup.server.game;

import com.neodem.bandaid.gamemaster.BaseGameMaster;
import com.neodem.bandaid.gamemaster.PlayerCallback;
import com.neodem.bandaid.gamemaster.PlayerError;
import com.neodem.common.utility.collections.Lists;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.CoupPlayerCallback;
import com.neodem.coup.common.game.actions.ComplexCoupAction;
import com.neodem.coup.common.game.actions.CoupAction;
import com.neodem.coup.common.game.actions.CoupAction.ActionType;
import com.neodem.coup.common.util.DisplayUtils;
import com.neodem.coup.server.game.actionProcessors.ActionProcessor;
import com.neodem.coup.server.game.actionProcessors.AssasinationProcessor;
import com.neodem.coup.server.game.actionProcessors.CoupActionProcessor;
import com.neodem.coup.server.game.actionProcessors.ExchangeActionProcessor;
import com.neodem.coup.server.game.actionProcessors.IncomeProcessor;
import com.neodem.coup.server.game.actionProcessors.StealActionProcessor;
import com.neodem.coup.server.game.resolvers.ChallengeResolver;
import com.neodem.coup.server.game.resolvers.CounterResolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.neodem.coup.common.game.actions.CoupAction.ActionType.Assassinate;
import static com.neodem.coup.common.game.actions.CoupAction.ActionType.Coup;
import static com.neodem.coup.common.game.actions.CoupAction.ActionType.Exchange;
import static com.neodem.coup.common.game.actions.CoupAction.ActionType.ForeignAid;
import static com.neodem.coup.common.game.actions.CoupAction.ActionType.Income;
import static com.neodem.coup.common.game.actions.CoupAction.ActionType.Steal;
import static com.neodem.coup.common.game.actions.CoupAction.ActionType.Tax;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupGameMaster extends BaseGameMaster {

    private static final Logger log = LogManager.getLogger(CoupGameMaster.class.getName());
    private ServerSideGameContext context;
    private Map<ActionType, ActionProcessor> actionProcessors;
    private ChallengeResolver challengeResolver;
    private CounterResolver counterResolver;
    private AssasinationProcessor assasinationProcessor;

    public static void main(String[] args) {
        String springContextFile = "server-config.xml";
        log.info(springContextFile);
        new ClassPathXmlApplicationContext(springContextFile);
    }

    @Override
    protected Logger getLog() {
        return log;
    }

    @Override
    public void initGame(List<PlayerCallback> registeredPlayers) {

        context = new ServerSideGameContext();

        // add players to context (this will set them up)
        for (PlayerCallback p : registeredPlayers) {
            context.addPlayer(p);
        }

        // setup processors
        actionProcessors = new HashMap<>();
        IncomeProcessor ip = new IncomeProcessor(context);
        actionProcessors.put(Income, ip);
        actionProcessors.put(ForeignAid, ip);
        actionProcessors.put(Tax, ip);
        actionProcessors.put(Coup, new CoupActionProcessor(context));

        assasinationProcessor = new AssasinationProcessor(context);
        actionProcessors.put(Assassinate, assasinationProcessor);
        actionProcessors.put(Exchange, new ExchangeActionProcessor(context));
        actionProcessors.put(Steal, new StealActionProcessor(context));

        // setup resolvers
        challengeResolver = new ChallengeResolver(context);
        counterResolver = new CounterResolver(challengeResolver);

        // initialize players
        for (CoupPlayerCallback p : context.getPlayerList()) {
            p.initializePlayer(getCurrentGameContext(p));
        }
    }

    @Override
    protected void runGame() {
        CoupPlayerCallback winningPlayer = null;
        while (winningPlayer == null) {

            for (CoupPlayerCallback currentPlayer : context.getPlayerList()) {
                log.info(context.generateCurrentPublicGameContext());
                PlayerInfoState currentPlayerInfo = context.getPlayerInfo(currentPlayer);
                if (currentPlayerInfo.isActive()) {

                    log.info("It is " + currentPlayer.getPlayerName() + "'s turn");

                    CoupAction currentAction = getValidCoupAction(currentPlayer);

                    // alert other players in sequence (and let them counter or challenge)
                    boolean actionSucceeds = alertOtherPlayers(currentPlayer, currentAction);

                    // process the action
                    if (actionSucceeds) processAction(currentPlayer, currentAction);

                    updatePlayers();

                    // evaluate end players (is there a winner?)
                    winningPlayer = evaluateGame();
                    if (winningPlayer != null) break;
                } else {
                    log.info(currentPlayer.getPlayerName() + " is not active.");
                }
            }
        }
        String msg = "The game is over : " + winningPlayer.getPlayerName() + " was the winner!";
        log.info(msg);

        for (CoupPlayerCallback p : context.getPlayerList()) {
            p.messageFromGM(msg);
        }
    }

    @Override
    protected String getGameThreadName() {
        return "Coup GameMaster";
    }

    public CoupGameContext getCurrentGameContext(CoupPlayerCallback p) {
        return context.generatePrivateGameContext(p);
    }

    /**
     * alert players of the current players context and their specific context
     */
    private void updatePlayers() {
        for (CoupPlayerCallback p : context.getPlayerList()) {
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
    private boolean alertOtherPlayers(CoupPlayerCallback currentPlayer, CoupAction currentAction) {
        log.debug("alertOtherPlayers()");

        // get a list of the players starting with the person taking the turn
        List<CoupPlayerCallback> orderedPlayers = Lists.reorder(context.getPlayerList(), currentPlayer);

        // remove the currentPlayer
        orderedPlayers.remove(currentPlayer);

        // let all players know of the action
        log.debug("alerting other players of the action...");
        for (CoupPlayerCallback op : orderedPlayers) {
            op.actionHappened(currentPlayer.getPlayerName(), currentAction, getCurrentGameContext(op));
        }

        // remove inactive players from our list
        for (Iterator<CoupPlayerCallback> i = orderedPlayers.iterator(); i.hasNext(); ) {
            CoupPlayerCallback p = i.next();
            if (!context.getPlayerInfo(p).isActive()) {
                i.remove();
            }
        }

        // go to each one in turn and see if they want to challenge it
        if (currentAction.getActionType().isChallengeable()) {
            boolean someoneChallenged = false;
            log.debug("determining if players want to challenge this action...");
            for (CoupPlayerCallback op : orderedPlayers) {
                if (op.doYouWantToChallengeThisAction(currentAction, currentPlayer.getPlayerName(), getCurrentGameContext(op))) {
                    someoneChallenged = true;
                    log.debug("{} is going to challenge this action", op.getPlayerName());
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
            if (!someoneChallenged) {
                log.info("No one decided to Challenge this action.");
            }
        } else {
            log.debug(currentAction + " is not Challengeable");
        }

        // go to each one in turn and see if they want to counter it
        if (currentAction.getActionType().isCounterable()) {

            if (currentAction.getActionType().isCounterableByGroup()) {
                boolean someoneCountered = false;
                log.debug("determining if players want to counter this action...");
                for (CoupPlayerCallback op : orderedPlayers) {
                    if (op.doYouWantToCounterThisAction(currentAction, currentPlayer.getPlayerName(), getCurrentGameContext(op))) {
                        someoneCountered = true;
                        log.debug("{} is going to counter this action", op.getPlayerName());
                        if (counterResolver.resolveCounter(currentPlayer, op, currentAction)) {
                            // if we are here, the counter succeeded, thus the action was blocked/failed
                            log.info("Counter Succeeded thus the Action Failed.");
                            return false;
                        }
                    }
                }
                if (!someoneCountered) {
                    log.info("No one attempted to Counter this action.");
                }
            } else {
                CoupPlayerCallback actionOn = context.getCoupPlayer(((ComplexCoupAction) currentAction).getActionOn());
                log.debug("seeing if " + actionOn.getPlayerName() + " wants to counter this action...");
                if (actionOn.doYouWantToCounterThisAction(currentAction, currentPlayer.getPlayerName(), getCurrentGameContext(actionOn))) {
                    log.debug("{} is going to counter this action", actionOn.getPlayerName());
                    if (counterResolver.resolveCounter(currentPlayer, actionOn, currentAction)) {
                        // if we are here, the counter succeeded, thus the action was blocked/failed
                        log.info("Counter Succeeded thus the Action Failed.");

                        if (currentAction.getActionType() == Assassinate) {
                            log.info(currentPlayer.getPlayerName() + " still has to pay for his/her assasination attempt!");
                            assasinationProcessor.handlePayment(currentPlayer);
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

    private CoupAction getValidCoupAction(CoupPlayerCallback p) {

        CoupAction a = p.yourTurn(getCurrentGameContext(p));

        log.info(DisplayUtils.formatAction(a, p.getPlayerName()));

        try {
            validateAction(p, a);
        } catch (PlayerError e) {
            return tryAgain(e, p);
        }

        return a;
    }

    private CoupAction tryAgain(PlayerError pe, CoupPlayerCallback p) {
        CoupAction a;

        p.tryAgain(pe.getMessage());
        a = p.yourTurn(getCurrentGameContext(p));

        try {
            validateAction(p, a);
        } catch (PlayerError e) {
            return tryAgain(e, p);
        }

        return a;
    }

    /**
     * determine who may still have a turn
     *
     * @return null if no winner, the player that won if there was
     */
    private CoupPlayerCallback evaluateGame() {
        int activeCount = 0;
        CoupPlayerCallback possibleWinner = null;
        for (CoupPlayerCallback p : context.getPlayerList()) {
            PlayerInfoState info = context.getPlayerInfo(p);
            if (info.isActive()) {
                activeCount++;
                possibleWinner = p;
            }
        }

        if (activeCount == 1) return possibleWinner;

        return null;
    }

    private void validateAction(CoupPlayerCallback actingPlayer, CoupAction a) throws PlayerError {
        if (!a.getActionType().isValidPlayableAction()) {
            String msg = "Player has attempted an non playable action : " + a.getActionType();
            log.error(msg);
            throw new PlayerError(msg);
        }

        for (ActionProcessor ap : actionProcessors.values()) {
            if (a instanceof ComplexCoupAction)
                ap.validate(actingPlayer, ((ComplexCoupAction) a).getActionOn(), a);
            else
                ap.validate(actingPlayer, null, a);

        }
    }

    /**
     * @param actingPlayer  the player doing the action
     * @param currentAction the current action
     */
    private void processAction(CoupPlayerCallback actingPlayer, CoupAction currentAction) {
        ActionProcessor ap = actionProcessors.get(currentAction.getActionType());

        if (currentAction instanceof ComplexCoupAction)
            ap.process(actingPlayer, ((ComplexCoupAction) currentAction).getActionOn(), currentAction);
        else
            ap.process(actingPlayer, null, currentAction);

        updatePlayers();
    }
}
