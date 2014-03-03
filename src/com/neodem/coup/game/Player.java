package com.neodem.coup.game;

/**
 *
 * Author: vfumo
 * Date: 2/28/14
 */
public interface Player {

    /**
     * called by the GameMaster when this Player has a turn to process.
     * 'turn' may not be a game turn but it is at least a time that
     * the client has to do something
     *
     * @param gc
     * @return
     */
    public Action yourTurn(GameContext gc);

    /**
     * called by the GameMaster to alert other players to an action
     * by another player.
     *
     * @param playerId
     * @param hisAction
     * @return
     */
    public Action actionHappened(PlayerId playerId, Action hisAction, GameContext gc);

    /**
     * the player is sent a message/alert. Players must always be aware of these.
     *
     * @param a
     */
    public void alert(Alert a);

    /**
     * get the id of the player
     *
     * @return
     */
    public PlayerId getPlayerId();
}
