package com.neodem.coup.serverside.actionProcessors;

import com.neodem.coup.CoupAction;
import com.neodem.coup.players.CoupPlayer;
import com.neodem.coup.serverside.ServerSideGameContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class CounterResolver {
    private static Log log = LogFactory.getLog(CounterResolver.class.getName());
    private ServerSideGameContext context;

    public CounterResolver(ServerSideGameContext context) {
        this.context = context;
    }

    /**
     * @param actingPlayer      the player who is being countered
     * @param challengingPlayer the player doing the countering
     * @param counteredAction   the action being countered
     * @return true if the counter was successful, false otherwise
     */
    public boolean resolveCounter(CoupPlayer actingPlayer, CoupPlayer challengingPlayer, CoupAction counteredAction) {


        return false;
    }


}
