package com.htmake.htbot.discord.commands.battle.util;

import com.htmake.htbot.discord.commands.dungeon.data.GetItem;
import com.htmake.htbot.discord.util.ObjectMapperUtil;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BattleActionUtil {

    private final HttpClient httpClient;
    private final ObjectMapperUtil objectMapperUtil;

    public BattleActionUtil() {
        this.httpClient = new HttpClientImpl();
        this.objectMapperUtil = new ObjectMapperUtil();
    }

    public JSONObject getMonsterLoot(String monsterId) {
        String endPoint = "/monster/loot/{monster_id}";
        Pair<String, String> routeParam = new Pair<>("monster_id", monsterId);

        HttpResponse<JsonNode> response = httpClient.sendGetRequest(endPoint, routeParam);

        if (response.getStatus() == 200) {
            return response.getBody().getObject();
        }

        return null;
    }

    public HttpResponse<JsonNode> request(String playerId, JSONObject monsterLoot) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("exp", monsterLoot.getInt("exp"));
        requestData.put("gold", monsterLoot.getInt("gold"));

        String endPoint = "/player/battle/win/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        String jsonBody = objectMapperUtil.mapper(requestData);

        return httpClient.sendPatchRequest(endPoint, routeParam, jsonBody);
    }

    public List<GetItem> randomDropItemList(JSONArray dropItemArray) {
        List<GetItem> getItemList = new ArrayList<>();

        for (int i = 0; i < dropItemArray.length(); i++) {
            JSONObject dropItemObject = dropItemArray.getJSONObject(i);

            Random random = new Random();

            int randomNum = random.nextInt(100);
            int dropChance = dropItemObject.getInt("chance");

            if (randomNum < dropChance) {
                GetItem getItem = GetItem.builder()
                        .id(dropItemObject.getString("id"))
                        .name(dropItemObject.getString("name"))
                        .build();

                getItemList.add(getItem);
            }
        }

        return getItemList;
    }

    public MessageEmbed buildEmbed(JSONObject monsterLoot, List<GetItem> getItemList, boolean levelUp, User user) {
        String levelUpMessage = (levelUp ? ":up: 레벨업!!" : "");

        StringBuilder getItemMessage = new StringBuilder();

        if (getItemList.size() > 0) {
            for (GetItem getItem : getItemList) {
                getItemMessage.append(getItem.getName()).append("\n");
            }
        } else {
            getItemMessage.append("획득한 아이템이 없습니다.");
        }

        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle(":crossed_swords: 전투 승리!")
                .setDescription(levelUpMessage)
                .addField(":sparkles: 획득 경험치", "" + monsterLoot.getInt("exp"), true)
                .addField(":coin: 획득 골드", "" + monsterLoot.get("gold"), true)
                .addField(":purse: 획득 아이템", getItemMessage.toString(), false)
                .build();
    }
}
