package com.neodem.coup.coup.serverside;

import com.neodem.coup.coup.CoupAlert;
import com.neodem.coup.coup.CoupGameContext;
import com.neodem.coup.coup.CoupPlayerInfo;
import com.neodem.coup.coup.cards.CoupCard;
import com.neodem.coup.coup.cards.CoupDeck;
import com.neodem.coup.game.BaseGameMaster;
import com.neodem.coup.game.GameContext;
import com.neodem.coup.game.Player;

import java.util.Collection;
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

    protected GameContext makeEmptyGameContextObject() {
        return new CoupGameContext();
    }

    @Override
    protected void runGameLoop() {
        while (true) {


            for (Player p : registeredPlayers) {
                CoupSSPlayerInfo info =  playerInfoMap.get(p);
                boolean mustCoup = false;

                // does the player have a manditory action?
                if(info.coins >= 10) {
                    // player must coup.
                    p.alert(CoupAlert.MustCoup);
                    mustCoup = true;
                }

                //  let the player take his turn
                CoupAction a = (CoupAction) p.yourTurn(generateCurrentGameContext());

                if(mustCoup && a.getActionType() != CoupAction.ActionType.Coup) {
                       // error
                }

                // alert other players in sequence
                for (Player op : registeredPlayers) {
                    if (op == p) continue;
                    CoupAction opa = (CoupAction) op.actionHappened(p.getPlayerId(), a, generateCurrentGameContext());
                    if (opa.getActionType() == CoupAction.ActionType.NoAction) continue;
                    if (opa.getActionType() == CoupAction.ActionType.Challenge) {
                        // resolve challenge
                    }
                    if (opa.getActionType() == CoupAction.ActionType.Counter) {
                        // resolve counter
                    }
                }

                // process the action
                processAction(p, info, a);

                // evaluate end game (is there a winner?)

            }
        }
    }

    private void processAction(Player p, CoupSSPlayerInfo info, CoupAction a) {

        switch (a.getActionType()) {
            case Income:
                info.addCoin();
                break;
            case ForeignAid:
                info.addCoins(2);
                break;
            case Coup:
                if (info.coins < 7) {
                    // not enough money
                } else {
                    // deal with coup
                }
                break;
            case Tax:
                info.addCoins(2);
                break;
            case Assassinate:
                break;
            case Steal:
                CoupSSPlayerInfo oinfo = playerInfoMap.get(a.getActionOn().getPlayerId());

                // we may also get 0 or 1 coin here
                int coins = oinfo.removeCoins(2);
                info.addCoins(coins);

                break;
            case Exchange:
                CoupCard c1 = deck.takeCard();
                CoupCard c2 = deck.takeCard();

                // deal with exchange  (collection must return 2 cards)
                Collection<CoupCard> returnedCards = dealWithExchange(c1, c2, p, info);

                for (CoupCard c : returnedCards) {
                    deck.putCard(c);
                }

                break;
        }


        playerInfoMap.put(p, info);
    }

    @Override
    protected void initGame() {
        deck = new CoupDeck();

        for (Player p : registeredPlayers) {
            CoupSSPlayerInfo info = makeNewPlayerInfo();
            playerInfoMap.put(p, info);
        }
    }

    private CoupSSPlayerInfo makeNewPlayerInfo() {
        CoupSSPlayerInfo info = new CoupSSPlayerInfo();

        info.coins = 2;
        info.cardsInHand = new HashSet<CoupCard>();
        info.cardsInHand.add(deck.takeCard());
        info.cardsInHand.add(deck.takeCard());

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

    private class CoupSSPlayerInfo {
        public int coins;
        public Collection<CoupCard> cardsInHand;

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

        public void addCoin() {
            coins++;
        }

        public int removeCoins(int i) {
            if(coins >= i) {
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
