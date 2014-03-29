package com.neodem.coup.common.game.cards;

import java.io.Serializable;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupCard implements Serializable {

    public CoupCardType type = CoupCardType.Unknown;
    public boolean faceUp = false;

    public CoupCard(CoupCardType type) {
        this.type = type;
        faceUp = false;
    }

    public CoupCard(CoupCardType type, boolean faceUp) {
        this.faceUp = faceUp;
        this.type = type;
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

        if (type != coupCard.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    public String displayCard() {
        if (faceUp) {
            return type.name();
        }
        return "(FACE DOWN)";
    }
}
