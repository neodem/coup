package com.neodem.coup.server.game.actionProcessors;

import com.neodem.coup.common.game.CoupCommunicationInterface;
import com.neodem.coup.common.game.PlayerError;
import com.neodem.coup.common.game.actions.CoupAction;
import com.neodem.coup.server.game.ServerSideGameContext;
import org.apache.logging.log4j.Logger;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/26/14
 */
public abstract class BaseActionProcessor implements ActionProcessor {
    protected final ServerSideGameContext context;

    public BaseActionProcessor(ServerSideGameContext context) {
        this.context = context;
    }

    protected abstract Logger getLog();

    @Override
    public void validate(CoupCommunicationInterface actingPlayer, String targetPlayerName, CoupAction currentAction) throws PlayerError {
    }

}
