package com.htmake.htbot.discord.commands.dungeon.event.fieldDungeon;

import com.htmake.htbot.discord.commands.battle.util.BattleUtil;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.cache.CacheFactory;
import com.htmake.htbot.discord.commands.dungeon.cache.FieldDungeonStatusCache;
import com.htmake.htbot.discord.commands.dungeon.data.FieldDungeonStatus;
import com.htmake.htbot.discord.commands.dungeon.data.GetItem;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class FieldDungeonCloseButtonEvent {

    private final ErrorUtil errorUtil;
    private final BattleUtil battleUtil;

    private final FieldDungeonStatusCache fieldDungeonStatusCache;

    public FieldDungeonCloseButtonEvent() {
        this.errorUtil = new ErrorUtil();
        this.battleUtil = new BattleUtil();

        this.fieldDungeonStatusCache = CacheFactory.fieldDungeonStatusCache;
    }

    public void execute(ButtonInteractionEvent event) {
        String playerId = event.getUser().getId();

        FieldDungeonStatus fieldDungeonStatus = fieldDungeonStatusCache.get(playerId);
        List<GetItem> getItemList = fieldDungeonStatus.getGetItemList();

        if (getItemList == null) {
            requestSuccess(event);
            fieldDungeonStatusCache.remove(playerId);
            return;
        }

        HttpResponse<JsonNode> response = battleUtil.insertGetItemList(getItemList, playerId);

        if (response.getStatus() == 200) {
            requestSuccess(event);
            fieldDungeonStatusCache.remove(playerId);
        } else {
            errorUtil.sendError(event.getHook(), "던전 퇴장", "던전 퇴장에 실패하였습니다.");
        }
    }

    private void requestSuccess(ButtonInteractionEvent event) {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("던전 퇴장")
                .setDescription("던전에서 퇴장하였습니다.")
                .build();

        event.getHook().editOriginalComponents(Collections.emptyList()).queue();
        event.getHook().editOriginalEmbeds(embed).queue();
    }
}
