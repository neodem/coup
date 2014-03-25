package com.neodem.coup.serverside.actionProcessors;

import com.neodem.coup.CoupAction;
import com.neodem.coup.cards.CoupCard;
import com.neodem.coup.players.CoupPlayer;
import com.neodem.coup.serverside.ServerSideGameContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class CounterResolver {
    private static Log log = LogFactory.getLog(CounterResolver.class.getName());
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
    public boolean resolveCounter(CoupPlayer actingPlayer, CoupPlayer counteringPlayer, CoupAction counteredAction) {
        if (actingPlayer.doYouWantToChallengeThisCounter(counteringPlayer)) {
            Collection<CoupCard> cardsNeededToCounter = counteredAction.getCounterCard();

            if (challengeResolver.resolveChallenge(actingPlayer, counteringPlayer, cardsNeededToCounter)) {
                // if we are here, the challenge succeeded and the counter failed
                return false;
            }
        }

        return true;
    }


}
