package com.htmake.htbot.discord.commands.battle.cache;

import com.htmake.htbot.cache.CacheManager;
import com.htmake.htbot.discord.commands.battle.data.Situation;

public class SituationCache {

    private final CacheManager<String, Situation> cache;

    public SituationCache() {
        this.cache = new CacheManager<>();
    }

    public void put(String key, Situation situation) {
        cache.put(key, situation);
    }

    public Situation get(String key) {
        return cache.get(key);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }
}
