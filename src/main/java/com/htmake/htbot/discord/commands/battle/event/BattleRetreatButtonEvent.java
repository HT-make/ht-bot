package com.htmake.htbot.discord.commands.battle.event;

import com.htmake.htbot.discord.commands.battle.util.BattleUtil;
import com.htmake.htbot.discord.commands.dungeon.cache.DungeonStatusCache;
import com.htmake.htbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Collections;

public class BattleRetreatButtonEvent {

    private final BattleUtil battleUtil;

    private final DungeonStatusCache dungeonStatusCache;

    public BattleRetreatButtonEvent() {
        this.battleUtil = new BattleUtil();

        this.dungeonStatusCache = CacheFactory.dungeonStatusCache;
    }

    public void execute(ButtonInteractionEvent event) {
        String playerId = event.getUser().getId();

        battleUtil.removeCurrentBattleCache(playerId);
        dungeonStatusCache.remove(playerId);

        MessageEmbed embed = battleUtil.battleDefeat(playerId);

        event.getHook().editOriginalComponents(Collections.emptyList()).queue();
        event.getHook().editOriginalEmbeds(embed).queue();
    }
}
