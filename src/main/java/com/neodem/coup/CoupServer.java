package com.neodem.coup;

import com.neodem.coup.common.CoupPlayer;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/26/14
 */
public interface CoupServer {
    public void registerPlayer(CoupPlayer player);

    public void triggerGameStart();
}
