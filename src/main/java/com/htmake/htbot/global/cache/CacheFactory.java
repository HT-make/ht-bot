package com.htmake.htbot.global.cache;

import com.htmake.htbot.discord.commands.dungeon.cache.DungeonTypeCache;
import com.htmake.htbot.discord.commands.dungeon.cache.FieldDungeonStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.MonsterStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.PlayerStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.SituationCache;
import com.htmake.htbot.discord.commands.global.cache.MessageCache;
import com.htmake.htbot.discord.commands.inventory.cache.InventoryCache;
import com.htmake.htbot.discord.commands.shop.cache.BossShopCache;
import org.springframework.stereotype.Component;

@Component
public class CacheFactory {

    //battle
    public static PlayerStatusCache playerStatusCache;
    public static MonsterStatusCache monsterStatusCache;
    public static SituationCache situationCache;

    //dungeon
    public static FieldDungeonStatusCache fieldDungeonStatusCache;
    public static DungeonTypeCache dungeonTypeCache;

    //inventory
    public static InventoryCache inventoryCache;

    //shop
    public static BossShopCache bossShopCache;

    //global
    public static MessageCache messageCache;

    public CacheFactory() {
        playerStatusCache = new PlayerStatusCache();
        monsterStatusCache = new MonsterStatusCache();
        situationCache = new SituationCache();

        fieldDungeonStatusCache = new FieldDungeonStatusCache();
        dungeonTypeCache = new DungeonTypeCache();

        inventoryCache = new InventoryCache();

        bossShopCache = new BossShopCache();

        messageCache = new MessageCache();
    }
}
