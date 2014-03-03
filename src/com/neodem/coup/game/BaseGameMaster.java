package com.neodem.coup.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 *
 * Author: vfumo
 * Date: 2/28/14
 */
public abstract class BaseGameMaster implements GameMaster {

    private Collection<String> usedNames;
    private int nextId = 0;
    protected List<Player> registeredPlayers;
    private int maxPlayers;

    public BaseGameMaster(int playersAllowed) {
        usedNames = new HashSet<String>();
        registeredPlayers = new ArrayList<Player>();
        maxPlayers = playersAllowed;
    }

    @Override
    public PlayerId registerPlayerName(String playerName) {
        if (usedNames.contains(playerName)) {
            return null;
        }

        PlayerId id = new PlayerId(playerName, nextId++);

        return id;
    }

    @Override
    public GameContext registerPlayerForNextGame(Player player) {
        if(registeredPlayers.size() == maxPlayers) {
            throw new IllegalStateException("max players already");
        }

        PlayerId id = player.getPlayerId();
        if (id == null) throw new IllegalArgumentException("need to be registered");
        if (!registeredPlayers.contains(player)) throw new IllegalArgumentException("Already registered");
        registeredPlayers.add(player);

        return generateCurrentGameContext();
    }

    @Override
    public void startGame() {
         initGame();

         runGameLoop();
    }

    protected abstract void runGameLoop();

    protected abstract void initGame();

    protected GameContext generateCurrentGameContext() {
        GameContext gc = makeEmptyGameContextObject();
        
        for(Player p : registeredPlayers) {
            gc.addPlayer(p.getPlayerId());
        }
        
        return gc;
    }

    protected abstract GameContext makeEmptyGameContextObject();
}
