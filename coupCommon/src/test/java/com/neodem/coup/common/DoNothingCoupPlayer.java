package com.neodem.coup.common;

import com.neodem.coup.common.game.GamePlayer;

import java.io.Serializable;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class DoNothingCoupPlayer implements GamePlayer, Serializable {

    private String name = "DoNothingName";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoNothingCoupPlayer)) return false;

        DoNothingCoupPlayer that = (DoNothingCoupPlayer) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String getMyName() {
        return name;
    }


}
