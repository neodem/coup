package com.neodem.coup.common.game.actions;

import com.google.common.collect.Sets;
import com.neodem.coup.common.game.cards.CoupCardType;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 2/28/14
 */
public abstract class CoupAction {
    protected final ActionType actionType;

    public enum ActionType {
        Income,
        ForeignAid,
        Coup,
        Tax,
        Assassinate,
        Exchange,
        Steal,

        NoAction;

        public boolean isSimple() {
            switch (this) {
                case Income:
                case ForeignAid:
                case Tax:
                case Exchange:
                    return true;
            }

            return false;
        }

        public boolean isValidPlayableAction() {
            return this != NoAction;
        }

        /**
         * @return true if this action can be challenged
         */
        public boolean isChallengeable() {
            switch (this) {
                case Tax:
                case Assassinate:
                case Steal:
                case Exchange:
                    return true;
            }

            return false;
        }

        public boolean isCounterable() {
            switch (this) {
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
         * @return if this action type can be countered by players other than the one targeted
         */
        public boolean isCounterableByGroup() {
            return this == ForeignAid;
        }

        public boolean hasCost() {
            return this == Tax || this == Coup;
        }
    }

    public CoupAction(ActionType actionType) {
        this.actionType = actionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoupAction)) return false;

        CoupAction that = (CoupAction) o;

        return actionType == that.actionType;
    }

    @Override
    public int hashCode() {
        return actionType.hashCode();
    }

    protected abstract Logger getLog();

    public ActionType getActionType() {
        return actionType;
    }

    @Override
    public String toString() {
        return actionType.toString();

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
}
