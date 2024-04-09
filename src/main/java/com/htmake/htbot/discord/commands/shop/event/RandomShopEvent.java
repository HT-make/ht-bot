package com.htmake.htbot.discord.commands.shop.event;

import com.htmake.htbot.domain.shop.entity.RandomShopArmor;
import com.htmake.htbot.domain.shop.entity.RandomShopWeapon;
import com.htmake.htbot.unirest.HttpClient;
import com.htmake.htbot.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
public class RandomShopEvent {

    private final HttpClient httpClient;

    public RandomShopEvent() {
        this.httpClient = new HttpClientImpl();
    }

    public void execute(SlashCommandInteractionEvent event) {
        String endPoint = "/shop/random/list";

        HttpResponse<JsonNode> response = httpClient.sendGetRequest(endPoint);

        JSONObject randomShopObject = response.getBody().getObject();

        JSONArray randomShopWeaponArray = randomShopObject.getJSONArray("randomShopWeaponList");
        JSONArray randomShopArmorArray = randomShopObject.getJSONArray("randomShopArmorList");

        ArrayList<RandomShopWeapon> randomShopWeaponList = toRandomShopWeaponList(randomShopWeaponArray);
        ArrayList<RandomShopArmor> randomShopArmorList = toRandomShopArmorList(randomShopArmorArray);

        MessageEmbed embed = buildEmbed(randomShopWeaponList, randomShopArmorList);

        StringSelectMenu menu = StringSelectMenu.create("randomShopMenu")
                .setPlaceholder("아이템 선택")
                .addOptions(Arrays.asList(
                        SelectOption.of("1️⃣", "shop-randomShop-1"),
                        SelectOption.of("2️⃣", "shop-randomShop-2"),
                        SelectOption.of("3️⃣", "shop-randomShop-3"),
                        SelectOption.of("4️⃣", "shop-randomShop-4"),
                        SelectOption.of("5️⃣", "shop-randomShop-5"),
                        SelectOption.of("6️⃣", "shop-randomShop-6")
                ))
                .build();

        event.replyEmbeds(embed)
                .addActionRow(menu)
                .addActionRow(Button.danger("cancel", "닫기"))
                .queue();
    }

    private MessageEmbed buildEmbed(ArrayList<RandomShopWeapon> randomShopWeaponList, ArrayList<RandomShopArmor> randomShopArmorList) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(":game_die: 랜덤 상점")
                .setDescription("1분마다 아이템이 랜덤으로 변경됩니다.");


        embedBuilder.addField("무기", formatWeaponList(randomShopWeaponList), false);
        embedBuilder.addField("방어구", formatArmorList(randomShopArmorList), false);

        return embedBuilder.build();
    }

    private String formatWeaponList(ArrayList<RandomShopWeapon> itemList) {
        StringBuilder sb = new StringBuilder();

        for (RandomShopWeapon item : itemList) {
            sb.append("- "+item.getName())
                    .append(" - ")
                    .append(item.getGold())
                    .append("G (")
                    .append(item.getQuantity())
                    .append(")\n");
        }
        return sb.toString();
    }

    private String formatArmorList(ArrayList<RandomShopArmor> itemList) {
        StringBuilder sb = new StringBuilder();

        for (RandomShopArmor item : itemList) {
            sb.append("- "+item.getName())
                    .append(" - ")
                    .append(item.getGold())
                    .append("G (")
                    .append(item.getQuantity())
                    .append(")\n");
        }
        return sb.toString();
    }

    private ArrayList<RandomShopWeapon> toRandomShopWeaponList(JSONArray randomShopWeaponsArray) {
        ArrayList<RandomShopWeapon> randomShopWeapons = new ArrayList<>();

        for (int i = 0; i < randomShopWeaponsArray.length(); i++) {
            JSONObject randomShopWeaponObject = randomShopWeaponsArray.getJSONObject(i);

            RandomShopWeapon randomShopWeapon = RandomShopWeapon.builder()
                    .name(randomShopWeaponObject.getString("name"))
                    .quantity(randomShopWeaponObject.getInt("quantity"))
                    .gold(randomShopWeaponObject.getInt("gold"))
                    .build();

            randomShopWeapons.add(randomShopWeapon);
        }

        return randomShopWeapons;
    }

    private ArrayList<RandomShopArmor> toRandomShopArmorList(JSONArray randomShopArmorsArray) {
        ArrayList<RandomShopArmor> randomShopArmors = new ArrayList<>();

        for (int i = 0; i < randomShopArmorsArray.length(); i++) {
            JSONObject randomShopArmorObject = randomShopArmorsArray.getJSONObject(i);

            RandomShopArmor randomShopArmor = RandomShopArmor.builder()
                    .name(randomShopArmorObject.getString("name"))
                    .quantity(randomShopArmorObject.getInt("quantity"))
                    .gold(randomShopArmorObject.getInt("gold"))
                    .build();

            randomShopArmors.add(randomShopArmor);
        }

        return randomShopArmors;
    }
}