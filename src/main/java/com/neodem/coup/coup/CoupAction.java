package com.neodem.coup.coup;

import com.google.common.collect.Sets;
import com.neodem.bandaid.game.Action;
import com.neodem.coup.coup.cards.CoupCard;
import com.neodem.coup.coup.players.CoupPlayer;

import java.util.Collection;

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
    private CoupPlayer actionBy;
    private CoupPlayer actionOn;

    public CoupAction(CoupPlayer actionBy, CoupPlayer actionOn, ActionType actionType) {
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

    public CoupPlayer getActionBy() {
        return actionBy;
    }

    public CoupPlayer getActionOn() {
        return actionOn;
    }

    @Override
    public String toString() {
        return actionType.toString();
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
