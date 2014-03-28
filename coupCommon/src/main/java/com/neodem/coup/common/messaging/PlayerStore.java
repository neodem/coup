package com.neodem.coup.common.messaging;

import com.neodem.coup.common.game.CoupPlayer;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/28/14
 */
public interface PlayerStore {
    CoupPlayer getPlayer(String playerName);

    void storePlayer(CoupPlayer p);
}
