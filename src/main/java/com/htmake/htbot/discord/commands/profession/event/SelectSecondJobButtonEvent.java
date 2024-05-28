package com.htmake.htbot.discord.commands.profession.event;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.domain.profession.presentation.data.JobPromotionInfoResponse;
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
import java.util.ArrayList;
import java.util.List;

public class SelectSecondJobButtonEvent {

    private final HttpClient httpClient;

    private final ErrorUtil errorUtil;

    public SelectSecondJobButtonEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(ButtonInteractionEvent event) {
        HttpResponse<JsonNode> response = request(event.getUser().getId());
        if (response.getStatus() == 200) {
            JSONArray promotionDataArray = response.getBody().getObject().getJSONArray("jobPromotionInfoList");
            if (promotionDataArray.length() > 1){
                requestSuccess(event, promotionDataArray);
            } else {
                errorUtil.sendError(event.getHook(), "전직을 완료하지 못했습니다.", "자신에게 맞는 전직으로 다시 시도해 주세요.");
            }
        } else {
            errorUtil.sendError(event.getHook(), "전직을 완료하지 못했습니다.", "자신에게 맞는 전직으로 다시 시도해 주세요.");
        }
    }

    private HttpResponse<JsonNode> request(String playerId) {
        String endPoint = "/profession/job/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        return httpClient.sendGetRequest(endPoint, routeParam);
    }

    private void requestSuccess(ButtonInteractionEvent event, JSONArray promotionDataArray) {
        List<JobPromotionInfoResponse> professionList = toProfessionList(promotionDataArray);

        MessageEmbed newEmbed = buildEmbed(professionList, event.getUser());

        event.getHook().editOriginalEmbeds(newEmbed)
                .setActionRow(
                        Button.primary("promotion-class-" + professionList.get(0).getNextJob(), professionList.get(0).getNextJobName()),
                        Button.primary("promotion-class-" + professionList.get(1).getNextJob(), professionList.get(1).getNextJobName()),

                        Button.danger("cancel", "닫기")
                )
                .queue();
    }

    private List<JobPromotionInfoResponse> toProfessionList(JSONArray promotionDataArray) {
        List<JobPromotionInfoResponse> professionList = new ArrayList<>();

        for (int i = 0; i < promotionDataArray.length(); i++) {
            JSONObject professionObject = promotionDataArray.getJSONObject(i);

            JobPromotionInfoResponse profession = JobPromotionInfoResponse.builder()
                    .nextJob(professionObject.getString("nextJob"))
                    .nextJobName(professionObject.getString("nextJobName"))
                    .description(professionObject.getString("description"))
                    .build();

            professionList.add(profession);
        }

        return professionList;
    }

    private MessageEmbed buildEmbed(List<JobPromotionInfoResponse> professionList, User user) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle("전직")
                .addField(professionList.get(0).getNextJobName(), professionList.get(0).getDescription(), true)
                .addField(professionList.get(1).getNextJobName(), professionList.get(1).getDescription(), true)
                .build();
    }
}
