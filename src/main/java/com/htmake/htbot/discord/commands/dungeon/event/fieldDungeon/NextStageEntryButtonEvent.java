package com.htmake.htbot.discord.commands.dungeon.event.fieldDungeon;

import com.htmake.htbot.discord.commands.dungeon.cache.FieldDungeonStatusCache;
import com.htmake.htbot.discord.commands.dungeon.data.DungeonMonster;
import com.htmake.htbot.discord.commands.dungeon.data.DungeonPlayer;
import com.htmake.htbot.discord.commands.dungeon.data.FieldDungeonStatus;
import com.htmake.htbot.discord.commands.dungeon.util.DungeonUtil;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.Map;

public class NextStageEntryButtonEvent {

    private final ErrorUtil errorUtil;
    private final DungeonUtil dungeonUtil;

    private final FieldDungeonStatusCache fieldDungeonStatusCache;

    public NextStageEntryButtonEvent() {
        this.errorUtil = new ErrorUtil();
        this.dungeonUtil = new DungeonUtil();

        this.fieldDungeonStatusCache = CacheFactory.fieldDungeonStatusCache;
    }

    public void execute(ButtonInteractionEvent event) {
        String playerId = event.getUser().getId();

        if (!fieldDungeonStatusCache.containsKey(playerId)) {
            errorUtil.sendError(event.getHook(), "던전 입장", "던전 입장에 실패했습니다.");
            return;
        }

        FieldDungeonStatus fieldDungeonStatus = fieldDungeonStatusCache.get(playerId);

        int stage = fieldDungeonStatus.getStage() + 1;
        stageUp(playerId, fieldDungeonStatus, stage);

        Map<Integer, DungeonMonster> dungeonMonsterList = fieldDungeonStatus.getMonsterByStage();

        String dungeonName = fieldDungeonStatus.getName();
        DungeonMonster dungeonMonster = dungeonMonsterList.get(stage);
        DungeonPlayer dungeonPlayer = fieldDungeonStatus.getDungeonPlayer();

        dungeonUtil.savePlayerStatus(playerId, dungeonPlayer);
        dungeonUtil.saveMonsterStatus(playerId, dungeonMonster);
        dungeonUtil.saveSituation(playerId, dungeonMonster.getName());

        String dungeonTitle = dungeonName + "-" + stage;

        MessageEmbed embed = dungeonUtil.buildEmbed(dungeonTitle, dungeonMonster, dungeonPlayer, event.getUser());

        event.getHook().editOriginalEmbeds(embed)
                .setActionRow(
                        Button.success("battle-attack", "공격"),
                        Button.primary("battle-skill-open", "스킬"),
                        Button.danger("battle-retreat", "후퇴")
                )
                .queue();
    }

    private void stageUp(String playerId, FieldDungeonStatus fieldDungeonStatus, int stage) {
        fieldDungeonStatus.setStage(stage);
        fieldDungeonStatusCache.put(playerId, fieldDungeonStatus);
    }
}
