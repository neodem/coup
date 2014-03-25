package com.neodem.coup.serverside;

import com.neodem.bandaid.game.BaseGameMaster;
import com.neodem.bandaid.game.GameContext;
import com.neodem.common.utility.collections.Lists;
import com.neodem.coup.CoupAction;
import com.neodem.coup.CoupGameContext;
import com.neodem.coup.PlayerError;
import com.neodem.coup.players.CoupPlayer;

import java.util.List;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupGameMaster extends BaseGameMaster<CoupPlayer> {

    private ServerSideGameContext context;

    private ExchangeActionProcessor exchangeActionProcessor;
    private StealActionProcessor stealActionProcessor;
    private CoupActionProcessor coupActionProcessor;
    private ChallengeResolver challengeResolver;
    private CounterResolver counterResolver;

    public CoupGameMaster() {
        super(4);
    }

    @Override
    protected void initGame() {
        context = new ServerSideGameContext();

        for (CoupPlayer p : registeredPlayers) {
            context.addPlayer(p);
            p.updateInfo(context.getPlayerInfo(p).makePrivatePlayerInfo());
        }

        exchangeActionProcessor = new ExchangeActionProcessor(context);
        stealActionProcessor = new StealActionProcessor(context);
        coupActionProcessor = new CoupActionProcessor(context);
        challengeResolver = new ChallengeResolver(context);
        counterResolver = new CounterResolver(context);
    }

    @Override
    protected GameContext makeEmptyGameContextObject() {
        return new CoupGameContext();
    }

    @Override
    protected void runGameLoop() {
        boolean noWinner = true;
        while (noWinner) {
            for (CoupPlayer currentPlayer : registeredPlayers) {
                PlayerInfoState currentPlayerInfo = context.getPlayerInfo(currentPlayer);

                if (currentPlayerInfo.active) {
                    CoupAction currentAction = getValidCoupAction(currentPlayer, currentPlayerInfo);

                    // alert other players in sequence (and let them counter or challenge)
                    boolean actionSucceeds = alertOtherPlayers(currentPlayer, currentAction);

                    // process the action
                    if (actionSucceeds) processAction(currentPlayer, currentPlayerInfo, currentAction);

                    // evaluate end game (is there a winner?)
                    if (evaluateGame()) {
                        noWinner = false;
                        break;
                    }
                }
            }
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


        // get a list of the players starting with the person taking the turn
        List<CoupPlayer> orderedPlayers = Lists.reorder(registeredPlayers, currentPlayer);

        // go to each one in turn and let them know of the action and see if they want to challenge or counter it
        for (CoupPlayer op : orderedPlayers) {
            if (op == currentPlayer) continue;
            CoupAction opa = op.actionHappened(currentPlayer, currentAction, generateCurrentGameContext());
            if (opa.getActionType() == CoupAction.ActionType.NoAction) continue;
            if (opa.getActionType() == CoupAction.ActionType.Challenge) {
                if (currentAction.isChallengeable()) {
                    // resolve challenge
                    if (challengeResolver.resolveChallenge(currentPlayer, op, currentAction)) {
                        // if we are here, the challenge succeeded, thus the action failed
                        return false;
                    }
                }
            }
            if (opa.getActionType() == CoupAction.ActionType.Counter) {
                if (currentAction.isCounterable()) {
                    // resolve challenge
                    if (counterResolver.resolveCounter(currentPlayer, op, currentAction)) {
                        // if we are here, the counter succeeded, thus the action was blocked/failed
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private CoupAction getValidCoupAction(CoupPlayer p, PlayerInfoState info) {

        CoupAction a = p.yourTurn(generateCurrentGameContext());

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
     * @return true if there is a winner
     */
    private boolean evaluateGame() {
        int activeCount = 0;

        for (CoupPlayer p : registeredPlayers) {
            PlayerInfoState info = context.getPlayerInfo(p);
            if (info.evaluateActive()) activeCount++;
        }

        return activeCount == 1;
    }

    private void validateAction(PlayerInfoState info, CoupAction a) throws PlayerError {
        if ((info.coins >= 10) && a.getActionType() != CoupAction.ActionType.Coup) {
            // error
            throw new PlayerError("You need to Coup. You have 10 or more coins");
        }

        if (info.coins < 7 && a.getActionType() == CoupAction.ActionType.Coup) {
            // not enough money
            throw new PlayerError("Player doesn't have enough coins to Coup : " + info.coins);
        }

        if (!CoupAction.validPlayableAction(a.getActionType())) {
            throw new PlayerError("You need to perform a valid action on your turn");
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
                actingPlayerInfo.addCoin();
                break;
            case ForeignAid:
                actingPlayerInfo.addCoins(2);
                break;
            case Coup:
                coupActionProcessor.handleCoup(actingPlayer);
                break;
            case Tax:
                actingPlayerInfo.addCoins(2);
                break;
            case Assassinate:
                break;
            case Steal:
                stealActionProcessor.handleSteal(actingPlayer, currentAction);
                break;
            case Exchange:
                exchangeActionProcessor.handleExchange(actingPlayer);
                break;
        }

        context.updatePlayer(actingPlayer, actingPlayerInfo);
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
