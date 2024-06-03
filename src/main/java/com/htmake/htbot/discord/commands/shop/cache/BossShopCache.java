package com.htmake.htbot.discord.commands.shop.cache;

import com.htmake.htbot.discord.commands.shop.data.BossShopItem;
import com.htmake.htbot.domain.shop.entity.BossShop;
import com.htmake.htbot.global.cache.CacheManager;

import java.util.List;

public class BossShopCache {

    private final CacheManager<String, List<BossShopItem>> cache;

    public BossShopCache() {
        this.cache = new CacheManager<>();
    }

    public void put(String key, List<BossShopItem> bossShopItemList) {
        cache.put(key, bossShopItemList);
    }

    public List<BossShopItem> get(String key) {
        return cache.get(key);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }
}
