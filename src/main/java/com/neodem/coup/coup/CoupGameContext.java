package com.neodem.coup.coup;

import com.neodem.coup.coup.players.CoupPlayer;
import com.neodem.coup.game.GameContext;
import com.neodem.coup.game.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupGameContext extends GameContext {
    public Map<CoupPlayer, CoupPlayerInfo> playerInfos = new HashMap<CoupPlayer, CoupPlayerInfo>();

    public void addInfo(CoupPlayer p, CoupPlayerInfo coupPlayerInfo) {
        playerInfos.put(p, coupPlayerInfo);
    }

    public Map<CoupPlayer, CoupPlayerInfo> getCoupPlayerInfos() {
        return playerInfos;
    }

    @Override
    public String toString() {

        StringBuffer b = new StringBuffer();
        b.append(super.toString());
        b.append('\n');

        for (Player p : players) {
            b.append(p);
            b.append(" details:");
            b.append('\n');

            CoupPlayerInfo coupPlayerInfo = playerInfos.get(p);
            if(coupPlayerInfo != null) {
                b.append(playerInfos.get(p));
            }

            b.append('\n');
        }

        b.append('\n');

        return b.toString();
    }
}
