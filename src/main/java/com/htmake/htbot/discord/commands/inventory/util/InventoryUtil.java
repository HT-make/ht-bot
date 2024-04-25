package com.htmake.htbot.discord.commands.inventory.util;

import com.htmake.htbot.discord.commands.inventory.data.Inventory;
import com.htmake.htbot.discord.commands.inventory.data.InventoryItem;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryUtil {
    public List<InventoryItem> toInventoryItemList(JSONArray inventoryItemArray) {
        List<InventoryItem> inventoryItemList = new ArrayList<>();

        for (int i = 0; i < inventoryItemArray.length(); i++) {
            JSONObject inventoryItemObject = inventoryItemArray.getJSONObject(i);

            InventoryItem inventoryItem = InventoryItem.builder()
                    .id(inventoryItemObject.getString("id"))
                    .name(inventoryItemObject.getString("name"))
                    .quantity(inventoryItemObject.getInt("quantity"))
                    .build();

            inventoryItemList.add(inventoryItem);
        }

        return inventoryItemList;
    }

    public EmbedBuilder embedBuilder(MessageEmbed embed, Inventory inventory) {
        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(embed.getAuthor().getName(), null, embed.getAuthor().getIconUrl())
                .setTitle(embed.getTitle())
                .addField(":coin: 골드 ", "" + inventory.getGold(), true)
                .addField(":gem: 젬 ", "" + inventory.getGem(), true)
                .addBlankField(true)
                .addBlankField(false);
    }

    public static StringSelectMenu createInventoryMenu(String placeholder) {
        List<SelectOption> options = Arrays.asList(
                SelectOption.of("전체", "inventory-category-all"),
                SelectOption.of("장비", "inventory-category-equipment"),
                SelectOption.of("치장", "inventory-category-accessory"),
                SelectOption.of("기타", "inventory-category-misc")
        );

        return StringSelectMenu.create("inventoryMenu")
                .setPlaceholder(placeholder)
                .addOptions(options)
                .build();
    }

    public EmbedBuilder addItemField(EmbedBuilder embedBuilder, List<InventoryItem> inventoryItemList, int page) {
        int cnt = 0;

        for (int i = (page - 1) * 9; i < page * 9; i++) {
            if (i >= inventoryItemList.size()) break;

            InventoryItem inventoryItem = inventoryItemList.get(i);
            embedBuilder.addField(inventoryItem .getName(), "수량: " + inventoryItem .getQuantity(), true);
            cnt++;
        }

        for (int i = cnt; i < 9; i++) {
            embedBuilder.addBlankField(true);
        }

        return embedBuilder;
    }

    public String getCategory(String category) {
        if (category.equals("all")){
            category = "전체";
        } else if (category.equals("equipment")) {
            category = "장비";
        } else if (category.equals("accessory")) {
            category = "치장";
        } else if (category.equals("misc")) {
            category = "기타";
        }
        return category;
    }
}
