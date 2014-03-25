package com.neodem.coup.serverside;

import com.neodem.coup.CoupAction;
import com.neodem.coup.players.CoupPlayer;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class StealActionProcessor {

    private ServerSideGameContext context;

    public StealActionProcessor(ServerSideGameContext context) {
        this.context = context;
    }

    public void handleSteal(CoupPlayer actingPlayer, CoupAction a) {
        PlayerInfoState aInfo = context.getPlayerInfo(actingPlayer);
        PlayerInfoState oInfo = context.getPlayerInfo(a.getActionOn());

        // we may also get 0 or 1 coin here
        int coins = oInfo.removeCoins(2);
        aInfo.addCoins(coins);
    }
}
