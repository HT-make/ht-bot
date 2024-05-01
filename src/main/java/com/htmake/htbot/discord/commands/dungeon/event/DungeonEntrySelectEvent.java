package com.htmake.htbot.discord.commands.dungeon.event;

import com.htmake.htbot.discord.commands.battle.data.PlayerSkillStatus;
import com.htmake.htbot.discord.commands.dungeon.data.DungeonMonster;
import com.htmake.htbot.discord.commands.dungeon.data.DungeonPlayer;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.cache.CacheFactory;
import com.htmake.htbot.discord.commands.dungeon.cache.DungeonStatusCache;
import com.htmake.htbot.discord.commands.dungeon.data.DungeonStatus;
import com.htmake.htbot.discord.commands.dungeon.util.DungeonUtil;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class DungeonEntrySelectEvent {

    private final HttpClient httpClient;
    private final DungeonUtil dungeonUtil;
    private final ErrorUtil errorUtil;

    private final DungeonStatusCache dungeonStatusCache;

    public DungeonEntrySelectEvent() {
        this.httpClient = new HttpClientImpl();
        this.dungeonUtil = new DungeonUtil();
        this.errorUtil = new ErrorUtil();

        this.dungeonStatusCache = CacheFactory.dungeonStatusCache;
    }

    public void execute(StringSelectInteractionEvent event, String dungeonId) {
        HttpResponse<JsonNode> playerResponse = playerRequest(event.getUser().getId());
        HttpResponse<JsonNode> dungeonResponse = dungeonRequest(dungeonId);

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

    private HttpResponse<JsonNode> dungeonRequest(String dungeonId) {
        String endPoint = "/dungeon/{dungeon_id}";
        Pair<String, String> routeParam = new Pair<>("dungeon_id", dungeonId);
        return httpClient.sendGetRequest(endPoint, routeParam);
    }

    private void requestSuccess(StringSelectInteractionEvent event, JSONObject playerObject, JSONObject dungeonObject) {
        JSONArray dungeonMonsterArray = dungeonObject.getJSONArray("monsterList");
        List<DungeonMonster> dungeonMonsterList = toDungeonMonsterList(dungeonMonsterArray);

        String dungeonName = dungeonObject.getString("name");
        DungeonMonster dungeonMonster = dungeonUtil.randomMonster(dungeonMonsterList, 1);
        DungeonPlayer dungeonPlayer = toDungeonPlayer(playerObject);

        String playerId = event.getUser().getId();

        saveDungeonStatus(playerId, dungeonName, dungeonMonsterList, dungeonPlayer);

        dungeonUtil.savePlayerStatus(playerId, dungeonPlayer);
        dungeonUtil.saveMonsterStatus(playerId, dungeonMonster);
        dungeonUtil.saveSituation(playerId, dungeonMonster.getName());

        String dungeonTitle = dungeonName + "-" + 1;

        MessageEmbed embed = dungeonUtil.buildEmbed(dungeonTitle, dungeonMonster, dungeonPlayer, event.getUser());

        event.getHook().editOriginalEmbeds(embed)
                .setActionRow(
                        Button.success("battle-attack", "공격"),
                        Button.primary("battle-skill-open", "스킬"),
                        Button.primary("battle-potion-open", "포션"),
                        Button.danger("battle-retreat", "후퇴")
                )
                .queue();
    }

    private DungeonPlayer toDungeonPlayer(JSONObject playerObject) {
        JSONArray playerSkillArray = playerObject.getJSONArray("skillList");
        Map<Integer, PlayerSkillStatus> playerSkillMap = toPlayerSkillMap(playerSkillArray);

        return DungeonPlayer.builder()
                .level(playerObject.getInt("level"))
                .damage(playerObject.getInt("damage"))
                .health(playerObject.getInt("health"))
                .defence(playerObject.getInt("defence"))
                .mana(playerObject.getInt("mana"))
                .criticalChance(playerObject.getInt("criticalChance"))
                .criticalDamage(playerObject.getInt("criticalDamage"))
                .playerSkill(playerSkillMap)
                .build();
    }

    private Map<Integer, PlayerSkillStatus> toPlayerSkillMap(JSONArray playerSkillArray) {
        Map<Integer, PlayerSkillStatus> playerSkillMap = new HashMap<>();

        for (int i = 0;i < playerSkillArray.length(); i++) {
            JSONObject playerSkillObject = playerSkillArray.getJSONObject(i);

            Integer number = playerSkillObject.getInt("number");
            PlayerSkillStatus playerSkillStatus = PlayerSkillStatus.builder()
                    .name(playerSkillObject.getString("name"))
                    .value(playerSkillObject.getInt("value"))
                    .mana(playerSkillObject.getInt("mana"))
                    .skillType(playerSkillObject.getString("skillType"))
                    .build();

            playerSkillMap.put(number, playerSkillStatus);
        }

        return playerSkillMap;
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

        dungeonMonsterList.sort(Comparator.comparingInt(DungeonMonster::getLevel));

        return dungeonMonsterList;
    }

    private void saveDungeonStatus(
            String playerId,
            String name,
            List<DungeonMonster> dungeonMonsterList,
            DungeonPlayer dungeonPlayer
    ) {
        DungeonStatus dungeonStatus = DungeonStatus.builder()
                .name(name)
                .stage(1)
                .dungeonMonsterList(dungeonMonsterList)
                .dungeonPlayer(dungeonPlayer)
                .getItemList(new ArrayList<>())
                .build();

        dungeonStatusCache.put(playerId, dungeonStatus);
    }
}
