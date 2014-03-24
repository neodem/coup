package com.neodem.coup.coup.serverside;

import com.neodem.coup.coup.CoupAction;
import com.neodem.coup.coup.players.CoupPlayer;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class StealActionProcessor {

    private CoupGameMaster gm;

    public StealActionProcessor(CoupGameMaster gm) {
        this.gm = gm;
    }

    public void handleSteal(CoupPlayer actingPlayer, CoupAction a) {
        PlayerInfoState aInfo = gm.getPlayerInfoMap().get(actingPlayer);
        PlayerInfoState oInfo = gm.getPlayerInfoMap().get(a.getActionOn());

        // we may also get 0 or 1 coin here
        int coins = oInfo.removeCoins(2);
        aInfo.addCoins(coins);
    }
}
