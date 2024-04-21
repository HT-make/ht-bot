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
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

@Slf4j
public class DungeonEntryEvent {

    private final DungeonUtil dungeonUtil;
    private final ErrorUtil errorUtil;

    private final DungeonStatusCache dungeonStatusCache;

    public DungeonEntryEvent() {
        this.dungeonUtil = new DungeonUtil();
        this.errorUtil = new ErrorUtil();

        this.dungeonStatusCache = Caches.dungeonStatusCache;
    }

    public void execute(StringSelectInteractionEvent event, String dungeonId) {
        HttpResponse<JsonNode> response = dungeonUtil.dungeonResponse(dungeonId);

        if (response.getStatus() == 200) {
            handleSuccessfulResponse(event, response, dungeonId);
        } else {
            errorUtil.sendError(event.getMessage(), "던전 입장", "던전에 입장할 수 없습니다.");
        }
    }

    private void handleSuccessfulResponse(
            StringSelectInteractionEvent event,
            HttpResponse<JsonNode> response,
            String dungeonId
    ) {
        JSONObject dungeonObject = response.getBody().getObject();

        String dungeonName = dungeonObject.getString("name") + "-1";
        JSONArray monsterArray = dungeonObject.getJSONArray("monsterList");

        ArrayList<Pair<Monster, MonsterSkill>> monsterList = dungeonUtil.toMonsterList(monsterArray);

        String playerId = event.getUser().getId();
        JSONObject playerObject = dungeonUtil.playerDataResponse(playerId);
        dungeonUtil.savePlayerStatus(playerId, playerObject);

        if (playerObject == null) {
            errorUtil.sendError(event.getMessage(), "던전 입장", "플레이어를 찾을 수 없습니다.");
        }

        Pair<Monster, MonsterSkill> monster = dungeonUtil.randomMonster(monsterList, 1);
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

        saveDungeonStatus(playerId, dungeonId);
    }

    private void saveDungeonStatus(String playerId, String dungeonId) {
        DungeonStatus dungeonStatus = DungeonStatus.builder()
                .id(dungeonId)
                .stage(1)
                .getItemList(new ArrayList<>())
                .build();

        dungeonStatusCache.put(playerId, dungeonStatus);
    }
}
