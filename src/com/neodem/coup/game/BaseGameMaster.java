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
    protected List<PlayerId> registeredPlayers;
    private int maxPlayers;

    public BaseGameMaster(int playersAllowed) {
        usedNames = new HashSet<String>();
        registeredPlayers = new ArrayList<PlayerId>();
        maxPlayers = playersAllowed;
    }

    @Override
    public PlayerId registerPlayer(String playerName) {
        if (usedNames.contains(playerName)) {
            return null;
        }

        PlayerId id = new PlayerId(playerName, nextId++);

        return id;
    }

    @Override
    public GameContext register(Player player) {
        if(registeredPlayers.size() == maxPlayers) {
            throw new IllegalStateException("max players already");
        }

        PlayerId id = player.getPlayerId();
        if (id == null) throw new IllegalArgumentException("need to be registered");
        if (!registeredPlayers.contains(id)) throw new IllegalArgumentException("Already registered");
        registeredPlayers.add(id);

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
        
        for(PlayerId pid : registeredPlayers) {
            gc.addPlayer(pid);
        }
        
        return gc;
    }

    protected abstract GameContext makeEmptyGameContextObject();
}
