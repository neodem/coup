package com.neodem.coup.coup.players;

import com.neodem.bandaid.game.Player;
import com.neodem.coup.coup.CoupAction;
import com.neodem.coup.coup.CoupPlayerInfo;
import com.neodem.coup.coup.cards.CoupCard;

import java.util.Collection;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public interface CoupPlayer extends Player<CoupAction> {

    /**
     * this is used in the Exchange action. If the player elects an exchange, the CoupGameMaster
     * will add 2 cards to the players current face down cards and pass that as a param to this method.
     * The player must return one or two cards (depending on the number they have down) else
     * the CoupGameMaster will alert the player and ask to do this again.
     *
     * @param cards the entire 'hand' of cards the player has to choose from
     * @return the cards they are returning to the deck
     */
    public Collection<CoupCard> exchangeCards(Collection<CoupCard> cards);

    /**
     * this is used when a player is challenged. the CGM is asking the player if they would like to
     * prove they have the card. If you return false you will loose the challenge. If you return true
     * the CGM will determine if you have the card. If you do, you win the challenge. If not you will
     * loose the challenge.
     *
     * @param challengedAction the action that you initiated that is being challenged
     * @return weather you want to prove you have the card
     */
    public boolean proveYouHaveCorrectCard(CoupAction challengedAction);

    /**
     * the CGM is telling you that you've lost an influence. You need to choose
     * one of your face down cards to turn over. If you choose one that is invalid (face up already, or you don't
     * have it) you will be alerted and asked to do this again
     *
     * @return the card you want to turn face up
     */
    public CoupCard looseAnInfluence();

    /**
     * whenever the CGM feels like updating you on your current state in the game you will get this call.
     *
     * @param currentState the current state of your hand/coins
     */
    public void updateInfo(CoupPlayerInfo currentState);

}

