package com.neodem.coup.server.game.actionProcessors;

import com.neodem.bandaid.gamemaster.PlayerError;
import com.neodem.coup.common.game.CoupPlayerCallback;
import com.neodem.coup.common.game.actions.CoupAction;
import com.neodem.coup.common.game.actions.SimpleCoupAction;
import com.neodem.coup.server.game.PlayerInfoState;
import com.neodem.coup.server.game.ServerSideGameContext;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class StealActionProcessor extends BaseActionProcessor implements ActionProcessor {
    private static final Logger log = LogManager.getLogger(StealActionProcessor.class.getName());

    public StealActionProcessor(ServerSideGameContext context) {
        super(context);
    }

    @Override
    protected Logger getLog() {
        return log;
    }

    @Override
    public void validate(CoupPlayerCallback actingPlayer, String targetPlayerName, CoupAction currentAction) throws PlayerError {

        if (currentAction.getActionType() == SimpleCoupAction.ActionType.Steal) {
            if (StringUtils.isBlank(targetPlayerName)) {
                String msg = "invalid targetPlayerName";
                getLog().error(msg);
                throw new PlayerError(msg);
            }

            if (targetPlayerName != null && !context.isPlayerActive(targetPlayerName)) {
                String msg = "Player has attempted to Steal from an inactive player : " + targetPlayerName;
                getLog().error(msg);
                throw new PlayerError(msg);
            }
            if (targetPlayerName.equals(actingPlayer.getPlayerName())) {
                String msg = "Player has attempted to Steal from himself. Technically this is allowed but I'm not down so try again.";
                getLog().error(msg);
                throw new PlayerError(msg);
            }
        }
    }

    @Override
    public void process(CoupPlayerCallback actingPlayer, String targetPlayerName, CoupAction currentAction) {
        PlayerInfoState aInfo = context.getPlayerInfo(actingPlayer);
        PlayerInfoState oInfo = context.getPlayerInfo(targetPlayerName);

        // we may also get 0 or 1 coin here
        int coins = oInfo.removeCoins(2);
        getLog().debug(actingPlayer.getPlayerName() + " steals " + coins + " coins from " + targetPlayerName);
        aInfo.addCoins(coins);
    }
}
