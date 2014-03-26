package com.neodem.coup.server.actionProcessors;

import com.neodem.coup.common.CoupCard;
import com.neodem.coup.common.CoupCardType;
import com.neodem.coup.common.CoupPlayer;
import com.neodem.coup.server.PlayerInfoState;
import com.neodem.coup.server.ServerSideGameContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class ChallengeResolver extends DamagingActionProcessor {
    private static Log log = LogFactory.getLog(ChallengeResolver.class.getName());

    public ChallengeResolver(ServerSideGameContext context) {
        super(context);
    }

    @Override
    protected Log getLog() {
        return log;
    }

    /**
     * @param challengingPlayer the player doing the challenge
     * @param challengedPlayer  the player who is being challenged
     * @param challengedCard    the card being challenged
     * @return true if the challenge was successful, false otherwise
     */
    public boolean resolveChallenge(CoupPlayer challengingPlayer, CoupPlayer challengedPlayer, CoupCardType challengedCard) {
        log.debug(String.format("resolveChallenge() : %s is challenging that %s has the %s card.", challengingPlayer.getMyName(), challengedPlayer.getMyName(), challengedCard));

        // 1) acting player can decide to prove they have the card
        if (challengedPlayer.doYouWantToProveYouHaveACardOfThisType(challengedCard)) {

            // 2) does the acting player have the card?
            PlayerInfoState playerInfoState = context.getPlayerInfo(challengedPlayer);

            if (playerInfoState.hasCard(challengedCard)) {
                log.debug(String.format("%s does indeed have the %s card. Challenge failed :(", challengedPlayer.getMyName(), challengedCard));

                processLoss(challengingPlayer);

                // recycle the card

                log.debug(String.format("%s has to recycle their %s card.", challengedPlayer.getMyName(), challengedCard));
                CoupCard actualCard = playerInfoState.removeCardOfType(challengedCard);
                context.getDeck().putCard(actualCard);
                context.getDeck().shuffleDeck();
                playerInfoState.addCard(context.getDeck().takeCard());

                return false;
            }
        } else {
            log.debug(String.format("%s is refusing to prove they have the %s card.", challengedPlayer.getMyName(), challengedCard));
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
    public boolean resolveChallenge(CoupPlayer challengingPlayer, CoupPlayer challengedPlayer, Collection<CoupCardType> challengedCards) {
        Set<CoupCardType> uniqueTypes = new HashSet<>(challengedCards);

        for (CoupCardType card : uniqueTypes) {
            if (resolveChallenge(challengingPlayer, challengedPlayer, card)) return true;
        }

        return false;
    }
}
