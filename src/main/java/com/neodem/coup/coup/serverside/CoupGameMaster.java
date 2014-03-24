package com.neodem.coup.coup.serverside;

import com.neodem.bandaid.game.BaseGameMaster;
import com.neodem.bandaid.game.GameContext;
import com.neodem.common.utility.collections.Lists;
import com.neodem.coup.coup.CoupAction;
import com.neodem.coup.coup.CoupGameContext;
import com.neodem.coup.coup.PlayerError;
import com.neodem.coup.coup.cards.CoupDeck;
import com.neodem.coup.coup.players.CoupPlayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupGameMaster extends BaseGameMaster<CoupPlayer> {

    // keeps track of the state of the players
    private Map<CoupPlayer, PlayerInfoState> playerInfoMap = new HashMap<>();
    // the deck we are using
    private CoupDeck deck;
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
        deck = new CoupDeck();

        playerInfoMap.clear();
        for (CoupPlayer p : registeredPlayers) {
            PlayerInfoState info = makeNewPlayerInfo(p);
            playerInfoMap.put(p, info);
            p.updateInfo(info.getPlayerInfo());
        }

        exchangeActionProcessor = new ExchangeActionProcessor(this);
        stealActionProcessor = new StealActionProcessor(this);
        coupActionProcessor = new CoupActionProcessor(this);
        challengeResolver = new ChallengeResolver(this);
        counterResolver = new CounterResolver(this);
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
                PlayerInfoState currentPlayerInfo = playerInfoMap.get(currentPlayer);

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
            PlayerInfoState info = playerInfoMap.get(p);
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

        playerInfoMap.put(actingPlayer, actingPlayerInfo);
    }

    private PlayerInfoState makeNewPlayerInfo(CoupPlayer p) {
        PlayerInfoState info = new PlayerInfoState();

        info.coins = 2;
        info.cardsInHand = new HashSet<>();
        info.cardsInHand.add(deck.takeCard());
        info.cardsInHand.add(deck.takeCard());
        info.active = true;
        info.name = p.getPlayerName();

        return info;
    }

    @Override
    protected GameContext generateCurrentGameContext() {

        CoupGameContext gc = (CoupGameContext) super.generateCurrentGameContext();

        for (CoupPlayer p : registeredPlayers) {
            PlayerInfoState pi = playerInfoMap.get(p);
            if (pi != null) {
                gc.addInfo(p, pi.makePlayerInfo());
            }
        }

        return gc;
    }

    public Map<CoupPlayer, PlayerInfoState> getPlayerInfoMap() {
        return playerInfoMap;
    }

    public CoupDeck getDeck() {
        return deck;
    }
}
