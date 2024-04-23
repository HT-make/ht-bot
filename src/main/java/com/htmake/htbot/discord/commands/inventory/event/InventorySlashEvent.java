package com.htmake.htbot.discord.commands.inventory.event;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
public class InventorySlashEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;

    public InventorySlashEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(SlashCommandInteractionEvent event) {
        HttpResponse<JsonNode> response = request(event.getUser().getId());

        if (response.getStatus() == 200)  {
            JSONObject inventoryObject = response.getBody().getObject();
            requestSuccess(event, inventoryObject);

        } else {
            errorUtil.sendError(event, "인벤토리", "인벤토리를 찾을 수 없습니다.");
        }
    }

    private HttpResponse<JsonNode> request(String playerId) {
        String endPoint = "/inventory/info/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        return httpClient.sendGetRequest(endPoint, routeParam);
    }

    private void requestSuccess(SlashCommandInteractionEvent event, JSONObject inventoryObject) {
        JSONArray inventoryArray = inventoryObject.getJSONArray("inventoryList");
        MessageEmbed embed = buildEmbed(event.getUser(), inventoryArray);

        event.replyEmbeds(embed)
                .addActionRow(
                        Button.primary("inventory-left-0", "◄").asDisabled(),
                        Button.secondary("blank", "1/5").asDisabled(),
                        Button.primary("inventory-right-2", "►")
                )
                .queue();
    }

    private MessageEmbed buildEmbed(User user, JSONArray inventoryArray) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle(":backpack: 인벤토리");

        for (int i = 0; i < 9; i++) {
            if (i >= inventoryArray.length()) break;

            JSONObject item = inventoryArray.getJSONObject(i);
            String itemName = item.getString("name");
            int itemQuantity = item.getInt("quantity");

            embedBuilder.addField(itemName, "수량: " + itemQuantity, true);
        }

        return embedBuilder.build();
    }
}
