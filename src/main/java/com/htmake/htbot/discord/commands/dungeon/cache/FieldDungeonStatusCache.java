package com.htmake.htbot.discord.commands.dungeon.cache;

import com.htmake.htbot.global.cache.CacheManager;
import com.htmake.htbot.discord.commands.dungeon.data.FieldDungeonStatus;

public class FieldDungeonStatusCache {

    private final CacheManager<String, FieldDungeonStatus> cache;

    public FieldDungeonStatusCache() {
        this.cache = new CacheManager<>();
    }

    public void put(String key, FieldDungeonStatus FieldDungeonStatus) {
        cache.put(key, FieldDungeonStatus);
    }

    public FieldDungeonStatus get(String key) {
        return cache.get(key);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }
}
