package com.htmake.htbot.discord.commands.profession.event;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.awt.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class CompletePromotionButtonEvent {

    private final HttpClient httpClient;

    private final ErrorUtil errorUtil;

    public CompletePromotionButtonEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(ButtonInteractionEvent event, String jobName) {
        HttpResponse<JsonNode> response = request(event.getUser().getId(), jobName);

        if (response.getStatus() == 200) {
            requestSuccess(event);
        } else {
            errorUtil.sendError(event.getHook(), "전직을 완료하지 못했습니다.", "자신에게 맞는 전직으로 다시 시도해 주세요.");
        }
    }

    private HttpResponse<JsonNode> request(String playerId, String jobName) {
        String endPoint = "/profession/promotion/{player_id}/{job_name}";
        String encodedJob = URLEncoder.encode(jobName, StandardCharsets.UTF_8);
        Pair<String, String> firstRouteParam = new Pair<>("job_name", encodedJob);
        Pair<String, String> secondRouteParam = new Pair<>("player_id", playerId);
        return httpClient.sendPatchRequest(endPoint, firstRouteParam, secondRouteParam);
    }

    private void requestSuccess(ButtonInteractionEvent event) {
        MessageEmbed newEmbed = buildEmbed(event.getUser());

        event.getHook().editOriginalComponents(Collections.emptyList()).queue();
        event.getHook().editOriginalEmbeds(newEmbed)
                .queue();
    }

    private MessageEmbed buildEmbed(User user) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle("전직")
                .setDescription(":sparkler: 전직이 완료되었습니다.").build();
    }
}
