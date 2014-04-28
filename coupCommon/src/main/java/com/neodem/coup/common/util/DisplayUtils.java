package com.neodem.coup.common.util;

import com.neodem.coup.common.game.actions.CoupAction;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/25/14
 */
public class DisplayUtils {

    public static String formatAction(CoupAction a, String doneBy) {
        StringBuilder b = new StringBuilder();
        b.append(doneBy);
        b.append(" is doing ");
        b.append(a);

        return b.toString();
    }
}
