package com.neodem.coup.coup;

import com.neodem.coup.game.GameContext;
import com.neodem.coup.game.PlayerId;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupGameContext extends GameContext {
    public Map<PlayerId, CoupPlayerInfo> playerInfos;

    private Map<PlayerId, CoupPlayerInfo> infos = new HashMap<PlayerId, CoupPlayerInfo>();
    public void addInfo(PlayerId id, CoupPlayerInfo coupPlayerInfo) {
        infos.put(id, coupPlayerInfo);
    }
}
