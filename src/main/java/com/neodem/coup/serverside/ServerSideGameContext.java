package com.neodem.coup.serverside;

import com.neodem.coup.cards.CoupDeck;
import com.neodem.coup.players.CoupPlayer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/25/14
 */
public class ServerSideGameContext {
    private static Log log = LogFactory.getLog(ServerSideGameContext.class.getName());
    // keeps track of the state of the players
    private Map<CoupPlayer, PlayerInfoState> playerInfoMap;
    // the deck we are using
    private CoupDeck deck;

    public ServerSideGameContext(Map<CoupPlayer, PlayerInfoState> playerInfoMap, CoupDeck deck) {
        this.playerInfoMap = playerInfoMap;
        this.deck = deck;
    }

    public ServerSideGameContext() {
        deck = new CoupDeck();
        playerInfoMap = new HashMap<>();
    }

    public void addPlayer(CoupPlayer p) {
        PlayerInfoState info = makeNewPlayerInfo(p);
        updatePlayer(p, info);
    }

    public void updatePlayer(CoupPlayer p, PlayerInfoState info) {
        // update our internal map
        playerInfoMap.put(p, info);
    }

    private PlayerInfoState makeNewPlayerInfo(CoupPlayer p) {
        PlayerInfoState info = new PlayerInfoState();

        info.coins = 2;
        info.card1 = deck.takeCard();
        info.card2 = deck.takeCard();
        info.active = true;
        info.name = p.getMyName();

        return info;
    }

    public PlayerInfoState getPlayerInfo(CoupPlayer p) {
        return playerInfoMap.get(p);
    }

    public CoupDeck getDeck() {
        return deck;
    }
}
