package com.neodem.coup.server.game.actionProcessors;

import com.neodem.coup.common.game.CoupAction;
import com.neodem.coup.common.game.CoupPlayer;
import com.neodem.coup.common.game.PlayerError;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/26/14
 */
public interface ActionProcessor {
    public void validate(CoupPlayer actingPlayer, String targetPlayerName, CoupAction currentAction) throws PlayerError;

    public void process(CoupPlayer actingPlayer, String targetPlayerName, CoupAction currentAction);
}
