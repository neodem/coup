package com.neodem.coup.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupGameContext {

    private static Log log = LogFactory.getLog(CoupGameContext.class.getName());
    public Map<CoupPlayer, CoupPlayerInfo> playerInfos = new HashMap<>();
    // a list of the players in the game (ordered by their position in the game)
    protected List<CoupPlayer> players = new ArrayList<>();

    public void addInfo(CoupPlayer p, CoupPlayerInfo coupPlayerInfo) {
        playerInfos.put(p, coupPlayerInfo);
    }

    public Map<CoupPlayer, CoupPlayerInfo> getCoupPlayerInfos() {
        return playerInfos;
    }

    public void addPlayer(CoupPlayer p) {
        players.add(p);
    }

    public List<CoupPlayer> getPlayers() {
        return players;
    }

    @Override
    public String toString() {

        StringBuffer b = new StringBuffer();
        b.append(super.toString());
        b.append('\n');

        for (CoupPlayer p : players) {
            b.append(p);
            b.append(" details:");
            b.append('\n');

            CoupPlayerInfo coupPlayerInfo = playerInfos.get(p);
            if (coupPlayerInfo != null) {
                b.append(playerInfos.get(p));
            }

            b.append('\n');
        }

        b.append('\n');

        return b.toString();
    }
}
