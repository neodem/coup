package com.neodem.coup.coup.cards;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public enum CoupCard {

    Captain,
    Contessa,
    Assasin,
    Ambassador,
    Duke,
    Unknown;

    public boolean faceUp = false;

    @Override
    public String toString() {

        StringBuffer b = new StringBuffer();

        if (faceUp) {
            b.append(name());
        } else {
            b.append("(FACE DOWN)");
        }

        return b.toString();
    }
}
