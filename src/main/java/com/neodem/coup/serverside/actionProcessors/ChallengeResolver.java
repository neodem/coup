package com.neodem.coup.serverside.actionProcessors;

import com.neodem.coup.cards.CoupCard;
import com.neodem.coup.players.CoupPlayer;
import com.neodem.coup.serverside.PlayerInfoState;
import com.neodem.coup.serverside.ServerSideGameContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;

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
     * @param challengingPlayer the player doing the challenge
     * @param challengedPlayer  the player who is being challenged
     * @param challengedCard    the card being challenged
     * @return true if the challenge was successful, false otherwise
     */
    public boolean resolveChallenge(CoupPlayer challengingPlayer, CoupPlayer challengedPlayer, CoupCard challengedCard) {
        log.debug(String.format("resolveChallenge() : %s is challenging that %s has the %s card.", challengingPlayer.getMyName(), challengedPlayer.getMyName(), challengedCard));

        // 1) acting player can decide to prove they have the card
        if (challengedPlayer.doYouWantToProveYouHaveThisCard(challengedCard)) {

            // 2) does the acting player have the card?
            PlayerInfoState playerInfoState = context.getPlayerInfo(challengedPlayer);

            if (playerInfoState.hasCard(challengedCard)) {
                log.debug(String.format("%s has the %s card. Challenge failed :(", challengedPlayer.getMyName(), challengedCard));

                processLoss(challengingPlayer);

                // recycle the card

                log.debug(String.format("%s has to recycle their %s card.", challengedPlayer.getMyName(), challengedCard));
                playerInfoState.cardsInHand.remove(challengedCard);
                context.getDeck().putCard(challengedCard);
                context.getDeck().shuffleDeck();
                playerInfoState.cardsInHand.add(context.getDeck().takeCard());

                return false;
            }
        }

        log.debug(String.format("%s did not prove they had the %s card. Challenge succeeded!", challengedPlayer.getMyName(), challengedCard));
        processLoss(challengedPlayer);
        return true;
    }

    /**
     * @param challengingPlayer the player doing the challenge
     * @param challengedPlayer  the player who is being challenged
     * @param challengedCards   the cards being challenged. The challenged player has to have at least one of them
     * @return true if the challenge was successful, false otherwise
     */
    public boolean resolveChallenge(CoupPlayer challengingPlayer, CoupPlayer challengedPlayer, Collection<CoupCard> challengedCards) {
        for (CoupCard card : challengedCards) {
            if (resolveChallenge(challengingPlayer, challengedPlayer, card)) return true;
        }
        return false;
    }

    private void processLoss(CoupPlayer loser) {
        log.info(loser.getMyName() + " has to loose an influence..");

        PlayerInfoState playerInfoState = context.getPlayerInfo(loser);
        CoupCard card;
        do {
            card = loser.looseAnInfluence();
        } while (!playerInfoState.validInfluence(card));

        log.info(loser.getMyName() + " turns over the " + card + " card.");
        playerInfoState.turnFaceUp(card);
    }
}
