package com.neodem.coup.serverside.actionProcessors;

import com.neodem.coup.CoupAction;
import com.neodem.coup.players.CoupPlayer;
import com.neodem.coup.serverside.ServerSideGameContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/25/14
 */
public class AssasinationProcessor {
    private static Log log = LogFactory.getLog(AssasinationProcessor.class.getName());
    private ServerSideGameContext context;

    public AssasinationProcessor(ServerSideGameContext context) {
        this.context = context;
    }

    public void handleAssasinate(CoupPlayer actingPlayer, CoupAction currentAction) {
    }
}
