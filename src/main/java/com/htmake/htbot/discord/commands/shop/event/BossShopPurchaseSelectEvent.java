package com.htmake.htbot.discord.commands.shop.event;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.awt.*;
import java.util.Collections;

public class BossShopPurchaseSelectEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;

    public BossShopPurchaseSelectEvent() {
        this.httpClient = new HttpClientImpl();

        this.errorUtil = new ErrorUtil();
    }

    public void execute(StringSelectInteractionEvent event, String itemId) {
        HttpResponse<JsonNode> response = request(event, itemId);

        if (response.getStatus() == 200) {
            MessageEmbed embed = buildEmbed();
            event.getHook().editOriginalComponents(Collections.emptyList()).queue();
            event.getHook().editOriginalEmbeds(embed).queue();
        } else {
            String description = response.getBody().getObject().getString("message");
            errorUtil.sendError(event.getHook(), "보스 상점", description);
        }
    }

    private HttpResponse<JsonNode> request(StringSelectInteractionEvent event, String id) {
        String endPoint = "/shop/boss/purchase/{player_id}/{item_id}";
        Pair<String, String> firstRouteParam = new Pair<>("player_id", event.getUser().getId());
        Pair<String, String> secondRouteParam = new Pair<>("item_id", id);
        return httpClient.sendPostRequest(endPoint, firstRouteParam, secondRouteParam);
    }

    private MessageEmbed buildEmbed() {
        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("보스 아이템 구매에 성공했습니다!")
                .build();
    }
}
