package com.neodem.coup.coup;

import com.neodem.coup.game.Action;
import com.neodem.coup.game.Player;

/**
 * Created with IntelliJ IDEA.
 * User: vfumo
 * Date: 2/28/14
 * Time: 4:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoupAction extends Action {

    public static CoupAction NoAction = new CoupAction(null, null, ActionType.NoAction);

    private ActionType actionType;
    private Player actionBy;
    private Player actionOn;

    public CoupAction(Player actionBy, Player actionOn, ActionType actionType) {
        this.actionBy = actionBy;
        this.actionOn = actionOn;
        this.actionType = actionType;
    }

    public static boolean validPlayableAction(ActionType actionType) {
        return actionType != ActionType.NoAction &&
                actionType != ActionType.Challenge &&
                actionType != ActionType.Counter;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public Player getActionBy() {
        return actionBy;
    }

    public Player getActionOn() {
        return actionOn;
    }

    public enum ActionType {
        Income,
        ForeignAid,
        Coup,
        Tax,
        Assassinate,
        Exchange,
        Steal,

        NoAction, Challenge, Counter;
    }

}
