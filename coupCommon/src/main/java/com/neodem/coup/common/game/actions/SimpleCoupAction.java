package com.neodem.coup.common.game.actions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 2/28/14
 */
public class SimpleCoupAction extends CoupAction {

    public static SimpleCoupAction NoAction = new SimpleCoupAction(ActionType.NoAction);
    private static Logger log = LogManager.getLogger(SimpleCoupAction.class.getName());

    SimpleCoupAction(ActionType actionType) {
        super(actionType);
    }

    @Override
    protected Logger getLog() {
        return log;
    }
}
