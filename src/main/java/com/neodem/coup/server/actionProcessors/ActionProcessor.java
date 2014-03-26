package com.neodem.coup.server.actionProcessors;

import com.neodem.coup.common.CoupAction;
import com.neodem.coup.common.CoupPlayer;
import com.neodem.coup.common.PlayerError;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/26/14
 */
public interface ActionProcessor {
    public void validate(CoupPlayer actingPlayer, CoupPlayer targetPlayer, CoupAction currentAction) throws PlayerError;

    public void process(CoupPlayer actingPlayer, CoupPlayer targetPlayer, CoupAction currentAction);
}
