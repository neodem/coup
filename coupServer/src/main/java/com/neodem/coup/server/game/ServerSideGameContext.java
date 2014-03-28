package com.neodem.coup.server.game;

import com.neodem.coup.common.game.CoupDeck;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.CoupPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/25/14
 */
public class ServerSideGameContext {
    private static Logger log = LogManager.getLogger(ServerSideGameContext.class.getName());
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
        playerInfoMap.put(p, info);
        playerList.add(p);
    }

    private PlayerInfoState makeNewPlayerInfo(CoupPlayer p) {
        return new PlayerInfoState(2, deck.takeCard(), deck.takeCard(), p.getPlayerName());
    }

    public CoupGameContext generateCurrentPublicGameContext() {
        return generatePrivateGameContext(null);
    }

    public CoupGameContext generatePrivateGameContext(CoupPlayer privPlayer) {

        CoupGameContext gc = new CoupGameContext();

        for (CoupPlayer p : playerList) {
            String playerName = p.getPlayerName();
            gc.addPlayer(playerName);
            PlayerInfoState pi = getPlayerInfo(p);
            if (pi != null) {
                if (p.equals(privPlayer)) {
                    gc.addInfo(playerName, pi.makePrivatePlayerInfo());
                } else {
                    gc.addInfo(playerName, pi.makePublicPlayerInfo());
                }
            }
        }

        return gc;
    }

    public PlayerInfoState getPlayerInfo(String playerName) {
        return playerInfoMap.get(getCoupPlayer(playerName));
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

    public boolean isPlayerActive(String playerName) {
        PlayerInfoState pis = getPlayerInfo(playerName);
        return pis.isActive();
    }

    public void addCoinsToPlayer(int coins, CoupPlayer p) {
        playerInfoMap.get(p).addCoins(coins);
    }

    public CoupPlayer getCoupPlayer(String playerName) {

        for (CoupPlayer p : playerInfoMap.keySet()) {
            if (p.getPlayerName().equals(playerName)) {
                return p;
            }
        }

        return null;
    }
}
