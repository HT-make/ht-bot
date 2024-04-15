package com.htmake.htbot.discord.commands.battle.cache;

import com.htmake.htbot.global.cache.CacheManager;
import com.htmake.htbot.discord.commands.battle.data.PlayerStatus;

public class PlayerStatusCache {

    private final CacheManager<String, PlayerStatus> cache;

    public PlayerStatusCache() {
        this.cache = new CacheManager<>();
    }

    public void put(String key, PlayerStatus playerStatus) {
        cache.put(key, playerStatus);
    }

    public PlayerStatus get(String key) {
        return cache.get(key);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }
}
