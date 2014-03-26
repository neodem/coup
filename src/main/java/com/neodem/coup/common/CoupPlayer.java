package com.neodem.coup.common;

import com.google.common.collect.Multiset;

/**
 * note : Any time you get a CoupGameContext, it will show you the current game context and your specific info.
 * <p/>
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/24/14
 */
public interface CoupPlayer {

    /**
     * called by the GameMaster when this Player has a turn to process.
     * 'turn' may not be a game turn but it is at least a time that
     * the client has to do something
     *
     * @param gc the current game context
     * @return the action the player wants to perform
     */
    public CoupAction yourTurn(CoupGameContext gc);

    /**
     * something changed, the GameMaster wants to let you know
     *
     * @param gc the current game context
     */
    public void updateContext(CoupGameContext gc);

    /**
     * called by the GameMaster to alert other players to an action
     * by another player.
     *
     * @param player    the player who initiated the action
     * @param hisAction the action initiated
     * @param gc        the current game context
     */
    public void actionHappened(CoupPlayer player, CoupAction hisAction, CoupGameContext gc);

    /**
     * The players action was rejected and they will be called to try again
     *
     * @param reason the reason the player needs to try again
     *               TODO replace reason with an enum
     */
    public void tryAgain(String reason);

    /**
     * get the id of the player
     *
     * @return the name of the player (should never change)
     */
    public String getMyName();

    /**
     * will be called just before the game begins. This will allow the player to get set up
     *
     * @param g
     */
    public void initializePlayer(CoupGameContext g);

    /**
     * Called to ask the player if they want to counter the current action
     *
     * @param theAction the action initiated
     * @param thePlayer the player who initiated the action
     * @param gc        the current game context
     * @return true if the player wants to counter the current action
     */
    public boolean doYouWantToCounterThisAction(CoupAction theAction, CoupPlayer thePlayer, CoupGameContext gc);

    /**
     * Called to ask the player if they want to challenge the current action
     *
     * @param theAction the action initiated
     * @param thePlayer the player who initiated the action
     * @param gc        the current game context
     * @return true if the player wants to challenge the current action
     */
    public boolean doYouWantToChallengeThisAction(CoupAction theAction, CoupPlayer thePlayer, CoupGameContext gc);

    /**
     * Called to ask the player if he/she wants to challenge the counter that is being played against them
     *
     * @param playerCountering the player countering
     * @return true if the player wants to challenge this counter
     */
    public boolean doYouWantToChallengeThisCounter(CoupPlayer playerCountering);

    /**
     * this is called when a player is challenged. the CGM is asking the player if they would like to
     * prove they have the card. If you return false you will loose the challenge. If you return true
     * the CGM will determine if you have the card. If you do, you win the challenge. If not you will
     * loose the challenge.
     *
     * @param challengedCardType the type of card you are being asked to prove you have
     * @return weather you want to prove you have the card
     */
    public boolean doYouWantToProveYouHaveACardOfThisType(CoupCardType challengedCardType);

    /**
     * this is used in the Exchange action. If the player elects an exchange, the CoupGameMaster
     * will add 2 cards to the players current face down cards and pass that as a param to this method.
     * The player must return two cards from his ffa else
     * the CoupGameMaster will alert the player and ask to do this again.
     *
     * @param cards the new cards and the face downn cards from the players deck that he has to choose to discard from
     * @return the cards they are returning to the deck
     */
    public Multiset<CoupCard> exchangeCards(Multiset<CoupCard> cards);

    /**
     * the CGM is telling you that you've lost an influence. You need to choose
     * one of your face down cards to turn over. If you choose one that is invalid (face up already, or you don't
     * have it) you will be alerted and asked to do this again
     *
     * @return the card you want to turn face up
     */
    public CoupCard youMustLooseAnInfluence();
}