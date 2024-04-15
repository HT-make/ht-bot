package com.htmake.htbot.discord.commands.battle.cache;

import com.htmake.htbot.global.cache.CacheManager;
import com.htmake.htbot.discord.commands.battle.data.MonsterStatus;

public class MonsterStatusCache {

    private final CacheManager<String, MonsterStatus> cache;

    public MonsterStatusCache() {
        this.cache = new CacheManager<>();
    }

    public void put(String key, MonsterStatus monsterStatus) {
        cache.put(key, monsterStatus);
    }

    public MonsterStatus get(String key) {
        return cache.get(key);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }
}
