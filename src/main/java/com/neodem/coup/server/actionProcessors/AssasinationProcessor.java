package com.neodem.coup.server.actionProcessors;

import com.neodem.coup.common.CoupAction;
import com.neodem.coup.common.CoupPlayer;
import com.neodem.coup.common.PlayerError;
import com.neodem.coup.server.PlayerInfoState;
import com.neodem.coup.server.ServerSideGameContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/25/14
 */
public class AssasinationProcessor extends DamagingActionProcessor implements ActionProcessor {
    private static Log log = LogFactory.getLog(AssasinationProcessor.class.getName());

    public AssasinationProcessor(ServerSideGameContext context) {
        super(context);
    }

    @Override
    protected Log getLog() {
        return log;
    }

    public void handlePayment(CoupPlayer actingPlayer) {
        log.debug(actingPlayer + " has to pay 3 coins to assasinate.");
        PlayerInfoState info = context.getPlayerInfo(actingPlayer);
        info.removeCoins(3);
    }

    @Override
    public void validate(CoupPlayer actingPlayer, CoupPlayer targetPlayer, CoupAction currentAction) throws PlayerError {
        if (currentAction.getActionType() == CoupAction.ActionType.Assassinate) {

            PlayerInfoState info = context.getPlayerInfo(actingPlayer);
            if (info.getCoinCount() < 3) {
                String msg = "Player has attempted an Assasination but they only have " + info.getCoinCount() + " coins (they need 3).";
                log.error(msg);
                throw new PlayerError(msg);
            }

            if (targetPlayer != null && !context.isPlayerActive(targetPlayer)) {
                String msg = "Player has attempted to Asssanate an inactive player : " + targetPlayer;
                log.error(msg);
                throw new PlayerError(msg);
            }
        }
    }

    @Override
    public void process(CoupPlayer actingPlayer, CoupPlayer targetPlayer, CoupAction currentAction) {
        handlePayment(actingPlayer);

        PlayerInfoState infoState = context.getPlayerInfo(targetPlayer);
        if (infoState.isActive()) {
            log.debug(String.format("%s is assasinating %s", actingPlayer, targetPlayer));
            processLoss(targetPlayer);
        } else {
            log.debug(String.format("%s is assasinating %s but they have no cards left and are inactive.", actingPlayer, targetPlayer));
        }
    }
}
