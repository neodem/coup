package com.neodem.coup.common.game;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupGameContext implements Serializable {

    private static Log log = LogFactory.getLog(CoupGameContext.class.getName());
    private Map<CoupPlayer, CoupPlayerInfo> playerInfos = new HashMap<>();
    // a list of the players in the game (ordered by their position in the game)
    private List<CoupPlayer> players = new ArrayList<>();

    public CoupGameContext(List<CoupPlayer> playerList, Map<CoupPlayer, CoupPlayerInfo> playerInfoMap) {
        this.players = playerList;
        this.playerInfos = playerInfoMap;
    }

    public CoupGameContext() {
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoupGameContext)) return false;

        CoupGameContext that = (CoupGameContext) o;

        if (playerInfos != null ? !playerInfos.equals(that.playerInfos) : that.playerInfos != null) return false;
        if (players != null ? !players.equals(that.players) : that.players != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = playerInfos != null ? playerInfos.hashCode() : 0;
        result = 31 * result + (players != null ? players.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {

        StringBuffer b = new StringBuffer();
        b.append('\n');
        b.append('\n');
        b.append("Game Context\n");
        b.append("------------");
        b.append('\n');
        b.append('\n');

        for (CoupPlayer p : players) {
            b.append(p);
            b.append('\n');
            b.append("===============");
            b.append('\n');

            CoupPlayerInfo coupPlayerInfo = playerInfos.get(p);
            if (coupPlayerInfo != null) {
                b.append(playerInfos.get(p));
            }

            b.append('\n');
        }

        return b.toString();
    }
}
