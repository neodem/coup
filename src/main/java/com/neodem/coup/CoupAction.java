package com.neodem.coup;

import com.google.common.collect.Sets;
import com.neodem.bandaid.game.Action;
import com.neodem.coup.cards.CoupCard;
import com.neodem.coup.players.CoupPlayer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 2/28/14
 */
public class CoupAction extends Action {

    public static CoupAction NoAction = new CoupAction(null, ActionType.NoAction);
    private static Log log = LogFactory.getLog(CoupAction.class.getName());
    private ActionType actionType;
    private CoupPlayer actionOn;

    public CoupAction(CoupPlayer actionOn, ActionType actionType) {
        this.actionOn = actionOn;
        this.actionType = actionType;
    }

    public static boolean isValidPlayableAction(ActionType actionType) {
        return actionType != ActionType.NoAction;
    }

    @Override
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
    public CoupCard getActionCard() {
        switch (actionType) {
            case Tax:
                return CoupCard.Duke;
            case Assassinate:
                return CoupCard.Assasin;
            case Steal:
                return CoupCard.Captain;
            case Exchange:
                return CoupCard.Ambassador;
        }

        return null;
    }

    /**
     * @return the card(s) that the player needs to have to counter this action or null if not applicable
     */
    public Collection<CoupCard> getCounterCard() {
        switch (actionType) {
            case ForeignAid:
                return Sets.newHashSet(CoupCard.Duke);
            case Steal:
                return Sets.newHashSet(CoupCard.Ambassador, CoupCard.Captain);
            case Assassinate:
                return Sets.newHashSet(CoupCard.Contessa);

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