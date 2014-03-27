package com.neodem.coup.common.game;

import com.google.common.collect.Sets;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.Collection;

import static com.neodem.coup.common.game.CoupCardType.Ambassador;
import static com.neodem.coup.common.game.CoupCardType.Assasin;
import static com.neodem.coup.common.game.CoupCardType.Captain;
import static com.neodem.coup.common.game.CoupCardType.Contessa;
import static com.neodem.coup.common.game.CoupCardType.Duke;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 2/28/14
 */
public class CoupAction implements Serializable {

    public static CoupAction NoAction = new CoupAction(null, ActionType.NoAction);
    private static Log log = LogFactory.getLog(CoupAction.class.getName());
    private ActionType actionType;
    private CoupPlayer actionOn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoupAction)) return false;

        CoupAction that = (CoupAction) o;

        if (actionOn != null ? !actionOn.equals(that.actionOn) : that.actionOn != null) return false;
        if (actionType != that.actionType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = actionType != null ? actionType.hashCode() : 0;
        result = 31 * result + (actionOn != null ? actionOn.hashCode() : 0);
        return result;
    }

    public CoupAction(CoupPlayer actionOn, ActionType actionType) {
        this.actionOn = actionOn;
        this.actionType = actionType;
    }

    public static boolean isValidPlayableAction(ActionType actionType) {
        return actionType != ActionType.NoAction;
    }

    protected Log getLog() {
        return log;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public CoupPlayer getActionOn() {
        return actionOn;
    }

    @Override
    public String toString() {
        if (actionOn == null)
            return actionType.toString();

        return actionType.toString() + " on " + actionOn.getMyName();
    }

    /**
     * @return the card that the player needs to have to play this action or null if none needed
     */
    public CoupCardType getActionCard() {
        switch (actionType) {
            case Tax:
                return CoupCardType.Duke;
            case Assassinate:
                return CoupCardType.Assasin;
            case Steal:
                return CoupCardType.Captain;
            case Exchange:
                return CoupCardType.Ambassador;
        }

        return null;
    }

    /**
     * @return the card(s) that the player needs to have to counter this action or null if not applicable
     */
    public Collection<CoupCardType> getCounterCard() {
        switch (actionType) {
            case ForeignAid:
                return Sets.newHashSet(CoupCardType.Duke);
            case Steal:
                return Sets.newHashSet(CoupCardType.Ambassador, CoupCardType.Captain);
            case Assassinate:
                return Sets.newHashSet(CoupCardType.Contessa);

        }

        return null;
    }

    /**
     * @return true if this action can be challenged
     */
    public boolean isChallengeable() {
        switch (actionType) {
            case Tax:
            case Assassinate:
            case Steal:
            case Exchange:
                return true;
        }

        return false;
    }

    public boolean isCounterable() {
        switch (actionType) {
            case ForeignAid:
            case Steal:
            case Assassinate:
                return true;
        }

        return false;
    }

    /**
     * Steal and Assasinate can only be countered by the individiual. Foreign Aid can be
     * countered by anyone
     *
     * @return
     */
    public boolean isCounterableByGroup() {
        return actionType == ActionType.ForeignAid;
    }

    public boolean hasCost() {
        return actionType == ActionType.Tax || actionType == ActionType.Coup;
    }

    public enum ActionType {
        Income,
        ForeignAid,
        Coup,
        Tax,
        Assassinate,
        Exchange,
        Steal,

        NoAction
    }

}
