package com.htmake.htbot.discord.commands.skill.cache;

import com.htmake.htbot.discord.commands.skill.data.SkillList;
import com.htmake.htbot.global.cache.CacheManager;

public class SkillCache {

    private final CacheManager<String, SkillList> cache;

    public SkillCache() {
        this.cache = new CacheManager<>();
    }

    public void put(String key, SkillList skillList) {
        cache.put(key, skillList);
    }

    public SkillList get(String key) {
        return cache.get(key);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }
}
