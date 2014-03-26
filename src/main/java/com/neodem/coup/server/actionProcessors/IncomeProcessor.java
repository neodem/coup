package com.neodem.coup.server.actionProcessors;

import com.neodem.coup.common.CoupAction;
import com.neodem.coup.common.CoupPlayer;
import com.neodem.coup.server.ServerSideGameContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/26/14
 */
public class IncomeProcessor extends BaseActionProcessor implements ActionProcessor {
    private static Log log = LogFactory.getLog(IncomeProcessor.class.getName());

    public IncomeProcessor(ServerSideGameContext context) {
        super(context);
    }

    @Override
    protected Log getLog() {
        return log;
    }

    @Override
    public void process(CoupPlayer actingPlayer, CoupPlayer targetPlayer, CoupAction currentAction) {
        switch (currentAction.getActionType()) {
            case Income:
                getLog().info(actingPlayer + " gets one coin");
                context.addCoinsToPlayer(1, actingPlayer);
                break;
            case ForeignAid:
                getLog().info(actingPlayer + " gets two coins");
                context.addCoinsToPlayer(2, actingPlayer);
                break;
            case Tax:
                getLog().info(actingPlayer + " gets two coins");
                context.addCoinsToPlayer(2, actingPlayer);
                break;
        }
    }
}
