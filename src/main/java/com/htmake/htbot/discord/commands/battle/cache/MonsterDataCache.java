package com.htmake.htbot.discord.commands.battle.cache;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.global.cache.CacheManager;

public class MonsterDataCache {

    private final CacheManager<String, MonsterData> cache;

    public MonsterDataCache() {
        this.cache = new CacheManager<>();
    }

    public void put(String key, MonsterData monsterData) {
        cache.put(key, monsterData);
    }

    public MonsterData get(String key) {
        return cache.get(key);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }
}
