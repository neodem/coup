package com.neodem.coup.server.game;

import com.neodem.bandaid.gameMaster.PlayerCallback;
import com.neodem.coup.common.game.CoupGameContext;
import com.neodem.coup.common.game.CoupPlayerCallback;
import com.neodem.coup.common.game.cards.CoupDeck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/25/14
 */
public class ServerSideGameContext {
    //private static Logger log = LogManager.getLogger(ServerSideGameContext.class.getName());
    // the players in the players (in play order)
    private final List<CoupPlayerCallback> playerList;
    // keeps track of the state of the players
    private final Map<CoupPlayerCallback, PlayerInfoState> playerInfoMap;
    // the deck we are using
    private final CoupDeck deck;

    public ServerSideGameContext(Map<CoupPlayerCallback, PlayerInfoState> playerInfoMap, CoupDeck deck) {
        this.playerInfoMap = playerInfoMap;
        this.deck = deck;
        playerList = new ArrayList<>();
    }

    public ServerSideGameContext() {
        this(new HashMap<CoupPlayerCallback, PlayerInfoState>(), new CoupDeck());
    }

    public void addPlayer(PlayerCallback p) {
        CoupPlayerCallback cpc = (CoupPlayerCallback) p;

        PlayerInfoState info = makeNewPlayerInfo(cpc);
        playerInfoMap.put(cpc, info);
        playerList.add(cpc);
    }

    private PlayerInfoState makeNewPlayerInfo(CoupPlayerCallback p) {
        return new PlayerInfoState(2, deck.takeCard(), deck.takeCard(), p.getPlayerName());
    }

    public CoupGameContext generateCurrentPublicGameContext() {
        return generatePrivateGameContext(null);
    }

    public CoupGameContext generatePrivateGameContext(CoupPlayerCallback privPlayer) {

        CoupGameContext gc = new CoupGameContext();

        for (CoupPlayerCallback p : playerList) {
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

    public PlayerInfoState getPlayerInfo(CoupPlayerCallback p) {
        return playerInfoMap.get(p);
    }

    public CoupDeck getDeck() {
        return deck;
    }

    public List<CoupPlayerCallback> getPlayerList() {
        return playerList;
    }

    public boolean isPlayerActive(String playerName) {
        PlayerInfoState pis = getPlayerInfo(playerName);
        return pis.isActive();
    }

    public void addCoinsToPlayer(int coins, CoupPlayerCallback p) {
        playerInfoMap.get(p).addCoins(coins);
    }

    public CoupPlayerCallback getCoupPlayer(String playerName) {

        for (CoupPlayerCallback p : playerInfoMap.keySet()) {
            if (p.getPlayerName().equals(playerName)) {
                return p;
            }
        }

        return null;
    }
}
