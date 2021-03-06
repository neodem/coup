package com.neodem.coup.server.game.actionProcessors;

import com.neodem.bandaid.gamemasterstuff.PlayerError;
import com.neodem.coup.common.game.CoupPlayerCallback;
import com.neodem.coup.common.game.actions.CoupAction;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/26/14
 */
public interface ActionProcessor {
    public void validate(CoupPlayerCallback actingPlayer, String targetPlayerName, CoupAction currentAction) throws PlayerError;

    public void process(CoupPlayerCallback actingPlayer, String targetPlayerName, CoupAction currentAction);
}
