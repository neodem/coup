package com.neodem.coup.server.actionProcessors;

import com.neodem.coup.common.CoupAction;
import com.neodem.coup.common.CoupPlayer;
import com.neodem.coup.common.PlayerError;
import com.neodem.coup.server.PlayerInfoState;
import com.neodem.coup.server.ServerSideGameContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class StealActionProcessor extends BaseActionProcessor implements ActionProcessor {
    private static Logger log = LogManager.getLogger(StealActionProcessor.class.getName());

    public StealActionProcessor(ServerSideGameContext context) {
        super(context);
    }

    @Override
    protected Logger getLog() {
        return log;
    }

    @Override
    public void validate(CoupPlayer actingPlayer, CoupPlayer targetPlayer, CoupAction currentAction) throws PlayerError {
        if (currentAction.getActionType() == CoupAction.ActionType.Steal) {
            if (targetPlayer != null && !context.isPlayerActive(targetPlayer)) {
                String msg = "Player has attempted to Steal from an inactive player : " + targetPlayer;
                getLog().error(msg);
                throw new PlayerError(msg);
            }
        }
    }

    @Override
    public void process(CoupPlayer actingPlayer, CoupPlayer targetPlayer, CoupAction currentAction) {
        PlayerInfoState aInfo = context.getPlayerInfo(actingPlayer);
        PlayerInfoState oInfo = context.getPlayerInfo(targetPlayer);

        // we may also get 0 or 1 coin here
        int coins = oInfo.removeCoins(2);
        getLog().debug(actingPlayer.getMyName() + " steals " + coins + " coins from " + targetPlayer.getMyName());
        aInfo.addCoins(coins);
    }
}
