package com.neodem.coup.server.actionProcessors;

import com.neodem.coup.common.CoupCard;
import com.neodem.coup.common.CoupPlayer;
import com.neodem.coup.server.PlayerInfoState;
import com.neodem.coup.server.ServerSideGameContext;
import org.apache.commons.logging.Log;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/25/14
 */
public abstract class DamagingActionProcessor {
    protected abstract Log getLog();

    protected ServerSideGameContext context;

    public DamagingActionProcessor(ServerSideGameContext context) {
        this.context = context;
    }

    protected void processLoss(CoupPlayer loser) {
        getLog().info(loser.getMyName() + " has to loose an influence..");

        PlayerInfoState playerInfoState = context.getPlayerInfo(loser);
        CoupCard card;
        do {
            card = loser.looseAnInfluence();
        } while (!playerInfoState.validInfluence(card));

        getLog().info(loser.getMyName() + " turns over their " + card + " card.");
        playerInfoState.turnFaceUp(card);
    }
}
