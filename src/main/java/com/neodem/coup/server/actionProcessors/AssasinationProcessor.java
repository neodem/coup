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

    public AssasinationProcessor(ServerSideGameContext context) {
        super(context);
    }

    @Override
    protected Log getLog() {
        return log;
    }

    public void handleAssasinate(CoupPlayer actingPlayer, CoupPlayer targetPlayer) {
        handlePayment(actingPlayer);

        PlayerInfoState infoState = context.getPlayerInfo(targetPlayer);
        if (infoState.isActive()) {
            log.debug(String.format("%s is assasinating %s", actingPlayer, targetPlayer));
            processLoss(targetPlayer);
        } else {
            log.debug(String.format("%s is assasinating %s but they have no cards left and are inactive.", actingPlayer, targetPlayer));
        }
    }

    public void handlePayment(CoupPlayer actingPlayer) {
        log.debug(actingPlayer + " has to pay 3 coins to assasinate.");
        PlayerInfoState info = context.getPlayerInfo(actingPlayer);
        info.removeCoins(3);
    }
}