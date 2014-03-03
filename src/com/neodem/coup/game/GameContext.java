package com.neodem.coup.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class GameContext {

    // a list of the players in the game (ordered by thier position in the game)
    private List<PlayerId> players = new ArrayList<PlayerId>();

    public void addPlayer(PlayerId id) {
        players.add(id);
    }

    public List<PlayerId> getPlayers() {
        return players;
    }
}
