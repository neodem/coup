package com.neodem.coup.server;

import com.neodem.coup.common.CoupDeck;
import com.neodem.coup.common.CoupGameContext;
import com.neodem.coup.common.CoupPlayer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/25/14
 */
public class ServerSideGameContext {
    private static Log log = LogFactory.getLog(ServerSideGameContext.class.getName());

    // the players in the game (in play order)
    private List<CoupPlayer> playerList;
    // keeps track of the state of the players
    private Map<CoupPlayer, PlayerInfoState> playerInfoMap;
    // the deck we are using
    private CoupDeck deck;

    public ServerSideGameContext(Map<CoupPlayer, PlayerInfoState> playerInfoMap, CoupDeck deck) {
        this.playerInfoMap = playerInfoMap;
        this.deck = deck;
        playerList = new ArrayList<>();
    }

    public ServerSideGameContext() {
        this(new HashMap<CoupPlayer, PlayerInfoState>(), new CoupDeck());
    }

    public void addPlayer(CoupPlayer p) {
        PlayerInfoState info = makeNewPlayerInfo(p);
        updatePlayer(p, info);
        playerList.add(p);
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

    public CoupGameContext generateCurrentPublicGameContext() {
        CoupGameContext gc = new CoupGameContext();

        for (CoupPlayer p : playerList) {
            gc.addPlayer(p);
            PlayerInfoState pi = getPlayerInfo(p);
            if (pi != null) {
                gc.addInfo(p, pi.makePublicPlayerInfo());
            }

        }

        return gc;
    }

    public PlayerInfoState getPlayerInfo(CoupPlayer p) {
        return playerInfoMap.get(p);
    }

    public CoupDeck getDeck() {
        return deck;
    }

    public List<CoupPlayer> getPlayerList() {
        return playerList;
    }
}
