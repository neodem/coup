package com.neodem.coup.common.messaging;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public enum MessageType {
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
    gmMessage,
    register,
    unknown;

    public boolean requiresReply() {
        return this == yourTurn || this == counterAction || this == challengeAction || this == challengeCounter || this == proveCard || this == looseInfluence || this == exchangeCards;
    }
}
