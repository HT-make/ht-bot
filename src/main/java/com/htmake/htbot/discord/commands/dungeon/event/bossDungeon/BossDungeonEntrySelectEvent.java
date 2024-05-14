package com.htmake.htbot.discord.commands.dungeon.event.bossDungeon;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.domain.dungeon.presentation.data.response.BossDungeonInfoResponse;
import com.htmake.htbot.domain.dungeon.presentation.data.response.DungeonKeyResponse;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BossDungeonEntrySelectEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;

    public BossDungeonEntrySelectEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(StringSelectInteractionEvent event, String dungeonId) {
        HttpResponse<JsonNode> response = request(dungeonId, event.getUser().getId());

        if (response.getStatus() == 200) {
            JSONObject bossDungeonObject = response.getBody().getObject();
            requestSuccess(event, dungeonId, bossDungeonObject);
        } else {
            errorUtil.sendError(event.getHook(), "보스 던전 입장", "현재 던전에 입장할 수 없습니다.");
        }
    }

    private HttpResponse<JsonNode> request(String dungeonId, String playerId) {
        String endPoint = "/dungeon/{dungeon_id}/{player_id}";
        Pair<String, String> firstRouteParam = new Pair<>("dungeon_id", dungeonId);
        Pair<String, String> secondRouteParam = new Pair<>("player_id", playerId);

        return httpClient.sendGetRequest(endPoint, firstRouteParam, secondRouteParam);
    }

    private void requestSuccess(StringSelectInteractionEvent event, String dungeonId, JSONObject bossDungeonObject) {
        BossDungeonInfoResponse dungeonInfo = toBossDungeonInfoResponse(bossDungeonObject);

        MessageEmbed embed = buildEmbed(event.getUser(), dungeonInfo);

        List<Button> buttonList = new ArrayList<>();

        Button entryButton = Button.success("dungeon-boss-entry-" + dungeonId, "입장");

        if (quantityCheck(dungeonInfo.getDungeonKeyList())) {
            buttonList.add(entryButton);
        } else {
            buttonList.add(entryButton.asDisabled());
        }

        buttonList.add(Button.danger("cancel", "닫기"));

        event.getHook().editOriginalEmbeds(embed)
                .setComponents(ActionRow.of(buttonList))
                .queue();
    }

    private BossDungeonInfoResponse toBossDungeonInfoResponse(JSONObject bossDungeonObject) {
        JSONArray dungeonKeyArray = bossDungeonObject.getJSONArray("dungeonKeyList");
        List<DungeonKeyResponse> dungeonKeyList = toDungeonKeyList(dungeonKeyArray);

        JSONArray monsterNameArray = bossDungeonObject.getJSONArray("monsterNameList");
        List<String> monsterNameList = toMonsterNameList(monsterNameArray);

        return BossDungeonInfoResponse.builder()
                .name(bossDungeonObject.getString("name"))
                .dungeonKeyList(dungeonKeyList)
                .monsterNameList(monsterNameList)
                .build();
    }

    private List<DungeonKeyResponse> toDungeonKeyList(JSONArray dungeonKeyArray) {
        List<DungeonKeyResponse> dungeonKeyList = new ArrayList<>();

        for (int i = 0; i < dungeonKeyArray.length(); i++) {
            JSONObject dungeonKeyObject = dungeonKeyArray.getJSONObject(i);

            DungeonKeyResponse dungeonKey = DungeonKeyResponse.builder()
                    .name(dungeonKeyObject.getString("name"))
                    .requireQuantity(dungeonKeyObject.getInt("requireQuantity"))
                    .playerQuantity(dungeonKeyObject.getInt("playerQuantity"))
                    .build();

            dungeonKeyList.add(dungeonKey);
        }

        return dungeonKeyList;
    }

    private List<String> toMonsterNameList(JSONArray monsterNameArray) {
        List<String> monsterNameList = new ArrayList<>();

        for (int i = 0; i < monsterNameArray.length(); i++) {
            monsterNameList.add(String.valueOf(monsterNameArray.get(i)));
        }

        return monsterNameList;
    }

    private MessageEmbed buildEmbed(User user, BossDungeonInfoResponse dungeonInfo) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        String appearMonsterInfo = toAppearMonsterInfo(dungeonInfo.getMonsterNameList());
        String dungeonKeyInfo = toDungeonKeyInfo(dungeonInfo.getDungeonKeyList());

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle(dungeonInfo.getName())
                .setDescription("던전 정보")
                .addField("---------출현 보스---------", appearMonsterInfo, false)
                .addField("---------입장 재료---------", dungeonKeyInfo, false)
                .build();
    }

    private String toAppearMonsterInfo(List<String> monsterNameList) {
        StringBuilder appearMonsterInfo = new StringBuilder();

        for (String monsterName : monsterNameList) {
            appearMonsterInfo.append(monsterName).append("\n");
        }

        return String.valueOf(appearMonsterInfo);
    }

    private String toDungeonKeyInfo(List<DungeonKeyResponse> dungeonKeyList) {
        StringBuilder dungeonKeyInfo = new StringBuilder();

        for (DungeonKeyResponse dungeonKey : dungeonKeyList) {
            dungeonKeyInfo.append(dungeonKey.getName())
                    .append(" (")
                    .append(dungeonKey.getPlayerQuantity())
                    .append("/")
                    .append(dungeonKey.getRequireQuantity())
                    .append(")\n");
        }

        return String.valueOf(dungeonKeyInfo);
    }

    private boolean quantityCheck(List<DungeonKeyResponse> dungeonKeyList) {
        for (DungeonKeyResponse dungeonKey : dungeonKeyList) {
            if (dungeonKey.getPlayerQuantity() < dungeonKey.getRequireQuantity()) {
                return false;
            }
        }

        return true;
    }
}
