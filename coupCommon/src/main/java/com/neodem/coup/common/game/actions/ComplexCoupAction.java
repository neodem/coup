package com.neodem.coup.common.game.actions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is an action that happens to someone
 * <p/>
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 2/28/14
 */
public class ComplexCoupAction extends SimpleCoupAction {

    private static final Logger log = LogManager.getLogger(ComplexCoupAction.class.getName());
    private final String actionOn;

    ComplexCoupAction(String actionOn, ActionType actionType) {
        super(actionType);
        this.actionOn = actionOn;
    }

    @Override
    protected Logger getLog() {
        return log;
    }

    public String getActionOn() {
        return actionOn;
    }

    @Override
    public String toString() {
        return actionType.toString() + " on " + actionOn;
    }
}
