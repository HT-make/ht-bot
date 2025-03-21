package com.htmake.htbot.discord.commands.dictionary.event;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.FormatUtil;
import com.htmake.htbot.domain.dictionary.presentation.data.response.DictionaryResponse;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.json.JSONObject;

import java.awt.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DictionarySlashEvent {

    private final HttpClient httpClient;

    private final ErrorUtil errorUtil;

    public DictionarySlashEvent() {
        this.httpClient = new HttpClientImpl();

        this.errorUtil = new ErrorUtil();
    }

    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping categoryOption = event.getOption("카테고리");
        OptionMapping nameOption = event.getOption("이름");

        String category = categoryOption.getAsString();
        String name = nameOption.getAsString();

        HttpResponse<JsonNode> response = request(category, name);

        if (response.getStatus() == 200) {
            requestSuccess(event, category, response.getBody().getObject());
        } else {
            errorUtil.sendError(event, "사전", "올바르지 않은 이름입니다.");
        }
    }

    private HttpResponse<JsonNode> request(String category, String name){
        String endPoint = "/dictionary/info";
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        List<Pair<String, String>> requestParamList;
        requestParamList = List.of(new Pair<>("category", category), new Pair<>("name", encodedName));
        return httpClient.sendGetRequest(endPoint, requestParamList);
    }

    private void requestSuccess(SlashCommandInteractionEvent event, String category, JSONObject dictionaryObject) {
        new DictionaryResponse();
        DictionaryResponse response = switch (category) {
            case "weapon" -> toWeaponResponse(dictionaryObject);
            case "armor" -> toArmorResponse(dictionaryObject);
            case "misc" -> toMiscResponse(dictionaryObject);
            default -> new DictionaryResponse();
        };

        MessageEmbed embed = buildEmbed(response, category);

        event.replyEmbeds(embed).queue();
    }

    private DictionaryResponse toWeaponResponse(JSONObject dictionaryObject) {
        return DictionaryResponse.builder()
                .name(dictionaryObject.getString("name"))
                .level(dictionaryObject.getInt("level"))
                .damage(dictionaryObject.getInt("damage"))
                .health(dictionaryObject.getInt("health"))
                .defence(dictionaryObject.getInt("defence"))
                .criticalChance(dictionaryObject.getInt("criticalChance"))
                .criticalDamage(dictionaryObject.getInt("criticalDamage"))
                .mana(dictionaryObject.getInt("mana"))
                .gold(dictionaryObject.getInt("gold"))
                .build();
    }

    private DictionaryResponse toArmorResponse(JSONObject dictionaryObject) {
        return DictionaryResponse.builder()
                .name(dictionaryObject.getString("name"))
                .level(dictionaryObject.getInt("level"))
                .health(dictionaryObject.getInt("health"))
                .defence(dictionaryObject.getInt("defence"))
                .gold(dictionaryObject.getInt("gold"))
                .build();
    }

    private DictionaryResponse toMiscResponse(JSONObject dictionaryObject) {
        return DictionaryResponse.builder()
                .name(dictionaryObject.getString("name"))
                .gold(dictionaryObject.getInt("gold"))
                .build();
    }

    private String weaponFormat(DictionaryResponse response) {
        StringBuilder sb = new StringBuilder();

        sb.append("**레벨**: ").append(response.getLevel()).append("\n");
        sb.append("**공격력**: ").append(FormatUtil.decimalFormat(response.getDamage())).append("\n");
        if (response.getHealth() != 0) {
            sb.append("**체력**: ").append(FormatUtil.decimalFormat(response.getHealth())).append("\n");
        }
        if (response.getDefence() != 0) {
            sb.append("**방어력**: ").append(FormatUtil.decimalFormat(response.getDefence())).append("\n");
        }
        if (response.getCriticalChance() != 0) {
            sb.append("**치명타 확률**: ").append(response.getCriticalChance()).append("\n");
        }
        if (response.getCriticalDamage() != 0) {
            sb.append("**치명타 피해**: ").append(FormatUtil.decimalFormat(response.getCriticalDamage())).append("\n");
        }
        if (response.getMana() != 0) {
            sb.append("**마나**: ").append(FormatUtil.decimalFormat(response.getMana())).append("\n");
        }
        sb.append("**판매 가격**: ").append(FormatUtil.decimalFormat(response.getGold())).append("\n");

        return sb.toString();
    }

    private String armorFormat(DictionaryResponse response) {
        StringBuilder sb = new StringBuilder();

        sb.append("**레벨**: ").append(response.getLevel()).append("\n");
        sb.append("**체력**: ").append(FormatUtil.decimalFormat(response.getHealth())).append("\n");
        sb.append("**방어력**: ").append(FormatUtil.decimalFormat(response.getDefence())).append("\n");
        sb.append("**판매 가격**: ").append(FormatUtil.decimalFormat(response.getGold())).append("\n");

        return sb.toString();
    }

    private String miscFormat(DictionaryResponse response) {
        StringBuilder sb = new StringBuilder();

        sb.append("**판매 가격**: ").append(FormatUtil.decimalFormat(response.getGold())).append("\n");

        return sb.toString();
    }
    
    private MessageEmbed buildEmbed(DictionaryResponse response, String category) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(":bookmark: 도감");

        switch (category) {
            case "weapon" -> embedBuilder.addField(response.getName(), weaponFormat(response), false);
            case "armor" -> embedBuilder.addField(response.getName(), armorFormat(response), false);
            case "misc" -> embedBuilder.addField(response.getName(), miscFormat(response), false);
        }

        return embedBuilder.build();
    }
}
