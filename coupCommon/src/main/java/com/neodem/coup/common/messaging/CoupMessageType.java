package com.neodem.coup.common.messaging;

import com.neodem.bandaid.messaging.GameMessageType;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public enum CoupMessageType implements GameMessageType {

    //TODO figure out a way to abstract this out so we can have a base message type and a game specific one on top


    //==== game related message types
    yourTurn,
    updateContext,
    tryAgain,
    actionHappened,
    intializePlayer,
    counterAction,
    challengeAction,
    challengeCounter,
    proveCard,
    looseInfluence,
    exchangeCards,
    reply,
    gmMessage, unknown;

    @Override
    public boolean requiresReply() {
        return this == yourTurn || this == counterAction || this == challengeAction || this == challengeCounter || this == proveCard || this == looseInfluence || this == exchangeCards;
    }
}
