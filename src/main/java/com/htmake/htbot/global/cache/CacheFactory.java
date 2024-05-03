package com.htmake.htbot.global.cache;

import com.htmake.htbot.discord.commands.dungeon.cache.FieldDungeonStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.MonsterStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.PlayerStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.SituationCache;
import com.htmake.htbot.discord.commands.global.cache.MessageCache;
import com.htmake.htbot.discord.commands.inventory.cache.InventoryCache;
import org.springframework.stereotype.Component;

@Component
public class CacheFactory {

    public static PlayerStatusCache playerStatusCache;
    public static MonsterStatusCache monsterStatusCache;
    public static SituationCache situationCache;
    public static FieldDungeonStatusCache fieldDungeonStatusCache;
    public static InventoryCache inventoryCache;
    public static MessageCache messageCache;

    public CacheFactory() {
        playerStatusCache = new PlayerStatusCache();
        monsterStatusCache = new MonsterStatusCache();
        situationCache = new SituationCache();
        fieldDungeonStatusCache = new FieldDungeonStatusCache();
        inventoryCache = new InventoryCache();
        messageCache = new MessageCache();
    }
}
