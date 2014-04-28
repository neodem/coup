package com.neodem.coup.server.game.actionProcessors;

import com.neodem.coup.common.game.CoupCommunicationInterface;
import com.neodem.coup.common.game.PlayerError;
import com.neodem.coup.common.game.actions.CoupAction;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/26/14
 */
public interface ActionProcessor {
    public void validate(CoupCommunicationInterface actingPlayer, String targetPlayerName, CoupAction currentAction) throws PlayerError;

    public void process(CoupCommunicationInterface actingPlayer, String targetPlayerName, CoupAction currentAction);
}
