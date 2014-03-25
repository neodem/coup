package com.neodem.coup;

import com.neodem.bandaid.game.GameContext;
import com.neodem.coup.players.CoupPlayer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupGameContext extends GameContext<CoupPlayer> {

    private static Log log = LogFactory.getLog(CoupGameContext.class.getName());

    @Override
    protected Log getLog() {
        return log;
    }

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
