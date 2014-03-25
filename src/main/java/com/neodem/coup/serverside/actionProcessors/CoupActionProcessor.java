package com.neodem.coup.serverside.actionProcessors;

import com.neodem.coup.players.CoupPlayer;
import com.neodem.coup.serverside.ServerSideGameContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class CoupActionProcessor extends DamagingActionProcessor {
    private static Log log = LogFactory.getLog(CoupActionProcessor.class.getName());

    public CoupActionProcessor(ServerSideGameContext context) {
        super(context);
    }

    @Override
    protected Log getLog() {
        return log;
    }

    public void handleCoup(CoupPlayer actingPlayer, CoupPlayer targetPlayer) {
        log.debug(String.format("%s is launching a coup on %s", actingPlayer, targetPlayer));
        processLoss(targetPlayer);
    }
}
