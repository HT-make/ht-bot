package com.htmake.htbot.discord.commands.global.cache;

import com.htmake.htbot.global.cache.CacheManager;

public class MessageCache {

    private final CacheManager<String, Boolean> cache;

    public MessageCache() {
        this.cache = new CacheManager<>();
    }

    public void put(String key, Boolean check) {
        cache.put(key, check);
    }

    public Boolean get(String key) {
        return cache.get(key);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }
}
