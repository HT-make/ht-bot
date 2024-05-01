package com.htmake.htbot.discord.commands.inventory.event;

import com.htmake.htbot.discord.commands.inventory.cache.InventoryCache;
import com.htmake.htbot.discord.commands.inventory.data.InventoryItem;
import com.htmake.htbot.discord.commands.inventory.data.Inventory;
import com.htmake.htbot.discord.commands.inventory.util.InventoryUtil;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.cache.CacheFactory;
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
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class InventorySlashEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;
    private final InventoryUtil inventoryUtil;

    private final InventoryCache inventoryCache;

    public InventorySlashEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
        this.inventoryUtil = new InventoryUtil();

        this.inventoryCache = CacheFactory.inventoryCache;
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
        JSONArray inventoryItemArray = inventoryObject.getJSONArray("inventoryList");
        List<InventoryItem> inventoryItemList = inventoryUtil.toInventoryItemList(inventoryItemArray);

        Inventory inventory = toInventory(inventoryObject, inventoryItemList);

        inventoryCache.put(event.getUser().getId(), inventory);

        MessageEmbed embed = buildEmbed(event.getUser(), inventory);

        StringSelectMenu menu = buildMenu();

        event.replyEmbeds(embed)
                .addActionRow(
                        Button.primary("inventory-0", "◄").asDisabled(),
                        Button.secondary("blank", "1/5").asDisabled(),
                        Button.primary("inventory-2", "►"),
                        Button.danger("cancel", "닫기")
                )
                .addActionRow(menu)
                .queue();
    }

    private Inventory toInventory(JSONObject inventoryObject, List<InventoryItem> inventoryItemList) {
        return Inventory.builder()
                .gold(inventoryObject.getInt("gold"))
                .gem(inventoryObject.getInt("gem"))
                .category("all")
                .inventoryItemList(inventoryItemList)
                .build();
    }

    private MessageEmbed buildEmbed(User user, Inventory inventory) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle(":backpack: 인벤토리")
                .addField(":coin: 골드 ", "" + inventory.getGold(), true)
                .addField(":gem: 젬 ", "" + inventory.getGem(), true)
                .addBlankField(true)
                .addBlankField(false);

        List<InventoryItem> inventoryItemList = inventory.getInventoryItemList();

        for (int i = 0; i < 9; i++) {
            if (i >= inventoryItemList.size()) break;

            InventoryItem inventoryItem = inventoryItemList.get(i);
            embedBuilder.addField(inventoryItem.getName(), "수량: " + inventoryItem.getQuantity(), true);
        }

        return embedBuilder.build();
    }

    private StringSelectMenu buildMenu() {
        return StringSelectMenu.create("inventoryMenu")
                .setPlaceholder("현재 페이지 : 전체")
                .addOptions(Arrays.asList(
                        SelectOption.of("전체", "inventory-category-all"),
                        SelectOption.of("장비", "inventory-category-equipment"),
                        SelectOption.of("치장", "inventory-category-accessory"),
                        SelectOption.of("기타", "inventory-category-misc")
                ))
                .build();
    }
}