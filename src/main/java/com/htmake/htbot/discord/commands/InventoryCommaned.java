package com.htmake.htbot.discord.commands;

import com.htmake.htbot.unirest.HttpClient;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class InventoryCommaned extends ListenerAdapter {
    private final HttpClient httpClient;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("인벤토리")) {
            handleInventoryInfo(event);
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        List<String> components = List.of(event.getComponentId().split("-"));
        String component = components.get(0);
        int page = Integer.parseInt(components.get(1));

        if (component.equals("left") && page >= 1) {
            handlePageButton(event, page - 1);
        } else if (component.equals("right") && page < 6) {
            handlePageButton(event, page);
        }
    }

    private void handleInventoryInfo(SlashCommandInteractionEvent event) {
        User user = event.getUser();

        String endPoint = "/inventory/info/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", user.getId());

        HttpResponse<JsonNode> response = httpClient.sendGetRequest(endPoint, routeParam);

        JSONObject inventoryObject = response.getBody().getObject();
        JSONArray inventoryArray = inventoryObject.getJSONArray("inventoryList");

        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        if (response.getStatus() == 200) {

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setAuthor(user.getName(), null, profileUrl)
                    .setTitle(":backpack: 인벤토리");

            for (int i = 0; i < 9; i++) {
                JSONObject item = inventoryArray.getJSONObject(i);
                String itemName = item.getString("name");
                int itemQuantity = item.getInt("quantity");

                embedBuilder.addField(itemName, "수량: " + itemQuantity, true);
            }



            MessageEmbed embed = embedBuilder.build();

            event.replyEmbeds(embed)
                    .addActionRow(
                            Button.primary("left-0", "◄"),
                            Button.secondary("blank", "1/5"),
                            Button.primary("right-2", "►")
                    )
                    .queue();
        } else {
            MessageEmbed embed = new EmbedBuilder()
                    .setColor(Color.YELLOW)
                    .setTitle(":warning: 인벤토리를 찾을 수 없습니다!")
                    .build();

            event.replyEmbeds(embed).queue();
            log.error(String.valueOf(response.getBody()));
        }
    }

    private void handlePageButton(ButtonInteractionEvent event, int page) {

        JSONArray inventoryArray = getInventoryArray(event);

        MessageEmbed embed = event.getMessage().getEmbeds().get(0);

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(embed.getAuthor().getName(), null, embed.getAuthor().getIconUrl())
                .setTitle(embed.getTitle());

        int min = page - 1;

        for (int i = min * 9; i < page * 9; i++) {
            if (i >= inventoryArray.length()) break;

            JSONObject item = inventoryArray.getJSONObject(i);
            String itemName = item.getString("name");
            int itemQuantity = item.getInt("quantity");

            embedBuilder.addField(itemName, "수량: " + itemQuantity, true);
        }

        MessageEmbed newEmbed = embedBuilder.build();

        String leftId = "left-" + page;
        String pageLabel = page + "/5";
        String rightId = "right-" + (page + 1);

        event.getMessage().editMessageEmbeds(newEmbed)
                .setActionRow(
                        Button.primary(leftId, "◄"),
                        Button.secondary("blank", pageLabel),
                        Button.primary(rightId, "►")
                )
                .queue();
    }

    private JSONArray getInventoryArray(ButtonInteractionEvent event) {
        User user = event.getUser();

        String endPoint = "/inventory/info/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", user.getId());

        HttpResponse<JsonNode> response = httpClient.sendGetRequest(endPoint, routeParam);

        if (response.getStatus() == 200) {
            JSONObject inventoryObject = response.getBody().getObject();
            return inventoryObject.getJSONArray("inventoryList");
        } else {
            errorEmbed(event);
        }

        return null;
    }

    private void errorEmbed(ButtonInteractionEvent event) {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.YELLOW)
                .setTitle(":warning: 인벤토리를 찾을 수 없습니다!")
                .build();

        event.getMessage().editMessageComponents(Collections.emptyList()).queue();
        event.getMessage().editMessageEmbeds(embed).queue();
    }
}
