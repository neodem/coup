package com.neodem.coup;

import com.neodem.bandaid.game.Alert;

/**
 * Created with IntelliJ IDEA.
 * User: vfumo
 * Date: 3/3/14
 * Time: 12:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoupAlert extends Alert {
    public static final CoupAlert MustCoup = new CoupAlert(AlertType.MustCoup);
    private final AlertType alertType;

    public CoupAlert(AlertType t) {
        this.alertType = t;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public enum AlertType {
        MustCoup;
    }
}
