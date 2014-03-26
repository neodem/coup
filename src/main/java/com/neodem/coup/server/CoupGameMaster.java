package com.neodem.coup.server;

import com.neodem.bandaid.game.BaseGameMaster;
import com.neodem.bandaid.game.GameContext;
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

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupGameMaster extends BaseGameMaster<CoupPlayer> {

    private static Log log = LogFactory.getLog(CoupGameMaster.class.getName());
    private ServerSideGameContext context;
    private ExchangeActionProcessor exchangeActionProcessor;
    private StealActionProcessor stealActionProcessor;
    private CoupActionProcessor coupActionProcessor;
    private ChallengeResolver challengeResolver;
    private CounterResolver counterResolver;
    private AssasinationProcessor assasianationProcessor;

    public CoupGameMaster() {
        super(4);
        context = new ServerSideGameContext();
    }

    @Override
    protected void initGame() {

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

        updatePlayers();
    }

    private void updatePlayers() {
        // alert players of current game context and their specific context
        GameContext gc = generateCurrentGameContext();
        for (CoupPlayer p : registeredPlayers) {
            p.updateContext(gc);
            p.updateInfo(context.getPlayerInfo(p).makePrivatePlayerInfo());
        }
    }

    @Override
    protected GameContext makeEmptyGameContextObject() {
        return new CoupGameContext();
    }

    @Override
    protected Log getLog() {
        return log;
    }

    @Override
    protected void runGameLoop() {
        CoupPlayer winningPlayer = null;
        while (winningPlayer == null) {
            for (CoupPlayer currentPlayer : registeredPlayers) {
                PlayerInfoState currentPlayerInfo = context.getPlayerInfo(currentPlayer);

                if (currentPlayerInfo.active) {

                    getLog().info(generateCurrentGameContext());
                    getLog().info("It is " + currentPlayer.getMyName() + "'s turn");

                    CoupAction currentAction = getValidCoupAction(currentPlayer, currentPlayerInfo);

                    // alert other players in sequence (and let them counter or challenge)
                    boolean actionSucceeds = alertOtherPlayers(currentPlayer, currentAction);

                    // process the action
                    if (actionSucceeds) processAction(currentPlayer, currentPlayerInfo, currentAction);

                    // evaluate end game (is there a winner?)
                    winningPlayer = evaluateGame();
                } else {
                    getLog().info(currentPlayer.getMyName() + " is not active.");
                }
            }
        }

        getLog().info("The game is over : " + winningPlayer.getMyName() + " was the winner!");
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
        getLog().debug("alertOtherPlayers()");

        // get a list of the players starting with the person taking the turn
        List<CoupPlayer> orderedPlayers = Lists.reorder(registeredPlayers, currentPlayer);

        // remove the currentPlayer
        orderedPlayers.remove(currentPlayer);

        // let all players know of the action
        getLog().debug("alerting other players of the action...");
        for (CoupPlayer op : orderedPlayers) {
            op.actionHappened(currentPlayer, currentAction, generateCurrentGameContext());
        }

        // remove inactive players
        for (Iterator<CoupPlayer> i = orderedPlayers.iterator(); i.hasNext(); ) {
            CoupPlayer p = i.next();
            if (!context.getPlayerInfo(p).active) {
                i.remove();
            }
        }

        // go to each one in turn and see if they want to challenge it
        if (currentAction.isChallengeable()) {
            getLog().debug("determining if players want to challenge this action...");
            for (CoupPlayer op : orderedPlayers) {
                if (op.doYouWantToChallengeThisAction(currentAction, currentPlayer, generateCurrentGameContext())) {
                    if (challengeResolver.resolveChallenge(op, currentPlayer, currentAction.getActionCard())) {
                        // if we are here, the challenge succeeded, thus the action failed
                        getLog().info("Action Failed.");
                        return false;
                    } else {
                        // no more challenges (?? is this a rule?)
                        break;
                    }
                }
            }
        } else {
            getLog().debug(currentAction + " is not Challengeable");
        }

        // go to each one in turn and see if they want to counter it
        if (currentAction.isCounterable()) {
            getLog().debug("determining if players want to counter this action...");
            for (CoupPlayer op : orderedPlayers) {
                if (op.doYouWantToCounterThisAction(currentAction, currentPlayer, generateCurrentGameContext())) {
                    if (counterResolver.resolveCounter(currentPlayer, op, currentAction)) {
                        // if we are here, the counter succeeded, thus the action was blocked/failed
                        getLog().info("Action Failed.");
                        return false;
                    } else {
                        // no more counters (?? is this a rule?)
                        break;
                    }
                }
            }
        } else {
            getLog().debug(currentAction + " is not Counterable");
        }

        getLog().info("Action succeeds.");
        return true;
    }

    private CoupAction getValidCoupAction(CoupPlayer p, PlayerInfoState info) {

        CoupAction a = p.yourTurn(generateCurrentGameContext());

        getLog().info(DisplayUtils.formatAction(a, p));

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
        a = p.yourTurn(generateCurrentGameContext());

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
        for (CoupPlayer p : registeredPlayers) {
            PlayerInfoState info = context.getPlayerInfo(p);
            if (info.evaluateActive()) {
                activeCount++;
                possibleWinner = p;
            } else {
                info.active = false;
                p.updateInfo(info.makePrivatePlayerInfo());
            }
        }

        if (activeCount == 1) return possibleWinner;

        return null;
    }

    private void validateAction(PlayerInfoState info, CoupAction a) throws PlayerError {
        if ((info.coins >= 10) && a.getActionType() != CoupAction.ActionType.Coup) {
            String msg = "Player has more than 10 coins and they need to Coup someone. Yet they didn't";
            getLog().error(msg);
            throw new PlayerError(msg);
        }

        if (info.coins < 7 && a.getActionType() == CoupAction.ActionType.Coup) {
            String msg = "Player has attempted a Coup but they only have " + info.coins + " coins (they need 7).";
            getLog().error(msg);
            throw new PlayerError(msg);
        }

        if (info.coins < 3 && a.getActionType() == CoupAction.ActionType.Assassinate) {
            String msg = "Player has attempted an Assasination but they only have " + info.coins + " coins (they need 3).";
            getLog().error(msg);
            throw new PlayerError(msg);
        }

        if (!CoupAction.isValidPlayableAction(a.getActionType())) {
            String msg = "Player has attempted an non playable action : " + a.getActionType();
            getLog().error(msg);
            throw new PlayerError(msg);
        }
    }

    /**
     * @param actingPlayer     the player doing the action
     * @param actingPlayerInfo the PIS of the acting player
     * @param currentAction    the current action
     */
    private void processAction(CoupPlayer actingPlayer, PlayerInfoState actingPlayerInfo, CoupAction currentAction) {
        switch (currentAction.getActionType()) {
            case Income:
                getLog().info(actingPlayer + " gets one coin");
                actingPlayerInfo.addCoin();
                break;
            case ForeignAid:
                getLog().info(actingPlayer + " gets two coins");
                actingPlayerInfo.addCoins(2);
                break;
            case Coup:
                coupActionProcessor.handleCoup(actingPlayer, currentAction.getActionOn());
                break;
            case Tax:
                getLog().info(actingPlayer + " gets two coins");
                actingPlayerInfo.addCoins(2);
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

        context.updatePlayer(actingPlayer, actingPlayerInfo);
        updatePlayers();
    }

    @Override
    protected GameContext generateCurrentGameContext() {

        CoupGameContext gc = (CoupGameContext) super.generateCurrentGameContext();

        for (CoupPlayer p : registeredPlayers) {
            PlayerInfoState pi = context.getPlayerInfo(p);
            if (pi != null) {
                gc.addInfo(p, pi.makePublicPlayerInfo());
            }
        }

        return gc;
    }
}
