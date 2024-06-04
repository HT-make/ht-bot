package com.htmake.htbot.discord.commands.profession.event;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.FormatUtil;
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
import org.json.JSONObject;

import java.awt.*;

public class PromotionButtonEvent {

    private final HttpClient httpClient;

    private final ErrorUtil errorUtil;

    public PromotionButtonEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(ButtonInteractionEvent event) {
        HttpResponse<JsonNode> response = request(event.getUser().getId(), "first");

        if (response.getStatus() == 200) {
            JSONObject promotionData = response.getBody().getObject();
            requestSuccess(event, promotionData);
        } else {
            errorUtil.sendError(event.getHook(), "전직을 완료하지 못했습니다.", "자신에게 맞는 전직으로 다시 시도해 주세요.");
        }
    }

    public void execute(ButtonInteractionEvent event, String jobName){
        HttpResponse<JsonNode> response = request(event.getUser().getId(), jobName);

        if (response.getStatus() == 200) {
            JSONObject promotionData = response.getBody().getObject();
            requestSuccess(event, promotionData);
        } else {
            errorUtil.sendError(event.getHook(), "전직을 완료하지 못했습니다.", "자신에게 맞는 전직으로 다시 시도해 주세요.");
        }
    }

    private HttpResponse<JsonNode> request(String playerId, String jobName) {
        String endPoint = "/profession/promotion/{player_id}/{job_name}";
        Pair<String, String> firstRouteParam = new Pair<>("player_id", playerId);
        Pair<String, String> secondRouteParam = new Pair<>("job_name", jobName);
        return httpClient.sendGetRequest(endPoint, firstRouteParam, secondRouteParam);
    }

    private void requestSuccess(ButtonInteractionEvent event, JSONObject promotionData) {
        MessageEmbed newEmbed = buildEmbed(promotionData, event.getUser());

        Button completeButton = Button.primary("promotion-complete-" + promotionData.getString("nextJob"), "전직하기").asDisabled();

        if (promotionData.getInt("requiredLevel") >= promotionData.getInt("level")
            && promotionData.getInt("requiredGold") >= promotionData.getInt("gold")
            && promotionData.getInt("requiredGem") >= promotionData.getInt("gem")
            && promotionData.getInt("requiredItemQuantity") >= promotionData.getInt("itemQuantity")) {
            completeButton = Button.primary("promotion-complete-" + promotionData.getString("nextJob"), "전직하기");
        }
        event.getHook().editOriginalEmbeds(newEmbed)
                .setActionRow(
                        completeButton,
                        Button.danger("cancel", "닫기")
                )
                .queue();
    }

    private MessageEmbed buildEmbed(JSONObject promotionData, User user) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle("전직")
                .setDescription(format(promotionData)).build();
    }

    private String format(JSONObject promotionData){
        StringBuilder sb = new StringBuilder();
        sb.append("\n**").append(promotionData.getString("nextJobName")).append("**").append("\n\n");

        sb.append("필요 레벨 : ").append("(").append(FormatUtil.decimalFormat(promotionData.getInt("requiredLevel"))).append("/")
                .append(FormatUtil.decimalFormat(promotionData.getInt("level"))).append(")").append("\n");
        sb.append("필요 골드 : ").append("(").append(FormatUtil.decimalFormat(promotionData.getInt("requiredGold"))).append("/")
                .append(FormatUtil.decimalFormat(promotionData.getInt("gold"))).append(")").append("\n");
        sb.append("필요 젬 : ").append("(").append(promotionData.getInt("requiredGem")).append("/")
                .append(promotionData.getInt("gem")).append(")").append("\n");

        sb.append("필요 아이템 : ").append(promotionData.getString("itemName")).append("\n");
        sb.append("필요 아이템 수량 : ").append("(").append(promotionData.getInt("requiredItemQuantity")).append("/")
                .append(promotionData.getInt("itemQuantity")).append(")").append("\n");

        return sb.toString();
    }
}
