package com.neodem.coup.server.game.actionProcessors;

import com.neodem.coup.common.game.CoupCommunicationInterface;
import com.neodem.coup.common.game.PlayerError;
import com.neodem.coup.common.game.actions.CoupAction;
import com.neodem.coup.common.game.actions.SimpleCoupAction;
import com.neodem.coup.server.game.PlayerInfoState;
import com.neodem.coup.server.game.ServerSideGameContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class CoupActionProcessor extends DamagingActionProcessor implements ActionProcessor {
    private static Logger log = LogManager.getLogger(CoupActionProcessor.class.getName());

    public CoupActionProcessor(ServerSideGameContext context) {
        super(context);
    }

    @Override
    protected Logger getLog() {
        return log;
    }

    @Override
    public void validate(CoupCommunicationInterface actingPlayer, String targetPlayerName, CoupAction currentAction) throws PlayerError {
        if (currentAction.getActionType() == SimpleCoupAction.ActionType.Coup) {
            PlayerInfoState info = context.getPlayerInfo(actingPlayer);

            if (info.getCoinCount() < 7) {
                String msg = "Player has attempted a Coup but they only have " + info.getCoinCount() + " coins (they need 7).";
                log.error(msg);
                throw new PlayerError(msg);
            }

            if (info.getCoinCount() >= 10) {
                String msg = "Player has more than 10 coins and they need to Coup someone. Yet they didn't";
                log.error(msg);
                throw new PlayerError(msg);
            }

            if (targetPlayerName != null && !context.isPlayerActive(targetPlayerName)) {
                String msg = "Player has attempted to Coup an inactive player : " + targetPlayerName;
                log.error(msg);
                throw new PlayerError(msg);
            }
        }
    }

    @Override
    public void process(CoupCommunicationInterface actingPlayer, String targetPlayerName, CoupAction currentAction) {
        log.debug(actingPlayer + " has to pay 7 coins to coup.");

        PlayerInfoState info = context.getPlayerInfo(actingPlayer);
        info.removeCoins(7);

        log.debug(String.format("%s is launching a coup on %s", actingPlayer, targetPlayerName));
        processLoss(targetPlayerName);
    }
}
