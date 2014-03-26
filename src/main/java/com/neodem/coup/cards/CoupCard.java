package com.neodem.coup.cards;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class CoupCard {

    public CoupCardType type;
    public boolean faceUp = false;

    public CoupCard(CoupCardType type) {
        this.type = type;
        faceUp = false;
    }

    @Override
    public String toString() {
        return type.name();
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
