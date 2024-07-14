package com.htmake.htbot.discord.commands.quest.cache;

import com.htmake.htbot.discord.commands.quest.data.QuestDialogueData;
import com.htmake.htbot.global.cache.CacheManager;

public class QuestDialogueCache {

    private final CacheManager<String, QuestDialogueData> cache;

    public QuestDialogueCache() {
        this.cache = new CacheManager<>();
    }

    public void put(String key, QuestDialogueData questDialogueData) {
        cache.put(key, questDialogueData);
    }

    public QuestDialogueData get(String key) {
        return cache.get(key);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }
}
