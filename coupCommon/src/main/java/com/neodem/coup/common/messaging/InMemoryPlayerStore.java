package com.neodem.coup.common.messaging;

import com.neodem.coup.common.game.CoupPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/28/14
 */
public class InMemoryPlayerStore implements PlayerStore {
    private Map<String, CoupPlayer> players = new HashMap<>();

    @Override
    public CoupPlayer getPlayer(String playerName) {
        return players.get(playerName);
    }

    @Override
    public void storePlayer(CoupPlayer p) {
        players.put(p.getMyName(), p);
    }
}
