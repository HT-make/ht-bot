package com.htmake.htbot.discord.commands.inventory.cache;

import com.htmake.htbot.discord.commands.inventory.data.Inventory;
import com.htmake.htbot.global.cache.CacheManager;

public class InventoryCache {

    private final CacheManager<String, Inventory> cache;

    public InventoryCache() {
        this.cache = new CacheManager<>();
    }

    public void put(String key, Inventory inventory) {
        cache.put(key, inventory);
    }

    public Inventory get(String key) {
        return cache.get(key);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }
}
