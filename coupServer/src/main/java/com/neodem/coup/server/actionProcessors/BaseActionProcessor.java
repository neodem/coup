package com.neodem.coup.server.actionProcessors;

import com.neodem.coup.common.CoupAction;
import com.neodem.coup.common.CoupPlayer;
import com.neodem.coup.common.PlayerError;
import com.neodem.coup.server.ServerSideGameContext;
import org.apache.logging.log4j.Logger;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/26/14
 */
public abstract class BaseActionProcessor implements ActionProcessor {
    protected ServerSideGameContext context;

    public BaseActionProcessor(ServerSideGameContext context) {
        this.context = context;
    }

    protected abstract Logger getLog();

    @Override
    public void validate(CoupPlayer actingPlayer, CoupPlayer targetPlayer, CoupAction currentAction) throws PlayerError {

    }

}
