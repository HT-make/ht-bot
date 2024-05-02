package com.htmake.htbot.discord.commands.battle.action;

import com.htmake.htbot.discord.commands.dungeon.data.DungeonPlayer;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.ObjectMapperUtil;
import com.htmake.htbot.global.cache.CacheFactory;
import com.htmake.htbot.discord.commands.dungeon.cache.FieldDungeonStatusCache;
import com.htmake.htbot.discord.commands.dungeon.data.GetItem;
import com.htmake.htbot.discord.commands.dungeon.data.FieldDungeonStatus;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BattleResultAction {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;
    private final ObjectMapperUtil objectMapperUtil;

    private final FieldDungeonStatusCache fieldDungeonStatusCache;

    public BattleResultAction() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
        this.objectMapperUtil = new ObjectMapperUtil();

        this.fieldDungeonStatusCache = CacheFactory.fieldDungeonStatusCache;
    }

    public void execute(ButtonInteractionEvent event, String monsterId) {
        JSONObject monsterLoot = getMonsterLoot(monsterId);

        if (monsterLoot == null) {
            errorUtil.sendError(event.getHook(), "전투", "보상을 획득하지 못했습니다.");
            return;
        }

        HttpResponse<JsonNode> response = request(event.getUser().getId(), monsterLoot);

        if (response.getStatus() == 200) {
            boolean levelUp = response.getBody().getObject().getBoolean("levelUp");
            requestSuccess(event, monsterLoot, levelUp);
        } else {
            errorUtil.sendError(event.getHook(), "전투", "보상을 획득하지 못했습니다.");
        }
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

    private HttpResponse<JsonNode> request(String playerId, JSONObject monsterLoot) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("exp", monsterLoot.getInt("exp"));
        requestData.put("gold", monsterLoot.getInt("gold"));

        String endPoint = "/player/battle/win/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        String jsonBody = objectMapperUtil.mapper(requestData);

        return httpClient.sendPatchRequest(endPoint, routeParam, jsonBody);
    }

    private void requestSuccess(ButtonInteractionEvent event, JSONObject monsterLoot, boolean levelUp) {
        String playerId = event.getUser().getId();
        FieldDungeonStatus fieldDungeonStatus = fieldDungeonStatusCache.get(playerId);

        List<GetItem> getItemList = getNewGetItemList(playerId, monsterLoot, fieldDungeonStatus);
        String levelUpMessage = levelUpCheck(playerId, levelUp, fieldDungeonStatus);
        MessageEmbed embed = buildEmbed(monsterLoot, getItemList, levelUpMessage, event.getUser());

        List<Button> buttonList = new ArrayList<>();
        int stage = fieldDungeonStatus.getStage();

        if (stage != 10) buttonList.add(Button.success("dungeon-next", "전진하기"));
        buttonList.add(Button.danger("dungeon-close", "돌아가기"));

        event.getHook().editOriginalEmbeds(embed)
                .setActionRow(buttonList)
                .queue();
    }

    private List<GetItem> getNewGetItemList(String playerId, JSONObject monsterLoot, FieldDungeonStatus fieldDungeonStatus) {
        JSONArray dropItemArray = monsterLoot.getJSONArray("dropItemList");

        List<GetItem> currentGetItemList = fieldDungeonStatus.getGetItemList();
        List<GetItem> newGetItemList = randomDropItemList(dropItemArray);
        currentGetItemList.addAll(newGetItemList);

        fieldDungeonStatus.setGetItemList(currentGetItemList);
        fieldDungeonStatusCache.put(playerId, fieldDungeonStatus);

        return newGetItemList;
    }

    private List<GetItem> randomDropItemList(JSONArray dropItemArray) {
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

    private String levelUpCheck(String playerId, boolean levelUp, FieldDungeonStatus fieldDungeonStatus) {
        if (levelUp) {
            DungeonPlayer dungeonPlayer = fieldDungeonStatus.getDungeonPlayer();
            dungeonPlayer.setLevel(dungeonPlayer.getLevel() + 1);

            fieldDungeonStatus.setDungeonPlayer(dungeonPlayer);
            fieldDungeonStatusCache.put(playerId, fieldDungeonStatus);

            return ":up: 레벨업!!";
        } else {
            return "";
        }
    }

    private MessageEmbed buildEmbed(JSONObject monsterLoot, List<GetItem> getItemList, String levelUpMessage, User user) {
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
