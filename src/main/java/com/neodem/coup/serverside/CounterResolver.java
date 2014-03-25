package com.neodem.coup.serverside;

import com.neodem.coup.CoupAction;
import com.neodem.coup.players.CoupPlayer;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class CounterResolver {

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
