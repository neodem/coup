package com.neodem.coup.common.game.cards;

import java.io.Serializable;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupCard implements Serializable {

    public CoupCardType type;
    public boolean faceUp;
    public int cardId;

    public static CoupCard makeUnknown() {
        return new CoupCard(-1, CoupCardType.Unknown);
    }

    public CoupCard(int cardId) {
        this(cardId, CoupCardType.Unknown, false);
    }

    public CoupCard(int cardId, CoupCardType type) {
        this(cardId, type, false);
    }

    public CoupCard(int cardId, CoupCardType type, boolean faceUp) {
        this.faceUp = faceUp;
        this.type = type;
        this.cardId = cardId;
    }

    @Override
    public String toString() {
        return type.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoupCard)) return false;

        CoupCard coupCard = (CoupCard) o;

        if (cardId != coupCard.cardId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return cardId;
    }

    public String displayCard() {
        if (faceUp) {
            return type.name();
        }
        return "(FACE DOWN)";
    }


}
