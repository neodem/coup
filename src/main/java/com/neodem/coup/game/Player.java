package com.neodem.coup.game;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public interface Player {

    /**
     * called by the GameMaster when this Player has a turn to process.
     * 'turn' may not be a game turn but it is at least a time that
     * the client has to do something
     *
     * @param gc the current game context
     * @return
     */
    public Action yourTurn(GameContext gc);

    /**
     * called by the GameMaster to alert other players to an action
     * by another player.
     *
     * @param player    the player who initiated the action
     * @param hisAction the action initiated
     * @param gc        the current game context
     * @return
     */
    public Action actionHappened(Player player, Action hisAction, GameContext gc);

    /**
     * The players action was rejected and they will be called to try again
     *
     * @param reason
     */
    public void tryAgain(String reason);

    /**
     * get the id of the player
     *
     * @return
     */
    public String getPlayerName();
}
