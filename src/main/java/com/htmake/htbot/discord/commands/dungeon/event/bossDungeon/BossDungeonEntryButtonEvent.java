package com.htmake.htbot.discord.commands.dungeon.event.bossDungeon;

import com.htmake.htbot.discord.commands.dungeon.data.DungeonMonster;
import com.htmake.htbot.discord.commands.dungeon.data.DungeonPlayer;
import com.htmake.htbot.discord.commands.dungeon.enums.DungeonType;
import com.htmake.htbot.discord.commands.dungeon.util.DungeonUtil;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BossDungeonEntryButtonEvent {

    private final HttpClient httpClient;
    private final DungeonUtil dungeonUtil;
    private final ErrorUtil errorUtil;

    public BossDungeonEntryButtonEvent() {
        this.httpClient = new HttpClientImpl();
        this.dungeonUtil = new DungeonUtil();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(ButtonInteractionEvent event, String dungeonId) {
        String playerId = event.getUser().getId();

        HttpResponse<JsonNode> playerResponse = playerRequest(playerId);
        HttpResponse<JsonNode> dungeonResponse = dungeonRequest(dungeonId, playerId);

        if (playerResponse.getStatus() == 200 && dungeonResponse.getStatus() == 200) {
            JSONObject playerObject = playerResponse.getBody().getObject();
            JSONObject dungeonObject = dungeonResponse.getBody().getObject();

            requestSuccess(event, playerObject, dungeonObject);
        } else {
            errorUtil.sendError(event.getHook(), "던전 입장", "던전에 입장할 수 없습니다.");
        }
    }

    private HttpResponse<JsonNode> playerRequest(String playerId) {
        String endPoint = "/player/battle/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        return httpClient.sendGetRequest(endPoint, routeParam);
    }

    private HttpResponse<JsonNode> dungeonRequest(String dungeonId, String playerId) {
        String endPoint = "/dungeon/{dungeon_id}/{player_id}";
        Pair<String, String> firstRouteParam = new Pair<>("dungeon_id", dungeonId);
        Pair<String, String> secondRouteParam = new Pair<>("player_id", playerId);
        return httpClient.sendPatchRequest(endPoint, firstRouteParam, secondRouteParam);
    }

    private void requestSuccess(ButtonInteractionEvent event, JSONObject playerObject, JSONObject dungeonObject) {
        JSONArray dungeonMonsterArray = dungeonObject.getJSONArray("monsterList");
        List<DungeonMonster> dungeonMonsterList = toDungeonMonsterList(dungeonMonsterArray);

        User user = event.getUser();

        String dungeonName = dungeonObject.getString("name");
        DungeonMonster dungeonMonster = getRandomDungeonMonster(dungeonMonsterList);
        DungeonPlayer dungeonPlayer = dungeonUtil.toDungeonPlayer(user.getName(), playerObject);

        String playerId = user.getId();

        dungeonUtil.savePlayerStatus(playerId, dungeonPlayer);
        dungeonUtil.saveMonsterStatus(playerId, dungeonMonster);
        dungeonUtil.saveSituation(playerId, dungeonMonster.getName());
        dungeonUtil.saveDungeonType(playerId, DungeonType.BOSS);

        MessageEmbed embed = dungeonUtil.buildEmbed(dungeonName, dungeonMonster, dungeonPlayer, event.getUser());

        event.getHook().editOriginalEmbeds(embed)
                .setActionRow(
                        Button.success("battle-attack", "공격"),
                        Button.primary("battle-skill-open", "스킬"),
                        Button.primary("battle-potion-open", "포션"),
                        Button.danger("battle-retreat", "후퇴")
                )
                .queue();
    }

    private List<DungeonMonster> toDungeonMonsterList(JSONArray dungeonMonsterArray) {
        List<DungeonMonster> dungeonMonsterList = new ArrayList<>();

        for (int i = 0; i < dungeonMonsterArray.length(); i++) {
            JSONObject dungeonMonsterObject = dungeonMonsterArray.getJSONObject(i);

            String skillName = null;
            int skillDamage = 0;

            if (dungeonMonsterObject.getString("skillName") != null) {
                skillName = dungeonMonsterObject.getString("skillName");
                skillDamage = dungeonMonsterObject.getInt("skillDamage");
            }

            DungeonMonster dungeonMonster = DungeonMonster.builder()
                    .id(dungeonMonsterObject.getString("id"))
                    .name(dungeonMonsterObject.getString("name"))
                    .level(dungeonMonsterObject.getInt("level"))
                    .damage(dungeonMonsterObject.getInt("damage"))
                    .health(dungeonMonsterObject.getInt("health"))
                    .defence(dungeonMonsterObject.getInt("defence"))
                    .skillName(skillName)
                    .skillDamage(skillDamage)
                    .build();

            dungeonMonsterList.add(dungeonMonster);
        }

        return dungeonMonsterList;
    }

    private DungeonMonster getRandomDungeonMonster(List<DungeonMonster> dungeonMonsterList) {
        RandomGenerator random = new MersenneTwister();

        int size = dungeonMonsterList.size();
        int index = random.nextInt(size);

        return dungeonMonsterList.get(index);
    }
}
