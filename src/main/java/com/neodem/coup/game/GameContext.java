package com.neodem.coup.game;

import com.neodem.common.collections.CircularList;
import com.neodem.common.collections.DefaultCircularList;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class GameContext<P extends Player> {

    // a list of the players in the game (ordered by their position in the game)
    protected List<P> players = new ArrayList<P>();

    public void addPlayer(P p) {
        players.add(p);
    }

    public List<P> getPlayers() {
        return players;
    }

    @Override
    public String toString() {

        StringBuffer b = new StringBuffer();
        b.append("Game Context\n");
        b.append("------------\n");
        b.append(players.size());
        b.append(" players: ");

        for (Player p : players) {
            b.append(p);
        }

        b.append('\n');

        return b.toString();
    }
}
