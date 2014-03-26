package com.neodem.coup.server.actionProcessors;

import com.neodem.coup.common.CoupPlayer;
import com.neodem.coup.server.PlayerInfoState;
import com.neodem.coup.server.ServerSideGameContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/25/14
 */
public class AssasinationProcessor extends DamagingActionProcessor {
    private static Log log = LogFactory.getLog(AssasinationProcessor.class.getName());

    @Override
    protected Log getLog() {
        return log;
    }

    public AssasinationProcessor(ServerSideGameContext context) {
        super(context);
    }

    public void handleAssasinate(CoupPlayer actingPlayer, CoupPlayer targetPlayer) {
        handlePayment(actingPlayer);

        log.debug(String.format("%s is assasinating %s", actingPlayer, targetPlayer));
        processLoss(targetPlayer);
    }

    public void handlePayment(CoupPlayer actingPlayer) {
        log.debug(actingPlayer + " has to pay 3 coins to assasinate.");
        PlayerInfoState info = context.getPlayerInfo(actingPlayer);
        info.removeCoins(3);
    }
}
