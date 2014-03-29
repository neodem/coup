package com.neodem.coup.server.game.resolvers;

import com.neodem.coup.common.game.CoupCommunicationInterface;
import com.neodem.coup.common.game.actions.CoupAction;
import com.neodem.coup.common.game.cards.CoupCardType;
import com.neodem.coup.server.game.ServerSideGameContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class CounterResolver {
    private static Logger log = LogManager.getLogger(CounterResolver.class.getName());
    private final ChallengeResolver challengeResolver;
    private ServerSideGameContext context;

    public CounterResolver(ServerSideGameContext context, ChallengeResolver challengeResolver) {
        this.context = context;
        this.challengeResolver = challengeResolver;
    }

    /**
     * @param actingPlayer     the player who is being countered
     * @param counteringPlayer the player doing the countering
     * @param counteredAction  the action being countered
     * @return true if the counter was successful, false otherwise
     */
    public boolean resolveCounter(CoupCommunicationInterface actingPlayer, CoupCommunicationInterface counteringPlayer, CoupAction counteredAction) {
        if (actingPlayer.doYouWantToChallengeThisCounter(counteringPlayer.getPlayerName())) {
            Collection<CoupCardType> cardsNeededToCounter = counteredAction.getCounterCard();

            if (challengeResolver.resolveChallenge(actingPlayer, counteringPlayer, cardsNeededToCounter)) {
                // if we are here, the challenge succeeded and the counter failed
                return false;
            }
        }

        return true;
    }


}
