package com.neodem.coup.common.game;

/**
 * Created with IntelliJ IDEA.
 * User: vfumo
 * Date: 3/3/14
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerError extends Exception {
    public PlayerError() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public PlayerError(String message) {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public PlayerError(String message, Throwable cause) {
        super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public PlayerError(Throwable cause) {
        super(cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected PlayerError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
