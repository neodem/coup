package com.neodem.coup.coup.serverside;

import com.neodem.common.utility.collections.Lists;
import com.neodem.coup.coup.CoupAction;
import com.neodem.coup.coup.CoupGameContext;
import com.neodem.coup.coup.players.CoupPlayer;
import com.neodem.coup.coup.CoupPlayerInfo;
import com.neodem.coup.coup.PlayerError;
import com.neodem.coup.coup.cards.CoupCard;
import com.neodem.coup.coup.cards.CoupDeck;
import com.neodem.coup.game.BaseGameMaster;
import com.neodem.coup.game.GameContext;

import java.util.*;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupGameMaster extends BaseGameMaster<CoupPlayer> {

    // keeps track of the state of the players
    private Map<CoupPlayer, PlayerInfoState> playerInfoMap = new HashMap<CoupPlayer, PlayerInfoState>();;

    // the deck we are using
    private CoupDeck deck;

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
        }
    }

    @Override
    protected GameContext makeEmptyGameContextObject() {
        return new CoupGameContext();
    }

    @Override
    protected void runGameLoop() {
        while (true) {

            for (CoupPlayer currentPlayer : registeredPlayers) {
                PlayerInfoState currentPlayerInfo = playerInfoMap.get(currentPlayer);

                if (currentPlayerInfo.active) {
                    CoupAction currentAction = getValidCoupAction(currentPlayer, currentPlayerInfo);

                    // alert other players in sequence (and let them counter or challenge)
                    alertOtherPlayers(currentPlayer, currentAction);

                    // process the action
                    processAction(currentPlayer, currentPlayerInfo, currentAction);

                    // evaluate end game (is there a winner?)
                    if (evaluateGame()) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * *
     * @param p
     * @param a
     */
    private void alertOtherPlayers(CoupPlayer p, CoupAction a) {
        List<CoupPlayer> orderedPlayers = Lists.reorder(registeredPlayers, p);

        for (CoupPlayer op : orderedPlayers) {
            if (op == p) continue;
            CoupAction opa =  op.actionHappened(p, a, generateCurrentGameContext());
            if (opa.getActionType() == CoupAction.ActionType.NoAction) continue;
            if (opa.getActionType() == CoupAction.ActionType.Challenge) {
                // resolve challenge
                //TODO impl this.
            }
            if (opa.getActionType() == CoupAction.ActionType.Counter) {
                // resolve counter
                // TODO impl this.
            }
        }
    }

    private CoupAction getValidCoupAction(CoupPlayer p, PlayerInfoState info) {
        CoupAction a;
        boolean playerCantGetTheirShitTogether = false;
        do {
            a = p.yourTurn(generateCurrentGameContext());

            try {
                validateAction(info, a);
            } catch (PlayerError e) {
                p.tryAgain(e.getMessage());
                playerCantGetTheirShitTogether = true;
            }
        } while (playerCantGetTheirShitTogether);


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

        if (activeCount == 1) return true;

        return false;
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
     * @param p
     * @param info
     * @param a
     * @return true if the action passed
     */
    private void processAction(CoupPlayer p, PlayerInfoState info, CoupAction a) {
        switch (a.getActionType()) {
            case Income:
                info.addCoin();
                break;
            case ForeignAid:
                info.addCoins(2);
                break;
            case Coup:
                handleCoup(info);
                break;
            case Tax:
                info.addCoins(2);
                break;
            case Assassinate:
                break;
            case Steal:
                handleSteal(info, a);
                break;
            case Exchange:
                handleExchange((CoupPlayer) p, info);
                break;
        }

        playerInfoMap.put(p, info);
    }

    private void handleSteal(PlayerInfoState info, CoupAction a) {
        PlayerInfoState oinfo = playerInfoMap.get(a.getActionOn());

        // we may also get 0 or 1 coin here
        int coins = oinfo.removeCoins(2);
        info.addCoins(coins);
    }

    // TODO finish this method
    private void handleCoup(PlayerInfoState info) {

    }

    private void handleExchange(CoupPlayer p, PlayerInfoState info) {
        // TODO : deal with the case where 1 card is up!

        info.cardsInHand.add(deck.takeCard());
        info.cardsInHand.add(deck.takeCard());

        // deal with exchange  (collection must return 2 cards)
        Collection<CoupCard> returnedCards = ((CoupPlayer) p).exchangeCards(info.cardsInHand);

        if (returnedCards.size() != 2) {
            // error..
        }

        for (CoupCard c : returnedCards) {
            info.cardsInHand.remove(c);
            deck.putCard(c);
        }
    }

    private PlayerInfoState makeNewPlayerInfo(CoupPlayer p) {
        PlayerInfoState info = new PlayerInfoState();

        info.coins = 2;
        info.cardsInHand = new HashSet<CoupCard>();
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
            if(pi != null) {
                gc.addInfo(p, pi.makePlayerInfo());
            }
        }

        return gc;
    }
}
