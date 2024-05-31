package com.htmake.htbot.global.cache;

import com.htmake.htbot.discord.commands.dungeon.cache.DungeonTypeCache;
import com.htmake.htbot.discord.commands.dungeon.cache.FieldDungeonStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.MonsterDataCache;
import com.htmake.htbot.discord.commands.battle.cache.PlayerDataCache;
import com.htmake.htbot.discord.commands.battle.cache.SituationCache;
import com.htmake.htbot.discord.commands.global.cache.MessageCache;
import com.htmake.htbot.discord.commands.inventory.cache.InventoryCache;
import org.springframework.stereotype.Component;

@Component
public class CacheFactory {

    //battle
    public static PlayerDataCache playerDataCache;
    public static MonsterDataCache monsterDataCache;
    public static SituationCache situationCache;

    //dungeon
    public static FieldDungeonStatusCache fieldDungeonStatusCache;
    public static DungeonTypeCache dungeonTypeCache;

    //inventory
    public static InventoryCache inventoryCache;

    //global
    public static MessageCache messageCache;

    public CacheFactory() {
        playerDataCache = new PlayerDataCache();
        monsterDataCache = new MonsterDataCache();
        situationCache = new SituationCache();

        fieldDungeonStatusCache = new FieldDungeonStatusCache();
        dungeonTypeCache = new DungeonTypeCache();

        inventoryCache = new InventoryCache();

        messageCache = new MessageCache();
    }
}
