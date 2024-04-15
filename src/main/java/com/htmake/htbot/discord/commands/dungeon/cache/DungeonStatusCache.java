package com.htmake.htbot.discord.commands.dungeon.cache;

import com.htmake.htbot.global.cache.CacheManager;
import com.htmake.htbot.discord.commands.dungeon.data.DungeonStatus;

public class DungeonStatusCache {

    private final CacheManager<String, DungeonStatus> cache;

    public DungeonStatusCache() {
        this.cache = new CacheManager<>();
    }

    public void put(String key, DungeonStatus DungeonStatus) {
        cache.put(key, DungeonStatus);
    }

    public DungeonStatus get(String key) {
        return cache.get(key);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }
}
