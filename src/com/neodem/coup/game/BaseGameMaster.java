package com.neodem.coup.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public abstract class BaseGameMaster implements GameMaster {

    protected List<Player> registeredPlayers;
    private Collection<String> usedNames;
    private int maxPlayers;

    public BaseGameMaster(int playersAllowed) {
        usedNames = new HashSet<String>();
        registeredPlayers = new ArrayList<Player>();
        maxPlayers = playersAllowed;
    }

    protected abstract void runGameLoop();

    protected abstract void initGame();

    protected abstract GameContext makeEmptyGameContextObject();

    @Override
    public GameContext registerPlayerForNextGame(Player player) {
        String playerName = player.getPlayerName();

        if (usedNames.contains(playerName)) throw new IllegalArgumentException("name already used");
        usedNames.add(playerName);

        if (registeredPlayers.size() == maxPlayers) {
            throw new IllegalStateException("max players already");
        }

        if (registeredPlayers.contains(player)) throw new IllegalArgumentException("Already registered");
        registeredPlayers.add(player);

        return generateCurrentGameContext();
    }

    @Override
    public void startGame() {
        initGame();
        runGameLoop();
    }

    protected GameContext generateCurrentGameContext() {
        GameContext gc = makeEmptyGameContextObject();

        for (Player p : registeredPlayers) {
            gc.addPlayer(p);
        }

        return gc;
    }
}
