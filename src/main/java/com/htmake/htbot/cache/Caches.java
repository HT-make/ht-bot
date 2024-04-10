package com.htmake.htbot.cache;

import com.htmake.htbot.discord.commands.dungeon.cache.DungeonStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.MonsterStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.PlayerStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.SituationCache;
import org.springframework.stereotype.Component;

@Component
public class Caches {

    public static PlayerStatusCache playerStatusCache;
    public static MonsterStatusCache monsterStatusCache;
    public static SituationCache situationCache;
    public static DungeonStatusCache dungeonStatusCache;

    public Caches() {
        playerStatusCache = new PlayerStatusCache();
        monsterStatusCache = new MonsterStatusCache();
        situationCache = new SituationCache();
        dungeonStatusCache = new DungeonStatusCache();
    }
}
