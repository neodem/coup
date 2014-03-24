package com.neodem.coup.coup.serverside;

import com.neodem.coup.coup.CoupAction;
import com.neodem.coup.coup.players.CoupPlayer;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public class CounterResolver {

    private CoupGameMaster gm;

    public CounterResolver(CoupGameMaster gm) {
        this.gm = gm;
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
