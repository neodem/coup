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
    protected ServerSideGameContext context;

    public DamagingActionProcessor(ServerSideGameContext context) {
        this.context = context;
    }

    protected abstract Log getLog();

    protected void processLoss(CoupPlayer loser) {
        String playerName = loser.getMyName();

        getLog().info(playerName + " has to loose an influence..");

        PlayerInfoState playerInfoState = context.getPlayerInfo(loser);

        CoupCard card;

        if (playerInfoState.hasOneInfluenceLeft()) {
            getLog().info(playerName + " has only one down card left so they are forced to turn it over and become inactive. :-(");
            card = playerInfoState.getDownCards().iterator().next();
        } else {
            // let them choose the card to turn over.
            do {
                card = loser.looseAnInfluence();
            } while (!playerInfoState.validInfluence(card));
        }

        getLog().info(loser.getMyName() + " turns over their " + card + " card.");
        playerInfoState.turnFaceUp(card);
    }
}
