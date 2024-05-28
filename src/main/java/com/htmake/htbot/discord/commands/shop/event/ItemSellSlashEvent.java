package com.htmake.htbot.discord.commands.shop.event;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.ObjectMapperUtil;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ItemSellSlashEvent {

    private final HttpClient httpClient;

    private final ObjectMapperUtil objectMapperUtil;

    private final ErrorUtil errorUtil;

    public ItemSellSlashEvent() {
        this.httpClient = new HttpClientImpl();

        this.objectMapperUtil = new ObjectMapperUtil();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping categoryOption = event.getOption("카테고리");
        OptionMapping nameOption = event.getOption("이름");
        OptionMapping quantityOption = event.getOption("수량");
        String category = categoryOption.getAsString();
        String name = nameOption.getAsString();
        int quantity = quantityOption.getAsInt();

        HttpResponse<JsonNode> response = request(event.getUser(), category, name, quantity);

        if (response.getStatus() == 200) {
            MessageEmbed embed = buildEmbed();
            event.replyEmbeds(embed).queue();
        } else {
            errorUtil.sendError(event, "판매 실패", "정확한 값을 입력했는지 확인해 주세요.");
        }
    }

    private HttpResponse<JsonNode> request(User user, String category, String name, int quantity) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("category", category);
        requestData.put("name", name);
        requestData.put("quantity", quantity);

        String endPoint = "/shop/sell/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", user.getId());
        String jsonBody = objectMapperUtil.mapper(requestData);

        return httpClient.sendPatchRequest(endPoint, routeParam, jsonBody);
    }
    private MessageEmbed buildEmbed() {
        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("아이템 판매에 성공했습니다!")
                .build();
    }
}
