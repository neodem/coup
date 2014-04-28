package com.neodem.coup.server.game.resolvers;

import com.neodem.coup.common.game.CoupPlayerCallback;
import com.neodem.coup.common.game.actions.CoupAction;
import com.neodem.coup.common.game.cards.CoupCard;
import com.neodem.coup.common.game.cards.CoupCardType;
import com.neodem.coup.server.game.PlayerInfoState;
import com.neodem.coup.server.game.ServerSideGameContext;
import com.neodem.coup.server.game.actionProcessors.DamagingActionProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/24/14
 */
public class ChallengeResolver extends DamagingActionProcessor {
    private static final Logger log = LogManager.getLogger(ChallengeResolver.class.getName());

    public ChallengeResolver(ServerSideGameContext context) {
        super(context);
    }

    @Override
    protected Logger getLog() {
        return log;
    }

    /**
     * @param challengingPlayer the player doing the challenge
     * @param challengedPlayer  the player who is being challenged
     * @param challengedCard    the card being challenged
     * @return true if the challenge was successful, false otherwise
     */
    public boolean resolveChallenge(CoupPlayerCallback challengingPlayer, CoupPlayerCallback challengedPlayer, CoupCardType challengedCard) {
        log.debug(String.format("resolveChallenge() : %s is challenging that %s has the %s card.", challengingPlayer.getPlayerName(), challengedPlayer.getPlayerName(), challengedCard));

        // 1) acting player can decide to prove they have the card
        if (challengedPlayer.doYouWantToProveYouHaveACardOfThisType(challengedCard)) {

            // 2) does the acting player have the card?
            PlayerInfoState playerInfoState = context.getPlayerInfo(challengedPlayer);

            if (playerInfoState.hasCard(challengedCard)) {
                log.debug(String.format("%s does indeed have the %s card. Challenge failed :(", challengedPlayer.getPlayerName(), challengedCard));

                processLoss(challengingPlayer.getPlayerName());

                // recycle the card

                log.debug(String.format("%s has to recycle their %s card.", challengedPlayer.getPlayerName(), challengedCard));
                CoupCard actualCard = playerInfoState.removeCardOfType(challengedCard);
                context.getDeck().putCard(actualCard);
                context.getDeck().shuffleDeck();
                playerInfoState.addCard(context.getDeck().takeCard());

                return false;
            }
        } else {
            log.debug(String.format("%s is refusing to prove they have the %s card.", challengedPlayer.getPlayerName(), challengedCard));
        }

        log.debug(String.format("%s did not prove they had the %s card. Challenge succeeded!", challengedPlayer.getPlayerName(), challengedCard));
        processLoss(challengedPlayer.getPlayerName());
        return true;
    }

    /**
     * @param challengingPlayer the player doing the challenge
     * @param challengedPlayer  the player who is being challenged
     * @param challengedCards   the cards being challenged. The challenged player has to have at least one of them
     * @return true if the challenge was successful, false otherwise
     */
    public boolean resolveChallenge(CoupPlayerCallback challengingPlayer, CoupPlayerCallback challengedPlayer, Collection<CoupCardType> challengedCards) {
        Set<CoupCardType> uniqueTypes = new HashSet<>(challengedCards);

        for (CoupCardType card : uniqueTypes) {
            if (resolveChallenge(challengingPlayer, challengedPlayer, card)) return true;
        }

        return false;
    }

    @Override
    public void process(CoupPlayerCallback actingPlayer, String targetPlayer, CoupAction currentAction) {
        //noop : this should never be called
    }
}
