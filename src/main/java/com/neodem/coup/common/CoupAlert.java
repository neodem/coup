package com.neodem.coup.common;

import com.neodem.bandaid.game.Alert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created with IntelliJ IDEA.
 * User: vfumo
 * Date: 3/3/14
 * Time: 12:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoupAlert extends Alert {

    public static final CoupAlert MustCoup = new CoupAlert(AlertType.MustCoup);
    private static Log log = LogFactory.getLog(CoupAlert.class.getName());
    private final AlertType alertType;

    public CoupAlert(AlertType t) {
        this.alertType = t;
    }

    @Override
    protected Log getLog() {
        return log;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public enum AlertType {
        MustCoup;
    }
}
