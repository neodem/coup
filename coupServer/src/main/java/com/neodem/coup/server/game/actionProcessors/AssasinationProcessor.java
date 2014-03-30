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
 * Created Date: 3/25/14
 */
public class AssasinationProcessor extends DamagingActionProcessor implements ActionProcessor {
    private static final Logger log = LogManager.getLogger(AssasinationProcessor.class.getName());

    public AssasinationProcessor(ServerSideGameContext context) {
        super(context);
    }

    @Override
    protected Logger getLog() {
        return log;
    }

    public void handlePayment(CoupCommunicationInterface actingPlayer) {
        log.debug(actingPlayer + " has to pay 3 coins to assasinate.");
        PlayerInfoState info = context.getPlayerInfo(actingPlayer);
        info.removeCoins(3);
    }

    @Override
    public void validate(CoupCommunicationInterface actingPlayer, String targetPlayerName, CoupAction currentAction) throws PlayerError {
        if (currentAction.getActionType() == SimpleCoupAction.ActionType.Assassinate) {

            PlayerInfoState info = context.getPlayerInfo(actingPlayer);
            if (info.getCoinCount() < 3) {
                String msg = "Player has attempted an Assasination but they only have " + info.getCoinCount() + " coins (they need 3).";
                log.error(msg);
                throw new PlayerError(msg);
            }

            if (targetPlayerName != null && !context.isPlayerActive(targetPlayerName)) {
                String msg = "Player has attempted to Asssanate an inactive player : " + targetPlayerName;
                log.error(msg);
                throw new PlayerError(msg);
            }
        }
    }

    @Override
    public void process(CoupCommunicationInterface actingPlayer, String targetPlayerName, CoupAction currentAction) {
        handlePayment(actingPlayer);

        PlayerInfoState infoState = context.getPlayerInfo(targetPlayerName);
        if (infoState.isActive()) {
            log.debug(String.format("%s is assasinating %s", actingPlayer, targetPlayerName));
            processLoss(targetPlayerName);
        } else {
            log.debug(String.format("%s is assasinating %s but they have no cards left and are inactive.", actingPlayer, targetPlayerName));
        }
    }
}
