package com.neodem.coup.server.actionProcessors;

import com.neodem.coup.common.CoupAction;
import com.neodem.coup.common.CoupPlayer;
import com.neodem.coup.server.PlayerInfoState;
import com.neodem.coup.server.ServerSideGameContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class StealActionProcessor {
    private static Log log = LogFactory.getLog(StealActionProcessor.class.getName());
    private ServerSideGameContext context;

    public StealActionProcessor(ServerSideGameContext context) {
        this.context = context;
    }

    public void handleSteal(CoupPlayer actingPlayer, CoupAction a) {
        PlayerInfoState aInfo = context.getPlayerInfo(actingPlayer);
        PlayerInfoState oInfo = context.getPlayerInfo(a.getActionOn());

        // we may also get 0 or 1 coin here
        int coins = oInfo.removeCoins(2);
        log.debug(actingPlayer.getMyName() + " steals " + coins + " coins from " + a.getActionOn().getMyName());
        aInfo.addCoins(coins);
    }
}
