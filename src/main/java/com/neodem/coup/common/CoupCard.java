package com.neodem.coup.common;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupCard {

    public CoupCardType type = CoupCardType.Unknown;
    public boolean faceUp = false;

    public CoupCard(CoupCardType type) {
        this.type = type;
        faceUp = false;
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

        StringBuffer b = new StringBuffer();

        if (faceUp) {
            b.append(type.name());
        } else {
            b.append("(FACE DOWN)");
        }

        return b.toString();
    }
}
