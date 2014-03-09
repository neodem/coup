package com.neodem.coup.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public class GameContext {

    // a list of the players in the game (ordered by their position in the game)
    protected List<Player> players = new ArrayList<Player>();

    public void addPlayer(Player p) {
        players.add(p);
    }

    public List<Player> getPlayers() {
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
