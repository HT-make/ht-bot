package com.htmake.htbot.discord.commands.dungeon.event.bossDungeon;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.domain.dungeon.presentation.data.response.BossDungeonInfoResponse;
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

        Button entryButton = Button.success("dungeon-boss-" + dungeonId, "입장");

        if (dungeonInfo.getPlayerKeyQuantity() < 1) {
            buttonList.add(entryButton.asDisabled());
        } else {
            buttonList.add(entryButton);
        }

        buttonList.add(Button.danger("cancel", "닫기"));

        event.getHook().editOriginalEmbeds(embed)
                .setComponents(ActionRow.of(buttonList))
                .queue();
    }

    private BossDungeonInfoResponse toBossDungeonInfoResponse(JSONObject bossDungeonObject) {
        return BossDungeonInfoResponse.builder()
                .name(bossDungeonObject.getString("name"))
                .dungeonKey(bossDungeonObject.getString("dungeonKey"))
                .playerKeyQuantity(bossDungeonObject.getInt("playerKeyQuantity"))
                .build();
    }

    private MessageEmbed buildEmbed(User user, BossDungeonInfoResponse dungeonInfo) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        String entryStuffInfo = dungeonInfo.getDungeonKey() + " (" + dungeonInfo.getPlayerKeyQuantity() + "/1)";

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle(dungeonInfo.getName())
                .addField("던전 정보", "\n보스1\n보스2", false)
                .addField("-------------------------", entryStuffInfo, false)
                .build();
    }
}
