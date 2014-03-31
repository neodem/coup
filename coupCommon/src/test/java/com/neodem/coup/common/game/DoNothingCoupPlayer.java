package com.neodem.coup.common.game;

import com.google.common.collect.Multiset;
import com.neodem.coup.common.game.actions.CoupAction;
import com.neodem.coup.common.game.actions.SimpleCoupAction;
import com.neodem.coup.common.game.cards.CoupCard;
import com.neodem.coup.common.game.cards.CoupCardType;

import java.io.Serializable;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class DoNothingCoupPlayer implements CoupPlayerCallback, Serializable {

    private final String name = "DoNothingName";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoNothingCoupPlayer)) return false;

        DoNothingCoupPlayer that = (DoNothingCoupPlayer) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String getPlayerName() {
        return name;
    }

    @Override
    public SimpleCoupAction yourTurn(CoupGameContext gc) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateContext(CoupGameContext gc) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void messageFromGM(String message) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void actionHappened(String player, CoupAction hisAction, CoupGameContext gc) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void tryAgain(String reason) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void initializePlayer(CoupGameContext g) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean doYouWantToCounterThisAction(CoupAction theAction, String thePlayer, CoupGameContext gc) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean doYouWantToChallengeThisAction(CoupAction theAction, String thePlayer, CoupGameContext gc) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean doYouWantToChallengeThisCounter(String playerCountering) {
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
