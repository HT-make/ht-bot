package com.htmake.htbot.discord.commands.dungeon.event.fieldDungeon;

import com.htmake.htbot.discord.commands.dungeon.data.DungeonMonster;
import com.htmake.htbot.discord.commands.dungeon.data.DungeonPlayer;
import com.htmake.htbot.discord.commands.dungeon.enums.DungeonType;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.cache.CacheFactory;
import com.htmake.htbot.discord.commands.dungeon.cache.FieldDungeonStatusCache;
import com.htmake.htbot.discord.commands.dungeon.data.FieldDungeonStatus;
import com.htmake.htbot.discord.commands.dungeon.util.DungeonUtil;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class FieldDungeonEntrySelectEvent {

    private final HttpClient httpClient;
    private final DungeonUtil dungeonUtil;
    private final ErrorUtil errorUtil;

    private final FieldDungeonStatusCache fieldDungeonStatusCache;

    public FieldDungeonEntrySelectEvent() {
        this.httpClient = new HttpClientImpl();
        this.dungeonUtil = new DungeonUtil();
        this.errorUtil = new ErrorUtil();

        this.fieldDungeonStatusCache = CacheFactory.fieldDungeonStatusCache;
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
        Map<Integer, List<DungeonMonster>> monsterListByLevel = toMonsterListByLevel(dungeonMonsterArray);
        Map<Integer, DungeonMonster> monsterByStage = toMonsterByStage(monsterListByLevel);

        String dungeonName = dungeonObject.getString("name");
        DungeonMonster dungeonMonster = monsterByStage.get(1);
        DungeonPlayer dungeonPlayer = dungeonUtil.toDungeonPlayer(playerObject);

        String playerId = event.getUser().getId();

        saveFieldDungeonStatus(playerId, dungeonName, monsterByStage, dungeonPlayer);

        dungeonUtil.savePlayerStatus(playerId, dungeonPlayer);
        dungeonUtil.saveMonsterStatus(playerId, dungeonMonster);
        dungeonUtil.saveSituation(playerId, dungeonMonster.getName());
        dungeonUtil.saveDungeonType(playerId, DungeonType.FIELD);

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

    private Map<Integer, List<DungeonMonster>> toMonsterListByLevel(JSONArray dungeonMonsterArray) {
        Map<Integer, List<DungeonMonster>> monsterListByLevel = new HashMap<>();

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

            int level = dungeonMonster.getLevel() % 10;

            List<DungeonMonster> dungeonMonsterList;

            if (!monsterListByLevel.containsKey(level)) {
                dungeonMonsterList = new ArrayList<>();
            } else {
                dungeonMonsterList = monsterListByLevel.get(level);
            }

            dungeonMonsterList.add(dungeonMonster);
            monsterListByLevel.put(level, dungeonMonsterList);
        }

        return monsterListByLevel;
    }

    private Map<Integer, DungeonMonster> toMonsterByStage(Map<Integer, List<DungeonMonster>> monsterListByLevel) {
        Map<Integer, DungeonMonster> monsterByStage = new HashMap<>();

        for (int stage = 1; stage <= 10; stage++) {
            RandomGenerator random = new MersenneTwister();

            List<Integer> levelList = getLevelList(stage);

            int randomLevel = random.nextInt(3);
            int level = levelList.get(randomLevel);

            List<DungeonMonster> dungeonMonsterList = monsterListByLevel.get(level);

            int size = dungeonMonsterList.size();
            int randomMonsterNumber = random.nextInt(size);
            DungeonMonster dungeonMonster = dungeonMonsterList.get(randomMonsterNumber);

            monsterByStage.put(stage, dungeonMonster);
        }

        return monsterByStage;
    }

    private List<Integer> getLevelList(int stage) {
        List<Integer> levelList = new ArrayList<>();

        if (stage == 1) {
            levelList.addAll(Arrays.asList(1, 1, 2));
        } else if (stage == 10) {
            levelList.addAll(Arrays.asList(9, 0, 0));
        } else {
            levelList.addAll(Arrays.asList(stage - 1, stage, (stage + 1) % 10));
        }

        return levelList;
    }

    private void saveFieldDungeonStatus(
            String playerId,
            String name,
            Map<Integer, DungeonMonster> monsterByStage,
            DungeonPlayer dungeonPlayer
    ) {
        FieldDungeonStatus fieldDungeonStatus = FieldDungeonStatus.builder()
                .name(name)
                .stage(1)
                .monsterByStage(monsterByStage)
                .dungeonPlayer(dungeonPlayer)
                .getItemList(new ArrayList<>())
                .build();

        fieldDungeonStatusCache.put(playerId, fieldDungeonStatus);
    }
}
