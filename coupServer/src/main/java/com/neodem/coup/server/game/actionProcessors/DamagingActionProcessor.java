package com.neodem.coup.server.game.actionProcessors;

import com.neodem.coup.common.game.CoupPlayerCallback;
import com.neodem.coup.common.game.cards.CoupCard;
import com.neodem.coup.server.game.PlayerInfoState;
import com.neodem.coup.server.game.ServerSideGameContext;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/25/14
 */
public abstract class DamagingActionProcessor extends BaseActionProcessor {

    protected DamagingActionProcessor(ServerSideGameContext context) {
        super(context);
    }

    protected void processLoss(String losingPlayerName) {

        getLog().info(losingPlayerName + " has to loose an influence..");

        PlayerInfoState playerInfoState = context.getPlayerInfo(losingPlayerName);

        CoupCard card;

        if (playerInfoState.hasOneInfluenceLeft()) {
            getLog().info(losingPlayerName + " has only one down card left so they are forced to turn it over and become inactive. :-(");
            card = playerInfoState.getDownCards().iterator().next();
        } else {
            CoupPlayerCallback losingPlayer = context.getCoupPlayer(losingPlayerName);
            // let them choose the card to turn over.
            do {
                card = losingPlayer.youMustLooseAnInfluence();
            } while (!playerInfoState.validInfluence(card));
        }

        getLog().info(losingPlayerName + " turns over their " + card + " card.");
        playerInfoState.turnFaceUp(card);
    }
}
