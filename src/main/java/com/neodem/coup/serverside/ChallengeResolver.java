package com.neodem.coup.serverside;

import com.neodem.coup.CoupAction;
import com.neodem.coup.cards.CoupCard;
import com.neodem.coup.players.CoupPlayer;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class ChallengeResolver {

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
