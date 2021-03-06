package com.neodem.coup.server.game.actionProcessors;

import com.neodem.coup.common.game.CoupPlayerCallback;
import com.neodem.coup.common.game.actions.CoupAction;
import com.neodem.coup.server.game.ServerSideGameContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/26/14
 */
public class IncomeProcessor extends BaseActionProcessor implements ActionProcessor {
    private static Logger log = LogManager.getLogger(IncomeProcessor.class.getName());

    public IncomeProcessor(ServerSideGameContext context) {
        super(context);
    }

    @Override
    protected Logger getLog() {
        return log;
    }

    @Override
    public void process(CoupPlayerCallback actingPlayer, String targetPlayerName, CoupAction currentAction) {
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
