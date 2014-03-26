package com.neodem.coup.client;

import com.google.common.collect.Multiset;
import com.neodem.coup.common.CoupAction;
import com.neodem.coup.common.CoupCard;
import com.neodem.coup.common.CoupCardType;
import com.neodem.coup.common.CoupGameContext;
import com.neodem.coup.common.CoupPlayer;

import java.io.Serializable;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class DoNothingCoupPlayer implements CoupPlayer, Serializable {

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
    public CoupAction yourTurn(CoupGameContext gc) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateContext(CoupGameContext gc) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void actionHappened(CoupPlayer player, CoupAction hisAction, CoupGameContext gc) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void tryAgain(String reason) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getMyName() {
        return name;
    }

    @Override
    public void initializePlayer(CoupGameContext g) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean doYouWantToCounterThisAction(CoupAction theAction, CoupPlayer thePlayer, CoupGameContext gc) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean doYouWantToChallengeThisAction(CoupAction theAction, CoupPlayer thePlayer, CoupGameContext gc) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean doYouWantToChallengeThisCounter(CoupPlayer playerCountering) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean doYouWantToProveYouHaveACardOfThisType(CoupCardType challengedCardType) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Multiset<CoupCard> exchangeCards(Multiset<CoupCard> cards) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CoupCard youMustLooseAnInfluence() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
