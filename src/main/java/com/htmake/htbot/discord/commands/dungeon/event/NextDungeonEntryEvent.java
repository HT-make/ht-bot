package com.htmake.htbot.discord.commands.dungeon.event;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.cache.Caches;
import com.htmake.htbot.discord.commands.dungeon.cache.DungeonStatusCache;
import com.htmake.htbot.discord.commands.dungeon.data.DungeonStatus;
import com.htmake.htbot.discord.commands.dungeon.util.DungeonUtil;
import com.htmake.htbot.domain.monster.entity.Monster;
import com.htmake.htbot.domain.monster.entity.MonsterSkill;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@Slf4j
public class NextDungeonEntryEvent {

    private final DungeonUtil dungeonUtil;
    private final ErrorUtil errorUtil;

    private final DungeonStatusCache dungeonStatusCache;

    public NextDungeonEntryEvent() {
        this.dungeonUtil = new DungeonUtil();
        this.errorUtil = new ErrorUtil();

        this.dungeonStatusCache = Caches.dungeonStatusCache;
    }

    public void execute(ButtonInteractionEvent event) {
        String playerId = event.getUser().getId();

        DungeonStatus dungeonStatus = dungeonStatusCache.get(playerId);

        String dungeonId = dungeonStatus.getId();
        int stage = dungeonStatus.getStage() + 1;

        HttpResponse<JsonNode> response = dungeonUtil.dungeonResponse(dungeonId);

        if (response.getStatus() == 200) {
            handleSuccessfulResponse(event, response, stage);
        } else {
            errorUtil.sendError(event.getMessage(), "던전 입장", "던전에 입장할 수 없습니다.");
        }
    }

    private void handleSuccessfulResponse(
            ButtonInteractionEvent event,
            HttpResponse<JsonNode> response,
            int stage
    ) {
        JSONObject dungeonObject = response.getBody().getObject();

        String dungeonName = dungeonObject.getString("name") + "-" + stage;
        JSONArray monsterArray = dungeonObject.getJSONArray("monsterList");

        ArrayList<Pair<Monster, MonsterSkill>> monsterList = dungeonUtil.toMonsterList(monsterArray);

        String playerId = event.getUser().getId();
        JSONObject playerObject = dungeonUtil.playerDataResponse(playerId);
        dungeonUtil.savePlayerStatus(playerId, playerObject);

        if (playerObject == null) {
            errorUtil.sendError(event.getMessage(), "던전 입장", "플레이어를 찾을 수 없습니다.");
        }

        Pair<Monster, MonsterSkill> monster = dungeonUtil.randomMonster(monsterList, stage);
        dungeonUtil.saveMonsterStatus(playerId, monster);

        MessageEmbed embed = dungeonUtil.buildEmbed(dungeonName, monster.getFirst(), playerObject);

        MessageEmbed.Field field = embed.getFields().get(3);
        dungeonUtil.saveSituation(playerId, field);

        event.getMessage().editMessageEmbeds(embed)
                .setActionRow(
                        Button.success("battle-attack", "공격"),
                        Button.primary("battle-potion-open", "포션"),
                        Button.danger("battle-retreat", "후퇴")
                )
                .queue();

        updateStage(playerId, stage);
    }

    private void updateStage(String playerId, int stage) {
        DungeonStatus dungeonStatus = dungeonStatusCache.get(playerId);
        dungeonStatus.setStage(stage);
        dungeonStatusCache.put(playerId, dungeonStatus);
    }
}
