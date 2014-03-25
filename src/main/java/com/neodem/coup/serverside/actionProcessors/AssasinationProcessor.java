package com.neodem.coup.serverside.actionProcessors;

import com.neodem.coup.players.CoupPlayer;
import com.neodem.coup.serverside.ServerSideGameContext;
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
        log.debug(String.format("%s is assasinating %s", actingPlayer, targetPlayer));
        processLoss(targetPlayer);
    }
}
