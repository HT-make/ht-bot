package com.htmake.htbot.discord.commands.battle.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.cache.Caches;
import com.htmake.htbot.discord.commands.dungeon.cache.DungeonStatusCache;
import com.htmake.htbot.discord.commands.dungeon.data.GetItem;
import com.htmake.htbot.discord.commands.dungeon.data.DungeonStatus;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class GetAwardEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;
    private final BattleResultEvent battleResultEvent;

    private final DungeonStatusCache dungeonStatusCache;

    public GetAwardEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
        this.battleResultEvent = new BattleResultEvent();

        this.dungeonStatusCache = Caches.dungeonStatusCache;
    }

    public void execute(ButtonInteractionEvent event, String monsterId) {
        JSONObject monsterLoot = getMonsterLoot(monsterId);

        if (monsterLoot == null) {
            errorUtil.sendError(event.getMessage(), "전투", "보상을 획득하지 못했습니다.");
        }

        List<GetItem> getItemList = updateGetItem(event.getUser().getId(), monsterLoot);

        JSONObject levelUp = sendRequest(event, monsterLoot);

        battleResultEvent.execute(event, levelUp, monsterLoot, getItemList);
    }

    private JSONObject getMonsterLoot(String monsterId) {
        String endPoint = "/monster/loot/{monster_id}";
        Pair<String, String> routeParam = new Pair<>("monster_id", monsterId);

        HttpResponse<JsonNode> response = httpClient.sendGetRequest(endPoint, routeParam);

        if (response.getStatus() == 200) {
            return response.getBody().getObject();
        }

        return null;
    }

    private List<GetItem> updateGetItem(String playerId, JSONObject monsterLoot) {
        JSONArray dropItems = monsterLoot.getJSONArray("dropItemList");

        List<GetItem> getCurrentItems = dungeonStatusCache.get(playerId).getGetItemList();
        List<GetItem> getNewItems = randomDropItems(dropItems);
        getCurrentItems.addAll(getNewItems);

        DungeonStatus dungeonStatus = dungeonStatusCache.get(playerId);
        dungeonStatus.setGetItemList(getCurrentItems);

        dungeonStatusCache.put(playerId, dungeonStatus);

        return getNewItems;
    }

    private List<GetItem> randomDropItems(JSONArray dropItems) {
        List<GetItem> getItems = new ArrayList<>();

        for (int i = 0; i < dropItems.length(); i++) {
            JSONObject dropItemObject = dropItems.getJSONObject(i);

            Random random = new Random();

            int randomNum = random.nextInt(100);
            int dropChance = dropItemObject.getInt("chance");

            if (randomNum < dropChance) {
                GetItem getItem = GetItem.builder()
                        .id(dropItemObject.getString("id"))
                        .name(dropItemObject.getString("name"))
                        .build();

                getItems.add(getItem);
            }
        }

        return getItems;
    }

    private JSONObject sendRequest(ButtonInteractionEvent event, JSONObject monsterLoot) {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> requestData = new HashMap<>();

        requestData.put("exp", monsterLoot.getInt("exp"));
        requestData.put("gold", monsterLoot.getInt("gold"));

        String endPoint = "/player/battle/win/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", event.getUser().getId());
        String jsonBody;

        try {
            jsonBody = objectMapper.writeValueAsString(requestData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpResponse<JsonNode> response = httpClient.sendPatchRequest(endPoint, routeParam, jsonBody);

        if (response.getStatus() == 200) {
            return response.getBody().getObject();
        } else {
            errorUtil.sendError(event.getMessage(), "전투", "보상 획득에 실패하였습니다.");
        }

        return null;
    }
}
