package com.htmake.htbot.discord.commands.inventory.event;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryButtonEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;

    public InventoryButtonEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(ButtonInteractionEvent event, int page){
        HttpResponse<JsonNode> response = request(event.getUser().getId());

        if (response.getStatus() == 200) {
            JSONObject inventoryObject = response.getBody().getObject();
            JSONArray inventoryObjectJSONArray = inventoryObject.getJSONArray("inventoryList");

            requestSuccess(event, inventoryObjectJSONArray, page);
        } else {
            errorUtil.sendError(event.getMessage(), "인벤토리", "인벤토리를 찾을 수 없습니다." );
        }
    }

    private HttpResponse<JsonNode> request(String playerId) {
        String endPoint = "/inventory/info/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        return httpClient.sendGetRequest(endPoint, routeParam);
    }

    public void requestSuccess(ButtonInteractionEvent event, JSONArray inventoryArray, int page) {
        MessageEmbed embed = event.getMessage().getEmbeds().get(0);

        MessageEmbed newEmbed = buildEmbed(embed, inventoryArray, page);

        List<Button> buttonList = new ArrayList<>();

        buttonEmbed(page, buttonList);

        event.getMessage().editMessageEmbeds(newEmbed)
                .setActionRow(buttonList)
                .queue();
    }

    private MessageEmbed buildEmbed(MessageEmbed embed, JSONArray inventoryArray, int page) {

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(embed.getAuthor().getName(), null, embed.getAuthor().getIconUrl())
                .setTitle(embed.getTitle());

        int min = page - 1;

        int cnt = 0;

        for (int i = min * 9; i < page * 9; i++) {
            if (i >= inventoryArray.length()) break;

            JSONObject item = inventoryArray.getJSONObject(i);
            String itemName = item.getString("name");
            int itemQuantity = item.getInt("quantity");

            embedBuilder.addField(itemName, "수량: " + itemQuantity, true);

            cnt++;
        }

        for (int i = cnt; i < 9; i++) {
            embedBuilder.addBlankField(true);
        }

        return embedBuilder.build();
    }

    public void buttonEmbed(int page, List<Button> buttonList) {
        String leftId = "inventory-left-" + page;
        String pageLabel = page + "/5";
        String rightId = "inventory-right-" + (page + 1);

        Button pageButton = Button.secondary("blank", pageLabel).asDisabled();
        Button leftButton = Button.primary(leftId, "◄");
        Button rightButton = Button.primary(rightId, "►");

        if (page == 1) {
            buttonList.add(leftButton.asDisabled());
            buttonList.add(pageButton);
            buttonList.add(rightButton);
        } else if (page == 5) {
            buttonList.add(leftButton);
            buttonList.add(pageButton);
            buttonList.add(rightButton.asDisabled());
        } else {
            buttonList.add(leftButton.asEnabled());
            buttonList.add(pageButton);
            buttonList.add(rightButton.asEnabled());
        }
    }
}
