package com.neodem.coup.server.game.actionProcessors;

import com.neodem.coup.common.game.CoupAction;
import com.neodem.coup.common.game.CoupCommunicationInterface;
import com.neodem.coup.common.game.PlayerError;
import com.neodem.coup.server.game.PlayerInfoState;
import com.neodem.coup.server.game.ServerSideGameContext;
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
    public void validate(CoupCommunicationInterface actingPlayer, String targetPlayerName, CoupAction currentAction) throws PlayerError {
        if (currentAction.getActionType() == CoupAction.ActionType.Steal) {
            if (targetPlayerName != null && !context.isPlayerActive(targetPlayerName)) {
                String msg = "Player has attempted to Steal from an inactive player : " + targetPlayerName;
                getLog().error(msg);
                throw new PlayerError(msg);
            }
        }
    }

    @Override
    public void process(CoupCommunicationInterface actingPlayer, String targetPlayerName, CoupAction currentAction) {
        PlayerInfoState aInfo = context.getPlayerInfo(actingPlayer);
        PlayerInfoState oInfo = context.getPlayerInfo(targetPlayerName);

        // we may also get 0 or 1 coin here
        int coins = oInfo.removeCoins(2);
        getLog().debug(actingPlayer.getPlayerName() + " steals " + coins + " coins from " + targetPlayerName);
        aInfo.addCoins(coins);
    }
}
