package com.htmake.htbot.discord.commands.dungeon.event;

import com.htmake.htbot.discord.commands.dungeon.cache.DungeonStatusCache;
import com.htmake.htbot.discord.commands.dungeon.data.DungeonMonster;
import com.htmake.htbot.discord.commands.dungeon.data.DungeonPlayer;
import com.htmake.htbot.discord.commands.dungeon.data.DungeonStatus;
import com.htmake.htbot.discord.commands.dungeon.util.DungeonUtil;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

public class NextDungeonEntryButtonEvent {

    private final ErrorUtil errorUtil;
    private final DungeonUtil dungeonUtil;

    private final DungeonStatusCache dungeonStatusCache;

    public NextDungeonEntryButtonEvent() {
        this.errorUtil = new ErrorUtil();
        this.dungeonUtil = new DungeonUtil();

        this.dungeonStatusCache = CacheFactory.dungeonStatusCache;
    }

    public void execute(ButtonInteractionEvent event) {
        String playerId = event.getUser().getId();

        if (!dungeonStatusCache.containsKey(playerId)) {
            errorUtil.sendError(event.getMessage(), "던전 입장", "던전 입장에 실패했습니다.");
        }

        DungeonStatus dungeonStatus = dungeonStatusCache.get(playerId);

        int stage = dungeonStatus.getStage() + 1;
        stageUp(playerId, dungeonStatus, stage);

        List<DungeonMonster> dungeonMonsterList = dungeonStatus.getDungeonMonsterList();

        String dungeonName = dungeonStatus.getName();
        DungeonMonster dungeonMonster = dungeonUtil.randomMonster(dungeonMonsterList, stage);
        DungeonPlayer dungeonPlayer = dungeonStatus.getDungeonPlayer();

        dungeonUtil.savePlayerStatus(playerId, dungeonPlayer);
        dungeonUtil.saveMonsterStatus(playerId, dungeonMonster);
        dungeonUtil.saveSituation(playerId, dungeonMonster.getName());

        String dungeonTitle = dungeonName + "-" + stage;

        MessageEmbed embed = dungeonUtil.buildEmbed(dungeonTitle, dungeonMonster, dungeonPlayer, event.getUser().getName());

        event.getMessage().editMessageEmbeds(embed)
                .setActionRow(
                        Button.success("battle-attack", "공격"),
                        Button.primary("battle-skill-open", "스킬"),
                        Button.primary("battle-potion-open", "포션"),
                        Button.danger("battle-retreat", "후퇴")
                )
                .queue();
    }

    private void stageUp(String playerId, DungeonStatus dungeonStatus, int stage) {
        dungeonStatus.setStage(stage);
        dungeonStatusCache.put(playerId, dungeonStatus);
    }
}
