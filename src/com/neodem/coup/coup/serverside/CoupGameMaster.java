package com.neodem.coup.coup.serverside;

import com.neodem.coup.coup.CoupGameContext;
import com.neodem.coup.coup.CoupPlayer;
import com.neodem.coup.coup.CoupPlayerInfo;
import com.neodem.coup.coup.PlayerError;
import com.neodem.coup.coup.cards.CoupCard;
import com.neodem.coup.coup.cards.CoupDeck;
import com.neodem.coup.game.BaseGameMaster;
import com.neodem.coup.game.GameContext;
import com.neodem.coup.game.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupGameMaster extends BaseGameMaster {
    private Map<Player, CoupSSPlayerInfo> playerInfoMap;
    private CoupDeck deck;

    public CoupGameMaster() {
        super(4);
    }

    @Override
    protected void initGame() {
        deck = new CoupDeck();
        playerInfoMap = new HashMap<Player, CoupSSPlayerInfo>();

        for (Player p : registeredPlayers) {
            CoupSSPlayerInfo info = makeNewPlayerInfo();
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

            for (Player p : registeredPlayers) {
                CoupSSPlayerInfo info = playerInfoMap.get(p);

                if (info.active) {
                    CoupAction a = getValidCoupAction(p, info);

                    // alert other players in sequence
                    for (Player op : registeredPlayers) {
                        if (op == p) continue;
                        CoupAction opa = (CoupAction) op.actionHappened(p.getPlayerId(), a, generateCurrentGameContext());
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

                    // process the action
                    processAction(p, info, a);

                    // evaluate end game (is there a winner?)
                    if (evaluateGame()) {
                        break;
                    }
                }
            }
        }
    }

    private CoupAction getValidCoupAction(Player p, CoupSSPlayerInfo info) {
        CoupAction a;
        boolean playerCantGetTheirShitTogether = false;
        do {
            a = (CoupAction) p.yourTurn(generateCurrentGameContext());

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

        for (Player p : registeredPlayers) {
            CoupSSPlayerInfo info = playerInfoMap.get(p);
            if (info.evaluateActive()) activeCount++;
        }

        if (activeCount == 1) return true;

        return false;
    }

    private void validateAction(CoupSSPlayerInfo info, CoupAction a) throws PlayerError {
        if ((info.coins >= 10) && a.getActionType() != CoupAction.ActionType.Coup) {
            // error
            throw new PlayerError("You need to Coup. You have 10 or more coins");
        }

        if (info.coins < 7 && a.getActionType() == CoupAction.ActionType.Coup) {
            // not enough money
            throw new PlayerError("Player doesn't have enough coins to Coup : " + info.coins);
        }
    }

    /**
     * @param p
     * @param info
     * @param a
     * @return true if the action passed
     */
    private void processAction(Player p, CoupSSPlayerInfo info, CoupAction a) {
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

    private void handleSteal(CoupSSPlayerInfo info, CoupAction a) {
        CoupSSPlayerInfo oinfo = playerInfoMap.get(a.getActionOn().getPlayerId());

        // we may also get 0 or 1 coin here
        int coins = oinfo.removeCoins(2);
        info.addCoins(coins);
    }

    // TODO finish this method
    private void handleCoup(CoupSSPlayerInfo info) {

    }

    private void handleExchange(CoupPlayer p, CoupSSPlayerInfo info) {
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

    private CoupSSPlayerInfo makeNewPlayerInfo() {
        CoupSSPlayerInfo info = new CoupSSPlayerInfo();

        info.coins = 2;
        info.cardsInHand = new HashSet<CoupCard>();
        info.cardsInHand.add(deck.takeCard());
        info.cardsInHand.add(deck.takeCard());
        info.active = true;

        return info;
    }

    @Override
    protected GameContext generateCurrentGameContext() {

        CoupGameContext gc = (CoupGameContext) super.generateCurrentGameContext();

        for (Player p : playerInfoMap.keySet()) {
            CoupSSPlayerInfo pi = playerInfoMap.get(p);
            gc.addInfo(p.getPlayerId(), pi.makePlayerInfo());
        }

        return gc;
    }

    /**
     * this class is for the servers view of all the users
     */
    private class CoupSSPlayerInfo {
        public int coins;
        public Collection<CoupCard> cardsInHand;
        public boolean active;

        public CoupPlayerInfo makePlayerInfo() {
            CoupPlayerInfo pi = new CoupPlayerInfo();
            pi.coins = coins;

            for (CoupCard card : cardsInHand) {
                if (card.faceUp) {
                    pi.addUpCard(card);
                }
            }

            return pi;
        }

        public boolean evaluateActive() {
            short upCount = 0;
            for (CoupCard card : cardsInHand) {
                if (card.faceUp) {
                    upCount++;
                }
            }

            if (upCount == 2) active = false;

            return active;
        }

        public void addCoin() {
            coins++;
        }

        public int removeCoins(int i) {
            if (coins >= i) {
                coins = coins - i;
                return i;
            }

            int avail = coins;
            coins = 0;
            return avail;
        }

        public void addCoins(int i) {
            coins = coins + i;
        }
    }


}
