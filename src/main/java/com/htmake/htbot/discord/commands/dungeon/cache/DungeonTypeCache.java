package com.htmake.htbot.discord.commands.dungeon.cache;

import com.htmake.htbot.discord.commands.dungeon.enums.DungeonType;
import com.htmake.htbot.global.cache.CacheManager;

public class DungeonTypeCache {

    private final CacheManager<String, DungeonType> cache;

    public DungeonTypeCache() {
        this.cache = new CacheManager<>();
    }

    public void put(String key, DungeonType dungeonType) {
        cache.put(key, dungeonType);
    }

    public DungeonType get(String key) {
        return cache.get(key);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }
}
