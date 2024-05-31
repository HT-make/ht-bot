package com.htmake.htbot.discord.commands.battle.cache;

import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.global.cache.CacheManager;

public class PlayerDataCache {

    private final CacheManager<String, PlayerData> cache;

    public PlayerDataCache() {
        this.cache = new CacheManager<>();
    }

    public void put(String key, PlayerData playerData) {
        cache.put(key, playerData);
    }

    public PlayerData get(String key) {
        return cache.get(key);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }
}
