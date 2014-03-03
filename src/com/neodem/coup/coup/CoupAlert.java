package com.neodem.coup.coup;

import com.neodem.coup.game.Alert;

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

    CoupAlert(AlertType t) {
        this.alertType = t;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public enum AlertType {
        MustCoup;
    }
}
