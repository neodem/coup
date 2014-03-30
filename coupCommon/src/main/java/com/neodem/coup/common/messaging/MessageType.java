package com.neodem.coup.common.messaging;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public enum MessageType {

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
    gmMessage,

    //==== server related message types

    // called from client : used to see the current status of the server
    status,

    // called from client : used to resiger your name with the server
    register,

    // called from server : used to tell a client the current status
    serverStatus,

    unknown;

    public boolean requiresReply() {
        return this == yourTurn || this == counterAction || this == challengeAction || this == challengeCounter || this == proveCard || this == looseInfluence || this == exchangeCards;
    }
}
