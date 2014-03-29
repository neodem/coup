package com.neodem.coup.common.game.actions;

import com.neodem.coup.common.game.actions.CoupAction.ActionType;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/29/14
 */
public class CoupActionFactory {

    public static CoupAction newAction(ActionType type, String actionOn) {
        if (type.isSimple()) {
            return new SimpleCoupAction(type);
        }
        if (actionOn == null) {
            throw new IllegalArgumentException("This action requires a player to act on");
        }
        return new ComplexCoupAction(actionOn, type);
    }
}
