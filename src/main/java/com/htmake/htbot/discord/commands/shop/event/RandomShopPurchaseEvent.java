package com.htmake.htbot.discord.commands.shop.event;

import com.htmake.htbot.discord.commands.shop.util.ShopUtil;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.htmake.htbot.discord.util.ObjectMapperUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.HashMap;
import java.util.Map;

public class RandomShopPurchaseEvent {

    private final HttpClient httpClient;
    private final ShopUtil shopUtil;
    private final ObjectMapperUtil objectMapperUtil;
    private final ErrorUtil errorUtil;

    public RandomShopPurchaseEvent() {
        this.httpClient = new HttpClientImpl();
        this.shopUtil = new ShopUtil();
        this.objectMapperUtil = new ObjectMapperUtil();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping nameOption = event.getOption("장비이름");
        String name = nameOption.getAsString();

        HttpResponse<JsonNode> response = request(event, name);

        if (response.getStatus() == 200) {
            int gold = response.getBody().getObject().getInt("gold");
            shopUtil.successPurchase(event, gold);
        } else {
            String description = response.getBody().getObject().getString("message");
            errorUtil.sendError(event, "랜덤 상점", description);
        }
    }

    private HttpResponse<JsonNode> request(SlashCommandInteractionEvent event, String name) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("name", name);

        String endPoint = "/shop/random/purchase/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", event.getUser().getId());
        String jsonBody = objectMapperUtil.mapper(requestData);

        return httpClient.sendPostRequest(endPoint, routeParam, jsonBody);
    }
}
