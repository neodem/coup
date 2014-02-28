package com.neodem.coup.coup.serverside;

import com.neodem.coup.coup.CoupGameContext;
import com.neodem.coup.coup.CoupPlayerInfo;
import com.neodem.coup.coup.cards.CoupCard;
import com.neodem.coup.coup.cards.CoupDeck;
import com.neodem.coup.game.BaseGameMaster;
import com.neodem.coup.game.GameContext;
import com.neodem.coup.game.PlayerId;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupGameMaster extends BaseGameMaster {
    private Map<PlayerId, CoupSSPlayerInfo> playerInfoMap;
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

        }
    }

    @Override
    protected void initGame() {
        deck = new CoupDeck();

        for (PlayerId pid : registeredPlayers) {
            CoupSSPlayerInfo info = makeNewPlayerInfo();
            playerInfoMap.put(pid, info);
        }
    }

    private CoupSSPlayerInfo makeNewPlayerInfo() {
        CoupSSPlayerInfo info = new CoupSSPlayerInfo();

        info.coins = 3;
        info.cardsInHand = new HashSet<CoupCard>();
        info.cardsInHand.add(deck.takeCard());
        info.cardsInHand.add(deck.takeCard());

        return info;
    }

    @Override
    protected GameContext generateCurrentGameContext() {

        CoupGameContext gc = (CoupGameContext) super.generateCurrentGameContext();

        for (PlayerId id : playerInfoMap.keySet()) {
            CoupSSPlayerInfo pi = playerInfoMap.get(id);
            gc.addInfo(id, pi.makePlayerInfo());
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
    }


}
