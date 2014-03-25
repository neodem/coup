package com.neodem.coup.util;

import com.neodem.bandaid.game.Player;
import com.neodem.coup.CoupAction;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/25/14
 */
public class DisplayUtils {

    public static String formatAction(CoupAction a, Player doneBy) {
        StringBuilder b = new StringBuilder();
        b.append(doneBy.getMyName());
        b.append(" is doing ");
        b.append(a);

        return b.toString();
    }
}
