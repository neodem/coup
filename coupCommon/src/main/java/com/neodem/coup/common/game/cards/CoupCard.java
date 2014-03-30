package com.neodem.coup.common.game.cards;

import java.io.Serializable;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupCard implements Serializable {

    private final CoupCardType type;
    private boolean faceUp;
    private final int cardId;

    public static CoupCard newUnknownCard() {
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
        return String.format("%s : %s", type.name(), faceUp ? "up" : "down");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoupCard)) return false;

        CoupCard coupCard = (CoupCard) o;

        return cardId == coupCard.cardId;
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

    public CoupCardType getCardType() {
        return type;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void setFaceUp() {
        this.faceUp = true;
    }

    public void setFaceDown() {
        this.faceUp = false;
    }

    public int getCardId() {
        return cardId;
    }
}
