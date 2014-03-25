package com.neodem.coup.serverside.actionProcessors;

import com.neodem.coup.CoupAction;
import com.neodem.coup.cards.CoupCard;
import com.neodem.coup.players.CoupPlayer;
import com.neodem.coup.serverside.PlayerInfoState;
import com.neodem.coup.serverside.ServerSideGameContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class ChallengeResolver {
    private static Log log = LogFactory.getLog(ChallengeResolver.class.getName());
    private ServerSideGameContext context;

    public ChallengeResolver(ServerSideGameContext context) {
        this.context = context;
    }

    /**
     * @param actingPlayer      the player who is being challenged
     * @param challengingPlayer the player doing the challenge
     * @param challengedAction  the action being challenged
     * @return true if the challenge was successful, false otherwise
     */
    public boolean resolveChallenge(CoupPlayer actingPlayer, CoupPlayer challengingPlayer, CoupAction challengedAction) {
        log.debug("resolveChallenge()");

        // 1) acting player can decide to prove they have the card
        if (actingPlayer.proveYouHaveCorrectCard(challengedAction)) {

            // 2) does the acting player have the card?
            PlayerInfoState playerInfoState = context.getPlayerInfo(actingPlayer);
            CoupCard actionCard = challengedAction.getActionCard();
            if (playerInfoState.hasCard(actionCard)) {
                processLoss(challengingPlayer);

                // recycle the card
                playerInfoState.cardsInHand.remove(actionCard);
                context.getDeck().putCard(actionCard);
                context.getDeck().shuffleDeck();
                playerInfoState.cardsInHand.add(context.getDeck().takeCard());

                return false;
            }
        }

        processLoss(actingPlayer);
        return true;
    }

    private void processLoss(CoupPlayer loser) {
        PlayerInfoState playerInfoState = context.getPlayerInfo(loser);
        CoupCard card;
        do {
            card = loser.looseAnInfluence();
        } while (!playerInfoState.validInfluence(card));

        playerInfoState.turnFaceUp(card);
    }
}
